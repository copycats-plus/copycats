package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.mixin.copycat.VoxelShapeAccessor;
import com.google.common.math.DoubleMath;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.copycatsplus.copycats.utility.MathUtils.*;

public class BlockFaceUtils {

    private static final ThreadLocal<Map<BiFunction<VoxelShape, VoxelShape, Boolean>, Object2ByteLinkedOpenHashMap<ShapeKey>>> SHAPE_OP_CACHE = ThreadLocal.withInitial(HashMap::new);

    public static record ShapeKey(VoxelShape fromShape, VoxelShape toShape) {
    }

    private static boolean invokeCachedOperation(BiFunction<VoxelShape, VoxelShape, Boolean> operation, VoxelShape fromShape, VoxelShape toShape) {
        Object2ByteLinkedOpenHashMap<ShapeKey> cache = SHAPE_OP_CACHE.get().computeIfAbsent(operation, op -> {
            Object2ByteLinkedOpenHashMap<ShapeKey> cacheMap = new Object2ByteLinkedOpenHashMap<>(2048, 0.25f) {
                @Override
                protected void rehash(int i) {
                }
            };
            cacheMap.defaultReturnValue((byte) 127);
            return cacheMap;
        });
        ShapeKey key = new ShapeKey(fromShape, toShape);
        byte result = cache.getAndMoveToFirst(key);
        if (result == 127) {
            boolean value = operation.apply(fromShape, toShape);
            if (cache.size() >= 2048) cache.removeLastByte();
            cache.putAndMoveToFirst(key, (byte) (value ? 1 : 0));
            return value;
        } else {
            return result == 1;
        }
    }

    private static boolean processBlockFace(BlockGetter level,
                                            BlockState fromState,
                                            BlockPos fromPos,
                                            BlockState toState,
                                            BlockPos toPos,
                                            Direction fromFace,
                                            BiFunction<VoxelShape, VoxelShape, Boolean> operation) {
        VoxelShape fromShape = null;
        Vec3i scale = new Vec3i(1, 1, 1);
        Vec3i inner = new Vec3i(0, 0, 0);
        BlockPos truePos = fromPos;
        if (level instanceof ScaledBlockAndTintGetter scaledWorld) {
            scale = scaledWorld.getScale();
            truePos = scaledWorld.getTruePos(fromPos);
            if (fromState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock &&
                    copycatBlock.vectorScale(fromState).equals(scaledWorld.getScale())) {
                String property = scaledWorld.getPropertyForRender(fromState, fromPos);
                if (!copycatBlock.partExists(fromState, property))
                    return false;
                inner = copycatBlock.getVectorFromProperty(fromState, property);
                fromShape = copycatBlock.getPartialFaceShape(scaledWorld, fromState, property, fromFace);
            }
        }
        if (fromShape == null) {
            fromShape = fromState.getFaceOcclusionShape(level, fromPos, fromFace);
        }
        if (fromShape.isEmpty()) {
            return false;
        }

        VoxelShape toShape = null;
        List<String> potentialParts = null;
        if (toState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            Vec3i toScale = copycatBlock.vectorScale(toState);
            Axis connectingAxis = fromFace.getAxis();
            BlockGetter world = level;
            BlockPos toTruePos = toPos;
            String fallbackProperty = copycatBlock.defaultProperty();
            if (level instanceof ScaledBlockAndTintGetter scaledWorld) {
                world = scaledWorld.getWrapped();
                toTruePos = scaledWorld.getTruePos(toPos);
                fallbackProperty = scaledWorld.getRenderingProperty();
                if (toTruePos.equals(truePos)) {
                    potentialParts = List.of(scaledWorld.getPropertyForRender(toState, toPos));
                }
            }
            if (potentialParts == null) {
                if (replaceAxis(scale, connectingAxis, 0).equals(replaceAxis(toScale, connectingAxis, 0))) {
                    potentialParts = List.of(copycatBlock.getPropertyFromRender(fallbackProperty, toState, world, replaceAxis(inner, connectingAxis,
                            fromFace.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 0 : (toScale.get(connectingAxis) - 1)
                    ), toTruePos));
                } else {
                    potentialParts = new ArrayList<>(4);
                    int connectingPart = fromFace.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 0 : (toScale.get(connectingAxis) - 1);
                    Axis axis1 = switch (connectingAxis) {
                        case X -> Axis.Y;
                        case Y -> Axis.Z;
                        case Z -> Axis.X;
                    };
                    Axis axis2 = switch (connectingAxis) {
                        case X -> Axis.Z;
                        case Y -> Axis.X;
                        case Z -> Axis.Y;
                    };
                    for (int i = 0; i < toScale.get(axis1); i++) {
                        for (int j = 0; j < toScale.get(axis2); j++) {
                            potentialParts.add(copycatBlock.getPropertyFromRender(fallbackProperty, toState, world, replaceAxis(replaceAxis(new Vec3i(connectingPart, connectingPart, connectingPart), axis1, i), axis2, j), toTruePos));
                        }
                    }
                }
            }
            for (String partProp : potentialParts) {
                toShape = copycatBlock.getPartialFaceShape(world, toState, partProp, fromFace.getOpposite());
                if (invokeCachedOperation(operation, fromShape, toShape)) {
                    String property = CopycatExternalContext.getPropertyForAppearance();
                    if (property == null) property = copycatBlock.defaultProperty();
                    CopycatExternalContext.setPropertyForAppearance(copycatBlock.getPropertyFromRender(property, toState, world, copycatBlock.getVectorFromProperty(toState, partProp), toTruePos));
                    return true;
                }
            }
        }
        if (toShape == null) {
            toShape = toState.getFaceOcclusionShape(level, toPos, fromFace.getOpposite());
        }
        CopycatExternalContext.setPropertyForAppearance(null);
        return invokeCachedOperation(operation, fromShape, toShape);
    }

    /**
     * Compare the occlusion shape of two blocks to determine if the occluding block completely covers the occluded block, which allows occlusion.
     */
    public static boolean canOcclude(BlockGetter level,
                                     BlockState occludedState,
                                     BlockPos occludedPos,
                                     BlockState occludingState,
                                     BlockPos occludingPos,
                                     Direction occludedFace) {
        return processBlockFace(level,
                occludedState,
                occludedPos,
                occludingState,
                occludingPos,
                occludedFace,
                (occluded, occluding) -> !Shapes.joinIsNotEmpty(occluded, occluding, BooleanOp.ONLY_FIRST));
    }

    /**
     * Compare the occlusion shape of two blocks to determine if their faces match.
     */
    public static boolean faceMatch(BlockGetter level,
                                    BlockState fromState,
                                    BlockPos fromPos,
                                    BlockState toState,
                                    BlockPos toPos,
                                    Direction fromFace) {
        return processBlockFace(level,
                fromState,
                fromPos,
                toState,
                toPos,
                fromFace,
                (from, to) -> !Shapes.joinIsNotEmpty(from, to, BooleanOp.NOT_SAME));
    }

    public static VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        IMultiStateCopycatBlock copycatBlock = (IMultiStateCopycatBlock) state.getBlock();
        Vec3i scale = copycatBlock.vectorScale(state);
        Vec3i part = copycatBlock.getVectorFromProperty(state, property);
        return getPartialFaceShape(state.getOcclusionShape(level, BlockPos.ZERO), face, part, scale);
    }

    public static VoxelShape getPartialFaceShape(VoxelShape voxelShape, Direction direction, Vec3i part, Vec3i scale) {
        int i;
        Axis axis = direction.getAxis();
        double startX = part.getX() / (double) scale.getX();
        double startY = part.getY() / (double) scale.getY();
        double startZ = part.getZ() / (double) scale.getZ();
        double sizeX = 1.0 / scale.getX();
        double sizeY = 1.0 / scale.getY();
        double sizeZ = 1.0 / scale.getZ();
        double endX = startX + sizeX;
        double endY = startY + sizeY;
        double endZ = startZ + sizeZ;
        VoxelShape bounds = Shapes.box(startX, startY, startZ, endX, endY, endZ);
        voxelShape = Shapes.joinUnoptimized(voxelShape, bounds, BooleanOp.AND);
        int axisSize = ((VoxelShapeAccessor) voxelShape).copycats$getShape().getSize(axis);
        boolean isEmpty = false;
        if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            isEmpty = DoubleMath.fuzzyCompare(voxelShape.max(axis), switch (axis) {
                case X -> endX;
                case Y -> endY;
                case Z -> endZ;
            }, 1.0E-7) < 0;
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(endX, endY, endZ), -1, axisSize)) - 1;
        } else {
            isEmpty = DoubleMath.fuzzyCompare(voxelShape.min(axis), switch (axis) {
                case X -> startX;
                case Y -> startY;
                case Z -> startZ;
            }, 1.0E-7) > 0;
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(startX, startY, startZ), -1, axisSize));
        }
        if (isEmpty) {
            return Shapes.empty();
        }
        return new SliceShape(voxelShape, axis, i);
    }
}

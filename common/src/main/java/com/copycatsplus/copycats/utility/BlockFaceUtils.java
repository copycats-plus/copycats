package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.mixin.copycat.VoxelShapeAccessor;
import it.unimi.dsi.fastutil.objects.Object2ByteLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;

public class BlockFaceUtils {
    private static final ThreadLocal<Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey>> FACE_MATCH_CACHE = ThreadLocal.withInitial(() -> {
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> cacheMap = new Object2ByteLinkedOpenHashMap<>(2048, 0.25f) {

            @Override
            protected void rehash(int i) {
            }
        };
        cacheMap.defaultReturnValue((byte) 127);
        return cacheMap;
    });

    public static boolean facesMatch(BlockGetter level, BlockState fromState, BlockPos fromPos, BlockState toState, BlockPos toPos, Direction fromFace) {
        if (level instanceof ScaledBlockAndTintGetter scaledWorld) {
            Vec3i scale = scaledWorld.getScale();
            VoxelShape fromShape;
            BlockPos truePos = scaledWorld.getTruePos(fromPos);
            if (fromState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock && copycatBlock.vectorScale(fromState).equals(scale)) {
                String property = scaledWorld.getPropertyForRender(fromState, fromPos);
                if (!copycatBlock.partExists(fromState, property))
                    return false;
                Vec3i inner = copycatBlock.getVectorFromProperty(fromState, property);
                fromShape = getPartialFaceShape(fromState.getOcclusionShape(scaledWorld.getWrapped(), truePos),
                        fromFace,
                        inner.getX() / (double) scale.getX(),
                        inner.getY() / (double) scale.getY(),
                        inner.getZ() / (double) scale.getZ(),
                        1.0 / scale.getX(),
                        1.0 / scale.getY(),
                        1.0 / scale.getZ()
                );
            } else {
                fromShape = fromState.getFaceOcclusionShape(scaledWorld.getWrapped(), truePos, fromFace);
            }
            if (fromShape.isEmpty()) {
                return false;
            }
            VoxelShape toShape;
            BlockPos toTruePos = scaledWorld.getTruePos(toPos);
            if (toState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock2 && copycatBlock2.vectorScale(toState).equals(scaledWorld.getScale())) {
                String toProperty = scaledWorld.getPropertyForRender(toState, toPos);
                Vec3i toInner = copycatBlock2.getVectorFromProperty(toState, toProperty);
                toShape = getPartialFaceShape(toState.getOcclusionShape(scaledWorld.getWrapped(), toTruePos),
                        fromFace.getOpposite(),
                        toInner.getX() / (double) scale.getX(),
                        toInner.getY() / (double) scale.getY(),
                        toInner.getZ() / (double) scale.getZ(),
                        1.0 / scale.getX(),
                        1.0 / scale.getY(),
                        1.0 / scale.getZ()
                );
            } else {
                toShape = toState.getFaceOcclusionShape(scaledWorld.getWrapped(), toTruePos, fromFace.getOpposite());
            }
            return !Shapes.joinIsNotEmpty(fromShape, toShape, BooleanOp.ONLY_FIRST);
        }

        Block.BlockStatePairKey blockStatePair = new Block.BlockStatePairKey(fromState, toState, fromFace);
        Object2ByteLinkedOpenHashMap<Block.BlockStatePairKey> occlusionMap = FACE_MATCH_CACHE.get();
        byte b0 = occlusionMap.getAndMoveToFirst(blockStatePair);
        if (b0 != 127) {
            return b0 == 0;
        }
        VoxelShape fromShape = fromState.getFaceOcclusionShape(level, fromPos, fromFace);
        if (fromShape.isEmpty()) {
            return false;
        }
        VoxelShape toShape = toState.getFaceOcclusionShape(level, toPos, fromFace.getOpposite());
        boolean mismatch = Shapes.joinIsNotEmpty(fromShape, toShape, BooleanOp.ONLY_FIRST);
        if (occlusionMap.size() == 2048) {
            occlusionMap.removeLastByte();
        }
        occlusionMap.putAndMoveToFirst(blockStatePair, (byte) (mismatch ? 1 : 0));
        return !mismatch;
    }

    public static VoxelShape getPartialFaceShape(VoxelShape voxelShape, Direction direction, double startX, double startY, double startZ, double sizeX, double sizeY, double sizeZ) {
        int i;
        Direction.Axis axis = direction.getAxis();
        double endX = startX + sizeX;
        double endY = startY + sizeY;
        double endZ = startZ + sizeZ;
        VoxelShape bounds = Shapes.box(startX, startY, startZ, endX, endY, endZ);
        voxelShape = Shapes.joinUnoptimized(voxelShape, bounds, BooleanOp.AND);
        int axisSize = ((VoxelShapeAccessor) voxelShape).copycats$getShape().getSize(axis);
        if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(endX, endY, endZ), -1, axisSize)) - 1;
        } else {
            i = Mth.floor(Mth.clamp(axisSize * axis.choose(startX, startY, startZ), -1, axisSize));
        }
        return new SliceShape(voxelShape, axis, i);
    }
}

package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.stacked_half_layer.CopycatStackedHalfLayerBlock;
import com.copycatsplus.copycats.content.copycat.vertical_half_layer.CopycatVerticalHalfLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.CopycatTransformableState;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.OctahedralGroup;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Function;

import static net.minecraft.core.Direction.Axis;
import static net.minecraft.core.Direction.AxisDirection;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatHalfLayerBlock extends WaterloggedMultiStateCopycatBlock implements ISpecialBlockItemRequirement {


    public static final EnumProperty<Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final IntegerProperty POSITIVE_LAYERS = IntegerProperty.create("positive_layers", 0, 8);
    public static final IntegerProperty NEGATIVE_LAYERS = IntegerProperty.create("negative_layers", 0, 8);
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;

    private static record FaceData(String property, Axis axis, Half half, int layers, Direction face) {
    }

    public CopycatHalfLayerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(AXIS, Axis.X)
                .setValue(HALF, Half.BOTTOM)
                .setValue(POSITIVE_LAYERS, 0)
                .setValue(NEGATIVE_LAYERS, 0)
        );
        this.shapesCache = this.getShapeForEachState(CopycatHalfLayerBlock::calculateMultiFaceShape);
        ImmutableMap.Builder<FaceData, VoxelShape> builder = ImmutableMap.builder();
        for (String property : storageProperties()) {
            for (Axis axis : AXIS.getPossibleValues()) {
                for (Half half : HALF.getPossibleValues()) {
                    for (int layers : POSITIVE_LAYERS.getPossibleValues()) {
                        for (Direction face : Direction.values()) {
                            builder.put(new FaceData(property, axis, half, layers, face), BlockFaceUtils.getPartialFaceShape(null, defaultBlockState().setValue(AXIS, axis).setValue(HALF, half).setValue(NEGATIVE_LAYERS, layers).setValue(POSITIVE_LAYERS, layers), property, face));
                        }
                    }
                }
            }
        }
        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return NEGATIVE_LAYERS.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return switch (state.getValue(AXIS)) {
            case X -> new Vec3i(2, 1, 1);
            case Y -> new Vec3i(1, 2, 1);
            case Z -> new Vec3i(1, 1, 2);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(POSITIVE_LAYERS.getName())) {
            return state.getValue(POSITIVE_LAYERS) > 0;
        } else if (property.equals(NEGATIVE_LAYERS.getName())) {
            return state.getValue(NEGATIVE_LAYERS) > 0;
        }
        return false;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(POSITIVE_LAYERS.getName(), NEGATIVE_LAYERS.getName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(POSITIVE_LAYERS.getName()) ? 1 : 0;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        if (hitLocation.get(state.getValue(AXIS)) > 0) {
            return POSITIVE_LAYERS.getName();
        } else {
            return NEGATIVE_LAYERS.getName();
        }
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch (state.getValue(AXIS)) {
            case X -> property.equals(POSITIVE_LAYERS.getName()) ? new Vec3i(1, 0, 0) : new Vec3i(0, 0, 0);
            case Y -> property.equals(POSITIVE_LAYERS.getName()) ? new Vec3i(0, 1, 0) : new Vec3i(0, 0, 0);
            case Z -> property.equals(POSITIVE_LAYERS.getName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            Vec3 clickPosition = context.getClickLocation()
                    .add(Vec3.atLowerCornerOf(context.getClickedFace().getNormal()).scale(1 / 16f))
                    .subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
            IntegerProperty targetProp;
            if (state.getValue(AXIS).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5f) {
                targetProp = NEGATIVE_LAYERS;
            } else {
                targetProp = POSITIVE_LAYERS;
            }
            if (state.getValue(targetProp) < 8)
                return state.cycle(targetProp);
            else {
                Copycats.LOGGER.warn("Can't figure out where to place a half layer! Please file an issue if you see this.");
                return state;
            }
        } else {
            Axis axis = context.getHorizontalDirection().getAxis();
            Vec3 clickPosition = context.getClickLocation()
                    .subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
            return stateForPlacement
                    .setValue(AXIS, axis)
                    .setValue(HALF, clickPosition.y > 0.5 ? Half.TOP : Half.BOTTOM)
                    .setValue(axis.choose(clickPosition.x, clickPosition.y, clickPosition.z) > 0.5 ? POSITIVE_LAYERS : NEGATIVE_LAYERS, 1);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        Direction clickFace = pUseContext.getClickedFace();
        if (clickFace.getAxis().isVertical() && (pState.getValue(HALF) == Half.TOP) == (clickFace == Direction.UP)) {
            return false;
        }
        Vec3 clickPosition = pUseContext.getClickLocation()
                .add(Vec3.atLowerCornerOf(pUseContext.getClickedFace().getNormal()).scale(1 / 16f))
                .subtract(Vec3.atLowerCornerOf(pUseContext.getClickedPos()));
        IntegerProperty targetProp;
        if (pState.getValue(AXIS).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5f) {
            targetProp = NEGATIVE_LAYERS;
        } else {
            targetProp = POSITIVE_LAYERS;
        }
        if (pState.getValue(targetProp) == 8) return false;
        return true;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(POSITIVE_LAYERS) + state.getValue(NEGATIVE_LAYERS) <= 1)
            return super.onSneakWrenched(state, context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Vec3 clickPosition = context.getClickLocation()
                .subtract(Vec3.atLowerCornerOf(context.getClickedFace().getNormal()).scale(1 / 16f))
                .subtract(Vec3.atLowerCornerOf(pos));
        IntegerProperty targetProp;
        if (state.getValue(AXIS).choose(clickPosition.x, clickPosition.y, clickPosition.z) < 0.5f) {
            targetProp = NEGATIVE_LAYERS;
        } else {
            targetProp = POSITIVE_LAYERS;
        }
        if (state.getValue(targetProp) == 1)
            onWrenched(state, context);
        if (world instanceof ServerLevel serverLevel) {
            if (player != null) {
                List<ItemStack> drops = Block.getDrops(
                        state.setValue(POSITIVE_LAYERS, 0).setValue(NEGATIVE_LAYERS, 0).setValue(targetProp, 1),
                        serverLevel, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            // need to call updateShape before setBlock to schedule a tick for water
            world.setBlockAndUpdate(pos, state.setValue(targetProp, state.getValue(targetProp) - 1).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return ICopycatBlock.getRequiredItemsForLayer(state, POSITIVE_LAYERS).union(ICopycatBlock.getRequiredItemsForLayer(state, NEGATIVE_LAYERS));
    }

    public static CopycatTransformableState<Integer> toTransformableState(BlockState state) {
        if (state.is(CCBlocks.COPYCAT_HALF_LAYER.get())) {
            Axis axis = state.getValue(AXIS);
            Half half = state.getValue(HALF);
            return CopycatTransformableState.create(t -> {
                t.addPart(axis == Axis.X ? 12 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 12 : 8).setData(state.getValue(POSITIVE_LAYERS));
                t.addPart(axis == Axis.X ? 4 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 4 : 8).setData(state.getValue(NEGATIVE_LAYERS));
            });
        } else if (state.is(CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatVerticalHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(12, 8, 16).rotateY(facing).setData(state.getValue(POSITIVE_LAYERS));
                t.addPart(4, 8, 16).rotateY(facing).setData(state.getValue(NEGATIVE_LAYERS));
            });
        } else if (state.is(CCBlocks.COPYCAT_STACKED_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatStackedHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(8, 12, 16).rotateY(facing).setData(state.getValue(POSITIVE_LAYERS));
                t.addPart(8, 4, 16).rotateY(facing).setData(state.getValue(NEGATIVE_LAYERS));
            });
        } else {
            throw new IllegalArgumentException("Unsupported block state");
        }
    }

    public static CopycatTransformableState<MaterialItemStorage.MaterialItem> toTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be) {
        if (state.is(CCBlocks.COPYCAT_HALF_LAYER.get())) {
            Axis axis = state.getValue(AXIS);
            Half half = state.getValue(HALF);
            return CopycatTransformableState.create(t -> {
                t.addPart(axis == Axis.X ? 12 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 12 : 8)
                        .setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(axis == Axis.X ? 4 : 8, half == Half.BOTTOM ? 0 : 16, axis == Axis.Z ? 4 : 8)
                        .setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        } else if (state.is(CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatVerticalHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(12, 8, 16).rotateY(facing)
                        .setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(4, 8, 16).rotateY(facing)
                        .setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        } else if (state.is(CCBlocks.COPYCAT_STACKED_HALF_LAYER.get())) {
            Direction facing = state.getValue(CopycatStackedHalfLayerBlock.FACING);
            return CopycatTransformableState.create(t -> {
                t.addPart(8, 12, 16).rotateY(facing)
                        .setData(be.getMaterialItemStorage().getMaterialItem(POSITIVE_LAYERS.getName()));
                t.addPart(8, 4, 16).rotateY(facing)
                        .setData(be.getMaterialItemStorage().getMaterialItem(NEGATIVE_LAYERS.getName()));
            });
        } else {
            throw new IllegalArgumentException("Unsupported block state");
        }
    }

    public static BlockState fromTransformableState(BlockState state, CopycatTransformableState<Integer> transformableState) {
        CopycatTransformableState.Part<Integer> firstPart = transformableState.parts.get(0);
        Direction facing = firstPart.getFacing();
        if (facing.getAxis().isVertical()) {
            Axis axis = firstPart.getHorizontalFacing().getAxis();
            Half half = firstPart.isTop() ? Half.TOP : Half.BOTTOM;
            state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_HALF_LAYER.getDefaultState())
                    .setValue(CopycatHalfLayerBlock.AXIS, axis)
                    .setValue(CopycatHalfLayerBlock.HALF, half);
            for (CopycatTransformableState.Part<Integer> part : transformableState.parts) {
                boolean positive = part.vector.get(axis) > 8;
                state = state.setValue(positive ? POSITIVE_LAYERS : NEGATIVE_LAYERS, part.data);
            }
        } else if (firstPart.vector.getY() == 8) {
            state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_VERTICAL_HALF_LAYER.getDefaultState())
                    .setValue(CopycatVerticalHalfLayerBlock.FACING, facing);
            for (CopycatTransformableState.Part<Integer> part : transformableState.parts) {
                boolean right = part.isRight(facing);
                state = state.setValue(right ? NEGATIVE_LAYERS : POSITIVE_LAYERS, part.data);
            }
        } else {
            state = BlockUtils.tryCopyProperties(state, CCBlocks.COPYCAT_STACKED_HALF_LAYER.getDefaultState())
                    .setValue(CopycatStackedHalfLayerBlock.FACING, facing);
            for (CopycatTransformableState.Part<Integer> part : transformableState.parts) {
                boolean top = part.isTop();
                state = state.setValue(top ? POSITIVE_LAYERS : NEGATIVE_LAYERS, part.data);
            }
        }
        return state;
    }

    public static void fromTransformableStorage(BlockState state, IMultiStateCopycatBlockEntity be, CopycatTransformableState<MaterialItemStorage.MaterialItem> transformableState) {
        CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> firstPart = transformableState.parts.get(0);
        Direction facing = firstPart.getFacing();
        if (facing.getAxis().isVertical()) {
            Axis axis = firstPart.getHorizontalFacing().getAxis();
            for (CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> part : transformableState.parts) {
                boolean positive = part.vector.get(axis) > 8;
                be.getMaterialItemStorage().storeMaterialItem(positive ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName(), part.data);
            }
        } else if (firstPart.vector.getY() == 8) {
            for (CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> part : transformableState.parts) {
                boolean right = part.isRight(facing);
                be.getMaterialItemStorage().storeMaterialItem(right ? NEGATIVE_LAYERS.getName() : POSITIVE_LAYERS.getName(), part.data);
            }
        } else {
            for (CopycatTransformableState.Part<MaterialItemStorage.MaterialItem> part : transformableState.parts) {
                boolean top = part.isTop();
                be.getMaterialItemStorage().storeMaterialItem(top ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName(), part.data);
            }
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return fromTransformableState(state, toTransformableState(state).transform(transform));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        state = fromTransformableState(state, toTransformableState(state).untransform(transform));
        fromTransformableStorage(state, be, toTransformableStorage(state, be).transform(transform));
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(AXIS, HALF, POSITIVE_LAYERS, NEGATIVE_LAYERS));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, CCShapes.HALF_LAYER_BOTTOM.get(pState.getValue(AXIS)).get(pState.getValue(HALF)).get(pState.getValue(NEGATIVE_LAYERS)).toShape(), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, CCShapes.HALF_LAYER_TOP.get(pState.getValue(AXIS)).get(pState.getValue(HALF)).get(pState.getValue(POSITIVE_LAYERS)).toShape(), BooleanOp.OR);
        return shape.optimize();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.get(pState));
    }

    @Override
    public VoxelShape getPartialFaceShape(BlockGetter level, BlockState state, String property, Direction face) {
        if (!partExists(state, property)) return Shapes.empty();
        return Objects.requireNonNull(partialFaceCache.getOrDefault(
                new FaceData(property, state.getValue(AXIS), state.getValue(HALF), state.getValue(property.equals(NEGATIVE_LAYERS.getName()) ? NEGATIVE_LAYERS : POSITIVE_LAYERS), face),
                Shapes.empty()
        ));
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level,
                                     BlockPos pos,
                                     BlockState state,
                                     BlockState neighborState,
                                     Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }
}

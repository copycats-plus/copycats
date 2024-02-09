package com.copycatsplus.copycats.content.copycat.steplayer;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.CTWaterloggedCopycatBlock;
import com.google.common.collect.ImmutableMap;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static net.minecraft.core.Direction.Axis;
import static net.minecraft.core.Direction.AxisDirection;

public class CopycatStepLayerBlock extends CTWaterloggedCopycatBlock implements ISpecialBlockItemRequirement {


    public static final EnumProperty<Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final IntegerProperty POSITIVE_LAYERS = IntegerProperty.create("positive_layers", 0, 8);
    public static final IntegerProperty NEGATIVE_LAYERS = IntegerProperty.create("negative_layers", 0, 8);
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;

    private static final VoxelShaper[] TOP_BY_LAYER = new VoxelShaper[]{
            CCShapes.EMPTY,
            CCShapes.STEP_LAYER_TOP_2PX,
            CCShapes.STEP_LAYER_TOP_4PX,
            CCShapes.STEP_LAYER_TOP_6PX,
            CCShapes.STEP_LAYER_TOP_8PX,
            CCShapes.STEP_LAYER_TOP_10PX,
            CCShapes.STEP_LAYER_TOP_12PX,
            CCShapes.STEP_LAYER_TOP_14PX,
            CCShapes.STEP_LAYER_TOP_16PX
    };
    private static final VoxelShaper[] BOTTOM_BY_LAYER = new VoxelShaper[]{
            CCShapes.EMPTY,
            CCShapes.STEP_LAYER_BOTTOM_2PX,
            CCShapes.STEP_LAYER_BOTTOM_4PX,
            CCShapes.STEP_LAYER_BOTTOM_6PX,
            CCShapes.STEP_LAYER_BOTTOM_8PX,
            CCShapes.STEP_LAYER_BOTTOM_10PX,
            CCShapes.STEP_LAYER_BOTTOM_12PX,
            CCShapes.STEP_LAYER_BOTTOM_14PX,
            CCShapes.STEP_LAYER_BOTTOM_16PX
    };

    public CopycatStepLayerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(AXIS, Axis.X)
                .setValue(HALF, Half.BOTTOM)
                .setValue(POSITIVE_LAYERS, 0)
                .setValue(NEGATIVE_LAYERS, 0)
        );
        this.shapesCache = this.getShapeForEachState(CopycatStepLayerBlock::calculateMultiFaceShape);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
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
                Copycats.LOGGER.warn("Can't figure out where to place a step layer! Please file an issue if you see this.");
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
        if (world instanceof ServerLevel serverLevel) {
            if (player != null && !player.isCreative()) {
                // Respect loot tables
                List<ItemStack> drops = Block.getDrops(
                        state.setValue(POSITIVE_LAYERS, 0).setValue(NEGATIVE_LAYERS, 0).setValue(targetProp, 1),
                        serverLevel, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                for (ItemStack drop : drops) {
                    player.getInventory().placeItemBackInInventory(drop);
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
        return new ItemRequirement(
                ItemRequirement.ItemUseType.CONSUME,
                new ItemStack(asItem(), state.getValue(POSITIVE_LAYERS) + state.getValue(NEGATIVE_LAYERS))
        );
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, Rotation rot) {
        Function<Axis, Axis> swap = axis -> axis == Axis.Z ? Axis.X : Axis.Z;
        return switch (rot) {
            case NONE -> state;
            case CLOCKWISE_90 -> {
                Axis axis = state.getValue(AXIS);
                if (axis == Axis.X) {
                    yield state.setValue(AXIS, swap.apply(axis));
                } else {
                    yield state.setValue(AXIS, swap.apply(axis))
                            .setValue(POSITIVE_LAYERS, state.getValue(NEGATIVE_LAYERS))
                            .setValue(NEGATIVE_LAYERS, state.getValue(POSITIVE_LAYERS));
                }
            }
            case CLOCKWISE_180 -> state
                    .setValue(POSITIVE_LAYERS, state.getValue(NEGATIVE_LAYERS))
                    .setValue(NEGATIVE_LAYERS, state.getValue(POSITIVE_LAYERS));
            case COUNTERCLOCKWISE_90 -> {
                Axis axis = state.getValue(AXIS);
                if (axis == Axis.Z) {
                    yield state.setValue(AXIS, swap.apply(axis));
                } else {
                    yield state.setValue(AXIS, swap.apply(axis))
                            .setValue(POSITIVE_LAYERS, state.getValue(NEGATIVE_LAYERS))
                            .setValue(NEGATIVE_LAYERS, state.getValue(POSITIVE_LAYERS));
                }
            }
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS))));
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
        shape = Shapes.or(shape,
                (pState.getValue(HALF) == Half.TOP ? TOP_BY_LAYER : BOTTOM_BY_LAYER)[pState.getValue(POSITIVE_LAYERS)]
                        .get(Direction.get(AxisDirection.POSITIVE, pState.getValue(AXIS)))
        );
        shape = Shapes.or(shape,
                (pState.getValue(HALF) == Half.TOP ? TOP_BY_LAYER : BOTTOM_BY_LAYER)[pState.getValue(NEGATIVE_LAYERS)]
                        .get(Direction.get(AxisDirection.NEGATIVE, pState.getValue(AXIS)))
        );
        return shape;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.get(pState));
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return false;
    }

}

package com.copycatsplus.copycats.content.copycat.vertical_slice;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VoxelShaper;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.copycatsplus.copycats.content.copycat.MathHelper.DirectionFromDelta;
import static net.minecraft.core.Direction.Axis;

public class CopycatVerticalSliceBlock extends CTWaterloggedCopycatBlock implements ISpecialBlockItemRequirement {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    private static final VoxelShaper[] SHAPE_BY_LAYER = new VoxelShaper[]{CCShapes.EMPTY, CCShapes.SLICE_VERTICAL_2PX, CCShapes.SLICE_VERTICAL_4PX, CCShapes.SLICE_VERTICAL_6PX, CCShapes.SLICE_VERTICAL_8PX, CCShapes.SLICE_VERTICAL_10PX, CCShapes.SLICE_VERTICAL_12PX, CCShapes.SLICE_VERTICAL_14PX, CCShapes.SLICE_VERTICAL_16PX};


    public CopycatVerticalSliceBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(LAYERS, 1)
        );
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        Direction direction = state.getValue(FACING);
        int layers = state.getValue(LAYERS);
        BlockState toState = reader.getBlockState(toPos);

        if (toState.is(this)) {
            // connecting to another copycat beam
            return toState.getValue(FACING) != direction || toState.getValue(LAYERS) != layers;
        } else {
            // doesn't connect to any other blocks
            return true;
        }
    }

    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos,
                                            BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
                if (!toState.is(this)) return false;
        if (!state.is(this)) return false;
        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction face = DirectionFromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) {
            return false;
        }

        Direction facing = state.getValue(FACING);
        int layers = state.getValue(LAYERS);

        if (toState.is(this)) {
            try {
                return toState.getValue(FACING) == facing && toState.getValue(LAYERS) == layers && face.getAxis() == Axis.Y;
            } catch (IllegalStateException ignored) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState otherState, Direction face, BlockPos fromPos, BlockPos toPos) {
        return (canConnectTexturesToward(reader, fromPos, toPos, reader.getBlockState(fromPos)) ? getMaterial(reader, toPos) : null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        Direction facing = state.getValue(FACING);
        return face.getAxis() == Axis.Y || face == facing || face == facing.getCounterClockWise();
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }

    private static final Map<Pair<Integer, Integer>, Direction> VERTICAL_POSITION_MAP = new HashMap<>();
    private static final Map<Pair<Direction, Integer>, Direction> HORIZONTAL_POSITION_MAP = new HashMap<>();

    static {
        for (Direction main : Iterate.horizontalDirections) {
            Direction cross = main.getCounterClockWise();

            int mainOffset = main.getAxisDirection().getStep();
            int crossOffset = cross.getAxisDirection().getStep();

            if (main.getAxis() == Axis.X)
                VERTICAL_POSITION_MAP.put(Pair.of(mainOffset, crossOffset), main);
            else
                VERTICAL_POSITION_MAP.put(Pair.of(crossOffset, mainOffset), main);

            HORIZONTAL_POSITION_MAP.put(Pair.of(main.getOpposite(), crossOffset), main);
            HORIZONTAL_POSITION_MAP.put(Pair.of(cross.getOpposite(), mainOffset), main);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            if (state.getValue(LAYERS) < 8)
                return state.cycle(LAYERS);
            else {
                Copycats.LOGGER.warn("Can't figure out where to place a slice! Please file an issue if you see this.");
                return state;
            }
        } else {
            int xOffset = context.getClickLocation().x - context.getClickedPos().getX() > 0.5 ? 1 : -1;
            int zOffset = context.getClickLocation().z - context.getClickedPos().getZ() > 0.5 ? 1 : -1;

            if (context.getClickedFace().getAxis() == Axis.Y) {
                return stateForPlacement.setValue(FACING, VERTICAL_POSITION_MAP.get(Pair.of(xOffset, zOffset)));
            } else {
                return stateForPlacement.setValue(FACING, HORIZONTAL_POSITION_MAP.get(
                        Pair.of(context.getClickedFace(), context.getClickedFace().getAxis() == Axis.X ? zOffset : xOffset)
                ));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        if (pState.getValue(LAYERS) == 8) return false;
        Direction facing = pState.getValue(FACING);
        return pUseContext.getClickedFace() == facing.getOpposite() || pUseContext.getClickedFace() == facing.getClockWise();
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(LAYERS) <= 1)
            return super.onSneakWrenched(state, context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (world instanceof ServerLevel serverLevel) {
            if (player != null && !player.isCreative()) {
                // Respect loot tables
                List<ItemStack> drops = Block.getDrops(state.setValue(LAYERS, 1), serverLevel, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                for (ItemStack drop : drops) {
                    player.getInventory().placeItemBackInInventory(drop);
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            // need to call updateShape before setBlock to schedule a tick for water
            world.setBlockAndUpdate(pos, state.setValue(LAYERS, state.getValue(LAYERS) - 1).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return new ItemRequirement(
                ItemRequirement.ItemUseType.CONSUME,
                new ItemStack(asItem(), state.getValue(LAYERS))
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, LAYERS));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)].get(pState.getValue(FACING));
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }


    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            if (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite())) {
                int layers = state.getValue(LAYERS);
                int neighborLayers = neighborState.getValue(LAYERS);
                if (layers == 8 && neighborLayers == 8) return true;
                return dir.getAxis().isVertical() &&
                        neighborState.getValue(FACING) == state.getValue(FACING) &&
                        layers == neighborLayers;
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
        Axis mirrorAxis = null;
        for (Axis axis : Iterate.axes) {
            if (pMirror.rotation().inverts(axis)) {
                mirrorAxis = axis;
                break;
            }
        }
        if (mirrorAxis == null || mirrorAxis.isVertical()) {
            return super.mirror(pState, pMirror);
        }
        Direction facing = pState.getValue(FACING);
        if (facing.getAxis() != mirrorAxis) {
            return pState.setValue(FACING, facing.getClockWise());
        } else {
            return pState.setValue(FACING, facing.getCounterClockWise());
        }
    }

}

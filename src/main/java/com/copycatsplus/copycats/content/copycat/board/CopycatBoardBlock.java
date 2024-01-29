package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCShapes;
import com.google.common.collect.ImmutableMap;
import com.simibubi.create.content.decoration.copycat.WaterloggedCopycatBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public class CopycatBoardBlock extends WaterloggedCopycatBlock implements ISpecialBlockItemRequirement {
    public static BooleanProperty UP = BlockStateProperties.UP;
    public static BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static BooleanProperty EAST = BlockStateProperties.EAST;
    public static BooleanProperty WEST = BlockStateProperties.WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;

    public CopycatBoardBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
        );
        this.shapesCache = this.getShapeForEachState(CopycatBoardBlock::calculateMultifaceShape);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST));
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return !reader.getBlockState(toPos).is(this);
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return reader.getBlockState(toPos).is(this);
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return !state.getValue(byDirection(face.getOpposite()));
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return !canFaceBeOccluded(state, face);
    }

    private static VoxelShape calculateMultifaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        for (Direction direction : Iterate.directions) {
            if (pState.getValue(byDirection(direction))) {
                shape = Shapes.or(shape, CCShapes.CASING_1PX.get(direction));
            }
        }
        return shape;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.get(pState));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            if (!state.getValue(byDirection(context.getClickedFace().getOpposite())))
                return state.setValue(byDirection(context.getClickedFace().getOpposite()), true);
            else
                return state.setValue(byDirection(context.getClickedFace()), true);
        } else {
            return stateForPlacement.setValue(byDirection(context.getClickedFace().getOpposite()), true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        if (!pState.getValue(byDirection(pUseContext.getClickedFace().getOpposite()))) {
            Direction direction = pUseContext.getClickedFace().getOpposite();
            double pos = getByAxis(pUseContext.getClickedPos(), direction.getAxis());
            if (getByAxis(direction.getNormal(), direction.getAxis()) > 0) pos += 1;
            double loc = getByAxis(pUseContext.getClickLocation(), direction.getAxis());
            if (Math.abs(pos - loc) < 2 / 16.0) {
                return true;
            }
        }
        if (!pState.getValue(byDirection(pUseContext.getClickedFace()))) {
            double hitLoc = getByAxis(pUseContext.getClickLocation(), pUseContext.getClickedFace().getAxis());
            int direction = getByAxis(pUseContext.getClickedFace().getNormal(), pUseContext.getClickedFace().getAxis());
            double offset = hitLoc - Math.round(hitLoc);
            if (Mth.sign(direction) == Mth.sign(offset) && Math.abs(offset) < 2 / 16.0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        int faceCount = 0;
        for (Direction direction : Iterate.directions) {
            if (state.getValue(byDirection(direction))) faceCount++;
        }
        if (faceCount <= 1) return super.onSneakWrenched(state, context);

        List<Direction> options = new ArrayList<>(6);
        for (Direction direction : Iterate.directions) {
            if (!state.getValue(byDirection(direction))) continue;
            double pos = getByAxis(context.getClickedPos(), direction.getAxis());
            if (getByAxis(direction.getNormal(), direction.getAxis()) > 0) pos += 1;
            double loc = getByAxis(context.getClickLocation(), direction.getAxis());
            if (Math.abs(pos - loc) < 2 / 16.0) {
                options.add(direction);
            }
        }
        if (options.size() > 1) {
            Direction backup = options.get(0);
            options.removeIf(d -> d.getAxis() != context.getClickedFace().getAxis());
            if (options.size() == 0) options.add(backup);
        }
        if (options.size() == 0) {
            return super.onSneakWrenched(state, context);
        }

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (world instanceof ServerLevel) {
            if (player != null && !player.isCreative()) {
                List<ItemStack> drops = Block.getDrops(defaultBlockState().setValue(byDirection(options.get(0)), true), (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                for (ItemStack drop : drops) {
                    player.getInventory().placeItemBackInInventory(drop);
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, state.setValue(byDirection(options.get(0)), false).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return new ItemRequirement(
                ItemRequirement.ItemUseType.CONSUME,
                new ItemStack(asItem(), (int) Arrays.stream(Iterate.directions).filter(d -> state.getValue(byDirection(d))).count())
        );
    }

    private static int getByAxis(Vec3i pos, Direction.Axis axis) {
        return switch (axis) {
            case X -> pos.getX();
            case Y -> pos.getY();
            case Z -> pos.getZ();
        };
    }

    private static double getByAxis(Position pos, Direction.Axis axis) {
        return switch (axis) {
            case X -> pos.x();
            case Y -> pos.y();
            case Z -> pos.z();
        };
    }

    @Override
    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) && !state.getValue(byDirection(dir))) return false;
        if (neighborState.is(this) && !neighborState.getValue(byDirection(dir.getOpposite()))) return false;
        if (state.is(this) == neighborState.is(this)) {
            return (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite()));
        }

        return getMaterial(level, pos).skipRendering(neighborState, dir.getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, Rotation pRotation) {
        return mapDirections(pState, pRotation::rotate);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, Mirror pMirror) {
        return mapDirections(pState, pMirror::mirror);
    }

    private BlockState mapDirections(BlockState pState, Function<Direction, Direction> pDirectionalFunction) {
        BlockState blockstate = pState;

        for (Direction direction : Iterate.directions) {
            blockstate = blockstate.setValue(byDirection(pDirectionalFunction.apply(direction)), pState.getValue(byDirection(direction)));
        }

        return blockstate;
    }

    public static BooleanProperty byDirection(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }
}


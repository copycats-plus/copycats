package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock;
import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockFaceUtils;
import com.google.common.collect.ImmutableMap;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.utility.BlockUtils.transformFacing;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatBoardBlock extends WaterloggedMultiStateCopycatBlock implements ICustomCTBlocking, ISpecialBlockItemRequirement {
    public static BooleanProperty UP = BlockStateProperties.UP;
    public static BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static BooleanProperty EAST = BlockStateProperties.EAST;
    public static BooleanProperty WEST = BlockStateProperties.WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;
    private final ImmutableMap<FaceData, VoxelShape> partialFaceCache;

    public static record FaceData(String property, Direction direction) {
    }

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
        ImmutableMap.Builder<FaceData, VoxelShape> builder = ImmutableMap.builder();
        BlockState state = defaultBlockState()
                .setValue(UP, true)
                .setValue(DOWN, true)
                .setValue(NORTH, true)
                .setValue(SOUTH, true)
                .setValue(EAST, true)
                .setValue(WEST, true);
        for (String property : storageProperties()) {
            for (Direction face : Direction.values()) {
                builder.put(new FaceData(property, face), BlockFaceUtils.getPartialFaceShape(null, state, property, face));
            }
        }
        this.partialFaceCache = builder.build();
    }

    @Override
    public String defaultProperty() {
        return UP.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(UP, DOWN, NORTH, EAST, SOUTH, WEST).stream().map(BooleanProperty::getName).collect(Collectors.toSet());
    }

    @Override
    public int getColorIndex(String property) {
        if (property.equals(UP.getName())) return 0;
        if (property.equals(DOWN.getName())) return 0;
        if (property.equals(NORTH.getName())) return 1;
        if (property.equals(SOUTH.getName())) return 1;
        if (property.equals(EAST.getName())) return 2;
        if (property.equals(WEST.getName())) return 2;
        return 0;
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(UP.getName())) return state.getValue(UP);
        if (property.equals(DOWN.getName())) return state.getValue(DOWN);
        if (property.equals(NORTH.getName())) return state.getValue(NORTH);
        if (property.equals(SOUTH.getName())) return state.getValue(SOUTH);
        if (property.equals(EAST.getName())) return state.getValue(EAST);
        if (property.equals(WEST.getName())) return state.getValue(WEST);
        return false;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        facing = Direction.fromAxisAndDirection(facing.getAxis(), unscaledHit.get(facing.getAxis()) > 0.5 ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
        BooleanProperty face = byDirection(facing);
        return face.getName();
    }

    @Override
    public String getPropertyFromRender(String renderingProperty, BlockState state, BlockGetter level, Vec3i vector, BlockPos blockPos) {
        return renderingProperty;
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return Vec3i.ZERO;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(UP, DOWN, NORTH, SOUTH, EAST, WEST));
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos, BlockState toState) {
        return !reader.getBlockState(toPos).is(this);
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return reader.getBlockState(toPos).is(this);
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        if (!reader.getBlockState(ctPos).is(this)) return Optional.empty();
        return Optional.of(false);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return switch (pType) {
            case LAND -> (!pState.getValue(UP) && pState.getValue(DOWN) || pState.getValue(UP));
            default -> false;
        };
    }

    private static VoxelShape calculateMultifaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        for (Direction direction : Iterate.directions) {
            if (pState.getValue(byDirection(direction))) {
                shape = Shapes.joinUnoptimized(shape, CCShapes.BOARD.get(direction).toShape(), BooleanOp.OR);
            }
        }
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
                new FaceData(property, face),
                Shapes.empty()
        ));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;
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
        onWrenched(state, context);
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
            if (player != null) {
                List<ItemStack> drops = Block.getDrops(defaultBlockState().setValue(byDirection(options.get(0)), true), (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
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
        return IMultiStateCopycatBlock.getRequiredItemsForParts(state, UP, DOWN, NORTH, SOUTH, EAST, WEST);
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

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return mapDirections(state, dir -> transformFacing(transform, dir));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        be.getMaterialItemStorage().remapStorage(key -> directionToProperty(transformFacing(transform, propertyToDirection(key))));
    }

    private static Direction propertyToDirection(String property) {
        return switch (property) {
            case "up" -> Direction.UP;
            case "down" -> Direction.DOWN;
            case "north" -> Direction.NORTH;
            case "south" -> Direction.SOUTH;
            case "east" -> Direction.EAST;
            case "west" -> Direction.WEST;
            default -> throw new IllegalStateException("Unexpected value: " + property);
        };
    }

    public static String directionToProperty(Direction direction) {
        return direction.getName().toLowerCase(Locale.ROOT);
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

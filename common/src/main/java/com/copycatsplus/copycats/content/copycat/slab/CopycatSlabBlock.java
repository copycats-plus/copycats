package com.copycatsplus.copycats.content.copycat.slab;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.mojang.math.OctahedralGroup;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static com.copycatsplus.copycats.utility.BlockUtils.transformFacing;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatSlabBlock extends WaterloggedMultiStateCopycatBlock implements ISpecialBlockItemRequirement {

    public static final EnumProperty<Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<SlabType> SLAB_TYPE = BlockStateProperties.SLAB_TYPE;

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

    public CopycatSlabBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(AXIS, Axis.Y)
                .setValue(SLAB_TYPE, SlabType.BOTTOM));
    }

    @Override
    public String defaultProperty() {
        return SlabType.TOP.getSerializedName();
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
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        if (hitLocation.get(state.getValue(AXIS)) > 0) {
            return SlabType.TOP.getSerializedName();
        } else {
            return SlabType.BOTTOM.getSerializedName();
        }
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return switch (state.getValue(AXIS)) {
            case X -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(1, 0, 0) : new Vec3i(0, 0, 0);
            case Y -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(0, 1, 0) : new Vec3i(0, 0, 0);
            case Z -> property.equals(SlabType.TOP.getSerializedName()) ? new Vec3i(0, 0, 1) : new Vec3i(0, 0, 0);
        };
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        SlabType slabType = state.getValue(SLAB_TYPE);
        if (property.equals(SlabType.BOTTOM.getSerializedName())) {
            return slabType == SlabType.DOUBLE || slabType == SlabType.BOTTOM;
        } else if (property.equals(SlabType.TOP.getSerializedName())) {
            return slabType == SlabType.DOUBLE || slabType == SlabType.TOP;
        }
        return false;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(SlabType.TOP.getSerializedName(), SlabType.BOTTOM.getSerializedName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(SlabType.BOTTOM.getSerializedName()) ? 0 : 1;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult ray) {
        return InteractionUtils.sequential(
                () -> InteractionUtils.usePlacementHelper(placementHelperId, state, world, pos, player, hand, ray),
                () -> super.use(state, world, pos, player, hand, ray)
        );
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        if (state.getValue(SLAB_TYPE) != SlabType.DOUBLE) return super.onSneakWrenched(state, context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        String property = getPropertyFromInteraction(state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        if (!partExists(state, property)) return InteractionResult.FAIL;
        if (world instanceof ServerLevel) {
            if (player != null) {
                List<ItemStack> drops = Block.getDrops(defaultBlockState().setValue(SLAB_TYPE, property.equals(SlabType.BOTTOM.getSerializedName()) ? SlabType.BOTTOM : SlabType.TOP), (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, state.setValue(SLAB_TYPE, property.equals(SlabType.BOTTOM.getSerializedName()) ? SlabType.TOP : SlabType.BOTTOM).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return new ItemRequirement(
                ItemRequirement.ItemUseType.CONSUME,
                new ItemStack(asItem(), switch (state.getValue(SLAB_TYPE)) {
                    case BOTTOM, TOP -> 1;
                    case DOUBLE -> 2;
                })
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            return state
                    .setValue(SLAB_TYPE, SlabType.DOUBLE)
                    .setValue(WATERLOGGED, false);
        } else {
            Axis axis = context.getNearestLookingDirection().getAxis();
            boolean flag = switch (axis) {
                case X -> context.getClickLocation().x - (double) blockPos.getX() > 0.5D;
                case Y -> context.getClickLocation().y - (double) blockPos.getY() > 0.5D;
                case Z -> context.getClickLocation().z - (double) blockPos.getZ() > 0.5D;
            };
            Direction clickedFace = context.getClickedFace();
            return stateForPlacement
                    .setValue(AXIS, axis)
                    .setValue(SLAB_TYPE, clickedFace == Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE) || clickedFace.getAxis() != axis && !flag ? SlabType.BOTTOM : SlabType.TOP);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        SlabType slabtype = pState.getValue(SLAB_TYPE);
        Axis axis = pState.getValue(AXIS);
        if (slabtype != SlabType.DOUBLE && itemstack.is(this.asItem())) {
            boolean flag = switch (axis) {
                case X -> pUseContext.getClickLocation().x - (double) pUseContext.getClickedPos().getX() > 0.5D;
                case Y -> pUseContext.getClickLocation().y - (double) pUseContext.getClickedPos().getY() > 0.5D;
                case Z -> pUseContext.getClickLocation().z - (double) pUseContext.getClickedPos().getZ() > 0.5D;
            };
            Direction direction = pUseContext.getClickedFace();
            if (slabtype == SlabType.BOTTOM) {
                return direction == Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE) || flag;
            } else {
                return direction == Direction.fromAxisAndDirection(axis, AxisDirection.NEGATIVE) || !flag;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(AXIS).add(SLAB_TYPE));
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return super.isPathfindable(pState, pLevel, pPos, pType);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        SlabType type = pState.getValue(SLAB_TYPE);
        Axis axis = pState.getValue(AXIS);
        if (type == SlabType.DOUBLE) {
            return Shapes.block();
        } else if (type == SlabType.BOTTOM) {
            return CCShapes.SLAB_BOTTOM.get(axis).toShape();
        } else {
            return CCShapes.SLAB_TOP.get(axis).toShape();
        }
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
        return setApparentDirection(state, transformFacing(transform, getApparentDirection(state)));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        Axis axis = state.getValue(AXIS);
        // the given block state is post-transform, so we need to adjust the axis if the transform is a rotation
        if (transform.rotationAxis != null && transform.rotation == Rotation.CLOCKWISE_90 || transform.rotation == Rotation.COUNTERCLOCKWISE_90) {
            axis = switch (axis) {
                case X -> Direction.Axis.Z;
                case Z -> Direction.Axis.X;
                default -> axis;
            };
        }
        if (transformFacing(transform, Direction.fromAxisAndDirection(axis, AxisDirection.POSITIVE)).getAxisDirection() == AxisDirection.NEGATIVE) {
            be.getMaterialItemStorage().remapStorage(s -> s.equals(Half.BOTTOM.getSerializedName()) ? Half.TOP.getSerializedName() : Half.BOTTOM.getSerializedName());
        }
    }

    /**
     * Return the area of the face that is at the edge of the block.
     */
    public static FaceShape getFaceShape(BlockState state, Direction face) {
        SlabType slab = state.getValue(SLAB_TYPE);

        if (state.getValue(AXIS) != face.getAxis()) {
            return FaceShape.forSlabSide(slab);
        }

        return switch (slab) {
            case TOP -> FaceShape.fullOrNone(face.getAxisDirection() == AxisDirection.POSITIVE);
            case BOTTOM -> FaceShape.fullOrNone(face.getAxisDirection() == AxisDirection.NEGATIVE);
            case DOUBLE -> FaceShape.FULL;
        };
    }

    public static Direction getApparentDirection(BlockState state) {
        return Direction.fromAxisAndDirection(state.getValue(AXIS), state.getValue(SLAB_TYPE) == SlabType.BOTTOM ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE);
    }

    public static BlockState setApparentDirection(BlockState state, Direction direction) {
        SlabType type = state.getValue(SLAB_TYPE);
        if (type == SlabType.DOUBLE) {
            return state.setValue(AXIS, direction.getAxis());
        }
        if (getApparentDirection(state).getAxisDirection() != direction.getAxisDirection()) {
            return state.setValue(AXIS, direction.getAxis()).setValue(SLAB_TYPE, type == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM);
        } else {
            return state.setValue(AXIS, direction.getAxis());
        }
    }

    public enum FaceShape {
        FULL,
        TOP,
        BOTTOM,
        NONE;

        public static FaceShape forSlabSide(SlabType type) {
            return switch (type) {
                case TOP -> TOP;
                case BOTTOM -> BOTTOM;
                case DOUBLE -> FULL;
            };
        }

        public static FaceShape fullOrNone(boolean value) {
            return value ? FULL : NONE;
        }

        public static boolean canConnect(FaceShape shape1, FaceShape shape2) {
            return shape1 == shape2 || shape1 == FaceShape.FULL && shape2 != FaceShape.NONE || shape2 == FaceShape.FULL && shape1 != FaceShape.NONE;
        }

        public boolean hasContact() {
            return this != NONE;
        }
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {
        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return CCBlocks.COPYCAT_SLAB::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return CCBlocks.COPYCAT_SLAB::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(),
                    state.getValue(AXIS),
                    dir -> world.getBlockState(pos.relative(dir))
                            .canBeReplaced());

            if (directions.isEmpty())
                return PlacementOffset.fail();
            else {
                if (state.getValue(SLAB_TYPE).equals(SlabType.DOUBLE)) {
                    return PlacementOffset.success(pos.relative(directions.get(0)),
                            s -> s.setValue(AXIS, state.getValue(AXIS)).setValue(SLAB_TYPE, SlabType.BOTTOM));
                } else {
                    return PlacementOffset.success(pos.relative(directions.get(0)),
                            s -> s.setValue(AXIS, state.getValue(AXIS)).setValue(SLAB_TYPE, state.getValue(SLAB_TYPE)));
                }
            }
        }
    }

}

package com.copycatsplus.copycats.content.copycat.half_layer;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.model.ScaledBlockAndTintGetter;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    public CopycatHalfLayerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(AXIS, Axis.X)
                .setValue(HALF, Half.BOTTOM)
                .setValue(POSITIVE_LAYERS, 0)
                .setValue(NEGATIVE_LAYERS, 0)
        );
        this.shapesCache = this.getShapeForEachState(CopycatHalfLayerBlock::calculateMultiFaceShape);
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

    @Override
    public boolean isIgnoredConnectivitySide(String property, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);
        return !toState.is(this);
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (reader instanceof ScaledBlockAndTintGetter scaledReader && toState.is(this)) {
            String toProperty = scaledReader.getPropertyForRender(toState, toPos);
            int fromLayers = state.getValue(property.equals(POSITIVE_LAYERS.getName()) ? POSITIVE_LAYERS : NEGATIVE_LAYERS);
            int toLayers = toState.getValue(toProperty.equals(POSITIVE_LAYERS.getName()) ? POSITIVE_LAYERS : NEGATIVE_LAYERS);
            return fromLayers == toLayers;
        }
        return toState.is(this);
    }

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
    public @NotNull BlockState mirror(@NotNull BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(Direction.get(AxisDirection.POSITIVE, state.getValue(AXIS))));
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        Axis axis = state.getValue(AXIS);
        if (transform.mirror != null) {
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Z && axis == Axis.Z || transform.mirror.rotation() == OctahedralGroup.INVERT_X && axis == Axis.X) {
                be.getMaterialItemStorage().remapStorage(s -> s.equals(POSITIVE_LAYERS.getName()) ? NEGATIVE_LAYERS.getName() : POSITIVE_LAYERS.getName());
            }
        }
        if (transform.rotationAxis != null && transform.rotationAxis.isVertical()) {
            if (transform.rotation == Rotation.CLOCKWISE_90 && axis == Axis.X ||
                    transform.rotation == Rotation.CLOCKWISE_180 ||
                    transform.rotation == Rotation.COUNTERCLOCKWISE_90 && axis == Axis.Z) {
                be.getMaterialItemStorage().remapStorage(s -> s.equals(POSITIVE_LAYERS.getName()) ? NEGATIVE_LAYERS.getName() : POSITIVE_LAYERS.getName());
            }
        }
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
        VoxelShape shapeOverride = IMultiStateCopycatBlock.blockShapeOverride(pState, pLevel, pPos, pContext);
        if (shapeOverride != null) return shapeOverride;
        return Objects.requireNonNull(this.shapesCache.get(pState));
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level,
                                     BlockPos pos,
                                     BlockState state,
                                     BlockState neighborState,
                                     Direction dir) {
        return IMultiStateCopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }
}

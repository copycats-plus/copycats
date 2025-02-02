package com.copycatsplus.copycats.content.copycat.stacked_half_layer;

import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.google.common.collect.ImmutableMap;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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

import static com.copycatsplus.copycats.content.copycat.half_layer.CopycatHalfLayerBlock.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatStackedHalfLayerBlock extends WaterloggedMultiStateCopycatBlock implements ISpecialBlockItemRequirement {


    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;

    public CopycatStackedHalfLayerBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(POSITIVE_LAYERS, 0)
                .setValue(NEGATIVE_LAYERS, 0)
        );
        this.shapesCache = this.getShapeForEachState(CopycatStackedHalfLayerBlock::calculateMultiFaceShape);
    }

    @Override
    public String defaultProperty() {
        return POSITIVE_LAYERS.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 2, 1);
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
        return hitLocation.getY() > 0.5 ? POSITIVE_LAYERS.getName() : NEGATIVE_LAYERS.getName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return new Vec3i(0, property.equals(POSITIVE_LAYERS.getName()) ? 1 : 0, 0);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) return null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) {
            String property = getPropertyFromInteraction(state, context.getLevel(), blockPos, context.getClickLocation(), context.getClickedFace(), false);
            IntegerProperty targetProp;
            if (property.equals(POSITIVE_LAYERS.getName())) {
                targetProp = POSITIVE_LAYERS;
            } else {
                targetProp = NEGATIVE_LAYERS;
            }
            if (state.getValue(targetProp) < 8)
                return state.cycle(targetProp);
            else {
                Copycats.LOGGER.warn("Can't figure out where to place a half layer! Please file an issue if you see this.");
                return state;
            }
        } else {
            Direction facing = context.getClickedFace().getOpposite();
            if (facing.getAxis().isVertical()) {
                facing = context.getHorizontalDirection();
            }
            Vec3 clickPosition = context.getClickLocation()
                    .subtract(Vec3.atLowerCornerOf(context.getClickedPos()));
            return stateForPlacement
                    .setValue(FACING, facing)
                    .setValue((clickPosition.y >= 0.5f) ? POSITIVE_LAYERS : NEGATIVE_LAYERS, 1);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        if (context.getClickedFace() == state.getValue(FACING)) {
            return false;
        }
        String property = getPropertyFromInteraction(state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), false);
        IntegerProperty targetProp;
        if (property.equals(POSITIVE_LAYERS.getName())) {
            targetProp = POSITIVE_LAYERS;
        } else {
            targetProp = NEGATIVE_LAYERS;
        }
        return state.getValue(targetProp) != 8;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (state.getValue(POSITIVE_LAYERS) + state.getValue(NEGATIVE_LAYERS) <= 1)
            return super.onSneakWrenched(state, context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        String property = getPropertyFromInteraction(state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        IntegerProperty targetProp;
        if (property.equals(POSITIVE_LAYERS.getName())) {
            targetProp = POSITIVE_LAYERS;
        } else {
            targetProp = NEGATIVE_LAYERS;
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
        super.createBlockStateDefinition(pBuilder.add(FACING, POSITIVE_LAYERS, NEGATIVE_LAYERS));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, CCShapes.STACKED_HALF_LAYER_TOP.get(pState.getValue(FACING)).get(pState.getValue(POSITIVE_LAYERS)).toShape(), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, CCShapes.STACKED_HALF_LAYER_BOTTOM.get(pState.getValue(FACING)).get(pState.getValue(NEGATIVE_LAYERS)).toShape(), BooleanOp.OR);
        return shape.optimize();
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
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
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }
}

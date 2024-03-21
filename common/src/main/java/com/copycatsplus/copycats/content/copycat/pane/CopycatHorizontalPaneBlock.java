package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCShapes;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.ICustomCTBlocking;
import com.copycatsplus.copycats.content.copycat.slab.CopycatSlabBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.WaterloggedCopycatBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

import static com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

public class CopycatHorizontalPaneBlock extends CTWaterloggedCopycatBlock {

    private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());


    public CopycatHorizontalPaneBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult result = super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (!pPlayer.isShiftKeyDown() && pPlayer.mayBuild()) {
            ItemStack heldItem = pPlayer.getItemInHand(pHand);
            IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
            if (placementHelper.matchesItem(heldItem)) {
                placementHelper.getOffset(pPlayer, pLevel, pState, pPos, pHit)
                        .placeInWorld(pLevel, (BlockItem) heldItem.getItem(), pPlayer, pHand, pHit);
                return result;
            }
        }
        return result;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this)) return true;
        return toState.setValue(WATERLOGGED, false) == state.setValue(WATERLOGGED, false);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return CCShapes.HORIZONTAL_PANE.get(Direction.Axis.Y);
    }


    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState,
                                     Direction dir) {
        if (state.is(this) == neighborState.is(this)) {
            return (getMaterial(level, pos).skipRendering(getMaterial(level, pos.relative(dir)), dir.getOpposite()));
        }

        return getMaterial(level, pos).skipRendering(neighborState, dir.getOpposite());
    }


    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return CCBlocks.COPYCAT_HORIZONTAL_PANE::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return CCBlocks.COPYCAT_HORIZONTAL_PANE::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                         BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(),
                    Direction.Axis.Y,
                    dir -> world.getBlockState(pos.relative(dir))
                            .canBeReplaced());

            if (directions.isEmpty())
                return PlacementOffset.fail();
            else {
                return PlacementOffset.success(pos.relative(directions.get(0)));
            }
        }

    }
}

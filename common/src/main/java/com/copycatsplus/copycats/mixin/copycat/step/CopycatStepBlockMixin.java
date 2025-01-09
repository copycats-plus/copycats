package com.copycatsplus.copycats.mixin.copycat.step;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.CopycatExternalContext;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatStepBlock;
import com.simibubi.create.content.decoration.copycat.WaterloggedCopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Add CT toggle to Create's {@link CopycatStepBlock}.
 */
@Mixin(CopycatStepBlock.class)
public abstract class CopycatStepBlockMixin extends WaterloggedCopycatBlock implements ICopycatBlock {
    public CopycatStepBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        if (!this.isCTEnabled(state, level, queryPos))
            return state;
        return super.getAppearance(state, level, pos, side, queryState, queryPos);
    }

    @Inject(
            at = @At("HEAD"),
            method = "use",
            cancellable = true
    )
    public void use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult toggleResult = toggleCT(state, world, pos, player, hand, ray);
        if (toggleResult.consumesAction()) cir.setReturnValue(toggleResult);
    }

    @Nullable
    @Override
    public CopycatBlockEntity getBlockEntity(BlockGetter worldIn, BlockPos pos) {
        return super.getBlockEntity(worldIn, pos);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return transform(state, new StructureTransform(BlockPos.ZERO, Direction.Axis.Y, rotation, Mirror.NONE));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return transform(state, new StructureTransform(BlockPos.ZERO, null, Rotation.NONE, mirror));
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return BlockUtils.transformStepLikeHorizontal(state, transform, CCBlocks.COPYCAT_VERTICAL_STEP.getDefaultState());
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        if (CopycatExternalContext.isForBlockingLogic()) {
            return false;
        }

        return !checkConnection(reader, toPos, fromPos, reader.getBlockState(toPos));
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState fromState) {
        BlockState toState = reader.getBlockState(toPos);

        if (toState.getBlock() instanceof ICopycatBlock) {
            return true;
        }

        return checkConnection(reader, fromPos, toPos, fromState);
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

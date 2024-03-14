package com.copycatsplus.copycats.mixin.copycat.panel;

import com.copycatsplus.copycats.content.copycat.base.ICTCopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatPanelBlock;
import com.simibubi.create.content.decoration.copycat.WaterloggedCopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CopycatPanelBlock.class)
public abstract class CopycatPanelBlockMixin extends WaterloggedCopycatBlock implements ICTCopycatBlock {
    public CopycatPanelBlockMixin(Properties pProperties) {
        super(pProperties);
    }

/*    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        if (!allowCTAppearance(state, level, pos, side, queryState, queryPos))
            return state;
        return super.getAppearance(state, level, pos, side, queryState, queryPos);
    }*/

    @Inject(
            at = @At("HEAD"),
            method = "use",
            cancellable = true
    )
    public void use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray, CallbackInfoReturnable<InteractionResult> cir) {
        InteractionResult toggleResult = toggleCT(state, world, pos, player, hand, ray);
        if (toggleResult.consumesAction()) cir.setReturnValue(toggleResult);
    }
}

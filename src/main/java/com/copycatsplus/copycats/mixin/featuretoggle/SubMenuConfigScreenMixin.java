package com.copycatsplus.copycats.mixin.featuretoggle;

import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SubMenuConfigScreen.class, remap = false)
public class SubMenuConfigScreenMixin {
    @Inject(
            method = "saveChanges()V",
            at = @At("TAIL")
    )
    private void saveChangesAndRefresh(CallbackInfo ci) {
/*        if (ConfigScreen.modID.equals(Copycats.MODID)) {
            if (EffectiveSide.get().isServer() || FMLEnvironment.dist == Dist.CLIENT && Minecraft.getInstance().hasSingleplayerServer())
                LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER).submit(() -> CCConfigs.common().syncToAllPlayers());
        }*/
    }
}

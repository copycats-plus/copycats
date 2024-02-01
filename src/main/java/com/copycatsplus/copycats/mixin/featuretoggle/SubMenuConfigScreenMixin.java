package com.copycatsplus.copycats.mixin.featuretoggle;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.simibubi.create.foundation.config.ui.ConfigScreen;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import io.github.fabricators_of_create.porting_lib.util.LogicalSidedProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
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
        if (ConfigScreen.modID.equals(Copycats.MODID)) {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER || FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && Minecraft.getInstance().hasSingleplayerServer())
                LogicalSidedProvider.WORKQUEUE.get(EnvType.SERVER).submit(() -> CCConfigs.common().syncToAllPlayers());
        }
    }
}

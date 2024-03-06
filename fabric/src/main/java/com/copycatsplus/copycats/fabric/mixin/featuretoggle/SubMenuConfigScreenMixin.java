package com.copycatsplus.copycats.fabric.mixin.featuretoggle;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.multiloader.LogicalSidedProvider;
import com.copycatsplus.copycats.multiloader.Platform;
import com.copycatsplus.copycats.network.CCPackets;
import com.copycatsplus.copycats.network.ConfigSyncPacket;
import com.copycatsplus.copycats.network.PlayerSelection;
import com.simibubi.create.foundation.config.ui.ConfigScreen;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.config.ModConfig;
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
                LogicalSidedProvider.WORKQUEUE.get(Platform.Environment.SERVER).submit(() -> CCPackets.PACKETS.sendTo(PlayerSelection.all(), new ConfigSyncPacket(CCConfigs.common().getSyncConfig(), ModConfig.Type.COMMON)));
        }
    }
}

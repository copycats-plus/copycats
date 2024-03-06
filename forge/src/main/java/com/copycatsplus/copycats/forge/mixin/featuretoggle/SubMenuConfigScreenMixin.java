package com.copycatsplus.copycats.forge.mixin.featuretoggle;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.network.CCPackets;
import com.copycatsplus.copycats.network.ConfigSyncPacket;
import com.copycatsplus.copycats.network.PlayerSelection;
import com.simibubi.create.foundation.config.ui.ConfigScreen;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.thread.EffectiveSide;
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
            if (EffectiveSide.get().isServer() || FMLEnvironment.dist == Dist.CLIENT && Minecraft.getInstance().hasSingleplayerServer())
                LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER).submit(() -> CCPackets.PACKETS.sendTo(PlayerSelection.all(), new ConfigSyncPacket(CCConfigs.common().getSyncConfig(), ModConfig.Type.COMMON)));
        }
    }
}

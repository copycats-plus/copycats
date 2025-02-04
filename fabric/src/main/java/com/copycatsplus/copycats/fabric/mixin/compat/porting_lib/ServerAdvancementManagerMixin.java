package com.copycatsplus.copycats.fabric.mixin.compat.porting_lib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

/**
 * Porting Lib returns null Advancement.Builder for entries that do not satisfy loading conditions, which causes a crash in vanilla code.
 */
@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {

    @WrapOperation(
            method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementList;add(Ljava/util/Map;)V")
    )
    private void cleanNullEntries(AdvancementList instance, Map<ResourceLocation, Advancement.Builder> map, Operation<Void> original) {
        map.entrySet().removeIf(e -> e.getValue() == null);
        original.call(instance, map);
    }
}

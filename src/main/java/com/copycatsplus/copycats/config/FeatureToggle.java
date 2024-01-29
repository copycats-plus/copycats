package com.copycatsplus.copycats.config;

import com.copycatsplus.copycats.compat.CreateConnectedJEI;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.mixin.featuretoggle.CreativeModeTabsAccessor;
import com.tterrag.registrate.builders.Builder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeatureToggle {
    public static final Set<ResourceLocation> TOGGLEABLE_FEATURES = new HashSet<>();
    public static final Map<ResourceLocation, ResourceLocation> DEPENDENT_FEATURES = new HashMap<>();

    public static void register(ResourceLocation key) {
        TOGGLEABLE_FEATURES.add(key);
    }

    public static void registerDependent(ResourceLocation key, ResourceLocation dependency) {
        DEPENDENT_FEATURES.put(key, dependency);
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> register() {
        return b -> {
            register(new ResourceLocation(b.getOwner().getModid(), b.getName()));
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(ResourceLocation dependency) {
        return b -> {
            registerDependent(new ResourceLocation(b.getOwner().getModid(), b.getName()), dependency);
            return b;
        };
    }

    public static <R, T extends R, P, S extends Builder<R, T, P, S>> NonNullUnaryOperator<S> registerDependent(BlockEntry<?> dependency) {
        return b -> {
            registerDependent(new ResourceLocation(b.getOwner().getModid(), b.getName()), dependency.getId());
            return b;
        };
    }

    private static CFeatures getToggles() {
        return CCConfigs.common().toggle;
    }

    public static boolean isEnabled(ResourceLocation key) {
        if (getToggles().hasToggle(key)) {
            return getToggles().isEnabled(key);
        } else {
            ResourceLocation dependency = DEPENDENT_FEATURES.get(key);
            if (dependency != null) return isEnabled(dependency);
        }
        return true;
    }

    static void refreshItemVisibility() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                LogicalSidedProvider.WORKQUEUE.get(LogicalSide.CLIENT).submit(() -> {
                    CreativeModeTab.ItemDisplayParameters cachedParameters = CreativeModeTabsAccessor.getCACHED_PARAMETERS();
                    if (cachedParameters != null) {
                        CreativeModeTabsAccessor.callBuildAllTabContents(cachedParameters);
                    }
                    Mods.JEI.executeIfInstalled(() -> CreateConnectedJEI::refreshItemList);
                })
        );
    }
}

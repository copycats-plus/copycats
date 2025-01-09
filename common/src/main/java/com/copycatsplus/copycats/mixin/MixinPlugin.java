package com.copycatsplus.copycats.mixin;

import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.foundation.annotation.ModMixin;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("Copycats+ | MixinPlugin");
    private boolean isFrameworkInstalled;

    @Override
    public void onLoad(String mixinPackage) {
        MixinExtrasBootstrap.init();
        try {
            Class.forName("com.copycatsplus.copycats.Copycats", false, this.getClass().getClassLoader());
            isFrameworkInstalled = true;
        } catch (Exception e) {
            isFrameworkInstalled = false;
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            List<AnnotationNode> annotationNodes = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName).visibleAnnotations;
            if (annotationNodes == null) return true;

            boolean shouldApply = true;
            for (AnnotationNode node : annotationNodes) {
                if (node.desc.equals(Type.getDescriptor(ModMixin.class))) {
                    List<Mods> mods = Annotations.getValue(node, "requiredMods", true, Mods.class);
                    boolean applyIfPresent = Annotations.getValue(node, "applyIfPresent", Boolean.TRUE);
                    boolean anyModsLoaded = anyModsLoaded(mods);
                    shouldApply = anyModsLoaded == applyIfPresent;
                    LOGGER.debug("{} is {} being applied because the mod(s) {} are {} loaded", mixinClassName, shouldApply ? " " : " not ", mods, anyModsLoaded ? " " : " not ");
                }
            }
            return shouldApply;
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error("An error occurred when checking if {} has the ModMixin annotation", mixinClassName, e);
        }
        return isFrameworkInstalled; // this makes sure that forge's helpful mods not found screen shows up
    }

    private static boolean anyModsLoaded(List<Mods> mods) {
        for (Mods mod : mods) {
            if (mod.isLoaded) return true;
        }
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}

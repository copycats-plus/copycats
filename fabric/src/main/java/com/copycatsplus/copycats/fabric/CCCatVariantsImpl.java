package com.copycatsplus.copycats.fabric;


import com.copycatsplus.copycats.CCCatVariants;
import com.copycatsplus.copycats.fabric.mixin.HolderReferenceInvoker;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.CatVariant;

public class CCCatVariantsImpl extends CCCatVariants {
    public static void register() {
        for (Pair<Holder.Reference<CatVariant>, ResourceLocation> entry : ENTRIES) {
            CatVariant value = new CatVariant(entry.getSecond());
            Registry.register(BuiltInRegistries.CAT_VARIANT, entry.getFirst().key().location().toString(), value);
            ((HolderReferenceInvoker) entry.getFirst()).callBindValue(value);
        }
    }
}

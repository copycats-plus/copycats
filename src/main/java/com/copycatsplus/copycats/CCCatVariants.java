package com.copycatsplus.copycats;

import com.copycatsplus.copycats.mixin.entity.HolderReferenceAccessor;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.CatVariant;

import java.util.LinkedList;
import java.util.List;

public class CCCatVariants {
    private static final List<Pair<Holder.Reference<CatVariant>, ResourceLocation>> ENTRIES = new LinkedList<>();

    public static final Holder.Reference<CatVariant> COPY_CAT = register("copy_cat", Copycats.asResource("textures/entity/cat/copy_cat.png"));

    private static Holder.Reference<CatVariant> register(String key, ResourceLocation texture) {
        Pair<Holder.Reference<CatVariant>, ResourceLocation> pair = Pair.of(Holder.Reference.createStandAlone(
                Registry.CAT_VARIANT,
                ResourceKey.create(Registry.CAT_VARIANT.key(), Copycats.asResource(key))
        ), texture);
        ENTRIES.add(pair);
        return pair.getFirst();
    }

    public static void register() {
        onRegister();
    }

    private static void onRegister() {
        for (Pair<Holder.Reference<CatVariant>, ResourceLocation> entry : ENTRIES) {
            CatVariant instance = new CatVariant(entry.getSecond());
            ((HolderReferenceAccessor) entry.getFirst())
                    .callBind(entry.getFirst().key(), Registry.register(Registry.CAT_VARIANT, entry.getFirst().key().location(), instance));
        }
    }
}
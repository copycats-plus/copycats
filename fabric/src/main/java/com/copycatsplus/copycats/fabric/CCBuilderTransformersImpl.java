package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import net.minecraft.client.renderer.RenderType;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.Block;

public class CCBuilderTransformersImpl {

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().color(MaterialColor.NONE))
                // fabric: only render base model on cutout. When rendering the wrapped model's material is copied.
                .addLayer(() -> RenderType::cutout)
                .color(() -> ICopycatBlock::wrappedColor)
                .transform(TagGen.axeOrPickaxe());
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().color(MaterialColor.NONE))
                .addLayer(() -> RenderType::solid)
                .addLayer(() -> RenderType::cutout)
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .color(() -> IMultiStateCopycatBlock::wrappedColor)
                .transform(TagGen.axeOrPickaxe());
    }
}

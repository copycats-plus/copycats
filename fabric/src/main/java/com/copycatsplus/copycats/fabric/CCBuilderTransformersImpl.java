package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.content.copycat.casing.WrappedCasingBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class CCBuilderTransformersImpl {

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().mapColor(MapColor.NONE).forceSolidOn())
                // fabric: only render base model on cutout. When rendering the wrapped model's material is copied.
                .addLayer(() -> RenderType::cutout)
                .color(() -> ICopycatBlock::wrappedColor)
                .transform(axeOrPickaxe());
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> multiCopycat() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion().mapColor(MapColor.NONE).forceSolidOn())
                .addLayer(() -> RenderType::solid)
                .addLayer(() -> RenderType::cutout)
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .color(() -> IMultiStateCopycatBlock::wrappedColor)
                .transform(axeOrPickaxe());
    }

    public static <B extends WrappedCasingBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> wrappedCasing(CTSpriteShiftEntry spriteShift) {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.WOOD).noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .addLayer(() -> RenderType::cutoutMipped)
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(spriteShift)))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, spriteShift)))
                .tag(AllTags.AllBlockTags.CASING.tag);
    }

    public static <B extends CasingBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> wrappedLayeredCasing(
            CTSpriteShiftEntry ct, CTSpriteShiftEntry ct2) {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.mapColor(MapColor.PODZOL).sound(SoundType.WOOD).noOcclusion())
                .transform(axeOrPickaxe())
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .cubeColumn(c.getName(), ct.getOriginalResourceLocation(),
                                ct2.getOriginalResourceLocation())))
                .addLayer(() -> RenderType::cutoutMipped)
                .onRegister(connectedTextures(() -> new HorizontalCTBehaviour(ct, ct2)))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, ct)))
                .tag(AllTags.AllBlockTags.CASING.tag);
    }
}

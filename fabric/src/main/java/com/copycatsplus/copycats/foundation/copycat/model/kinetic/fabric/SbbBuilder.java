package com.copycatsplus.copycats.foundation.copycat.model.kinetic.fabric;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.createmod.catnip.client.render.model.ShadeSeparatedResultConsumer;
import net.createmod.catnip.render.SuperByteBufferBuilder;
import net.minecraft.client.renderer.RenderType;

public class SbbBuilder extends SuperByteBufferBuilder implements ShadeSeparatedResultConsumer {
    @Override
    public void accept(RenderType renderType, boolean shaded, BufferBuilder.RenderedBuffer data) {
        add(data, shaded);
    }
}

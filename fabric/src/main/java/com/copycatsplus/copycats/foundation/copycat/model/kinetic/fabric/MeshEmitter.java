package com.copycatsplus.copycats.foundation.copycat.model.kinetic.fabric;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.createmod.catnip.client.render.model.ShadeSeparatedResultConsumer;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.UnknownNullability;

// Copied from https://github.com/Creators-of-Create/Ponder/blob/mc1.20.1/dev/Common/src/main/java/net/createmod/catnip/impl/client/render/model/MeshEmitter.java
public class MeshEmitter {
    private final RenderType renderType;
    private final BufferBuilder bufferBuilder;

    @UnknownNullability
    private ShadeSeparatedResultConsumer resultConsumer;
    private boolean currentShade;

    MeshEmitter(RenderType renderType) {
        this.renderType = renderType;
        this.bufferBuilder = new BufferBuilder(renderType.bufferSize());
    }

    public void prepare(ShadeSeparatedResultConsumer resultConsumer) {
        this.resultConsumer = resultConsumer;
    }

    public void end() {
        if (bufferBuilder.building()) {
            emit();
        }
        resultConsumer = null;
    }

    public BufferBuilder getBuffer(boolean shade) {
        prepareForGeometry(shade);
        return bufferBuilder;
    }

    private void prepareForGeometry(boolean shade) {
        if (!bufferBuilder.building()) {
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        } else if (shade != currentShade) {
            emit();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        }

        currentShade = shade;
    }

    private void emit() {
        var renderedBuffer = bufferBuilder.endOrDiscardIfEmpty();

        if (renderedBuffer != null) {
            resultConsumer.accept(renderType, currentShade, renderedBuffer);
            renderedBuffer.release();
        }
    }
}


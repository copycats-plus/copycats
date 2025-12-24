package com.copycatsplus.copycats.foundation.copycat.model.kinetic.fabric;


import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.createmod.catnip.client.render.model.ShadeSeparatedBufferSource;
import net.createmod.catnip.client.render.model.ShadeSeparatedResultConsumer;
import net.minecraft.client.renderer.RenderType;

// Copied from https://github.com/Creators-of-Create/Ponder/blob/mc1.20.1/dev/Common/src/main/java/net/createmod/catnip/impl/client/render/model/DefaultShadeSeparatedBufferSource.java
public class DefaultShadeSeparatedBufferSource implements ShadeSeparatedBufferSource {
    private static final RenderType[] CHUNK_LAYERS = RenderType.chunkBufferLayers().toArray(RenderType[]::new);
    private static final int CHUNK_LAYER_AMOUNT = CHUNK_LAYERS.length;

    private final MeshEmitter[] emitters = new MeshEmitter[CHUNK_LAYER_AMOUNT];
    private final Reference2ReferenceMap<RenderType, MeshEmitter> emitterMap = new Reference2ReferenceOpenHashMap<>();

    DefaultShadeSeparatedBufferSource() {
        for (int layerIndex = 0; layerIndex < CHUNK_LAYER_AMOUNT; layerIndex++) {
            RenderType renderType = CHUNK_LAYERS[layerIndex];
            MeshEmitter emitter = new MeshEmitter(renderType);
            emitters[layerIndex] = emitter;
            emitterMap.put(renderType, emitter);
        }
    }

    public void prepare(ShadeSeparatedResultConsumer resultConsumer) {
        for (MeshEmitter emitter : emitters) {
            emitter.prepare(resultConsumer);
        }
    }

    public void end() {
        for (MeshEmitter emitter : emitters) {
            emitter.end();
        }
    }

    @Override
    public VertexConsumer getBuffer(RenderType chunkRenderType, boolean shade) {
        return emitterMap.get(chunkRenderType).getBuffer(shade);
    }
}

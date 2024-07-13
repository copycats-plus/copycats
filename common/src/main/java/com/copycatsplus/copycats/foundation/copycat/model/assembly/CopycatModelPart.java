package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Assemble quads for a given model using the provided API in {@link CopycatRenderContext}.
 * <p>
 * The provided model can be a simple model of the copied material or any {@link net.minecraft.client.resources.model.BakedModel}
 * returned by a {@link CopycatModelCore.ModelGetter}.
 * <p>
 * To maintain platform-independence, the model quads are not directly accessible, and implementors must only use the API
 * provided by {@link CopycatRenderContext} to assemble the quads. If advanced quad manipulation is required, a custom
 * {@link QuadTransform} can be passed to the
 * assembly methods in the context.
 */
@FunctionalInterface
public interface CopycatModelPart {
    /**
     * Assemble quads given the provided context.
     *
     * @param key      The key of the model provided by the context. For simple copycats, the key of the copied material is {@link CopycatModelCore#MATERIAL_KEY}. For multi-state copycats, the keys come from the storage properties of the copycat.
     * @param state    The block state of the copycat.
     * @param context  The context to assemble the quads with.
     * @param material The block state of the copied material. This matches the key provided for both simple and multi-state copycats.
     */
    void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material);
}

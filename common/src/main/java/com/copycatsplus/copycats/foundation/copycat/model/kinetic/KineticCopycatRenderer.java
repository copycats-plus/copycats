package com.copycatsplus.copycats.foundation.copycat.model.kinetic;

import com.copycatsplus.copycats.CopycatsClient;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.jozufozu.flywheel.core.model.BlockModel;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.resources.model.BakedModel;

/**
 * Helper class to render kinetic copycat models.
 */
public class KineticCopycatRenderer {
    public static final SuperByteBufferCache.Compartment<KineticCopycatRenderData> KINETIC_COPYCAT = new SuperByteBufferCache.Compartment<>();

    public static SuperByteBuffer getBuffer(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(partialModel, be),
                () -> copycatRender(partialModel, be)
        );
    }

    public static SuperByteBuffer getBuffer(ICopycatPartialModel partialModel, IMultiStateCopycatBlockEntity be, String property) {
        return CopycatsClient.BUFFER_CACHE.get(KINETIC_COPYCAT,
                KineticCopycatRenderData.of(partialModel, be, property),
                () -> copycatRender(partialModel, be)
        );
    }

    public static BlockModel getInstanceModel(ICopycatPartialModel partialModel, ICopycatBlockEntity be, KineticCopycatRenderData renderData) {
        ShadeSeparatedBufferedData data = getCopycatBuffer(partialModel.getModel(), be);
        BlockModel blockModel = new BlockModel(data, renderData.toString());
        data.release();
        return blockModel;
    }

    public static SuperByteBuffer copycatRender(ICopycatPartialModel partialModel, ICopycatBlockEntity be) {
        ShadeSeparatedBufferedData bufferedData = getCopycatBuffer(partialModel.getModel(), be);
        SuperByteBuffer sbb = new SuperByteBuffer(bufferedData);
        bufferedData.release();
        return sbb;
    }

    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel partialModel, ICopycatBlockEntity be) {
        return getCopycatBuffer(partialModel, be, new PoseStack());
    }

    @ExpectPlatform
    public static ShadeSeparatedBufferedData getCopycatBuffer(BakedModel model, ICopycatBlockEntity be, PoseStack ms) {
        return null;
    }
}

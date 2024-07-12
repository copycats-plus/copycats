package com.copycatsplus.copycats.foundation.copycat.model.fabric;

import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.EncodingFormat;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("UnstableApiUsage")
@ApiStatus.Internal
public class IntermediateMutableQuadView extends MutableQuadViewImpl {
    public IntermediateMutableQuadView() {
        data = new int[EncodingFormat.TOTAL_STRIDE];
        clear();
    }

    @Override
    public void emitDirectly() {
        throw new NotImplementedException("IntermediateMutableQuadView.emitDirectly() is not implemented");
    }
}

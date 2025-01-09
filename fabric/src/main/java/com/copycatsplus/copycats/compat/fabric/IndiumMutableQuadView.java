package com.copycatsplus.copycats.compat.fabric;

import link.infra.indium.renderer.mesh.EncodingFormat;
import link.infra.indium.renderer.mesh.MutableQuadViewImpl;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

/**
 * An Indium implementation of {@link net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl}.
 */
@ApiStatus.Internal
public class IndiumMutableQuadView extends MutableQuadViewImpl {
    public IndiumMutableQuadView() {
        data = new int[EncodingFormat.TOTAL_STRIDE];
        clear();
    }

    @Override
    public void emitDirectly() {
        throw new NotImplementedException("IntermediateMutableQuadView.emitDirectly() is not implemented");
    }
}

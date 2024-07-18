package com.copycatsplus.copycats.compat.fabric;

import link.infra.indium.renderer.mesh.EncodingFormat;
import link.infra.indium.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
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
    public QuadEmitter emit() {
        throw new NotImplementedException("IntermediateMutableQuadView.emit() is not implemented");
    }
}

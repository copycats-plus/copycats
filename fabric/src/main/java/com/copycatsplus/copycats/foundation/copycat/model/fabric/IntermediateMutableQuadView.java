package com.copycatsplus.copycats.foundation.copycat.model.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.compat.fabric.IndiumMutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.EncodingFormat;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.ApiStatus;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("UnstableApiUsage")
@ApiStatus.Internal
public class IntermediateMutableQuadView extends MutableQuadViewImpl {
    private IntermediateMutableQuadView() {
        data = new int[EncodingFormat.TOTAL_STRIDE];
        clear();
    }

    @Override
    public void emitDirectly() {
        throw new NotImplementedException("IntermediateMutableQuadView.emitDirectly() is not implemented");
    }

    /**
     * Embeddium registers a stub for Indium but does not provide Indium classes,
     * so classes need to be checked for existence at runtime.
     */
    private static final AtomicBoolean indiumAvailable = new AtomicBoolean(true);

    public static MutableQuadView create() {
        if (!indiumAvailable.get()) {
            return new IntermediateMutableQuadView();
        }
        return Mods.INDIUM.<MutableQuadView>runIfInstalled(() -> {
            try {
                IndiumMutableQuadView instance = new IndiumMutableQuadView();
                return () -> instance;
            } catch (NoClassDefFoundError t) {
                if (indiumAvailable.compareAndSet(true, false)) {
                    Copycats.LOGGER.warn("Failed to load Indium classes, falling back to Fabric API");
                    t.printStackTrace();
                }
                return IntermediateMutableQuadView::new;
            }
        }).orElseGet(IntermediateMutableQuadView::new);
    }
}

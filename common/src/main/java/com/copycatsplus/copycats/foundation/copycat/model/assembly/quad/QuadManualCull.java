package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Assign cull face to the quad based on the light face and the existing cull face.
 * <p>
 * Return null to specify that the quad should never be culled.
 *
 * @param mapper The mapper function to determine the cull face.
 */
public record QuadManualCull(CullFaceMapper mapper) implements QuadTransform {

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        quad.cullFace = mapper.map(quad.computeLightFace(), quad.cullFace);
        quad.disableFinalAutoCull = true;
        return true;
    }

    @FunctionalInterface
    public interface CullFaceMapper {
        @Nullable
        Direction map(@NotNull Direction lightFace, @Nullable Direction cullFace);
    }
}

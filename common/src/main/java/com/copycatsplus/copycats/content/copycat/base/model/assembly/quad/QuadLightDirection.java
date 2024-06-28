package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import java.util.function.Function;

public record QuadLightDirection(Function<Direction, Direction> directionMapper) implements QuadTransform {

    @Override
    public void transformVertices(MutableQuad quad, TextureAtlasSprite sprite) {
        quad.direction = directionMapper.apply(quad.direction);
    }
}

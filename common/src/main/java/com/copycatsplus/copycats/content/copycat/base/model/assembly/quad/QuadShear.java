package com.copycatsplus.copycats.content.copycat.base.model.assembly.quad;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableQuad;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public record QuadShear(Axis axis, Direction direction, double amount) implements QuadTransform {

    @Override
    public void transformVertices(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            MutableVec3 vertex = quad.vertices.get(i).xyz;

            double shearAxis = vertex.get(this.axis);
            double amount = this.amount * (this.direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1);
            vertex.set(this.direction.getAxis(), vertex.get(this.direction.getAxis()) + shearAxis * amount);
        }
    }
}

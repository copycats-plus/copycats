package com.copycatsplus.copycats.foundation.copycat.model.assembly.quad;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

/**
 * Shears a quad along an axis towards the specified direction.
 *
 * @param axis      The axis to shear along. This is the axis that will be moved towards the specified direction.
 * @param direction The direction to shear towards. Cannot be aligned with the axis.
 * @param amount    The amount to shear by, in block space. A vertex located at 1 block along the axis will be moved towards the direction by this amount.
 */
public record QuadShear(Axis axis, Direction direction, double amount) implements QuadTransform {

    @Override
    public boolean transformQuad(MutableQuad quad, TextureAtlasSprite sprite) {
        for (int i = 0; i < 4; i++) {
            MutableVec3 vertex = quad.vertices.get(i).xyz;

            double shearAxis = vertex.get(this.axis);
            double amount = this.amount * (this.direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1);
            vertex.set(this.direction.getAxis(), vertex.get(this.direction.getAxis()) + shearAxis * amount);
        }
        return true;
    }
}

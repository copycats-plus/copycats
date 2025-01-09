package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadUVUpdate;

/**
 * A mutable container for UV coordinates. UV data is not transformable. Use
 * {@link QuadUVUpdate} to update UV coordinates while other transforms are applied.
 */
public class MutableUV {
    public float u;
    public float v;

    public MutableUV(float u, float v) {
        set(u, v);
    }

    public MutableUV set(float u, float v) {
        this.u = u;
        this.v = v;
        return this;
    }

    public boolean isZero() {
        return u == 0 && v == 0;
    }
}

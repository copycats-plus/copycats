package com.copycatsplus.copycats.content.copycat.base.model.assembly;

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

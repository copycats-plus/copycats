package com.copycatsplus.copycats.foundation.copycat.model.assembly;

/**
 * A mutable container for vertex data.
 */
public class MutableVertex {
    public MutableVec3 xyz;
    public MutableUV uv;

    public MutableVertex(MutableVec3 xyz, MutableUV uv) {
        set(xyz, uv);
    }

    public MutableVertex set(MutableVec3 xyz, MutableUV uv) {
        this.xyz = xyz;
        this.uv = uv;
        return this;
    }
}

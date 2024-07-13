package com.copycatsplus.copycats;

import com.simibubi.create.foundation.data.CreateRegistrate;

public class CopycatRegistrate extends CreateRegistrate {

    private static CopycatRegistrate instance;
    protected CopycatRegistrate(String modid) {
        super(modid);
        instance = this;
    }

    public static CopycatRegistrate create(String modid) {
        return new CopycatRegistrate(modid);
    }

    public static CopycatRegistrate getInstance() {
        return instance;
    }
}

package com.copycatsplus.copycats;

import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.architectury.injectables.annotations.ExpectPlatform;

public class CopycatRegistrate extends CreateRegistrate {

    private static CopycatRegistrate instance;
    protected CopycatRegistrate(String modid) {
        super(modid);
        instance = this;
    }

    public static CopycatRegistrate create(String modid) {
        return new CopycatRegistrate(modid);
    }

    @ExpectPlatform
    public static <Tab> CreateRegistrate setTab(Tab tab) {
        throw new AssertionError();
    }

    public static CopycatRegistrate getInstance() {
        return instance;
    }
}

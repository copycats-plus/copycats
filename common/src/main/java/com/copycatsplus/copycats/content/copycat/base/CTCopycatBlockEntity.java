package com.copycatsplus.copycats.content.copycat.base;

public interface CTCopycatBlockEntity {
    boolean isCTEnabled();

    void setCTEnabled(boolean value);

    void callRedraw();
}

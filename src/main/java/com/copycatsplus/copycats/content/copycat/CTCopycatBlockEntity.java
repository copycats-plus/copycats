package com.copycatsplus.copycats.content.copycat;

public interface CTCopycatBlockEntity {
    boolean isCTEnabled();

    void setCTEnabled(boolean value);

    void callRedraw();
}

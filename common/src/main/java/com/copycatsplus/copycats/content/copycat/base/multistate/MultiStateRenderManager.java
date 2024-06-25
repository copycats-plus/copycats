package com.copycatsplus.copycats.content.copycat.base.multistate;

public class MultiStateRenderManager {
    private static final ThreadLocal<String> renderingProperty = new ThreadLocal<>();

    public static void setRenderingProperty(String property) {
        renderingProperty.set(property);
    }

    public static String getRenderingProperty() {
        return renderingProperty.get();
    }
}

package com.copycatsplus.copycats.foundation.copycat.multistate;

import org.jetbrains.annotations.ApiStatus;

/**
 * Stores the currently rendering property for multi-state copycats.
 * <p>
 * The stored value is thread-local and only valid when determining block colors.
 */
@ApiStatus.Internal
public class MultiStateRenderManager {
    private static final ThreadLocal<String> renderingProperty = new ThreadLocal<>();

    public static void setRenderingProperty(String property) {
        renderingProperty.set(property);
    }

    public static String getRenderingProperty() {
        return renderingProperty.get();
    }
}

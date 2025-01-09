package com.copycatsplus.copycats.foundation.copycat;

import org.jetbrains.annotations.ApiStatus;

/**
 * Stores additional context when rendering copycats.
 */
@ApiStatus.Internal
public class CopycatExternalContext {
    /**
     * Stores the currently rendering property for multi-state copycats. The stored value is thread-local.
     * <p>
     * This is used when determining block colors and when block shape calculation results in a valid rendering property for getAppearance
     */
    private static final ThreadLocal<String> renderingProperty = new ThreadLocal<>();
    /**
     * Used to determine whether a {@link ICopycatBlock#getAppearance} call is made for determining blocking logic,
     * which causes the method to return appearances differently.
     */
    private static final ThreadLocal<Boolean> forBlockingLogic = ThreadLocal.withInitial(() -> false);

    public static void setRenderingProperty(String property) {
        renderingProperty.set(property);
    }

    public static String getRenderingProperty() {
        return renderingProperty.get();
    }

    public static void setForBlockingLogic(boolean value) {
        forBlockingLogic.set(value);
    }

    public static boolean isForBlockingLogic() {
        return forBlockingLogic.get();
    }
}

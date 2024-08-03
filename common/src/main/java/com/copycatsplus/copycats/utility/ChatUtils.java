package com.copycatsplus.copycats.utility;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.CCConfigs;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class ChatUtils {
    public static Set<String> messages = new HashSet<>();

    private static final Supplier<Boolean> disableWarnings = CCConfigs.safeGetter(() -> CCConfigs.client().disableGraphicsWarnings.get(), false);

    /**
     * Send a local warning message to the player once.
     *
     * @param id      Unique identifier for the message. Messages with the same id will only be sent once.
     * @param message The message to send.
     */
    public static void sendWarningOnce(String id, String message) {
        if (disableWarnings.get()) return;
        if (messages.contains(id)) return;
        messages.add(id);
        if (!Platform.Environment.CLIENT.isCurrent() || !ClientUtils.sendSystemMessage("Warning: " + message)) {
            Copycats.LOGGER.warn("Warning: {}", message);
        }
    }
}

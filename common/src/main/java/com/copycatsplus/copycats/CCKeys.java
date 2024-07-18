package com.copycatsplus.copycats;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.glfw.GLFW;

public enum CCKeys {

    FILL_COPYCAT("fill_copycat", GLFW.GLFW_KEY_LEFT_ALT),

    ;

    public KeyMapping keybind;
    public final String description;
    public final int key;
    public final boolean modifiable;

    CCKeys(String description, int defaultKey) {
        this.description = "keyinfo." + Copycats.MODID + "." + description;
        this.key = defaultKey;
        this.modifiable = !description.isEmpty();
    }

    public KeyMapping getKeybind() {
        return keybind;
    }

    public boolean isPressed() {
        if (!modifiable)
            return isKeyDown(key);
        return keybind.isDown();
    }

    public String getBoundKey() {
        return keybind.getTranslatedKeyMessage()
                .getString()
                .toUpperCase();
    }

    public int getBoundCode() {
        return KeyBindingHelper.getBoundKeyOf(keybind)
                .getValue();
    }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance()
                .getWindow()
                .getWindow(), key);
    }

    public static boolean isMouseButtonDown(int button) {
        return GLFW.glfwGetMouseButton(Minecraft.getInstance()
                .getWindow()
                .getWindow(), button) == 1;
    }

    @ExpectPlatform
    public static void register() {
    }

    public static boolean ctrlDown() {
        return Screen.hasControlDown();
    }

    public static boolean shiftDown() {
        return Screen.hasShiftDown();
    }

    public static boolean altDown() {
        return Screen.hasAltDown();
    }

}

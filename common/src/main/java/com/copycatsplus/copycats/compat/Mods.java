package com.copycatsplus.copycats.compat;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.CCLang.asId;

/**
 * For compatibility with and without another mod present, we have to define load conditions of the specific code.
 * <p>
 * Due to this class being used in the {@link com.copycatsplus.copycats.foundation.annotation.ModMixin} annotation,
 * it cannot reference most Minecraft classes due to this class being loaded before Mixins are applied.
 */
public enum Mods {
    /**
     * For the feature toggle system
     */
    JEI("jei"),
    EMI("emi"),
    CREATE("create"),
    /**
     * For copycat fence compatibility (only prevents crash)
     */
    ADDITIONAL_PLACEMENTS("additionalplacements"),
    DIAGONAL_FENCES("diagonalfences"),
    DIAGONAL_WALLS("diagonalwalls"),
    DIAGONAL_WINDOWS("diagonalwindows"),
    FLYWHEEL("flywheel"),
    SODIUM("sodium"),
    RUBIDIUM("rubidium"),
    ATHENA("athena"),
    INDIUM("indium"),
    LITHIUM("lithium"),
    RADIUM("radium"),
    STARLIGHT("starlight"),
    MORE_CULLING("moreculling"),
    DOUBLE_SLABS("doubleslabs"),
    VERTICAL_SLAB_COMPAT("v_slab_compat");

    public final String id;
    public final boolean isLoaded;

    Mods() {
        this(null);
    }


    Mods(String id) {
        this.id = id;
        this.isLoaded = getLoaded(asId(id));
    }

    /**
     * @return the mod id
     */
    public String id() {
        return id;
    }

    public boolean getLoaded() {
        return isLoaded;
    }

    /**
     * @return a boolean of whether the mod is loaded or not based on mod id
     */
    @ExpectPlatform
    public static boolean getLoaded(String id) {
        throw new AssertionError();
    }

    /**
     * Simple hook to run code if a mod is installed
     *
     * @param toRun will be run only if the mod is loaded
     * @return Optional.empty() if the mod is not loaded, otherwise an Optional of the return value of the given supplier
     */
    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        if (getLoaded())
            return Optional.of(toRun.get().get());
        return Optional.empty();
    }

    /**
     * Simple hook to execute code if a mod is installed
     *
     * @param toExecute will be executed only if the mod is loaded
     */
    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (getLoaded()) {
            toExecute.get().run();
        }
    }
}

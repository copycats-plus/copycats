package com.copycatsplus.copycats.compat.forge;

import com.copycatsplus.copycats.compat.AdditionalPlacementsCompat;
import com.firemerald.additionalplacements.generation.Registration;

public class AdditionalPlacementsCompatForge {
    public static void register() {
        Registration.addRegistration(new AdditionalPlacementsCompat());
    }
}

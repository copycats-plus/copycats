package com.copycatsplus.copycats;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;

public class CCSpriteShifts {
    public static final CTSpriteShiftEntry WRAPPED_ANDESITE_CASING = omni("wrapped_andesite_casing");
    public static final CTSpriteShiftEntry WRAPPED_BRASS_CASING = omni("wrapped_brass_casing");
    public static final CTSpriteShiftEntry WRAPPED_COPPER_CASING = omni("wrapped_copper_casing");
    public static final CTSpriteShiftEntry WRAPPED_RAILWAY_CASING = omni("wrapped_railway_casing");
    public static final CTSpriteShiftEntry WRAPPED_RAILWAY_CASING_SIDE = omni("wrapped_railway_casing_side");
    public static final CTSpriteShiftEntry WRAPPED_REFINED_RADIANCE_CASING = omni("wrapped_refined_radiance_casing");
    public static final CTSpriteShiftEntry WRAPPED_SHADOW_STEEL_CASING = omni("wrapped_shadow_steel_casing");

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Copycats.asResource(originalLocation), Copycats.asResource(targetLocation));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Copycats.asResource("block/" + blockTextureName),
                Copycats.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}

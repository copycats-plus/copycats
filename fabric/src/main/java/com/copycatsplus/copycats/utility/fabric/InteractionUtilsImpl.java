package com.copycatsplus.copycats.utility.fabric;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class InteractionUtilsImpl {

    public static AttributeInstance getPlayerReach(Player player) {
       return player.getAttribute(ReachEntityAttributes.REACH);
    }
}

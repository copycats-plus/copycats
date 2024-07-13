package com.copycatsplus.copycats.foundation.tooltip.forge;

import com.copycatsplus.copycats.foundation.tooltip.CopycatDescription;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.NotNull;

public class CopycatDescriptionImpl extends CopycatDescription implements TooltipModifier {

    protected CopycatDescriptionImpl(Item item) {
        super(item);
    }

    @Override
    public void modify(ItemTooltipEvent context) {
        super.modify(context.getItemStack().getItem(), context.getToolTip());
    }

    @NotNull
    public static TooltipModifier create(ItemLike item) {
        return new CopycatDescriptionImpl(item.asItem());
    }
}

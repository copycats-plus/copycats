package com.copycatsplus.copycats.foundation.tooltip;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.ChatFormatting.*;
import static net.minecraft.ChatFormatting.DARK_GRAY;

public class CopycatDescription {

    public static final Map<Item, List<CopycatCharacteristics>> ITEM_CHARACTERISTICS = new HashMap<>();

    public static void register(ItemLike item, CopycatCharacteristics... characteristics) {
        ITEM_CHARACTERISTICS.put(item.asItem(), List.of(characteristics));
    }

    public static <T extends ItemLike> NonNullConsumer<? super T> register(CopycatCharacteristics... characteristics) {
        return (item) -> register(item.asItem(), characteristics);
    }

    protected final Item item;
    private static String cachedLanguage = null;
    private static Map<CopycatCharacteristics, Pair<List<Component>, List<Component>>> descriptions;
    @Nullable
    private List<Component> shortDescription = null;
    @Nullable
    private List<Component> longDescription = null;

    protected CopycatDescription(Item item) {
        this.item = item;
    }

    private void loadDescriptions(Item item) {
        List<CopycatCharacteristics> characteristics = ITEM_CHARACTERISTICS.get(item);
        if (characteristics == null) {
            shortDescription = new ArrayList<>();
            longDescription = new ArrayList<>();
            return;
        }
        shortDescription = new ArrayList<>(characteristics.size());
        longDescription = new ArrayList<>(characteristics.size() * 2);
        for (CopycatCharacteristics characteristic : characteristics) {
            Pair<List<Component>, List<Component>> pair = descriptions.get(characteristic);
            if (pair == null)
                continue;
            shortDescription.addAll(pair.getFirst());
            longDescription.addAll(pair.getFirst());
            longDescription.addAll(pair.getSecond());
        }

        String[] holdDesc = Lang.translateDirect("tooltip.holdForDescription", "$")
                .getString()
                .split("\\$");
        MutableComponent keyShift = Lang.translateDirect("tooltip.keyShift");
        for (boolean shift : Iterate.falseAndTrue) {
            MutableComponent tabBuilder = Components.empty();
            tabBuilder.append(Components.literal(holdDesc[0]).withStyle(DARK_GRAY));
            tabBuilder.append(keyShift.plainCopy()
                    .withStyle(shift ? WHITE : GRAY));
            tabBuilder.append(Components.literal(holdDesc[1]).withStyle(DARK_GRAY));
            (shift ? longDescription : shortDescription).add(0, tabBuilder);
            (shift ? longDescription : shortDescription).add(1, Components.immutableEmpty());
        }
    }

    public void modify(Item item, List<Component> tooltip) {
        if (checkLocale()) {
            populateDescriptions();
            loadDescriptions(item);
        }
        if (shortDescription == null || longDescription == null)
            loadDescriptions(item);
        if (Screen.hasShiftDown()) {
            if (longDescription != null)
                tooltip.addAll(1, longDescription);
        } else {
            if (shortDescription != null)
                tooltip.addAll(1, shortDescription);
        }
    }

    private static boolean checkLocale() {
        String currentLanguage = Minecraft.getInstance()
                .getLanguageManager()
                .getSelected();
        if (!currentLanguage.equals(cachedLanguage)) {
            cachedLanguage = currentLanguage;
            return true;
        }
        return false;
    }

    private static void populateDescriptions() {
        descriptions = new HashMap<>();
        for (CopycatCharacteristics characteristics : CopycatCharacteristics.all()) {
            String titleKey = characteristics.getTitleKey();
            String descKey = characteristics.getDescriptionKey();

            if (!I18n.exists(titleKey) || !I18n.exists(descKey))
                continue;

            descriptions.put(characteristics, Pair.of(
                    List.of(Components.literal("- " + I18n.get(titleKey)).withStyle(GRAY)),
                    TooltipHelper.cutStringTextComponent(I18n.get(descKey), TooltipHelper.Palette.STANDARD_CREATE)
            ));
        }
    }

    @ExpectPlatform
    @NotNull
    public static TooltipModifier create(ItemLike item) {
        //noinspection DataFlowIssue
        return null;
    }
}

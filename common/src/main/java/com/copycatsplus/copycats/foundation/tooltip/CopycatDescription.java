package com.copycatsplus.copycats.foundation.tooltip;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

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
    private Language cachedLanguage = null;
    private Map<String, List<Object>> cachedArgs = new HashMap<>();
    private Map<CopycatCharacteristics, Pair<List<Component>, List<Component>>> descriptions;
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
            if (holdDesc.length > 1)
                tabBuilder.append(Components.literal(holdDesc[1]).withStyle(DARK_GRAY));
            (shift ? longDescription : shortDescription).add(0, tabBuilder);
            (shift ? longDescription : shortDescription).add(1, Components.immutableEmpty());
        }
    }

    public void modify(Item item, List<Component> tooltip) {
        if (shouldInvalidateCache()) {
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

    private boolean shouldInvalidateCache() {
        Language currentLanguage = Language.getInstance();
        Map<String, List<Object>> newArgs = new HashMap<>();
        for (CopycatCharacteristics characteristics : CopycatCharacteristics.all()) {
            newArgs.put(characteristics.getSerializedName(), Arrays.stream(characteristics.getArgs()).map(Supplier::get).toList());
        }
        if (!currentLanguage.equals(cachedLanguage) || !newArgs.equals(cachedArgs)) {
            cachedLanguage = currentLanguage;
            cachedArgs = newArgs;
            return true;
        }
        return false;
    }

    private void populateDescriptions() {
        descriptions = new HashMap<>();
        for (CopycatCharacteristics characteristics : CopycatCharacteristics.all()) {
            String titleKey = characteristics.getTitleKey();
            String descKey = characteristics.getDescriptionKey();

            if (!cachedLanguage.has(titleKey) || !cachedLanguage.has(descKey))
                continue;

            descriptions.put(characteristics, Pair.of(
                    List.of(Components.literal("- " + cachedLanguage.getOrDefault(titleKey)).withStyle(GRAY)),
                    TooltipHelper.cutStringTextComponent(String.format(cachedLanguage.getOrDefault(descKey), cachedArgs.get(characteristics.getSerializedName()).toArray()), TooltipHelper.Palette.STANDARD_CREATE)
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

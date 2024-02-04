package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.recipes.CCStandardRecipes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class CCDatagen implements DataGeneratorEntrypoint {

    private static final CreateRegistrate REGISTRATE = Copycats.getRegistrate();

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        addExtraRegistrateData();

        ExistingFileHelper helper = ExistingFileHelper.withResourcesFromArg();
        FabricDataGenerator.Pack pack = generator.createPack();
        Copycats.getRegistrate().setupDatagen(pack, helper);

        generator.createPack().addProvider(CCStandardRecipes::new);
    }

    private static void addExtraRegistrateData() {
        CCTagGen.addGenerators();

        REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            provideDefaultLang("interface", langConsumer);
            provideDefaultLang("tooltips", langConsumer);
        });
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/copycats/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }
}


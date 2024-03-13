package com.copycatsplus.copycats.datagen.fabric;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.CCDatagen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class CCDatagenImpl extends CCDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        addExtraRegistrateData();


        Path copycatsResources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
        // fixme re-enable the existing file helper when porting lib's ResourcePackLoader.createPackForMod is fixed
        ExistingFileHelper helper = new ExistingFileHelper(
                Set.of(copycatsResources), Set.of("create"), false, null, null
        );
        Copycats.getRegistrate().setupDatagen(generator, helper);
        Copycats.gatherData(generator);
    }
}

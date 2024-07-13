package com.copycatsplus.copycats.datagen.forge;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.datagen.CCDatagen;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

public class CCDatagenImpl extends CCDatagen {

    public static void gatherData(GatherDataEvent event) {
        addExtraRegistrateData();

        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            Copycats.gatherData(generator);
        }
    }
}

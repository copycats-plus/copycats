package com.copycatsplus.copycats;

import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoxItem;
import com.copycatsplus.copycats.content.copycat.board.CopycatCatwalkItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;

public class CCItems {

    private static final CreateRegistrate REGISTRATE = Copycats.getRegistrate();

    public static final ItemEntry<CopycatBoxItem> COPYCAT_BOX =
            REGISTRATE.item("copycat_box", CopycatBoxItem::new)
                    .model(AssetLookup.customBlockItemModel("copycat_base", "box"))
                    .tab(() -> CCCreativeTabs.MAIN)
                    .transform(FeatureToggle.registerDependent(CCBlocks.COPYCAT_BOARD))
                    .register();

    public static final ItemEntry<CopycatCatwalkItem> COPYCAT_CATWALK =
            REGISTRATE.item("copycat_catwalk", CopycatCatwalkItem::new)
                    .model(AssetLookup.customBlockItemModel("copycat_base", "catwalk"))
                    .tab(() -> CCCreativeTabs.MAIN)
                    .transform(FeatureToggle.registerDependent(CCBlocks.COPYCAT_BOARD))
                    .register();

    public static void register() {
    }
}

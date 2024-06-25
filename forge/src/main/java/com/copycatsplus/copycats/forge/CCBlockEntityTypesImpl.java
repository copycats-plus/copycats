package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.forge.MultiStateCopycatBlockEntityForge;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderMultiStateBlockEntity;
import com.copycatsplus.copycats.content.copycat.ladder.forge.CopycatLadderMultiStateBlockEntityForge;
import com.tterrag.registrate.builders.BlockEntityBuilder;

public class CCBlockEntityTypesImpl {

    public static BlockEntityBuilder.BlockEntityFactory<? extends MultiStateCopycatBlockEntity> getPlatformMultiState() {
        return MultiStateCopycatBlockEntityForge::new;
    }


    public static BlockEntityBuilder.BlockEntityFactory<? extends CopycatLadderMultiStateBlockEntity> getPlatformMultiStateLadder() {
        return CopycatLadderMultiStateBlockEntityForge::new;
    }
}

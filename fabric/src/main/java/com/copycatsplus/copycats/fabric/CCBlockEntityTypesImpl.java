package com.copycatsplus.copycats.fabric;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.fabric.MultiStateCopycatBlockEntityFabric;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderMultiStateBlockEntity;
import com.copycatsplus.copycats.content.copycat.ladder.fabric.CopycatLadderMultiStateBlockEntityFabric;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlockEntity;
import com.copycatsplus.copycats.content.copycat.shaft.fabric.CopycatShaftBlockEntityFabric;
import com.tterrag.registrate.builders.BlockEntityBuilder;

public class CCBlockEntityTypesImpl {

    public static BlockEntityBuilder.BlockEntityFactory<? extends MultiStateCopycatBlockEntity> getPlatformMultiState() {
        return MultiStateCopycatBlockEntityFabric::new;
    }

    public static BlockEntityBuilder.BlockEntityFactory<? extends CopycatLadderMultiStateBlockEntity> getPlatformMultiStateLadder() {
        return CopycatLadderMultiStateBlockEntityFabric::new;
    }

    public static BlockEntityBuilder.BlockEntityFactory<? extends CopycatShaftBlockEntity> getPlatformShaft() {
        return CopycatShaftBlockEntityFabric::new;
    }
}

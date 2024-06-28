package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftModel;
import com.copycatsplus.copycats.content.copycat.shaft.forge.CopycatShaftModelForge;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Block;

public class CCBlocksImpl {

    public static void getWrappedBlockState(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p, String name) {
        p.simpleBlock(c.getEntry(), p.models().withExistingParent(name, "block/barrier"));
    }

    public static BakedModel getShaftModel(BakedModel original, BakedModel copycat) {
        return new CopycatShaftModelForge(original, copycat);
    }
}

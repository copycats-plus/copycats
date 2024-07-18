package com.copycatsplus.copycats.fabric;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;

public class CCBlocksImpl {

    public static void getWrappedBlockState(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p, String name) {
        p.simpleBlock(c.getEntry(), p.models().withExistingParent(name, "block/barrier"));
    }
}

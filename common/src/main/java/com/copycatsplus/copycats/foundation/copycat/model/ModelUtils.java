package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.utility.Platform;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ModelUtils {

    @SafeVarargs
    @Environment(EnvType.CLIENT)
    public static <Model extends CopycatModelCore> NonNullConsumer<? super Block> createKineticModel(Function<BakedModel, BakedModel> original, Model... model) {
        return CreateRegistrate.blockModel(() -> m -> CopycatModelCore.createModel(original.apply(m), CopycatModelCore.kinetic(model)));
    }

    @Environment(EnvType.CLIENT)
    public static BakedModel getModelFor(BlockState mat) {
        return Platform.Environment.CLIENT.returnElseCurrent(() -> getModelOf(mat));
    }

    /**
     * Helper method to get the model of a block state.
     *
     * @param state The block state to get the model of.
     * @return The baked model of the block state.
     */
    @Environment(EnvType.CLIENT)
    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }
}

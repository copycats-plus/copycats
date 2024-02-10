package com.copycatsplus.copycats.content.copycat;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.mixin.copycat.CopycatModelAccessor;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ToggleableCopycatModel extends CopycatModel {
    private final CopycatModel base;
    private final CopycatModel enhanced;

    protected ToggleableCopycatModel(BakedModel originalModel, CopycatModel base, CopycatModel enhanced) {
        super(originalModel);
        this.base = base;
        this.enhanced = enhanced;
    }

    public static NonNullFunction<BakedModel, ? extends BakedModel> with(Function<BakedModel, CopycatModel> base, Function<BakedModel, CopycatModel> enhanced) {
        return model -> new ToggleableCopycatModel(model, base.apply(model), enhanced.apply(model));
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, Random rand, BlockState material, IModelData wrappedData) {
        return ((CopycatModelAccessor) (CCConfigs.client().useEnhancedModels.get() ? enhanced : base)).callGetCroppedQuads(state, side, rand, material, wrappedData);
    }
}

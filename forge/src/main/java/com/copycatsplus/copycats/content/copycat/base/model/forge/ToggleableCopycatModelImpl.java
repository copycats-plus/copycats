package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.ToggleableCopycatModel;
import com.copycatsplus.copycats.forge.mixin.copycat.base.CopycatModelAccessor;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.List;
import java.util.function.Function;

public class ToggleableCopycatModelImpl extends CopycatModel implements ToggleableCopycatModel {

    private final BakedModel base;
    private final BakedModel enhanced;

    public ToggleableCopycatModelImpl(BakedModel originalModel, BakedModel base, BakedModel enhanced) {
        super(originalModel);
        this.base = base;
        this.enhanced = enhanced;
    }

    public static NonNullFunction<BakedModel, ? extends BakedModel> with(SimpleCopycatPart base, SimpleCopycatPart enhanced) {
        return model -> new ToggleableCopycatModelImpl(model, SimpleCopycatPart.create(model, base), SimpleCopycatPart.create(model, enhanced));
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        return ((CopycatModelAccessor) (CCConfigs.client().useEnhancedModels.get() ? enhanced : base)).callGetCroppedQuads(state, side, rand, material, wrappedData, renderType);
    }
}

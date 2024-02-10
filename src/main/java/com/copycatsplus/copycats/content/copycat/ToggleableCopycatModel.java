package com.copycatsplus.copycats.content.copycat;

import com.copycatsplus.copycats.config.CCConfigs;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;
import java.util.function.Supplier;

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
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        (CCConfigs.client().useEnhancedModels.get() ? enhanced : base).emitBlockQuads(blockView, state, pos, randomSupplier, context);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        throw new RuntimeException("emitBlockQuadsInner should not be reachable in ToggleableCopycatModel");
    }
}

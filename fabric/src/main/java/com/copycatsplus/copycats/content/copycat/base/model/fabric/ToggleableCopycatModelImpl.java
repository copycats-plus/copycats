package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.ToggleableCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;
import java.util.function.Supplier;

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
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        ((FabricBakedModel) (CCConfigs.client().useEnhancedModels.get() ? enhanced : base)).emitBlockQuads(blockView, state, pos, randomSupplier, context);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        throw new RuntimeException("emitBlockQuadsInner should not be reachable in ToggleableCopycatModel");
    }
}

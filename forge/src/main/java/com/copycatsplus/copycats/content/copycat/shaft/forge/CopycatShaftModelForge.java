package com.copycatsplus.copycats.content.copycat.shaft.forge;

import com.copycatsplus.copycats.content.copycat.base.model.functional.forge.FunctionalCopycatRenderHelperImpl;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CopycatShaftModelForge extends BracketedKineticBlockModel {
    private final BakedModel copycat;

    public CopycatShaftModelForge(BakedModel template, BakedModel copycat) {
        super(template);
        this.copycat = copycat;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return copycat.getParticleIcon(data);
    }

    @Override
    public @NotNull ModelData getModelData(BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData) {
        return FunctionalCopycatRenderHelperImpl.mergeData(
                super.getModelData(world, pos, state, blockEntityData),
                copycat.getModelData(world, pos, state, blockEntityData)
        ).build();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        if (!ModelUtil.isVirtual(data)) return super.getQuads(state, side, rand, data, renderType);
        return copycat.getQuads(state, side, rand, data, renderType);
    }
}

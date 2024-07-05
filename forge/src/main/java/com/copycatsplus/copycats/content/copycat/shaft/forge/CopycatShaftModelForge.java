package com.copycatsplus.copycats.content.copycat.shaft.forge;

import com.copycatsplus.copycats.content.copycat.base.model.functional.forge.BakedModelWithDataBuilder;
import com.copycatsplus.copycats.content.copycat.base.model.functional.forge.FunctionalCopycatRenderHelperImpl;
import com.jozufozu.flywheel.core.model.ModelUtil;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

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
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull IModelData data) {
        return copycat.getParticleIcon(data);
    }

    @Override
    public IModelData getModelData(BlockAndTintGetter world, BlockPos pos, BlockState state, IModelData blockEntityData) {
        return FunctionalCopycatRenderHelperImpl.mergeData(
                BakedModelWithDataBuilder.isVirtual(blockEntityData) ? blockEntityData : super.getModelData(world, pos, state, blockEntityData),
                copycat.getModelData(world, pos, state, blockEntityData)
        ).build();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, Random rand, IModelData data) {
        if (!BakedModelWithDataBuilder.isVirtual(data)) return super.getQuads(state, side, rand, data);
        return copycat.getQuads(state, side, rand, data);
    }
}

package com.copycatsplus.copycats.content.copycat.shaft.fabric;

import com.jozufozu.flywheel.core.virtual.VirtualEmptyBlockGetter;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import io.github.fabricators_of_create.porting_lib.models.CustomParticleIconModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CopycatShaftModelFabric extends BracketedKineticBlockModel implements CustomParticleIconModel {
    private final BakedModel copycat;

    public CopycatShaftModelFabric(BakedModel template, BakedModel copycat) {
        super(template);
        this.copycat = copycat;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon(Object data) {
        return ((CustomParticleIconModel) copycat).getParticleIcon(data);
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (!VirtualEmptyBlockGetter.is(blockView)) {
            super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        } else {
            copycat.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        }
    }
}

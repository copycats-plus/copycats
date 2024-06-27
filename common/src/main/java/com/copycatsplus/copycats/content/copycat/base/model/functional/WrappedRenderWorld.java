package com.copycatsplus.copycats.content.copycat.base.model.functional;


import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlockEntity;
import com.jozufozu.flywheel.core.virtual.VirtualEmptyBlockGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LayerLightEventListener;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrappedRenderWorld implements VirtualEmptyBlockGetter {
    protected final BlockAndTintGetter level;
    protected final BlockPos targetPos;
    protected final LevelLightEngine lightEngine;
    protected final BlockState material;

    protected boolean ctMode = false;

    public WrappedRenderWorld(IFunctionalCopycatBlockEntity be) {
        this.level = be.getLevel();
        this.targetPos = be.getBlockPos();
        this.material = be.getMaterial();
        lightEngine = new LevelLightEngine(new LightChunkGetter() {
            @Override
            @Nullable
            public LightChunk getChunkForLighting(int p_63023_, int p_63024_) {
                return null;
            }

            @Override
            public @NotNull BlockGetter getLevel() {
                return WrappedRenderWorld.this;
            }
        }, false, false) {
            private final LayerLightEventListener blockListener = createStaticListener(15);
            private final LayerLightEventListener skyListener = createStaticListener(15);

            @Override
            public @NotNull LayerLightEventListener getLayerListener(@NotNull LightLayer layer) {
                return layer == LightLayer.BLOCK ? blockListener : skyListener;
            }

            @Override
            public int getRawBrightness(BlockPos blockPos, int amount) {
                return 15;
            }
        };
    }

    private static LayerLightEventListener createStaticListener(int light) {
        return new LayerLightEventListener() {
            @Override
            public void checkBlock(@NotNull BlockPos pos) {
            }

            @Override
            public boolean hasLightWork() {
                return false;
            }

            @Override
            public int runLightUpdates() {
                return 0;
            }

            @Override
            public void updateSectionStatus(@NotNull SectionPos pos, boolean isSectionEmpty) {
            }

            @Override
            public void setLightEnabled(@NotNull ChunkPos pos, boolean lightEnabled) {
            }

            @Override
            public void propagateLightSources(@NotNull ChunkPos pos) {
            }

            @Override
            public DataLayer getDataLayerData(@NotNull SectionPos pos) {
                return null;
            }

            @Override
            public int getLightValue(@NotNull BlockPos pos) {
                return light;
            }
        };
    }

    public WrappedRenderWorld setCTMode(boolean ctMode) {
        this.ctMode = ctMode;
        return this;
    }

    @Override
    @Nullable
    public BlockEntity getBlockEntity(@NotNull BlockPos pos) {
        if (!pos.equals(targetPos)) return null;
        return level.getBlockEntity(pos);
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pos) {
        if (!pos.equals(targetPos)) return ctMode ? material : Blocks.AIR.defaultBlockState();
        return level.getBlockState(pos);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pos) {
        if (!pos.equals(targetPos)) return Fluids.EMPTY.defaultFluidState();
        return level.getFluidState(pos);
    }

    @Override
    public int getHeight() {
        return level.getHeight();
    }

    @Override
    public int getMinBuildHeight() {
        return level.getMinBuildHeight();
    }

    @Override
    public float getShade(@NotNull Direction direction, boolean shaded) {
        return 1f;
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return lightEngine;
    }

    @Override
    public int getBrightness(LightLayer lightType, BlockPos blockPos) {
        return 15;
    }

    @Override
    public int getRawBrightness(BlockPos blockPos, int amount) {
        return 15;
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pos, @NotNull ColorResolver resolver) {
        Biome plainsBiome = Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
        return resolver.getColor(plainsBiome, pos.getX(), pos.getZ());
    }
}

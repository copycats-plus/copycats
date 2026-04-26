package com.copycatsplus.copycats.foundation.copycat.model.kinetic;


import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A virtual world to render the kinetic copycat models in.
 */
@ApiStatus.Internal
public class WrappedRenderWorld extends VirtualRenderWorld {
    protected final BlockAndTintGetter level;
    protected final BlockPos targetPos;
    protected final BlockState material;
    protected ModelData modelData;

    public WrappedRenderWorld(ICopycatBlockEntity be) {
        super(be.getLevel(), be.getLevel().getMinBuildHeight(), be.getLevel().getHeight(), Vec3i.ZERO, () -> {
        });
        this.level = be.getLevel();
        this.targetPos = be.getBlockPos();
        this.material = be.getMaterial();
    }

    public BlockAndTintGetter getWrappedLevel() {
        return this.level;
    }

    public WrappedRenderWorld withModelData(ModelData modelData) {
        this.modelData = modelData;
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
        if (!pos.equals(targetPos)) return material;
        return level.getBlockState(pos);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pos) {
        if (!pos.equals(targetPos)) return Fluids.EMPTY.defaultFluidState();
        return level.getFluidState(pos);
    }

    @Override
    public float getShade(@NotNull Direction direction, boolean shade) {
        return 1;
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver resolver) {
        Biome plainsBiome = Minecraft.getInstance().getConnection().registryAccess().registryOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
        return resolver.getColor(plainsBiome, pos.getX(), pos.getZ());
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockPos pos) {
        if (this.modelData != null && pos.equals(targetPos)) {
            return this.modelData;
        }
        return super.getModelData(pos);
    }

    @Override
    public void blockEntityChanged(BlockPos pos) {
        // no-op
        // cannot set changed state because getChunk is not implemented
    }
}

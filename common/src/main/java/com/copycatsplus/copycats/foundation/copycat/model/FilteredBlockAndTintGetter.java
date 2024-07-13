package com.copycatsplus.copycats.foundation.copycat.model;

import java.util.function.Predicate;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/**
 * A filtered world that allows copycats to control how connected textures behave.
 */
@ApiStatus.Internal
public class FilteredBlockAndTintGetter implements BlockAndTintGetter {

    public final BlockAndTintGetter wrapped;
    public final Predicate<BlockPos> filter;

    public FilteredBlockAndTintGetter(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        this.wrapped = wrapped;
        this.filter = filter;
    }

    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pPos) {
        return filter.test(pPos) ? wrapped.getBlockEntity(pPos) : null;
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pPos) {
        return filter.test(pPos) ? wrapped.getBlockState(pPos) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pPos) {
        return filter.test(pPos) ? wrapped.getFluidState(pPos) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight() {
        return wrapped.getHeight();
    }

    @Override
    public int getMinBuildHeight() {
        return wrapped.getMinBuildHeight();
    }

    @Override
    public float getShade(@NotNull Direction pDirection, boolean pShade) {
        return wrapped.getShade(pDirection, pShade);
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return wrapped.getLightEngine();
    }

    @Override
    public int getBlockTint(@NotNull BlockPos pBlockPos, @NotNull ColorResolver pColorResolver) {
        return wrapped.getBlockTint(pBlockPos, pColorResolver);
    }
}


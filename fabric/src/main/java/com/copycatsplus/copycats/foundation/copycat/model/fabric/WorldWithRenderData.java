package com.copycatsplus.copycats.foundation.copycat.model.fabric;

import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public record WorldWithRenderData(BlockAndTintGetter blockView, Object renderData,
                                  BlockPos origin) implements RenderAttachedBlockView {
    @Override
    public float getShade(@NotNull Direction direction, boolean shade) {
        return blockView.getShade(direction, shade);
    }

    @Override
    public @NotNull LevelLightEngine getLightEngine() {
        return blockView.getLightEngine();
    }

    @Override
    public int getBlockTint(@NotNull BlockPos blockPos, @NotNull ColorResolver colorResolver) {
        return blockView.getBlockTint(blockPos, colorResolver);
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pos) {
        return blockView.getBlockEntity(pos);
    }

    @Override
    public @NotNull BlockState getBlockState(@NotNull BlockPos pos) {
        return blockView.getBlockState(pos);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockPos pos) {
        return blockView.getFluidState(pos);
    }

    @Override
    public int getHeight() {
        return blockView.getHeight();
    }

    @Override
    public int getMinBuildHeight() {
        return blockView.getMinBuildHeight();
    }

    @Deprecated
    @Nullable
    public Object getBlockEntityRenderAttachment(BlockPos pos) {
        if (pos.equals(origin))
            return renderData;
        else if (blockView instanceof RenderAttachedBlockView renderView) {
            return renderView.getBlockEntityRenderAttachment(pos);
        }
        return null;
    }
}

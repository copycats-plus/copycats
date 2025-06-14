package com.copycatsplus.copycats.foundation.copycat;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A wrapped copycat block that extends {@link CopycatBlock} and delegates calls to an {@link ICopycatBlock}.
 * <p>
 * This class is used to get around instanceof CopycatBlock checks in Create's codebase. Do not use this class for any
 * other purpose.
 */
@ApiStatus.Internal
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class WrappedCopycatBlock extends CopycatBlock {

    private final ThreadLocal<ICopycatBlock> wrapped = new ThreadLocal<>();

    public WrappedCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    public ICopycatBlock getWrapped() {
        return wrapped.get();
    }

    public void setWrapped(ICopycatBlock wrapped) {
        this.wrapped.set(wrapped);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, @Nullable BlockPos toPos) {
        return wrapped.get().isIgnoredConnectivitySide(reader, state, face, fromPos, toPos, toPos == null ? null : reader.getBlockState(toPos));
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return wrapped.get().canConnectTexturesToward(reader, fromPos, toPos, state);
    }
}
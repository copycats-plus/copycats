package com.copycatsplus.copycats.foundation.copycat;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

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
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return wrapped.get().isIgnoredConnectivitySide(reader, state, face, fromPos, toPos);
    }

    @Nullable
    @Override
    public BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState fromState, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos); // toPos is the position with copycat

        if (fromState.getBlock() instanceof ICopycatBlock fromCopycat) {
            if (!fromCopycat.canConnectTexturesToward(reader, fromPos, toPos, fromState))
                return null;
        }

        if (toState.getBlock() instanceof ICopycatBlock toCopycat) {
            if (toCopycat.isIgnoredConnectivitySide(reader, toState, face, toPos, fromPos))
                return null;
        }

        return CopycatBlock.getMaterial(reader, toPos);
    }
}
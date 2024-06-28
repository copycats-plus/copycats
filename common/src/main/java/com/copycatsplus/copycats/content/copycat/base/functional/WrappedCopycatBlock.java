package com.copycatsplus.copycats.content.copycat.base.functional;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WrappedCopycatBlock extends CopycatBlock {

    private final ThreadLocal<IFunctionalCopycatBlock> wrapped = new ThreadLocal<>();

    public WrappedCopycatBlock(Properties pProperties) {
        super(pProperties);
    }

    public IFunctionalCopycatBlock getWrapped() {
        return wrapped.get();
    }

    public void setWrapped(IFunctionalCopycatBlock wrapped) {
        this.wrapped.set(wrapped);
    }

    @Nullable
    @Override
    public BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState otherState, Direction face, BlockPos fromPos, BlockPos toPos) {
        return wrapped.get().getConnectiveMaterial(reader, otherState, face, fromPos, toPos);
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return wrapped.get().isIgnoredConnectivitySide(reader, state, face, fromPos, toPos);
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return wrapped.get().canFaceBeOccluded(state, face);
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return wrapped.get().shouldFaceAlwaysRender(state, face);
    }
}

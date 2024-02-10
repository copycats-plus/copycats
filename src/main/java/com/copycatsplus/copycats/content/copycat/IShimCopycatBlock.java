package com.copycatsplus.copycats.content.copycat;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IShimCopycatBlock {
    default boolean isUnblockableConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    @Nullable
    default BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState fromState, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos); // toPos is the position with copycat

        if (fromState.getBlock() instanceof IShimCopycatBlock fromCopycat) {
            BlockEntity be = reader.getBlockEntity(fromPos);
            if (be instanceof CTCopycatBlockEntity ctbe) {
                if (!ctbe.isCTEnabled()) return null;
            }
            if (!fromCopycat.canConnectTexturesToward(reader, fromPos, toPos, fromState))
                return null;
        }

        if (toState.getBlock() instanceof IShimCopycatBlock toCopycat) {
            if (toCopycat instanceof ICTCopycatBlock ctBlock) {
                if (!ctBlock.allowCTAppearance(toState, reader, toPos, face.getOpposite(), fromState, fromPos))
                    return null;
            }
            if (toCopycat.isIgnoredConnectivitySide(reader, toState, face.getOpposite(), toPos, fromPos))
                return null;
        }

        return CopycatBlock.getMaterial(reader, toPos);
    }

    boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState selfState);

    boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos);
}

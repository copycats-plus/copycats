package com.copycatsplus.copycats.foundation.copycat;

import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface ICopycatCullable {
    /**
     * Whether this copycat can occlude faces of adjacent blocks if their shape is fully covered by the copycat.
     *
     * @param level The world.
     * @param state The state of the copycat block.
     * @param pos   The position of the copycat block.
     * @return Whether the copycat can occlude faces of adjacent blocks.
     */
    default boolean canOcclude(BlockGetter level, BlockState state, BlockPos pos) {
        BlockState material = ICopycatBlock.getMaterial(level, pos);
        if (AllBlocks.COPYCAT_BASE.has(material)) return false; // copycat_base is incorrectly set to occlude
        return material.canOcclude();
    }

    /**
     * Whether the shape of this copycat can occlude the face of an adjacent block.
     * <p>
     * Implementations of this method should not consider occlusion criteria that are based on the material of the copycat.
     * Only properties intrinsic to the copycat block, such as its shape, should be considered.
     *
     * @param level       The world.
     * @param pos         The position of the copycat block.
     * @param state       The state of the copycat block.
     * @param neighborPos The position of the adjacent block.
     * @param dir         The direction from the copycat block to the adjacent block.
     * @return Whether the shape of the copycat can occlude the face of the adjacent block. If empty, the vanilla occlusion logic is used.
     */
    default Optional<Boolean> shapeCanOccludeNeighbor(BlockGetter level, BlockPos pos, BlockState state, BlockPos neighborPos, Direction dir) {
        return Optional.empty();
    }
}

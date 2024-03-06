package com.copycatsplus.copycats.content.copycat.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

/**
 * A block should implement this interface if it wishes to use non-default logic to determine the "blocking" mechanic of
 * connected textures.
 * <br><br>
 * "Blocking" causes the texture on the rendering block to appear disconnected to an adjacent block even though it
 * should normally appear connected. This is due the existence of a third block that connects to the adjacent block
 * perpendicularly. With blocking in place, both the rendering block and the third block (by reverse blocking) shows an
 * edge along the 90-degree bend.
 * <br><br>
 * By default, blocking takes place on a face of the rendering block if the same face on the adjacent block is covered
 * up by an opposite full face of the third block. Whether a face is full is determined by {@link Block#getShape}.
 */
public interface ICustomCTBlocking {
    /**
     * Determine whether this block can block connected textures between another two blocks due to one of their faces being covered by this block.
     *
     * @param reader        The world.
     * @param state         The state of this block, which is blocking CT on another two blocks.
     * @param pos           The position of this block.
     * @param ctPos         The position of the block that is trying to render connected textures.
     * @param connectingPos The position of the block that ctPos is trying to connect to, but is blocked by this block.
     * @param face          The block face that this block is blocking with.
     * @return Whether the connection between ctPos and connectingPos should be blocked by this block. Return {@link Optional#empty()} to use default logic.
     */
    default Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return Optional.empty();
    }

    /**
     * Determine whether connected textures on this block are blocked by another block.
     *
     * @param reader        The world.
     * @param state         The state of this block, which is trying to render connected textures.
     * @param pos           The position of this block.
     * @param connectingPos The position of the adjacent block that this block is trying to connect to.
     * @param blockingPos   The position of a third block that is trying to block the CT of this block.
     * @param face          The block face that this block is trying to render.
     * @return Whether the connected textures on this block are blocked by another block. Return {@link Optional#empty()} to use default logic.
     */
    default Optional<Boolean> isCTBlocked(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face) {
        return Optional.empty();
    }
}
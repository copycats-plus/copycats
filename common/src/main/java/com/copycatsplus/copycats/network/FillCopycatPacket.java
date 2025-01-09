package com.copycatsplus.copycats.network;

import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fill every part of a multi-state copycat with the given material.
 * The consumed item must be empty.
 * <p>
 * Every part except the specified one will be filled. The specified part will be filled by the server-bound
 * item use packet.
 */
public record FillCopycatPacket(BlockPos pos, BlockState material, String property) implements PacketSystem.C2SPacket {
    public FillCopycatPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readById(Block.BLOCK_STATE_REGISTRY), buf.readUtf());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeId(Block.BLOCK_STATE_REGISTRY, material);
        buffer.writeUtf(property);
    }

    @Override
    public void handle(ServerPlayer sender) {
        Level level = sender.level();
        if (!level.isLoaded(pos)) return;
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock)) return;
        BlockState prevMaterial = IMultiStateCopycatBlock.getMaterial(level, pos, property);
        copycatBlock.fillEmptyParts(level, pos, state, material);
        copycatBlock.getCopycatBlockEntity(level, pos).setMaterial(property, prevMaterial);
    }
}

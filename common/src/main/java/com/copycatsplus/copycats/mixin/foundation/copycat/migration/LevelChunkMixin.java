package com.copycatsplus.copycats.mixin.foundation.copycat.migration;

import com.copycatsplus.copycats.foundation.copycat.MigrationManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Migrate copycat block entities to multi-state copycat block entities when loading worlds.
 */
@Mixin(value = LevelChunk.class)
public abstract class LevelChunkMixin {

    @Inject(method = "setBlockEntity", at = @At(value = "HEAD"), cancellable = true)
    private void migrateBlockEntity(BlockEntity blockEntity, CallbackInfo ci) {
        LevelChunk chunk = (LevelChunk) (Object) this;
        BlockPos pos = blockEntity.getBlockPos();
        BlockEntity newBlockEntity = MigrationManager.migrateBlockEntity(chunk, blockEntity);
        if (newBlockEntity != blockEntity) {
            chunk.removeBlockEntity(pos);
            chunk.setBlockEntity(newBlockEntity);
            ci.cancel();
        }
    }
}

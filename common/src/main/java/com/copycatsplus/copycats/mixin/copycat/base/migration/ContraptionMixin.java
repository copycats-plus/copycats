package com.copycatsplus.copycats.mixin.copycat.base.migration;

import com.copycatsplus.copycats.foundation.copycat.MigrationManager;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Migrate copycat block entities to multi-state copycat block entities when loading contraptions.
 */
@Mixin(Contraption.class)
public class ContraptionMixin {
    @Inject(
            method = "readStructureBlockInfo",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void readStructureBlockInfo(CompoundTag blockListEntry, HashMapPalette<BlockState> palette, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
        cir.setReturnValue(MigrationManager.migrateStructure(cir.getReturnValue()));
    }

    @Inject(
            method = "legacyReadStructureBlockInfo",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void legacyReadStructureBlockInfo(CompoundTag blockListEntry, HolderGetter<Block> holderGetter, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
        cir.setReturnValue(MigrationManager.migrateStructure(cir.getReturnValue()));
    }
}

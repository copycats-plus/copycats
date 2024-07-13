package com.copycatsplus.copycats.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.HashMapPalette;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
        copycats$migrateStructureBlockInfo(cir);
    }

    @Inject(
            method = "legacyReadStructureBlockInfo",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void legacyReadStructureBlockInfo(CompoundTag blockListEntry, HolderGetter<Block> holderGetter, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
        copycats$migrateStructureBlockInfo(cir);
    }

    @Unique
    private static void copycats$migrateStructureBlockInfo(CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
        BlockState state = cir.getReturnValue().state();
        CompoundTag nbt = cir.getReturnValue().nbt();
        if (state.getBlock() instanceof MultiStateCopycatBlock && nbt != null && nbt.contains("Material")) {
            BlockPos pos = cir.getReturnValue().pos();
            CopycatBlockEntity be = AllBlockEntityTypes.COPYCAT.create(pos, state);
            be.load(nbt);
            MultiStateCopycatBlockEntity multiBe = CCBlockEntityTypes.MULTI_STATE_COPYCAT.create(pos, state);
            multiBe.migrateData((ICopycatBlockEntity) be);
            nbt = multiBe.saveWithId();
            cir.setReturnValue(new StructureTemplate.StructureBlockInfo(pos, state, nbt));
        } else if (state.getBlock() instanceof CCCopycatBlock &&
                nbt != null &&
                nbt.contains("id") &&
                nbt.getString("id").equals(AllBlockEntityTypes.COPYCAT.getId().toString())) {
            BlockPos pos = cir.getReturnValue().pos();
            CCCopycatBlockEntity be = CCBlockEntityTypes.COPYCAT.create(pos, state);
            be.load(nbt);
            nbt = be.saveWithId();
            cir.setReturnValue(new StructureTemplate.StructureBlockInfo(pos, state, nbt));
        }
    }
}

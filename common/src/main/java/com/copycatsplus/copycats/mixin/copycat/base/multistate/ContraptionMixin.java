package com.copycatsplus.copycats.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
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
            CopycatBlockEntity be = new CopycatBlockEntity(AllBlockEntityTypes.COPYCAT.get(), pos, state);
            be.load(nbt);
            MultiStateCopycatBlockEntity multiBe = MultiStateCopycatBlockEntity.create(CCBlockEntityTypes.MULTI_STATE_COPYCAT_BLOCK_ENTITY.get(), pos, state);
            multiBe.migrateData(be);
            nbt = multiBe.saveWithId();
            cir.setReturnValue(new StructureTemplate.StructureBlockInfo(pos, state, nbt));
        }
    }
}

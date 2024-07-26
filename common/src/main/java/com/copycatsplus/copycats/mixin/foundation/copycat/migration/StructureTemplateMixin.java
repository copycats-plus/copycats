package com.copycatsplus.copycats.mixin.foundation.copycat.migration;

import com.copycatsplus.copycats.foundation.copycat.MigrationManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {
    @WrapOperation(
            method = "loadPalette",
            at = @At(value = "NEW", target = "net/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo")
    )
    private StructureTemplate.StructureBlockInfo migrateStructure(BlockPos pos, BlockState state, @Nullable CompoundTag nbt, Operation<StructureTemplate.StructureBlockInfo> original) {
        return MigrationManager.migrateStructure(original.call(pos, state, nbt));
    }
}

package com.copycatsplus.copycats.mixin.copycat.door;

import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix a hidden bug in Create which causes a crash due to missing ID when merging block entities in schematic printing.
 */
@Mixin(BlockHelper.class)
public class BlockHelperMixin {
    @Inject(
            method = "placeSchematicBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;loadStatic(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/block/entity/BlockEntity;")
    )
    private static void placeSchematicBlock(Level world, BlockState state, BlockPos target, ItemStack stack, CompoundTag data, CallbackInfo ci) {
        BlockEntity existingBlockEntity = world.getBlockEntity(target);
        if (existingBlockEntity != null && existingBlockEntity.getBlockState().getBlock().equals(state.getBlock())) {
            ResourceLocation resourceLocation = BlockEntityType.getKey(existingBlockEntity.getType());
            assert resourceLocation != null;
            data.putString("id", resourceLocation.toString());
        }
    }
}

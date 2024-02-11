package com.copycatsplus.copycats.mixin.copycat;


import com.copycatsplus.copycats.content.copycat.CTCopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CopycatBlockEntity.class)
public abstract class CopycatBlockEntityMixin extends SmartBlockEntity implements CTCopycatBlockEntity {
    public CopycatBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Invoker
    @Override
    public abstract void callRedraw();

    @Unique
    private boolean enableCT = true;

    @Unique
    @Override
    public boolean isCTEnabled() {
        return enableCT;
    }

    @Unique

    @Override
    public void setCTEnabled(boolean value) {
        enableCT = value;
        setChanged();
    }

    @Inject(
            at = @At("HEAD"),
            method = "write(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)V"
    )
    private void writeCT(CompoundTag tag, ItemStack stack, BlockState material, CallbackInfo ci) {
        tag.putBoolean("EnableCT", enableCT);
    }

    @Inject(
            at = @At("HEAD"),
            method = "read(Lnet/minecraft/nbt/CompoundTag;Z)V"
    )
    private void readCT(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        if (tag.contains("EnableCT")) // need to check because copycats migrated from C:Connected don't have this tag
            enableCT = tag.getBoolean("EnableCT");
        else
            enableCT = true;
    }
}

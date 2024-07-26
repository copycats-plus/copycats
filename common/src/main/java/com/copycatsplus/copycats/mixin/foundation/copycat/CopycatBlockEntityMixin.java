package com.copycatsplus.copycats.mixin.foundation.copycat;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlockEntity;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Expand Create's {@link CopycatBlockEntity} with CT toggle.
 * <p>
 * Also implement {@link ICopycatBlockEntity} to unify instanceof checks.
 */
@Mixin(value = CopycatBlockEntity.class)
public abstract class CopycatBlockEntityMixin extends SmartBlockEntity implements ICopycatBlockEntity {
    @Shadow
    private BlockState material;

    @Shadow
    private ItemStack consumedItem;

    public CopycatBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Unique
    private boolean copycats$enableCT = true;

    @Override
    public boolean isCTEnabled() {
        return copycats$enableCT;
    }

    @Override
    public void setMaterialInternal(BlockState material) {
        this.material = material;
    }

    @Override
    public void setConsumedItemInternal(ItemStack consumedItem) {
        this.consumedItem = consumedItem;
    }

    @Override
    public void setCTEnabledInternal(boolean value) {
        this.copycats$enableCT = value;
    }

    @Inject(
            at = @At("HEAD"),
            method = "write(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)V"
    )
    private void writeCT(CompoundTag tag, ItemStack stack, BlockState material, CallbackInfo ci) {
        tag.putBoolean("EnableCT", copycats$enableCT);
    }

    @Inject(
            at = @At("HEAD"),
            method = "read(Lnet/minecraft/nbt/CompoundTag;Z)V"
    )
    private void readCT(CompoundTag tag, boolean clientPacket, CallbackInfo ci) {
        if (tag.contains("EnableCT")) // need to check because copycats migrated from C:Connected don't have this tag
            copycats$enableCT = tag.getBoolean("EnableCT");
        else
            copycats$enableCT = true;
    }
}

package com.copycatsplus.copycats.mixin.entity;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCCatVariants;
import com.simibubi.create.AllTags;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Optional;

/**
 * Add a copycat variant to cats.
 */
@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {
    @Shadow
    protected abstract void usePlayerItem(Player pPlayer, InteractionHand pHand, ItemStack pStack);

    @Shadow
    public abstract Holder<CatVariant> getVariant();

    @Shadow
    public abstract void setVariant(Holder<CatVariant> variant);

    protected CatMixin(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    private static final EntityDataAccessor<Holder<CatVariant>> DATA_NATURAL_VARIANT_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.CAT_VARIANT);

    @Unique
    public Holder<CatVariant> getNaturalVariant() {
        return this.entityData.get(DATA_NATURAL_VARIANT_ID);
    }

    @Unique
    public void setNaturalVariant(Holder<CatVariant> pVariant) {
        this.entityData.set(DATA_NATURAL_VARIANT_ID, pVariant);
    }

    @Inject(
            at = @At("RETURN"),
            method = "defineSynchedData"
    )
    private void defineNaturalVariant(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_NATURAL_VARIANT_ID, BuiltInRegistries.CAT_VARIANT.getHolderOrThrow(CatVariant.ALL_BLACK));
    }

    @Inject(
            at = @At("RETURN"),
            method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"
    )
    private void addNaturalVariantData(CompoundTag compound, CallbackInfo ci) {
        compound.putString("NaturalVariant", this.getNaturalVariant().unwrapKey().orElse(CatVariant.ALL_BLACK).location().toString());
    }

    @Inject(
            at = @At("RETURN"),
            method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"
    )
    private void readNaturalVariantData(CompoundTag compound, CallbackInfo ci) {
        Optional<ResourceKey<CatVariant>> catVariant = Optional.ofNullable(ResourceLocation.tryParse(compound.getString("NaturalVariant"))).map((arg) -> ResourceKey.create(Registries.CAT_VARIANT, arg));
        Registry<CatVariant> registry = BuiltInRegistries.CAT_VARIANT;
        Objects.requireNonNull(registry);
        catVariant.flatMap(registry::getHolder).ifPresent(this::setNaturalVariant);
    }

    @Inject(
            at = @At("HEAD"),
            method = "usePlayerItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"
    )
    private void useCopycat(Player pPlayer, InteractionHand pHand, ItemStack pStack, CallbackInfo ci) {
        if (pStack.is(CCBlocks.COPYCAT_BLOCK.asItem())) {
            this.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 75f, .95f);
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "mobInteract(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            cancellable = true
    )
    private void copycatInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        if (stack.is(CCBlocks.COPYCAT_BLOCK.asItem())) {
            Holder<CatVariant> currentVariant = getVariant();
            if (currentVariant.equals(CCCatVariants.COPY_CAT)) return;

            if (!level().isClientSide()) {
                this.setNaturalVariant(currentVariant);
                this.setVariant(CCCatVariants.COPY_CAT);
                this.usePlayerItem(pPlayer, pHand, stack);
                this.setPersistenceRequired();
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level().isClientSide()));
        } else if (stack.is(Tags.Items.TOOLS_WRENCH)) {
            Holder<CatVariant> currentVariant = getVariant();
            if (!currentVariant.equals(CCCatVariants.COPY_CAT)) return;

            if (!level().isClientSide()) {
                this.setVariant(this.getNaturalVariant());
                this.setPersistenceRequired();
                this.spawnAtLocation(CCBlocks.COPYCAT_BLOCK.asItem());
                SoundType soundType = CCBlocks.COPYCAT_BLOCK.getDefaultState().getSoundType();
                this.playSound(soundType.getBreakSound(), (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level().isClientSide()));
        }
    }
}

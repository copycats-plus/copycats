package com.copycatsplus.copycats.mixin.entity;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.simibubi.create.AllTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimal {

    @Unique
    private static final int COPY_CAT_ID = 11;

    static {
        Cat.TEXTURE_BY_TYPE.put(COPY_CAT_ID, new ResourceLocation(Copycats.MODID, "textures/entity/cat/copy_cat.png"));
    }

    @Shadow
    public abstract int getCatType();

    @Shadow
    public abstract void setCatType(int pType);

    @Inject(
            at = @At("HEAD"),
            method = "setCatType(I)V",
            cancellable = true
    )
    private void setCopyCat(int pType, CallbackInfo ci) {
        if (pType == COPY_CAT_ID) {
            this.entityData.set(DATA_TYPE_ID, pType);
            ci.cancel();
        }
    }

    @ModifyArg(
            at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"),
            method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;",
            index = 0
    )
    private int spawnCopyCat(int original) {
        if (original <= 10) return original;
        else return original + 1;
    }

    @Shadow
    protected abstract void usePlayerItem(Player pPlayer, InteractionHand pHand, ItemStack pStack);

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_TYPE_ID;

    protected CatMixin(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    private static final EntityDataAccessor<Integer> DATA_NATURAL_TYPE_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.INT);

    @Unique
    public int getNaturalType() {
        return this.entityData.get(DATA_NATURAL_TYPE_ID);
    }

    @Unique
    public void setNaturalType(int pType) {
        this.entityData.set(DATA_NATURAL_TYPE_ID, pType);
    }

    @Inject(
            at = @At("RETURN"),
            method = "defineSynchedData()V"
    )
    private void defineNaturalVariant(CallbackInfo ci) {
        this.entityData.define(DATA_NATURAL_TYPE_ID, 10);
    }

    @Inject(
            at = @At("RETURN"),
            method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"
    )
    private void addNaturalVariantData(CompoundTag pCompound, CallbackInfo ci) {
        pCompound.putInt("NaturalType", this.getNaturalType());
    }

    @Inject(
            at = @At("RETURN"),
            method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"
    )
    private void readNaturalVariantData(CompoundTag pCompound, CallbackInfo ci) {
        int naturalType = pCompound.getInt("NaturalType");
        this.setNaturalType(naturalType);
    }

    @Inject(
            at = @At("HEAD"),
            method = "usePlayerItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"
    )
    private void useCopycat(Player pPlayer, InteractionHand pHand, ItemStack pStack, CallbackInfo ci) {
        if (pStack.is(CCBlocks.COPYCAT_BLOCK.get().asItem())) {
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
        if (stack.is(CCBlocks.COPYCAT_BLOCK.get().asItem())) {
            int currentType = getCatType();
            if (currentType == COPY_CAT_ID) return;

            if (!level.isClientSide()) {
                this.setNaturalType(currentType);
                this.setCatType(COPY_CAT_ID);
                this.usePlayerItem(pPlayer, pHand, stack);
                this.setPersistenceRequired();
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        } else if (stack.is(AllTags.AllItemTags.WRENCH.tag)) {
            int currentVariant = getCatType();
            if (currentVariant != COPY_CAT_ID) return;

            if (!level.isClientSide()) {
                this.setCatType(this.getNaturalType());
                this.setPersistenceRequired();
                this.spawnAtLocation(CCBlocks.COPYCAT_BLOCK.get().asItem());
                SoundType soundType = CCBlocks.COPYCAT_BLOCK.getDefaultState().getSoundType();
                this.playSound(soundType.getBreakSound(), (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }
}

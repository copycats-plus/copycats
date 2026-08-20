package com.copycatsplus.copycats.fabric.mixin.foundation.copycat;

import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
import com.copycatsplus.copycats.content.copycat.pane.CopycatPaneBlock;
import com.copycatsplus.copycats.content.copycat.sliding_door.CopycatSlidingDoorBlock;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonBlock;
import com.copycatsplus.copycats.content.copycat.fence.CopycatFenceBlock;
import com.copycatsplus.copycats.content.copycat.fence_gate.CopycatFenceGateBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.CopycatGlassFluidPipeBlock;
import com.copycatsplus.copycats.content.copycat.ladder.CopycatLadderBlock;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatPressurePlateBlock;
import com.copycatsplus.copycats.content.copycat.pressure_plate.CopycatWeightedPressurePlate;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.copycatsplus.copycats.content.copycat.stairs.CopycatStairsBlock;
import com.copycatsplus.copycats.content.copycat.trapdoor.CopycatTrapdoorBlock;
import com.copycatsplus.copycats.content.copycat.wall.CopycatWallBlock;
import com.simibubi.create.AllBlocks;
import io.github.fabricators_of_create.porting_lib.block.*;
import io.github.fabricators_of_create.porting_lib.enchant.EnchantmentBonusBlock;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.copycatsplus.copycats.foundation.copycat.ICopycatBlock.getMaterial;

/**
 * Implement platform-specific methods for copycat blocks.
 * <p>
 * All non-multi-state copycats should register their blocks here instead of writing their own platform-specific implementations.
 */
@Mixin({
        CCCopycatBlock.class,
        CopycatButtonBlock.class,
        CopycatFenceBlock.class,
        CopycatFenceGateBlock.class,
        CopycatLadderBlock.class,
        CopycatPressurePlateBlock.class,
        CopycatWeightedPressurePlate.class,
        CopycatStairsBlock.class,
        CopycatDoorBlock.class,
        CopycatTrapdoorBlock.class,
        CopycatWallBlock.class,
        CopycatShaftBlock.class,
        CopycatFluidPipeBlock.class,
        CopycatGlassFluidPipeBlock.class,
        CopycatSlidingDoorBlock.class,
        CopycatPaneBlock.class
})
public abstract class CopycatBlockMixin extends Block implements ICopycatBlock,
        CustomFrictionBlock, CustomSoundTypeBlock, LightEmissiveBlock, ExplosionResistanceBlock,
        BlockPickInteractionAware, CustomLandingEffectsBlock, CustomRunningEffectsBlock, EnchantmentBonusBlock,
        ValidSpawnBlock {

    public CopycatBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).getSoundType();
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return maybeMaterialAs(
                level, pos, CustomFrictionBlock.class,
                (material, block) -> block.getFriction(material, level, pos, entity),
                material -> material.getBlock().getFriction()
        );
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockState material = CopycatMaterialStore.getMaterial(level, pos).left().orElse(null);
        if (material == null)
            return state.getLightEmission();
        Block block = material.getBlock();
        if (block instanceof LightEmissiveBlock lightEmissiveBlock)
            return lightEmissiveBlock.getLightEmission(material, level, pos);
        return material.getLightEmission();
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return maybeMaterialAs(
                level, pos, ExplosionResistanceBlock.class,
                (material, block) -> block.getExplosionResistance(material, level, pos, explosion),
                material -> material.getBlock().getExplosionResistance()
        );
    }

    @Override
    public ItemStack getPickedStack(BlockState state, BlockGetter level, BlockPos pos, @Nullable Player player, @Nullable HitResult result) {
        BlockState material = getMaterial(level, pos);
        if (AllBlocks.COPYCAT_BASE.has(material) || player != null && player.isShiftKeyDown())
            return this.getCloneItemStack(level, pos, state);
        return maybeMaterialAs(
                level, pos, BlockPickInteractionAware.class,
                (mat, block) -> block.getPickedStack(mat, level, pos, player, result),
                mat -> mat.getBlock().getCloneItemStack(level, pos, mat)
        );
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
                                     LivingEntity entity, int numberOfParticles) {
        return maybeMaterialAs(
                level, pos, CustomLandingEffectsBlock.class, // duplicate material is not a bug
                (material, block) -> block.addLandingEffects(material, level, pos, material, entity, numberOfParticles),
                material -> false // default to vanilla, true cancels
        );
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        return maybeMaterialAs(
                level, pos, CustomRunningEffectsBlock.class,
                (material, block) -> block.addRunningEffects(material, level, pos, entity),
                material -> false // default to vanilla, true cancels
        );
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return maybeMaterialAs(
                level, pos, EnchantmentBonusBlock.class,
                (material, block) -> block.getEnchantPowerBonus(material, level, pos),
                material -> material.is(BlockTags.ENCHANTMENT_POWER_PROVIDER) ? 1f : 0f
        );
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type,
                                EntityType<?> entityType) {
        return false;
    }

    @Override
    public void fallOn(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockPos pPos, @NotNull Entity pEntity, float p_152430_) {
        BlockState material = getMaterial(pLevel, pPos);
        material.getBlock()
                .fallOn(pLevel, material, pPos, pEntity, p_152430_);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return getMaterial(pLevel, pPos).getDestroyProgress(pPlayer, pLevel, pPos);
    }

    @Unique
    private static <T, R> R maybeMaterialAs(BlockGetter level, BlockPos pos, Class<T> clazz,
                                            BiFunction<BlockState, T, R> ifType, Function<BlockState, R> ifNot) {
        BlockState material = getMaterial(level, pos);
        Block block = material.getBlock();
        if (clazz.isInstance(block))
            return ifType.apply(material, clazz.cast(block));
        return ifNot.apply(material);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return ICopycatBlock.getAppearance(this, state, renderView, pos, side, sourceState, sourcePos, ICopycatBlock::getMaterial);
    }
}

package com.copycatsplus.copycats.fabric.mixin.copycat.base.functional;

import com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock;
import com.copycatsplus.copycats.content.copycat.shaft.CopycatShaftBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import io.github.fabricators_of_create.porting_lib.block.*;
import io.github.fabricators_of_create.porting_lib.enchant.EnchantmentBonusBlock;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.copycatsplus.copycats.content.copycat.base.functional.IFunctionalCopycatBlock.getMaterial;

@Mixin(CopycatShaftBlock.class)
public abstract class FunctionalCopycatBlockMixin extends Block implements IFunctionalCopycatBlock,
        CustomFrictionBlock, CustomSoundTypeBlock, LightEmissiveBlock, ExplosionResistanceBlock,
        BlockPickInteractionAware, CustomLandingEffectsBlock, CustomRunningEffectsBlock, EnchantmentBonusBlock,
        ValidSpawnBlock {

    public FunctionalCopycatBlockMixin(Properties properties) {
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
        return maybeMaterialAs(
                level, pos, LightEmissiveBlock.class,
                (material, block) -> block.getLightEmission(material, level, pos),
                BlockStateBase::getLightEmission
        );
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
            return new ItemStack(this);
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
                material -> EnchantmentBonusBlock.super.getEnchantPowerBonus(material, level, pos)
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
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                    BlockState queryState, BlockPos queryPos) {

        if (isIgnoredConnectivitySide(level, state, side, pos, queryPos))
            return state;

        return CopycatModel.getMaterial(getMaterial(level, pos));
    }
}

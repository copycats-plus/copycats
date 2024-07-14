package com.copycatsplus.copycats.forge.mixin.copycat.base;

import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.content.copycat.button.CopycatButtonBlock;
import com.copycatsplus.copycats.content.copycat.door.CopycatDoorBlock;
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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

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
        CopycatDoorBlock.class
})
public abstract class CopycatBlockMixin extends Block implements ICopycatBlock {

    public CopycatBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).getSoundType();
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).getFriction(level, pos, entity);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return getMaterial(level, pos).getLightEmission(level, pos);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return getMaterial(level, pos).canHarvestBlock(level, pos, player);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return getMaterial(level, pos).getExplosionResistance(level, pos, explosion);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos,
                                       Player player) {
        BlockState material = getMaterial(level, pos);
        if (AllBlocks.COPYCAT_BASE.has(material) || player != null && player.isShiftKeyDown())
            return this.getCloneItemStack(level, pos, state);
        return material.getCloneItemStack(target, level, pos, player);
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2,
                                     LivingEntity entity, int numberOfParticles) {
        return getMaterial(level, pos).addLandingEffects(level, pos, state2, entity, numberOfParticles);
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).addRunningEffects(level, pos, entity);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return getMaterial(level, pos).getEnchantPowerBonus(level, pos);
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).canEntityDestroy(level, pos, entity);
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

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        return ICopycatBlock.getAppearance(this, state, level, pos, side, queryState, queryPos);
    }
}

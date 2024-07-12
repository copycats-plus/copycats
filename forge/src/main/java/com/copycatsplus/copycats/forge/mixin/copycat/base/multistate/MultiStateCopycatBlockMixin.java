package com.copycatsplus.copycats.forge.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.cogwheel.CopycatCogWheelBlock;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implement platform-specific methods for multi-state copycat blocks.
 * <p>
 * All multi-state copycats should register their blocks here instead of writing their own platform-specific implementations.
 */
@Mixin({
        MultiStateCopycatBlock.class,
        CopycatCogWheelBlock.class
})
@Pseudo
public abstract class MultiStateCopycatBlockMixin extends Block implements IForgeBlock, IMultiStateCopycatBlock {

    public MultiStateCopycatBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return ICopycatBlock.getMaterial(level, pos).getSoundType();
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);
            AtomicInteger count = new AtomicInteger(0);

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return state.getBlock().getFriction();
            copycatBE.getMaterialItemStorage().getAllMaterials().forEach(mat -> {
                count.getAndIncrement();
                bonus.accumulateAndGet(mat.is(Blocks.AIR) ? state.getFriction(level, pos, entity) : mat.getFriction(level, pos, entity), Float::sum);
            });
            return bonus.get() / count.get();
        }
        return state.getBlock().getFriction();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            AtomicInteger light = new AtomicInteger(0);

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return 0;
            copycatBE.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                light.accumulateAndGet(bs.getLightEmission(), Math::max);
            });
            return light.get();
        }
        return 0;
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> explosionResistance = new AtomicReference<>(state.getBlock().getExplosionResistance());

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return state.getBlock().getExplosionResistance();
            copycatBE.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                explosionResistance.accumulateAndGet(bs.getBlock().getExplosionResistance(), Math::max);
            });
            return explosionResistance.get();
        }
        return state.getBlock().getExplosionResistance(state, level, pos, explosion);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos,
                                       Player player) {
        String property = target == null
                ? null
                : getPropertyFromInteraction(state, level, pos, target.getLocation(), target instanceof BlockHitResult blockHit ? blockHit.getDirection() : Direction.UP, true);
        BlockState material = property == null ? ICopycatBlock.getMaterial(level, pos) : IMultiStateCopycatBlock.getMaterial(level, pos, property);
        if (AllBlocks.COPYCAT_BASE.has(material) || player != null && player.isSteppingCarefully())
            return new ItemStack(this);
        return material.getBlock().getCloneItemStack(level, pos, material);
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        if (state1.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            BlockHitResult hitResult = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            String property = copycatBlock.getPropertyFromInteraction(state1, level, pos, hitResult, true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return false;
            mat.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material());
            return mat.get().addLandingEffects(level, pos, mat.get(), entity, numberOfParticles);
        }
        return false;
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            BlockHitResult hitResult = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            String property = copycatBlock.getPropertyFromInteraction(state, level, pos, hitResult, true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return false;
            mat.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material());
            return mat.get().addRunningEffects(level, pos, entity);
        }
        return false;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return 0f;
            copycatBE.getMaterialItemStorage().getAllMaterials().forEach(mat -> bonus.accumulateAndGet(mat.getEnchantPowerBonus(level, pos), Float::max));
            return bonus.get();
        }
        return 0f;
    }

    @Override
    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type,
                                EntityType<?> entityType) {
        return false;
    }

    @Override
    public void fallOn(@NotNull Level pLevel, @NotNull BlockState state, @NotNull BlockPos pPos, @NotNull Entity pEntity, float p_152430_) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            BlockHitResult hitResult = pLevel.clip(new ClipContext(pEntity.position(), pEntity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, pEntity));
            String property = copycatBlock.getPropertyFromInteraction(state, pLevel, pPos, hitResult, true);
            AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(pLevel, pPos);
            if (copycatBE == null)
                return;
            material.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material());
            material.get().getBlock().fallOn(pLevel, material.get(), pPos, pEntity, p_152430_);
        }
    }

    @Override
    public float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        if (pState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            String property = copycatBlock.getPropertyFromInteraction(pState, pLevel, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true), true);
            AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(pLevel, pPos);
            if (copycatBE == null)
                return pState.getDestroyProgress(pPlayer, pLevel, pPos);
            material.set(copycatBE.getMaterialItemStorage().getMaterialItem(property).material());
            return material.get().getDestroyProgress(pPlayer, pLevel, pPos);
        }
        return pState.getDestroyProgress(pPlayer, pLevel, pPos);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter renderView, BlockPos pos, Direction side, @org.jetbrains.annotations.Nullable BlockState sourceState, @org.jetbrains.annotations.Nullable BlockPos sourcePos) {
        return IMultiStateCopycatBlock.getAppearance(this, state, renderView, pos, side, sourceState, sourcePos);
    }
}

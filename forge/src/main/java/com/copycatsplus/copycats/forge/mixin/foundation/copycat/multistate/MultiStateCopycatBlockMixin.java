package com.copycatsplus.copycats.forge.mixin.foundation.copycat.multistate;

import com.copycatsplus.copycats.foundation.copycat.CopycatMaterialStore;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MaterialItemStorage;
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

import java.util.Map;
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
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return super.getSoundType(state, level, pos, entity);
            for (MaterialItemStorage.MaterialItem materialItem : copycatBE.getMaterialItemStorage().getAllMaterialItems()) {
                if (materialItem.hasCustomMaterial()) {
                    return materialItem.material().getSoundType(level, pos, entity);
                }
            }
            return ICopycatBlock.getMaterial(level, pos).getSoundType(level, pos, entity);
        } else {
            return super.getSoundType(state, level, pos, entity);
        }
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            float bonus = 0f;
            int count = 0;

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return super.getFriction(state, level, pos, entity);
            for (String property : copycatBE.getMaterialItemStorage().getAllProperties()) {
                if (!copycatBlock.partExists(state, property)) continue;
                BlockState mat = copycatBE.getMaterialItemStorage().getMaterialItem(property).material();
                count++;
                bonus += mat.is(Blocks.AIR) ? super.getFriction(state, level, pos, entity) : mat.getFriction(level, pos, entity);
            }
            return bonus / count;
        } else {
            return super.getFriction(state, level, pos, entity);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            AtomicInteger light = new AtomicInteger(0);

            Map<String, BlockState> materials = CopycatMaterialStore.getMaterial(level, pos).right().orElse(null);
            if (materials == null)
                return super.getLightEmission(state, level, pos);
            materials.forEach((key, bs) -> {
                light.accumulateAndGet(bs.getLightEmission(), Math::max);
            });
            return light.get();
        } else {
            return super.getLightEmission(state, level, pos);
        }
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> explosionResistance = new AtomicReference<>(state.getBlock().getExplosionResistance());

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return super.getExplosionResistance(state, level, pos, explosion);
            copycatBE.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                explosionResistance.accumulateAndGet(bs.getBlock().getExplosionResistance(), Math::max);
            });
            return explosionResistance.get();
        } else {
            return super.getExplosionResistance(state, level, pos, explosion);
        }
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

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return super.addLandingEffects(state1, level, pos, state2, entity, numberOfParticles);
            BlockState material = copycatBE.getMaterialItemStorage().getMaterialItem(property).material();
            return material.addLandingEffects(level, pos, material, entity, numberOfParticles);
        } else {
            return super.addLandingEffects(state1, level, pos, state2, entity, numberOfParticles);
        }
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            BlockHitResult hitResult = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            String property = copycatBlock.getPropertyFromInteraction(state, level, pos, hitResult, true);

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return super.addRunningEffects(state, level, pos, entity);
            BlockState material = copycatBE.getMaterialItemStorage().getMaterialItem(property).material();
            return material.addRunningEffects(level, pos, entity);
        } else {
            return super.addRunningEffects(state, level, pos, entity);
        }
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(level, pos);
            if (copycatBE == null)
                return super.getEnchantPowerBonus(state, level, pos);
            copycatBE.getMaterialItemStorage().getAllMaterials().forEach(mat -> bonus.accumulateAndGet(mat.getEnchantPowerBonus(level, pos), Float::max));
            return bonus.get();
        } else {
            return super.getEnchantPowerBonus(state, level, pos);
        }
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

            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(pLevel, pPos);
            if (copycatBE == null) {
                super.fallOn(pLevel, state, pPos, pEntity, p_152430_);
                return;
            }
            BlockState material = copycatBE.getMaterialItemStorage().getMaterialItem(property).material();
            material.getBlock().fallOn(pLevel, material, pPos, pEntity, p_152430_);
        } else {
            super.fallOn(pLevel, state, pPos, pEntity, p_152430_);
        }
    }

    @Override
    public float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        // It is more convenient to always use a pickaxe than to guess what tool is needed for the copycat
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
//        if (pState.getBlock() instanceof IMultiStateCopycatBlock copycatBlock) {
//            String property = copycatBlock.getPropertyFromInteraction(pState, pLevel, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true), true);
//
//            IMultiStateCopycatBlockEntity copycatBE = copycatBlock.getCopycatBlockEntity(pLevel, pPos);
//            if (copycatBE == null)
//                return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
//            BlockState material = copycatBE.getMaterialItemStorage().getMaterialItem(property).material();
//            return material.getDestroyProgress(pPlayer, pLevel, pPos);
//        } else {
//            return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
//        }
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockAndTintGetter renderView, BlockPos pos, Direction side, @org.jetbrains.annotations.Nullable BlockState sourceState, @org.jetbrains.annotations.Nullable BlockPos sourcePos) {
        return IMultiStateCopycatBlock.getAppearance(this, state, renderView, pos, side, sourceState, sourcePos);
    }
}

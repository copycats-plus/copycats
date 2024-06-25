package com.copycatsplus.copycats.forge.mixin.copycat.base.multistate;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock.*;

@Mixin(MultiStateCopycatBlock.class)
@Pseudo
public abstract class MultiStateCopycatBlockCombinerMixin extends Block implements IForgeBlock {

    public MultiStateCopycatBlockCombinerMixin(Properties properties) {
        super(properties);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        // There really is no easy way to know which part of the block is being interacted with
        // So returning sounds of the copycat base should be less confusing
        return state.getSoundType();
    }

    @Override
    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);
            AtomicInteger count = new AtomicInteger(0);
            mscb.withBlockEntityDo(level, pos, mscbe -> mscbe.getMaterialItemStorage().getAllMaterials().forEach(mat -> {
                count.getAndIncrement();
                bonus.accumulateAndGet(mat.is(Blocks.AIR) ? state.getFriction(level, pos, entity) : mat.getFriction(level, pos, entity), Float::sum);
            }));
            return bonus.get() / count.get();
        }
        return state.getBlock().getFriction();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            AtomicInteger light = new AtomicInteger(0);
            mscb.withBlockEntityDo(level, pos, mscbe -> {
                mscbe.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                    light.accumulateAndGet(bs.getLightEmission(), Math::max);
                });
            });
            return light.get();
        }
        return 0;
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            AtomicReference<Float> explosionResistance = new AtomicReference<>(state.getBlock().getExplosionResistance(state, level, pos, explosion));
            mscb.withBlockEntityDo(level, pos, mscbe -> {
                mscbe.getMaterialItemStorage().getAllMaterials().forEach(bs -> {
                    explosionResistance.accumulateAndGet(bs.getBlock().getExplosionResistance(), Math::max);
                });
            });
            return explosionResistance.get();
        }
        return state.getBlock().getExplosionResistance(state, level, pos, explosion);
    }

    @Override
    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        if (state1.getBlock() instanceof MultiStateCopycatBlock mscb) {
            BlockHitResult hitResult = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            String property = mscb.getProperty(state1, level, pos, hitResult, true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(level, pos, mscbe -> mat.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            return mat.get().addLandingEffects(level, pos, mat.get(), entity, numberOfParticles);
        }
        return false;
    }

    @Override
    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            BlockHitResult hitResult = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            String property = mscb.getProperty(state, level, pos, hitResult, true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(level, pos, mscbe -> mat.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            return mat.get().addRunningEffects(level, pos, entity);
        }
        return false;
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);
            mscb.withBlockEntityDo(level, pos, mscbe -> mscbe.getMaterialItemStorage().getAllMaterials().forEach(mat -> bonus.set(bonus.get() + mat.getEnchantPowerBonus(level, pos))));
            return bonus.get();
        }
        return 0f;
    }

    @Override
    public void fallOn(@NotNull Level pLevel, @NotNull BlockState state, @NotNull BlockPos pPos, @NotNull Entity pEntity, float p_152430_) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            BlockHitResult hitResult = pLevel.clip(new ClipContext(pEntity.position(), pEntity.position().add(0, -2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, pEntity));
            String property = mscb.getProperty(state, pLevel, pPos, hitResult, true);
            AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(pLevel, pPos, mscbe -> material.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            material.get().getBlock().fallOn(pLevel, material.get(), pPos, pEntity, p_152430_);
        }
    }

    @Override
    public float getDestroyProgress(@NotNull BlockState pState, @NotNull Player pPlayer, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        if (pState.getBlock() instanceof MultiStateCopycatBlock mscb) {
            String property = mscb.getProperty(pState, pLevel, pPos, new BlockHitResult(Vec3.atCenterOf(pPos), Direction.UP, pPos, true), true);
            AtomicReference<BlockState> material = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(pLevel, pPos, mscbe -> material.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            return material.get().getDestroyProgress(pPlayer, pLevel, pPos);
        }
        return pState.getDestroyProgress(pPlayer, pLevel, pPos);
    }
}

package com.copycatsplus.copycats.content.copycat.base.multistate.fabric;

import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.ScaledBlockAndTintGetter;
import com.simibubi.create.AllBlocks;
import io.github.fabricators_of_create.porting_lib.block.CustomLandingEffectsBlock;
import io.github.fabricators_of_create.porting_lib.block.CustomRunningEffectsBlock;
import io.github.fabricators_of_create.porting_lib.enchant.EnchantmentBonusBlock;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MultiStateCopycatBlockImpl {

    public static boolean multiPlatformLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        if (state1.getBlock() instanceof MultiStateCopycatBlock mscb) {
            String property = mscb.getProperty(state1, level, pos, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, true), true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(level, pos, mscbe -> mat.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            return ((CustomLandingEffectsBlock) mat.get().getBlock()).addLandingEffects(state1, level, pos, state2, entity, numberOfParticles);
        }
        return false;
    }

    public static boolean multiPlatformRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            String property = mscb.getProperty(state, level, pos, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, true), true);
            AtomicReference<BlockState> mat = new AtomicReference<>(AllBlocks.COPYCAT_BASE.getDefaultState());
            mscb.withBlockEntityDo(level, pos, mscbe -> mat.set(mscbe.getMaterialItemStorage().getMaterialItem(property).material()));
            return ((CustomRunningEffectsBlock) mat.get().getBlock()).addRunningEffects(state, level, pos, entity);
        }
        return false;
    }

    public static float multiPlatformEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() instanceof MultiStateCopycatBlock mscb) {
            AtomicReference<Float> bonus = new AtomicReference<>(0f);
            mscb.withBlockEntityDo(level, pos, mscbe -> mscbe.getMaterialItemStorage().getAllMaterials().forEach(mat -> bonus.set(bonus.get() + ((EnchantmentBonusBlock)mat).getEnchantPowerBonus(state, level, pos))));
            return bonus.get();
        }
        return 0f;
    }

    @Nullable
    public static VoxelShape multiPlatformGetShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        if (pLevel instanceof ScaledBlockAndTintGetter) {
            return Shapes.block(); // todo: patch the blocking check to consider multistates properly
        }
        return null;
    }

/*    public ItemStack getPickedStack(BlockState state, BlockGetter level, BlockPos pos, @Nullable Player player, @Nullable HitResult result) {
        String property = result == null
                ? null
                : getProperty(state, level, pos, result.getLocation(), result instanceof BlockHitResult blockHit ? blockHit.getDirection() : Direction.UP, true);
        BlockState material = property == null ? getMaterial(level, pos) : getMaterial(level, pos, property);
        if (AllBlocks.COPYCAT_BASE.has(material) || player != null && player.isShiftKeyDown())
            return new ItemStack(this);
        return maybeMaterialAs(
                level, pos, BlockPickInteractionAware.class, material,
                (mat, block) -> block.getPickedStack(mat, level, pos, player, result),
                mat -> mat.getBlock().getCloneItemStack(level, pos, mat)
        );
    }*/

    private static <T, R> R maybeMaterialAs(BlockGetter level, BlockPos pos, Class<T> clazz, BlockState material,
                                            BiFunction<BlockState, T, R> ifType, Function<BlockState, R> ifNot) {
        Block block = material.getBlock();
        if (clazz.isInstance(block))
            return ifType.apply(material, clazz.cast(block));
        return ifNot.apply(material);
    }
}

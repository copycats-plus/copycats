package com.copycatsplus.copycats.content.copycat.base.multistate.forge;

import com.copycatsplus.copycats.content.copycat.base.model.multistate.forge.MultiStateCopycatModel;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlock;
import com.copycatsplus.copycats.content.copycat.base.multistate.ScaledBlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MultiStateCopycatBlockImpl {

    @Nullable
    public static VoxelShape multiPlatformGetShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        if (pLevel instanceof ScaledBlockAndTintGetter) {
            return Shapes.block(); // todo: patch the blocking check to consider multistates properly
        }
        return null;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static BlockState multiPlatformGetAppearance(MultiStateCopycatBlock block, BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side,
                                                        BlockState queryState, BlockPos queryPos) {
        String property;
        BlockPos truePos = null;
        if (level instanceof ScaledBlockAndTintGetter scaledLevel) {
            truePos = scaledLevel.getTruePos(pos);
            Vec3i inner = scaledLevel.getInner(pos);
            property = block.getPropertyFromRender(scaledLevel.getRenderingProperty(), state, scaledLevel, inner, truePos, side, queryState, queryPos);
        } else {
            property = block.storageProperties().stream().findFirst().get();
        }
        if (!block.allowCTAppearance(block, state, level, side, queryState, queryPos))
            return state;
        if (block.isIgnoredConnectivitySide(property, level, state, side, pos, queryPos))
            return state;

        ModelDataManager modelDataManager = level.getModelDataManager();
        BlockState appearance = null;
        if (modelDataManager != null)
            appearance = MultiStateCopycatModel.getMaterials(modelDataManager.getAt(truePos == null ? pos : truePos)).get(property);
        if (appearance == null)
            appearance = MultiStateCopycatBlock.getMaterial(level, pos, property);
        return appearance;
    }
}

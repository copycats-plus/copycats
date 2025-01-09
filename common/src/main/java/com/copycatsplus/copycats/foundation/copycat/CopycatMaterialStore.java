package com.copycatsplus.copycats.foundation.copycat;

import com.copycatsplus.copycats.compat.FlywheelCompat;
import com.copycatsplus.copycats.compat.Mods;
import com.copycatsplus.copycats.compat.SodiumCompat;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.WrappedRenderWorld;
import com.copycatsplus.copycats.utility.Platform;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe store for copycat materials. Useful for cross-thread access of copycat materials during rendering/lighting.
 */
public class CopycatMaterialStore {
    private final Map<ChunkPos, Map<BlockPos, Either<BlockState, Map<String, BlockState>>>> materialMap = new ConcurrentHashMap<>();

    private static final Map<BlockGetter, CopycatMaterialStore> STORES = Collections.synchronizedMap(new WeakHashMap<>());

    private void setMaterial(BlockPos pos, BlockState state) {
        ChunkPos chunkPos = new ChunkPos(pos);
        materialMap.computeIfAbsent(chunkPos, p -> new ConcurrentHashMap<>()).put(pos, Either.left(state));
    }

    private void setMaterial(BlockPos pos, Map<String, BlockState> states) {
        ChunkPos chunkPos = new ChunkPos(pos);
        materialMap.computeIfAbsent(chunkPos, p -> new ConcurrentHashMap<>()).put(pos, Either.right(states));
    }

    private Either<BlockState, Map<String, BlockState>> getMaterial(BlockPos pos) {
        return materialMap.getOrDefault(new ChunkPos(pos), Map.of()).getOrDefault(pos, Either.left(Blocks.AIR.defaultBlockState()));
    }

    public void unloadChunk(ChunkPos chunk) {
        materialMap.remove(chunk);
    }

    private static CopycatMaterialStore get(BlockGetter level) {
        if (Platform.Environment.CLIENT.isCurrent() && level instanceof WrappedRenderWorld wrapped) {
            level = wrapped.getLevel();
        }
        if (Platform.Environment.CLIENT.isCurrent() && Mods.SODIUM.getLoaded()) {
            try {
                level = SodiumCompat.unwrapSodiumLevel(level);
            } catch (Exception ex) {
                // Ignore, since Sodium might not be installed
            }
        }
        if (level instanceof LevelChunk chunk) {
            level = chunk.getLevel();
        }
        if (Platform.Environment.CLIENT.isCurrent() && Mods.FLYWHEEL.getLoaded()) {
            try {
                level = FlywheelCompat.unwrapFlywheelLevel(level);
            } catch (Exception ex) {
                // Ignore, since Flywheel might not be installed
            }
        }
        return STORES.computeIfAbsent(level, l -> new CopycatMaterialStore());
    }

    public static void setMaterial(BlockGetter level, BlockPos pos, BlockState state) {
        get(level).setMaterial(pos, state);
    }

    public static void setMaterial(BlockGetter level, BlockPos pos, Map<String, BlockState> states) {
        get(level).setMaterial(pos, states);
    }

    public static Either<BlockState, Map<String, BlockState>> getMaterial(BlockGetter level, BlockPos pos) {
        return get(level).getMaterial(pos);
    }

    public static void unloadLevel(BlockGetter level) {
        STORES.remove(level);
    }

    public static void unloadChunk(BlockGetter level, ChunkPos chunk) {
        get(level).unloadChunk(chunk);
    }
}

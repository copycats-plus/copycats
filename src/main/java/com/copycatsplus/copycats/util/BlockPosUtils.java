package com.copycatsplus.copycats.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;

public class BlockPosUtils {

    public static BlockPos containing(double pX, double pY, double pZ) {
        return new BlockPos(Mth.floor(pX), Mth.floor(pY), Mth.floor(pZ));
    }

    public static BlockPos containing(Position pPosition) {
        return containing(pPosition.x(), pPosition.y(), pPosition.z());
    }
}

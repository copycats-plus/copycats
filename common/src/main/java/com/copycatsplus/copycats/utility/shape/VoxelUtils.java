package com.copycatsplus.copycats.utility.shape;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

/*
* Methods needed/wanted copied from https://github.com/Fuzss/diagonalblocks
*/
public class VoxelUtils {
    /**
     * @param corners provided edges as top left, top right, bottom left and bottom right
     * @return vectors as pairs representing the edges
     */
    public static Vec3[] create12Edges(Vec3[] corners) {
        if (corners.length != 8) throw new IllegalStateException("Amount of corners must be 8");
        return new Vec3[]{
                // skew side
                corners[0], corners[1],
                corners[1], corners[3],
                corners[3], corners[2],
                corners[2], corners[0],
                // connections between skew sides
                corners[0], corners[4],
                corners[1], corners[5],
                corners[2], corners[6],
                corners[3], corners[7],
                // other skew side
                corners[4], corners[5],
                corners[5], corners[7],
                corners[7], corners[6],
                corners[6], corners[4]
        };
    }
}


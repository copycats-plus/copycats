package com.copycatsplus.copycats.utility.shape;

import com.copycatsplus.copycats.mixin.copycat.VoxelShapeAccessor;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.SliceShape;
import net.minecraft.world.phys.shapes.VoxelShape;

/*
 * Copied from https://github.com/Fuzss/diagonalblocks
 */
public abstract class ExtensibleVoxelShape extends SliceShape {

    public ExtensibleVoxelShape(VoxelShape voxelProvider) {
        super(voxelProvider, Direction.Axis.X, 0);
        ((VoxelShapeAccessor) this).copycats$setShape(((VoxelShapeAccessor) voxelProvider).copycats$getShape());
    }

    @Override
    protected abstract DoubleList getCoords(Direction.Axis axis);
}

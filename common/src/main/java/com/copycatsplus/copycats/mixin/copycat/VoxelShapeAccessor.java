package com.copycatsplus.copycats.mixin.copycat;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/*
 * Copied from https://github.com/Fuzss/diagonalblocks
 */
@Mixin(VoxelShape.class)
public interface VoxelShapeAccessor {

    @Accessor("shape")
    DiscreteVoxelShape copycats$getShape();

    @Accessor("shape")
    @Mutable
    void copycats$setShape(DiscreteVoxelShape shape);

    @Invoker("getCoords")
    DoubleList copycats$callGetCoords(Direction.Axis axis);
}

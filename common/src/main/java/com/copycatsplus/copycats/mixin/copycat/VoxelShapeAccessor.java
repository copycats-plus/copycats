package com.copycatsplus.copycats.mixin.copycat;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Access protected fields for diagonal hitboxes.
 * <p>
 * Copied from <a href="https://github.com/Fuzss/diagonalblocks">Fuzss/diagonalblocks</a>
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

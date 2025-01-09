package com.copycatsplus.copycats.utility.shape;

import com.copycatsplus.copycats.mixin.copycat.VoxelShapeAccessor;
import com.simibubi.create.foundation.utility.Pair;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/*
 * Copied from https://github.com/Fuzss/diagonalblocks
 */
public class OutlinedVoxelShape extends ExtensibleVoxelShape {
    private final VoxelShape collisionShape;
    private final List<Pair<Vec3, Vec3>> outlineShapeEdges;

    public OutlinedVoxelShape(VoxelShape collisionShape, List<Pair<Vec3, Vec3>> outlineShapeEdges) {
        super(collisionShape);
        this.collisionShape = collisionShape;
        this.outlineShapeEdges = outlineShapeEdges;
    }

    @Override
    protected DoubleList getCoords(Direction.Axis axis) {
        return ((VoxelShapeAccessor) this.collisionShape).copycats$callGetCoords(axis);
    }

    @Override
    public void forAllEdges(Shapes.DoubleLineConsumer boxConsumer) {
        for (Pair<Vec3, Vec3> edge : this.outlineShapeEdges) {
            boxConsumer.consume(edge.getFirst().x, edge.getFirst().y, edge.getFirst().z, edge.getSecond().x, edge.getSecond().y, edge.getSecond().z);
        }
    }
}

package com.copycatsplus.copycats.utility.shape;

import com.copycatsplus.copycats.mixin.copycat.VoxelShapeAccessor;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/*
 * Copied from https://github.com/Fuzss/diagonalblocks
 */
public class NoneVoxelShape extends ExtensibleVoxelShape {
    private final VoxelShape collisionShape;
    private final List<Vec3[]> outlineShapeEdges;

    public NoneVoxelShape(VoxelShape collisionShape, Vec3... outlineShapeEdges) {
        super(collisionShape);
        this.collisionShape = collisionShape;
        this.outlineShapeEdges = this.createOutlineList(outlineShapeEdges);
    }

    private List<Vec3[]> createOutlineList(Vec3[] outlineShapeEdges) {
        if (outlineShapeEdges.length % 2 != 0) throw new IllegalStateException("Edges must be in groups of two points");
        List<Vec3[]> list = Lists.newArrayList();
        for (int i = 0; i < outlineShapeEdges.length; i += 2) {
            list.add(new Vec3[]{outlineShapeEdges[i], outlineShapeEdges[i + 1]});
        }
        return list;
    }

    @Override
    protected DoubleList getCoords(Direction.Axis axis) {
        return ((VoxelShapeAccessor) this.collisionShape).copycats$callGetCoords(axis);
    }

    @Override
    public void forAllEdges(Shapes.DoubleLineConsumer boxConsumer) {
        for (Vec3[] edge : this.outlineShapeEdges) {
            boxConsumer.consume(edge[0].x, edge[0].y, edge[0].z, edge[1].x, edge[1].y, edge[1].z);
        }
    }
}

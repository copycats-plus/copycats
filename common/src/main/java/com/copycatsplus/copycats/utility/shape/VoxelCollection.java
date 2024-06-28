package com.copycatsplus.copycats.utility.shape;

import com.copycatsplus.copycats.mixin.copycat.VoxelShapeAccessor;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/*
 * Copied from https://github.com/Fuzss/diagonalblocks
 */
public class VoxelCollection extends ExtensibleVoxelShape {
    private VoxelShape collisionShape;
    private VoxelShape outlineShape;
    private VoxelShape particleShape;
    private final List<NoneVoxelShape> noneVoxels = Lists.newArrayList();

    public VoxelCollection(VoxelShape baseShape) {
        this(baseShape, baseShape);
    }

    public VoxelCollection(VoxelShape baseShape, VoxelShape outlineBaseShape) {
        super(baseShape);
        this.collisionShape = baseShape;
        this.outlineShape = outlineBaseShape;
        this.particleShape = baseShape;
    }

    @Override
    protected DoubleList getCoords(Direction.Axis axis) {
        return ((VoxelShapeAccessor) this.collisionShape).copycats$callGetCoords(axis);
    }

    private void setCollisionShape(VoxelShape voxelShape) {
        this.collisionShape = voxelShape;
        ((VoxelShapeAccessor) this).copycats$setShape(((VoxelShapeAccessor) this.collisionShape).copycats$getShape());
    }

    public void addVoxelShape(VoxelShape voxelShape, VoxelShape particleShape) {
        if (voxelShape instanceof NoneVoxelShape) {
            this.addNoneVoxelShape((NoneVoxelShape) voxelShape);
        } else {
            this.setCollisionShape(Shapes.joinUnoptimized(this.collisionShape, voxelShape, BooleanOp.OR));
            this.outlineShape = Shapes.joinUnoptimized(this.outlineShape, voxelShape, BooleanOp.OR);
        }
        this.particleShape = Shapes.joinUnoptimized(this.particleShape, particleShape, BooleanOp.OR);
    }

    private void addNoneVoxelShape(NoneVoxelShape voxelShape) {
        this.noneVoxels.add(voxelShape);
        this.setCollisionShape(Shapes.joinUnoptimized(this.collisionShape, voxelShape, BooleanOp.OR));
    }

    @Override
    public VoxelCollection optimize() {
        this.setCollisionShape(this.collisionShape.optimize());
        this.outlineShape = this.outlineShape.optimize();
        this.particleShape = this.particleShape.optimize();
        return this;
    }

    public void forAllParticleBoxes(Shapes.DoubleLineConsumer doubleLineConsumer) {
        this.particleShape.forAllBoxes(doubleLineConsumer);
    }

    @Override
    public void forAllEdges(Shapes.DoubleLineConsumer boxConsumer) {
        this.outlineShape.forAllEdges(boxConsumer);
        this.noneVoxels.forEach(voxelShape -> voxelShape.forAllEdges(boxConsumer));
    }
}

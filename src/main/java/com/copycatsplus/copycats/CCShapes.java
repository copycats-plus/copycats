package com.copycatsplus.copycats;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CCShapes {
    public static final VoxelShaper CASING_1PX = shape(0, 15, 0, 16, 16, 16).forDirectional();
    public static final VoxelShaper CASING_8PX = shape(0, 0, 0, 16, 8, 16).forAxis();
    public static final VoxelShaper CASING_3PX = shape(0, 0, 0, 8, 3, 16).forAxis();
    public static final VoxelShaper CASING_8PX_TOP = shape(0, 8, 0, 16, 16, 16).forAxis();
    public static final VoxelShaper CASING_8PX_CENTERED = shape(4, 0, 4, 12, 16, 12).forAxis();
    public static final VoxelShaper CASING_8PX_VERTICAL = shape(0, 0, 0, 8, 16, 8).forHorizontal(Direction.NORTH);
    public static final VoxelShaper EMPTY = shape(0.0, 0.0, 0.0, 0, 0, 0).forDirectional();
    public static final VoxelShaper LAYER_2PX = shape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_4PX = shape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_6PX = shape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_8PX = shape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_10PX = shape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_12PX = shape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_14PX = shape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0).forDirectional();
    public static final VoxelShaper LAYER_16PX = shape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0).forDirectional();

    private static AllShapes.Builder shape(VoxelShape shape) {
        return new AllShapes.Builder(shape);
    }

    private static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }
}

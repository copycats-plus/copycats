package com.copycatsplus.copycats;

import com.copycatsplus.copycats.content.copycat.base.multistate.BlockStateTransform;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class CCBlockStateProperties {

    public static final EnumProperty<VerticalStairShape> VERTICAL_STAIR_SHAPE = EnumProperty.create("vertical_stair_shape", VerticalStairShape.class);
    public static final EnumProperty<Side> SIDE = EnumProperty.create("side", Side.class);
    public static final EnumProperty<BlockStateTransform> TRANSFORM = EnumProperty.create("transform", BlockStateTransform.class);

    public enum Side implements StringRepresentable {
        LEFT,
        RIGHT;

        public boolean isRight() {
            return this == RIGHT;
        }

        public Side getOpposite() {
            return this == LEFT ? RIGHT : LEFT;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public enum VerticalStairShape implements StringRepresentable {
        STRAIGHT,
        OUTER_TOP,
        OUTER_BOTTOM,
        INNER_TOP,
        INNER_BOTTOM;

        @Override
        public @NotNull String getSerializedName() {
            return name().toLowerCase();
        }

        public boolean isOuter() {
            return this == OUTER_TOP || this == OUTER_BOTTOM;
        }

        public boolean isTop() {
            return this == OUTER_TOP || this == INNER_TOP;
        }
    }
}

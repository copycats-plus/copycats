package com.copycatsplus.copycats;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static com.copycatsplus.copycats.foundation.copycat.CopycatBaseBlock.BASE_TYPE_COUNT;

public class CCBlockStateProperties {

    public static final EnumProperty<VerticalStairShape> VERTICAL_STAIR_SHAPE = EnumProperty.create("vertical_stair_shape", VerticalStairShape.class);
    public static final EnumProperty<Side> SIDE = EnumProperty.create("side", Side.class);
    public static final IntegerProperty BASE_TYPE = IntegerProperty.create("base_type", 0, BASE_TYPE_COUNT - 1);

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
            return name().toLowerCase(Locale.ROOT);
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
            return name().toLowerCase(Locale.ROOT);
        }

        public boolean isOuter() {
            return this == OUTER_TOP || this == OUTER_BOTTOM;
        }

        public boolean isTop() {
            return this == OUTER_TOP || this == INNER_TOP;
        }
    }
}

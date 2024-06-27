package com.copycatsplus.copycats.content.copycat.base.multistate;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents the symmetry group of a block state (aka a square).
 * <p>
 * Ref: <a href="https://en.wikipedia.org/wiki/Group_(mathematics)#Second_example:_a_symmetry_group">Group (mathematics) - Wikipedia</a>
 * <p>
 * Using an imaginary quad that is facing up, the vertex at -X and -Z is A, +X and -Z is B, +X and +Z is C, and -X and +Z is D.
 * The order of the vertices represents a transformed state of the block.
 */
public enum BlockStateTransform implements StringRepresentable {
    ABCD,
    BCDA,
    CDAB,
    DABC,
    ADCB,
    DCBA,
    CBAD,
    BADC;

    public BlockStateTransform getClockwise() {
        return switch (this) {
            case ABCD -> BCDA;
            case BCDA -> CDAB;
            case CDAB -> DABC;
            case DABC -> ABCD;
            case ADCB -> DCBA;
            case DCBA -> CBAD;
            case CBAD -> BADC;
            case BADC -> ADCB;
        };
    }

    public BlockStateTransform getCounterClockwise() {
        return switch (this) {
            case ABCD -> DABC;
            case BCDA -> ABCD;
            case CDAB -> BCDA;
            case DABC -> CDAB;
            case ADCB -> BADC;
            case DCBA -> ADCB;
            case CBAD -> DCBA;
            case BADC -> CBAD;
        };
    }

    public BlockStateTransform flipX() {
        return switch (this) {
            case ABCD -> BADC;
            case BCDA -> CBAD;
            case CDAB -> DCBA;
            case DABC -> ADCB;
            case ADCB -> DABC;
            case DCBA -> CDAB;
            case CBAD -> BCDA;
            case BADC -> ABCD;
        };
    }

    public BlockStateTransform flipZ() {
        return switch (this) {
            case ABCD -> DCBA;
            case BCDA -> ADCB;
            case CDAB -> BADC;
            case DABC -> CBAD;
            case ADCB -> BCDA;
            case DCBA -> ABCD;
            case CBAD -> DABC;
            case BADC -> CDAB;
        };
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }

    public void undoTransform(Consumer<Rotation> rotate, Consumer<Mirror> mirror) {
        switch (this) {
            case ABCD -> {
            }
            case BCDA -> {
                rotate.accept(Rotation.CLOCKWISE_90);
            }
            case CDAB -> {
                rotate.accept(Rotation.CLOCKWISE_180);
            }
            case DABC -> {
                rotate.accept(Rotation.COUNTERCLOCKWISE_90);
            }
            case ADCB -> {
                mirror.accept(Mirror.FRONT_BACK);
                rotate.accept(Rotation.CLOCKWISE_90);
            }
            case DCBA -> {
                mirror.accept(Mirror.FRONT_BACK);
            }
            case CBAD -> {
                mirror.accept(Mirror.FRONT_BACK);
                rotate.accept(Rotation.COUNTERCLOCKWISE_90);
            }
            case BADC -> {
                mirror.accept(Mirror.FRONT_BACK);
                rotate.accept(Rotation.CLOCKWISE_180);
            }
        }
    }
}

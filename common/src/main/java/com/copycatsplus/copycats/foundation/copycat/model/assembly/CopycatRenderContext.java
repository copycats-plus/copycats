package com.copycatsplus.copycats.foundation.copycat.model.assembly;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface CopycatRenderContext {

    /**
     * Copy a piece of cuboid from the source model and assemble it to the copycat.
     *
     * @param assemblyTransform The transform to apply to the entire operation, changing the positions, AABBs and cull faces, while not affecting the source model. This can be used to combine repetitive assembly code, or to rotate the entire piece according to block state.
     * @param offset            The final position of the assembled piece, in voxel space.
     * @param select            The selection on the source model to copy from, in voxel space. {@link MutableAABB#move} can be used to move the selection if it does not start at the origin.
     * @param cull              Faces to skip rendering in the destination model.
     */
    void assemblePiece(
            @NotNull AssemblyTransform assemblyTransform,
            MutableVec3 offset,
            MutableAABB select,
            MutableCullFace cull
    );

    /**
     * Copy a piece of cuboid from the source model and assemble it to the copycat.
     *
     * @param assemblyTransform The transform to apply to the entire operation, changing the positions, AABBs, cull faces and quad transforms, while not affecting the source model. This can be used to combine repetitive assembly code, or to rotate the entire piece according to block state.
     * @param offset            The final position of the assembled piece, in voxel space.
     * @param select            The selection on the source model to copy from, in voxel space. {@link MutableAABB#move} can be used to move the selection if it does not start at the origin.
     * @param cull              Faces to skip rendering in the destination model.
     * @param transforms        Quad transforms to apply to the copied quads. These transforms are applied after the selection is copied, and mutate the vertices of the quads directly. Assembly transforms should be used over quad transforms for block state-dependent rotations/mirroring.
     */
    void assemblePiece(
            @NotNull AssemblyTransform assemblyTransform,
            MutableVec3 offset,
            MutableAABB select,
            MutableCullFace cull,
            QuadTransform... transforms
    );

    /**
     * Copy ALL quads from source to destination without modification.
     */
    void assembleAll();

    /**
     * Copy ALL quads from source to destination while applying the specified crop and move in block space.
     * <p>
     * {@link CopycatRenderContext#assemblePiece(AssemblyTransform, MutableVec3, MutableAABB, MutableCullFace)} should be used instead of this method.
     */
    @ApiStatus.Internal
    void assembleRaw(AABB crop, Vec3 move);

    /**
     * Copy ALL quads from source to destination while applying the specified transforms.
     * <p>
     * {@link CopycatRenderContext#assemblePiece(AssemblyTransform, MutableVec3, MutableAABB, MutableCullFace, QuadTransform...)} should be used instead of this method.
     */
    @ApiStatus.Internal
    void assembleRaw(AABB crop, Vec3 move, QuadTransform... transforms);

    /**
     * Specify faces to be culled. You should import static constants from {@link MutableCullFace} and use bitwise OR to combine them.
     *
     * @param mask The faces to be culled. Specify multiple faces by bitwise OR. Use 0 to render all faces.
     */
    static MutableCullFace cull(int mask) {
        return new MutableCullFace(mask);
    }

    /**
     * Specify a position in voxel space, where each block is 16 units.
     */
    static MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x / 16, y / 16, z / 16);
    }

    /**
     * Specify a position in voxel space, where each block is 16 units. This position is used as a pivot point for transforms such as rotation and scaling.
     */
    static MutableVec3.AsPivot pivot(double x, double y, double z) {
        return new MutableVec3.AsPivot(x / 16, y / 16, z / 16);
    }

    /**
     * Specify axis angles in degrees. Rotations are applied in the order of X, Y, Z.
     */
    static MutableVec3.AsAngle angle(double x, double y, double z) {
        return new MutableVec3.AsAngle(x, y, z);
    }

    /**
     * Specify scaling factors in each axis. 1 is the original size.
     */
    static MutableVec3.AsScale scale(double x, double y, double z) {
        return new MutableVec3.AsScale(x, y, z);
    }

    /**
     * Specify a volume in voxel space, where each block is 16 units. This volume is used as a selection box.
     * <p>
     * By default, volumes start at (0, 0, 0) and extend to the specified size. Use {@link MutableAABB#move(double, double, double)} to move the volume.
     */
    static MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX / 16, sizeY / 16, sizeZ / 16);
    }

    /**
     * Freely rotate the quad around the pivot point. Rotations are performed in the order of X, Y, Z.
     * <p>
     * Rotations of any angle and of multiple axes are allowed.
     */
    static QuadRotate rotate(MutableVec3.AsPivot pivot, MutableVec3.AsAngle rot) {
        return new QuadRotate(pivot, rot);
    }

    /**
     * Scale the quad around the pivot point.
     */
    static QuadScale scale(MutableVec3.AsPivot pivot, MutableVec3.AsScale scale) {
        return new QuadScale(pivot, scale);
    }

    /**
     * Translate the quad by the specified amount.
     */
    static QuadTranslate translate(double x, double y, double z) {
        return new QuadTranslate(x / 16, y / 16, z / 16);
    }

    /**
     * Maps the height of a quad along an axis to a slope function.
     * <p>
     * The mapping function can return values that vary with the position of the vertex to achieve slopes.
     * <p>
     * For example, if the mapping direction is UP and the function returns a scaling factor of 0.5, the height of the quad will be halved.
     * If the mapping direction is DOWN and the function returns 0.5, the bottom of the quad will be raised to 50% of its original height.
     * <p>
     * If the height is mapped to 0, a small epsilon is added to the output to prevent texture UVs from messing up due to loss of precision.
     *
     * @param face The face to map the height along. Vertices located on this face has a height of 1, while those located on the opposite face has a height of 0.
     * @param func The function that maps the coordinates of the quad to a scaling factor for the height in the axis aligned with the face. Coordinates are provided in voxel space.
     */
    static QuadSlope slope(Direction face, QuadSlope.QuadSlopeFunction func) {
        return new QuadSlope(face, (a, b) -> func.apply(a * 16, b * 16) / 16);
    }

    /**
     * Shears a quad along an axis towards the specified direction.
     *
     * @param axis      The axis to shear along. This is the axis that will be moved towards the specified direction.
     * @param direction The direction to shear towards. Cannot be aligned with the axis.
     * @param amount    The amount to shear by, in voxel space. A vertex located at 1 block (16 units) along the axis will be moved towards the direction by this amount.
     */
    static QuadShear shear(Direction.Axis axis, Direction direction, double amount) {
        return new QuadShear(axis, direction, amount / 16);
    }

    /**
     * Wraps other {@link QuadTransform}s such that the UV coordinates are updated after the wrapped transforms are applied.
     * The end result is that the textures appear to stay in the same place while the vertices are moved by the wrapped transforms,
     * as opposed to the default behavior where the textures are stretched or squished along with the vertices.
     *
     * @param transforms The transforms to apply before updating the UV coordinates.
     */
    static QuadUVUpdate updateUV(QuadTransform... transforms) {
        return new QuadUVUpdate(transforms);
    }

    /**
     * Assign cull face to the quad based on the light face and the existing cull face.
     * <p>
     * Return null to specify that the quad should never be culled.
     *
     * @param mapper The mapper function to determine the cull face.
     */
    static QuadManualCull manualCull(QuadManualCull.CullFaceMapper mapper) {
        return new QuadManualCull(mapper);
    }

    /**
     * Assign cull face to the quad based on the light face.
     * <p>
     * Return null to specify that the quad should never be culled.
     */
    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            }
            return cullFace;
        });
    }

    /**
     * Assign cull face to the quad based on the light face.
     * <p>
     * Return null to specify that the quad should never be culled.
     */
    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1, Direction face2, @Nullable Direction cull2) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            }
            return cullFace;
        });
    }

    /**
     * Assign cull face to the quad based on the light face.
     * <p>
     * Return null to specify that the quad should never be culled.
     */
    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1, Direction face2, @Nullable Direction cull2, Direction face3, @Nullable Direction cull3) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else if (face3 == lightFace) {
                return cull3;
            }
            return cullFace;
        });
    }

    /**
     * Assign cull face to the quad based on the light face.
     * <p>
     * Return null to specify that the quad should never be culled.
     */
    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1, Direction face2, @Nullable Direction cull2, Direction face3, @Nullable Direction cull3, Direction face4, @Nullable Direction cull4) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else if (face3 == lightFace) {
                return cull3;
            } else if (face4 == lightFace) {
                return cull4;
            }
            return cullFace;
        });
    }

    /**
     * Assign cull face to the quad based on the light face.
     * <p>
     * Return null to specify that the quad should never be culled.
     */
    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1, Direction face2, @Nullable Direction cull2, Direction face3, @Nullable Direction cull3, Direction face4, @Nullable Direction cull4, Direction face5, @Nullable Direction cull5) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else if (face3 == lightFace) {
                return cull3;
            } else if (face4 == lightFace) {
                return cull4;
            } else if (face5 == lightFace) {
                return cull5;
            }
            return cullFace;
        });
    }

    /**
     * Assign cull face to the quad based on the light face.
     * <p>
     * Return null to specify that the quad should never be culled.
     */
    static QuadManualCull manualCull(Direction face1, @Nullable Direction cull1, Direction face2, @Nullable Direction cull2, Direction face3, @Nullable Direction cull3, Direction face4, @Nullable Direction cull4, Direction face5, @Nullable Direction cull5, Direction face6, @Nullable Direction cull6) {
        return new QuadManualCull((lightFace, cullFace) -> {
            if (face1 == lightFace) {
                return cull1;
            } else if (face2 == lightFace) {
                return cull2;
            } else if (face3 == lightFace) {
                return cull3;
            } else if (face4 == lightFace) {
                return cull4;
            } else if (face5 == lightFace) {
                return cull5;
            } else if (face6 == lightFace) {
                return cull6;
            }
            return cullFace;
        });
    }

    /**
     * Disable face culling for the quad.
     */
    static QuadManualCull noCull() {
        return manualCull((lightFace, cullFace) -> null);
    }

    /**
     * Automatically assign cull face according to quad position and orientation.
     */
    static QuadAutoCull autoCull() {
        return QuadAutoCull.BLOCK;
    }

    /**
     * Automatically assign cull face according to quad position and orientation.
     *
     * @param cullingBox The bounding box to cull against. Faces that are touching the box are allowed to be culled.
     */
    static QuadAutoCull autoCull(MutableAABB cullingBox) {
        return new QuadAutoCull(cullingBox);
    }

    /**
     * Rotate the UV coordinates of a quad around a pivot point.
     * <p>
     * Rotations of any angle is allowed.
     *
     * @param face     The face to rotate the UV coordinates of.
     * @param pivotU   The U coordinate of the pivot of rotation, in voxel space.
     * @param pivotV   The V coordinate of the pivot of rotation, in voxel space.
     * @param rotation The rotation to apply, in degrees.
     */
    static QuadUVRotate uvRotate(Direction face, float pivotU, float pivotV, float rotation) {
        return new QuadUVRotate(face, pivotU, pivotV, rotation);
    }

    /**
     * Translate the UV coordinates of a quad.
     *
     * @param face    The face to translate the UV coordinates of.
     * @param offsetU The U coordinate offset in voxel space.
     * @param offsetV The V coordinate offset in voxel space.
     */
    static QuadUVTranslate uvTranslate(Direction face, float offsetU, float offsetV) {
        return new QuadUVTranslate(face, offsetU, offsetV);
    }

    /**
     * Scale the UV coordinates of a quad around a pivot point.
     *
     * @param face   The face to scale the UV coordinates of.
     * @param pivotU The U coordinate of the pivot of scaling, in voxel space.
     * @param pivotV The V coordinate of the pivot of scaling, in voxel space.
     * @param scaleU The U coordinate scale factor.
     * @param scaleV The V coordinate scale factor.
     */
    static QuadUVScale uvScale(Direction face, float pivotU, float pivotV, float scaleU, float scaleV) {
        return new QuadUVScale(face, pivotU, pivotV, scaleU, scaleV);
    }

    @ApiStatus.Internal
    abstract class Base<Source, Destination> implements CopycatRenderContext {
        private final Source source;
        private final Destination destination;
        private final String key;

        public Base(Source source, Destination destination, String key) {
            this.source = source;
            this.destination = destination;
            this.key = key;
        }

        public Source source() {
            return source;
        }

        public Destination destination() {
            return destination;
        }

        public String key() {
            return key;
        }
    }
}

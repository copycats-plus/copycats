package com.copycatsplus.copycats.content.copycat.base.model.forge;

import com.copycatsplus.copycats.content.copycat.base.model.QuadHelper;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;

public class QuadHelperImpl {

    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void assemblePiece(CopycatRenderContext<Source, Destination> context, int rotation, boolean flipY, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        select.rotate(rotation).flipY(flipY);
        offset.rotate(rotation).flipY(flipY);
        cull.rotate(rotation).flipY(flipY);
        for (BakedQuad quad : context.source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, context.destination(), select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f));
        }
    }

    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void assemblePiece(CopycatRenderContext<Source, Destination> context, int rotation, boolean flipY, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        select.rotate(rotation).flipY(flipY);
        offset.rotate(rotation).flipY(flipY);
        cull.rotate(rotation).flipY(flipY);
        for (QuadTransform transform : transforms) {
            transform.rotate(rotation).flipY(flipY);
        }
        for (BakedQuad quad : context.source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, context.destination(), select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f), transforms);
        }
    }

    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void assembleQuad(CopycatRenderContext<Source, Destination> context) {
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination());
        }
    }

    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest) {
        dest.add(BakedQuadHelper.clone(src));
    }

    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void assembleQuad(CopycatRenderContext<Source, Destination> context, AABB crop, Vec3 move) {
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination(), crop, move);
        }
    }

    public static <Source extends List<BakedQuad>, Destination extends List<BakedQuad>> void assembleQuad(CopycatRenderContext<Source, Destination> context, AABB crop, Vec3 move, QuadTransform... transforms) {
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination(), crop, move, transforms);
        }
    }


    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src,
                BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move)));
    }

    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, QuadTransform... transforms) {
        int[] vertices = BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move);
        for (QuadTransform transform : transforms) {
            vertices = transform.transformVertices(vertices, src.getSprite());
        }
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src, vertices));
    }

}

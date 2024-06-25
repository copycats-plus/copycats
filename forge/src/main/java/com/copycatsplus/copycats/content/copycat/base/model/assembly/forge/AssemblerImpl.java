package com.copycatsplus.copycats.content.copycat.base.model.assembly.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.*;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.QuadTransform;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;

public class AssemblerImpl {

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        AABB aabb = select.toAABB();
        Vec3 vec3 = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (BakedQuad quad : context.source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, context.destination(), aabb, vec3);
        }
    }

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        AABB aabb = select.toAABB();
        Vec3 vec3 = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (BakedQuad quad : context.source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, context.destination(), aabb, vec3, globalTransform, transforms);
        }
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination());
        }
    }

    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest) {
        dest.add(BakedQuadHelper.clone(src));
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination(), crop, move);
        }
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move, QuadTransform... transforms) {
        CopycatRenderContextForge context = (CopycatRenderContextForge) ctx;
        for (BakedQuad quad : context.source()) {
            assembleQuad(quad, context.destination(), crop, move, GlobalTransform.IDENTITY, transforms);
        }
    }


    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src,
                BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move)));
    }

    public static <Source extends BakedQuad, Destination extends List<BakedQuad>> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, GlobalTransform globalTransform, QuadTransform... transforms) {
        int[] vertices = BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move);
        MutableQuad mutableQuad = getMutableQuad(vertices);
        globalTransform.apply(mutableQuad);
        mutableQuad.undoMutate();
        for (QuadTransform transform : transforms) {
            transform.transformVertices(mutableQuad, src.getSprite());
        }
        mutableQuad.mutate();
        for (int i = 0; i < 4; i++) {
            BakedQuadHelper.setXYZ(vertices, i, mutableQuad.vertices.get(i).xyz.toVec3());
            BakedQuadHelper.setU(vertices, i, mutableQuad.vertices.get(i).uv.u);
            BakedQuadHelper.setV(vertices, i, mutableQuad.vertices.get(i).uv.v);
        }
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src, vertices));
    }

    public static <T> MutableQuad getMutableQuad(T data) {
        int[] vertexData = (int[]) data;
        List<MutableVertex> vertices = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            MutableVec3 xyz = new MutableVec3(BakedQuadHelper.getXYZ(vertexData, i));
            MutableUV uv = new MutableUV(BakedQuadHelper.getU(vertexData, i), BakedQuadHelper.getV(vertexData, i));
            vertices.add(new MutableVertex(xyz, uv));
        }
        return new MutableQuad(vertices);
    }

    public static class CopycatRenderContextForge extends CopycatRenderContext<List<BakedQuad>, List<BakedQuad>> {
        public CopycatRenderContextForge(List<BakedQuad> source, List<BakedQuad> destination) {
            super(source, destination);
        }
    }
}

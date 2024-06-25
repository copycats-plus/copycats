package com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.*;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.QuadTransform;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.CopycatRenderContext;

public class AssemblerImpl {

    static SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        if (cull.isCulled(context.source().lightFace())) {
            return;
        }
        assembleQuad(context.source(), context.destination(), select.toAABB(), offset.toVec3().subtract(select.minX, select.minY, select.minZ));
    }

    public static void assemblePiece(CopycatRenderContext<?, ?> ctx, GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        if (cull.isCulled(context.source().lightFace())) {
            return;
        }
        assembleQuad(context.source(), context.destination(), select.toAABB(), offset.toVec3().subtract(select.minX, select.minY, select.minZ), globalTransform, transforms);
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        assembleQuad(context.source(), context.destination());
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest) {
        dest.copyFrom(src);
        dest.emit();
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        assembleQuad(context.source(), context.destination(), crop, move);
    }

    public static void assembleQuad(CopycatRenderContext<?, ?> ctx, AABB crop, Vec3 move, QuadTransform... transforms) {
        CopycatRenderContextFabric context = (CopycatRenderContextFabric) ctx;
        assembleQuad(context.source(), context.destination(), crop, move, GlobalTransform.IDENTITY, transforms);
    }


    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {
        dest.copyFrom(src);
        BakedModelHelper.cropAndMove(dest, spriteFinder.find(src), crop, move);
        dest.emit();
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, GlobalTransform globalTransform, QuadTransform... transforms) {
        dest.copyFrom(src);
        TextureAtlasSprite sprite = spriteFinder.find(src);
        BakedModelHelper.cropAndMove(dest, sprite, crop, move);
        MutableQuad mutableQuad = getMutableQuad(dest);
        globalTransform.apply(mutableQuad);
        mutableQuad.undoMutate();
        for (QuadTransform transform : transforms) {
            transform.transformVertices(mutableQuad, sprite);
        }
        mutableQuad.mutate();
        for (int i = 0; i < 4; i++) {
            BakedQuadHelper.setXYZ(dest, i, mutableQuad.vertices.get(i).xyz.toVec3());
            dest.uv(i, mutableQuad.vertices.get(i).uv.u, mutableQuad.vertices.get(i).uv.v);
        }
        dest.emit();
    }

    public static <T> MutableQuad getMutableQuad(T data) {
        MutableQuadView vertexData = (MutableQuadView) data;
        List<MutableVertex> vertices = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            MutableVec3 xyz = new MutableVec3(vertexData.x(i), vertexData.y(i), vertexData.z(i));
            MutableUV uv = new MutableUV(vertexData.u(i), vertexData.v(i));
            vertices.add(new MutableVertex(xyz, uv));
        }
        return new MutableQuad(vertices);
    }

    public static class CopycatRenderContextFabric extends CopycatRenderContext<MutableQuadView, QuadEmitter> {
        public CopycatRenderContextFabric(MutableQuadView source, QuadEmitter destination) {
            super(source, destination);
        }
    }
}

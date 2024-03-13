package com.copycatsplus.copycats.content.copycat.base.model.fabric;

import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.copycatsplus.copycats.content.copycat.base.model.QuadHelper.*;

public class QuadHelperImpl {

    static SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assemblePiece(CopycatRenderContext<Source, Destination> context, int rotation, boolean flipY, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        select.rotate(rotation).flipY(flipY);
        offset.rotate(rotation).flipY(flipY);
        cull.rotate(rotation).flipY(flipY);
        if (cull.isCulled(context.source().lightFace())) {
            return;
        }
        assembleQuad(context, select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f));
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assemblePiece(CopycatRenderContext<Source, Destination> context, int rotation, boolean flipY, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        select.rotate(rotation).flipY(flipY);
        offset.rotate(rotation).flipY(flipY);
        cull.rotate(rotation).flipY(flipY);
        for (QuadTransform transform : transforms) {
            transform.rotate(rotation).flipY(flipY);
        }
        if (cull.isCulled(context.source().lightFace())) {
            return;
        }
        assembleQuad(context, select.toAABB(), offset.toVec3().subtract(select.minX / 16f, select.minY / 16f, select.minZ / 16f), transforms);
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(CopycatRenderContext<Source, Destination> context) {
        assembleQuad(context.source(), context.destination());
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest) {
        src.copyTo(dest);
        dest.emit();
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(CopycatRenderContext<Source, Destination> context, AABB crop, Vec3 move) {
        assembleQuad(context.source(), context.destination(), crop, move);
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(CopycatRenderContext<Source, Destination> context, AABB crop, Vec3 move, QuadTransform... transforms) {
        assembleQuad(context.source(), context.destination(), crop, move, transforms);
    }


    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move) {
        src.copyTo(dest);
        BakedModelHelper.cropAndMove(dest, spriteFinder.find(src, 0), crop, move);
        dest.emit();
    }

    public static <Source extends MutableQuadView, Destination extends QuadEmitter> void assembleQuad(Source src, Destination dest, AABB crop, Vec3 move, QuadTransform... transforms) {
  /*      int[] vertices = BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move);
        for (QuadTransform transform : transforms) {
            vertices = transform.transformVertices(vertices, src.getSprite());
        }
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src, vertices));*/
    }

}

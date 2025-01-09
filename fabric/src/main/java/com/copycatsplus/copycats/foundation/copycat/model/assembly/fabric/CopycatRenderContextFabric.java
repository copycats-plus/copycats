package com.copycatsplus.copycats.foundation.copycat.model.assembly.fabric;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.*;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatRenderContextFabric extends CopycatRenderContext.Base<List<MutableQuadView>, QuadEmitter> {
    public CopycatRenderContextFabric(List<MutableQuadView> source, QuadEmitter destination, String key) {
        super(source, destination, key); // todo: find a way to store the key in quads
    }

    static SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));

    @Override
    public void assemblePiece(AssemblyTransform assemblyTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        assemblyTransform.apply(select);
        assemblyTransform.apply(offset);
        assemblyTransform.apply(cull);
        AABB aabb = select.toAABB();
        Vec3 vec3 = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (MutableQuadView quad : source()) {
            if (cull.isCulled(quad.lightFace())) {
                continue;
            }
            assembleQuad(quad, destination(), aabb, vec3, assemblyTransform);
        }
    }

    @Override
    public void assemblePiece(AssemblyTransform assemblyTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        assemblyTransform.apply(select);
        assemblyTransform.apply(offset);
        assemblyTransform.apply(cull);
        AABB aabb = select.toAABB();
        Vec3 vec3 = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (MutableQuadView quad : source()) {
            if (cull.isCulled(quad.lightFace())) {
                continue;
            }
            assembleQuad(quad, destination(), aabb, vec3, assemblyTransform, transforms);
        }
    }

    @Override
    public void assembleAll() {
        for (MutableQuadView quad : source()) {
            assembleQuad(quad, destination());
        }
    }

    private static void assembleQuad(MutableQuadView src, QuadEmitter dest) {
        dest.copyFrom(src);
        dest.emit();
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move) {
        for (MutableQuadView quad : source()) {
            assembleQuad(quad, destination(), crop, move, AssemblyTransform.IDENTITY);
        }
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move, QuadTransform... transforms) {
        for (MutableQuadView quad : source()) {
            assembleQuad(quad, destination(), crop, move, AssemblyTransform.IDENTITY, transforms);
        }
    }

    private static void assembleQuad(MutableQuadView src, QuadEmitter dest, AABB crop, Vec3 move, AssemblyTransform assemblyTransform, QuadTransform... transforms) {
        dest.copyFrom(src);
        TextureAtlasSprite sprite = spriteFinder.find(src);
        BakedModelHelper.cropAndMove(dest, sprite, crop, move);
        MutableQuad mutableQuad = getMutableQuad(dest);
        assemblyTransform.apply(mutableQuad);
        mutableQuad.undoMutate();
        for (QuadTransform transform : transforms) {
            if (!transform.transformQuad(mutableQuad, sprite))
                return;
        }
        if (!mutableQuad.disableFinalAutoCull)
            if (!QuadAutoCull.BLOCK.transformQuad(mutableQuad, sprite))
                return;
        mutableQuad.mutate();
        for (int i = 0; i < 4; i++) {
            BakedQuadHelper.setXYZ(dest, i, mutableQuad.vertices.get(i).xyz.toVec3());
            dest.uv(i, mutableQuad.vertices.get(i).uv.u, mutableQuad.vertices.get(i).uv.v);
        }
        dest.cullFace(mutableQuad.cullFace);
        dest.emit();
    }

    public static MutableQuad getMutableQuad(MutableQuadView vertexData) {
        List<MutableVertex> vertices = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            MutableVec3 xyz = new MutableVec3(vertexData.x(i), vertexData.y(i), vertexData.z(i));
            MutableUV uv = new MutableUV(vertexData.u(i), vertexData.v(i));
            vertices.add(new MutableVertex(xyz, uv));
        }
        return new MutableQuad(vertices, vertexData.lightFace());
    }
}

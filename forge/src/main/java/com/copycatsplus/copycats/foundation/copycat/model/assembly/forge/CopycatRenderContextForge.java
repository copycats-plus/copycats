package com.copycatsplus.copycats.foundation.copycat.model.assembly.forge;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.*;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatRenderContextForge extends CopycatRenderContext.Base<List<CopycatRenderContextForge.CullingBakedQuad>, List<CopycatRenderContextForge.CullingBakedQuad>> {

    public CopycatRenderContextForge(List<CullingBakedQuad> source, List<CullingBakedQuad> destination) {
        super(source, destination);
    }

    @Override
    public void assemblePiece(AssemblyTransform assemblyTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        assemblyTransform.apply(select);
        assemblyTransform.apply(offset);
        assemblyTransform.apply(cull);
        AABB aabb = select.toAABB();
        Vec3 vec3 = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (CullingBakedQuad quad : source()) {
            if (cull.isCulled(quad.getDirection())) {
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
        for (CullingBakedQuad quad : source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, destination(), aabb, vec3, assemblyTransform, transforms);
        }
    }

    @Override
    public void assembleAll() {
        for (CullingBakedQuad quad : source()) {
            assembleQuad(quad, destination());
        }
    }

    private static void assembleQuad(CullingBakedQuad src, List<CullingBakedQuad> dest) {
        dest.add(src);
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move) {
        for (CullingBakedQuad quad : source()) {
            assembleQuad(quad, destination(), crop, move, AssemblyTransform.IDENTITY);
        }
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move, QuadTransform... transforms) {
        for (CullingBakedQuad quad : source()) {
            assembleQuad(quad, destination(), crop, move, AssemblyTransform.IDENTITY, transforms);
        }
    }

    private static void assembleQuad(CullingBakedQuad src, List<CullingBakedQuad> dest, AABB crop, Vec3 move, AssemblyTransform assemblyTransform, QuadTransform... transforms) {
        int[] vertices = BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move);
        MutableQuad mutableQuad = getMutableQuad(vertices, src.cullFace);
        assemblyTransform.apply(mutableQuad);
        mutableQuad.undoMutate();
        for (QuadTransform transform : transforms) {
            transform.transformQuad(mutableQuad, src.getSprite());
        }
        if (!mutableQuad.disableFinalAutoCull)
            QuadAutoCull.BLOCK.transformQuad(mutableQuad, src.getSprite());
        mutableQuad.mutate();
        for (int i = 0; i < 4; i++) {
            BakedQuadHelper.setXYZ(vertices, i, mutableQuad.vertices.get(i).xyz.toVec3());
            BakedQuadHelper.setU(vertices, i, mutableQuad.vertices.get(i).uv.u);
            BakedQuadHelper.setV(vertices, i, mutableQuad.vertices.get(i).uv.v);
        }
        dest.add(new CullingBakedQuad(vertices, src.getTintIndex(), mutableQuad.computeLightFace(), src.getSprite(), src.isShade(), mutableQuad.cullFace));
    }

    public static MutableQuad getMutableQuad(int[] vertexData, @Nullable Direction cullFace) {
        List<MutableVertex> vertices = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            MutableVec3 xyz = new MutableVec3(BakedQuadHelper.getXYZ(vertexData, i));
            MutableUV uv = new MutableUV(BakedQuadHelper.getU(vertexData, i), BakedQuadHelper.getV(vertexData, i));
            vertices.add(new MutableVertex(xyz, uv));
        }
        return new MutableQuad(vertices, cullFace);
    }

    public static class CullingBakedQuad extends BakedQuad {
        @Nullable
        public final Direction cullFace;

        public CullingBakedQuad(int[] vertices, int tintIndex, Direction direction, TextureAtlasSprite sprite, boolean shade, @Nullable Direction cullFace) {
            super(vertices, tintIndex, direction, sprite, shade);
            this.cullFace = cullFace;
        }

        public CullingBakedQuad(BakedQuad quad, @Nullable Direction cullFace) {
            this(quad.getVertices(), quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade(), cullFace);
        }
    }
}

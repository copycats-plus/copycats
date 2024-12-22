package com.copycatsplus.copycats.content.copycat.casing;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.utility.BlockUtils;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatCasingModelCore extends CopycatModelCore {

    private static RenderStrategy getRenderStrategy(BlockState state) {
        if (CopycatCasingBlock.ACCEPTED_CASINGS.get().containsKey(state.getBlock()))
            return RenderStrategy.SWAPPED;
        if (CopycatCasingBlock.ACCEPTED_CASINGS.get().containsValue(state.getBlock()))
            return RenderStrategy.AS_IS;
        if (state.getBlock() instanceof GlassBlock)
            return RenderStrategy.AS_IS;
        return RenderStrategy.CUTOUT;
    }

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(CopycatCasingBlock.Part.INNER.getSerializedName(), ModelGetter.MATERIAL, this, EntryType.COPYCAT));
        entries.add(new ModelEntry(CopycatCasingBlock.Part.OUTER.getSerializedName(), ModelGetter.MATERIAL, this, (state, mat) -> {
            if (getRenderStrategy(mat) == RenderStrategy.SWAPPED)
                return BlockUtils.tryCopyProperties(mat, CopycatCasingBlock.ACCEPTED_CASINGS.get().get(mat.getBlock()).defaultBlockState());
            return mat;
        }, EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (key.equals(CopycatCasingBlock.Part.INNER.getSerializedName())) {
            context.assemblePiece(AssemblyTransform.IDENTITY,
                    vec3(0.01, 0.01, 0.01),
                    aabb(15.98, 15.98, 15.98).move(0.01, 0.01, 0.01),
                    cull(0)
            );
            return;
        }

        if (getRenderStrategy(material) == RenderStrategy.CUTOUT) {
            for (Direction direction : Iterate.horizontalDirections) {
                int rot = (int) direction.toYRot();
                AssemblyTransform transform = t -> t.rotateY(rot);
                context.assemblePiece(transform,
                        vec3(0, 0, 0),
                        aabb(2, 2, 14),
                        cull(EAST | UP | SOUTH)
                );
                context.assemblePiece(transform,
                        vec3(0, 2, 0),
                        aabb(2, 12, 2).move(0, 2, 0),
                        cull(EAST | UP | DOWN | SOUTH)
                );
                context.assemblePiece(transform,
                        vec3(0, 14, 0),
                        aabb(2, 2, 14).move(0, 14, 0),
                        cull(EAST | DOWN | SOUTH)
                );
            }
        } else {
            context.assembleAll();
        }
    }

    enum RenderStrategy {
        AS_IS,
        SWAPPED,
        CUTOUT
    }
}

package com.copycatsplus.copycats.content.copycat.shaft;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.GlobalTransform;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.Assembler.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;
import static com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock.FACING;
import static com.copycatsplus.copycats.content.copycat.half_panel.CopycatHalfPanelBlock.OFFSET;

public class CopycatShaftModel implements SimpleCopycatPart {

    @Override
    public void emitCopycatQuads(BlockState state, CopycatRenderContext<?, ?> context, BlockState material) {
        Axis axis = state.getValue(CopycatShaftBlock.AXIS);

        GlobalTransform transform = t -> t.rotateY(axis == Axis.X ? 90 : 0).rotateX(axis == Axis.Y ? 90 : 0);
        assemblePiece(
                context,
                transform,
                vec3(6, 6, 0),
                aabb(2, 2, 16).move(0, 0, 0),
                cull(UP | EAST)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 6, 0),
                aabb(2, 2, 16).move(14, 0, 0),
                cull(UP | WEST)
        );
        assemblePiece(
                context,
                transform,
                vec3(6, 8, 0),
                aabb(2, 2, 16).move(0, 14, 0),
                cull(DOWN | EAST)
        );
        assemblePiece(
                context,
                transform,
                vec3(8, 8, 0),
                aabb(2, 2, 16).move(14, 14, 0),
                cull(DOWN | WEST)
        );
    }
}

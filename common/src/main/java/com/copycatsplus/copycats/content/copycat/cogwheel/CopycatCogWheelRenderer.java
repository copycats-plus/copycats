package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.IKineticCopycatBlockRenderer;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.KineticCopycatRenderer;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CopycatCogWheelRenderer extends BracketedKineticBlockEntityRenderer implements IKineticCopycatBlockRenderer {
    public CopycatCogWheelRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(BracketedKineticBlockEntity be, BlockState state) {
        return IKineticCopycatBlockRenderer.super.getRotatedModel(CCCopycatPartialModels.SHAFT, (IMultiStateCopycatBlockEntity) be, "shaft");
    }

    @Override
    protected void renderSafe(BracketedKineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(be.getLevel()))
            return;

        RenderType type = getRenderType(be, ((IMultiStateCopycatBlockEntity) be).getMaterialItemStorage().getMaterialItem(CopycatCogWheelBlock.Part.COGWHEEL.getSerializedName()).material());

        if (!ICogWheel.isLargeCog(be.getBlockState())) {
            super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
            if (type != null)
                renderRotatingBuffer(be, KineticCopycatRenderer.getBuffer(CCCopycatPartialModels.COGWHEEL, (IMultiStateCopycatBlockEntity) be, "cogwheel"), ms, buffer.getBuffer(type), light);
            return;
        }

        // Large cogs sometimes have to offset their teeth by 11.25 degrees in order to
        // mesh properly

        RenderType shaftType = getRenderType(be, ((IMultiStateCopycatBlockEntity) be).getMaterialItemStorage().getMaterialItem(CopycatCogWheelBlock.Part.SHAFT.getSerializedName()).material());

        Direction.Axis axis = getRotationAxisOf(be);
        renderRotatingBuffer(be,
                KineticCopycatRenderer.getBuffer(CCCopycatPartialModels.LARGE_COGWHEEL, (IMultiStateCopycatBlockEntity) be, "cogwheel"),
                ms, buffer.getBuffer(type), light);

        float angle = getAngleForLargeCogShaft(be, axis);
        SuperByteBuffer shaft =
                KineticCopycatRenderer.getBuffer(CCCopycatPartialModels.SHAFT, (IMultiStateCopycatBlockEntity) be, "shaft");
        kineticRotationTransform(shaft, be, axis, angle, light);
        shaft.renderInto(ms, buffer.getBuffer(shaftType));
    }
}

package com.copycatsplus.copycats.content.copycat.sliding_door;

import com.copycatsplus.copycats.CCCopycatPartialModels;
import com.copycatsplus.copycats.foundation.copycat.model.kinetic.IKineticCopycatBlockRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;

public class CopycatSlidingDoorRenderer extends SafeBlockEntityRenderer<CopycatSlidingDoorBlockEntity> implements IKineticCopycatBlockRenderer {

    public CopycatSlidingDoorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(CopycatSlidingDoorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        BlockState blockState = be.getBlockState();
        if (!be.shouldRenderSpecial(blockState))
            return;

        Direction facing = blockState.getValue(DoorBlock.FACING);
        Direction movementDirection = facing.getClockWise();

        if (blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.LEFT)
            movementDirection = movementDirection.getOpposite();

        float value = be.animation().getValue(partialTicks);
        float value2 = Mth.clamp(value * 10, 0, 1);

        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        Vec3 offset = Vec3.atLowerCornerOf(movementDirection.getNormal())
                .scale(value * value * 13 / 16f)
                .add(Vec3.atLowerCornerOf(facing.getNormal())
                        .scale(value2 * 1 / 32f));


        for (DoubleBlockHalf half : DoubleBlockHalf.values()) {
            CopycatSlidingDoorBlockEntity halfBE = half == DoubleBlockHalf.UPPER
                    ? be.getPaired()
                    : be;
            if (halfBE == null) continue;

            if (((SlidingDoorBlock) blockState.getBlock()).isFoldingDoor()) {
                boolean flip = blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
                for (boolean left : Iterate.trueAndFalse) {
                    SuperByteBuffer partial = left ^ flip ?
                            IKineticCopycatBlockRenderer.super.getRotatedModel(CCCopycatPartialModels.FOLDING_DOOR_LEFT, halfBE) :
                            IKineticCopycatBlockRenderer.super.getRotatedModel(CCCopycatPartialModels.FOLDING_DOOR_RIGHT, halfBE);
                    float f = flip ? -1 : 1;

                    partial.translate(0, half == DoubleBlockHalf.UPPER ? 1 : 0, 0);

                    partial.translate(0, -1 / 512f, 0)
                            .translate(Vec3.atLowerCornerOf(facing.getNormal())
                                    .scale(value2 * 1 / 32f));
                    partial.rotateCentered(Direction.UP,
                            Mth.DEG_TO_RAD * AngleHelper.horizontalAngle(facing.getClockWise()));

                    if (flip)
                        partial.translate(0, 0, 1);
                    partial.rotateY(91 * f * value * value);

                    if (!left)
                        partial.translate(0, 0, f / 2f)
                                .rotateY(-181 * f * value * value);

                    if (flip)
                        partial.translate(0, 0, -1 / 2f);

                    partial.light(light)
                            .renderInto(ms, vb);
                }
            } else {
                IKineticCopycatBlockRenderer.super.getRotatedModel(CCCopycatPartialModels.SLIDING_DOOR, halfBE)
                        .translate(0, half == DoubleBlockHalf.UPPER ? 1 - 1 / 512f : 0, 0)
                        .translate(offset)
                        .light(light)
                        .renderInto(ms, vb);
            }
        }

    }
}

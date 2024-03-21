package com.copycatsplus.copycats.content.copycat.configurable_block;

import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CopycatConfigurableBlockBlockEntity extends CopycatBlockEntity {

    private Vec3 size;
    private Vec3 offset;

    public CopycatConfigurableBlockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.size = new Vec3(16, 16, 16);
        this.offset = new Vec3(0, 0, 0);
    }

    public void adjustSize(Player player) {
        Direction direction = Minecraft.getInstance().getCameraEntity().getDirection();
        if (player.isCrouching()) {
            if (direction.getAxis().equals(Direction.Axis.X) && size.x() > 1 && size.x() <= 16 && size.relative(direction, -1).x() >= 1 && size.relative(direction, -1).x() <= 16) {
                setSize(size.relative(player.getDirection(), -1));
            } else if (direction.getAxis().equals(Direction.Axis.Z) && size.z() > 1 && size.z() <= 16 && size.relative(direction, -1).z() >= 1 && size.relative(direction, -1).z() <= 16) {
                setSize(size.relative(player.getDirection(), -1));
            } else if (direction.getAxis().equals(Direction.Axis.Y) && size.y() > 1 && size.y() <= 16 && size.relative(direction, -1).y() >= 1 && size.relative(direction, -1).y() <= 16) {
                setSize(size.relative(player.getDirection(), -1));
            }
        } else {
            if (direction.getAxis().equals(Direction.Axis.X) && size.x() < 16 && size.x() >= 0 && size.relative(direction, 1).x() <= 16 && size.relative(direction, 1).x() >= 1) {
                setSize(size.relative(player.getDirection(), 1));
            } else if (direction.getAxis().equals(Direction.Axis.Z) && size.z() < 16 && size.z() >= 0 && size.relative(direction, 1).z() <= 16 && size.relative(direction, 1).z() >= 1) {
                setSize(size.relative(player.getDirection(), 1));
            } else if (direction.getAxis().equals(Direction.Axis.Y) && size.y() < 16 && size.y() >= 0 && size.relative(direction, 1).y() <= 16 && size.relative(direction, 1).y() >= 1) {
                setSize(size.relative(player.getDirection(), 1));
            }
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        CompoundTag sizeTag = new CompoundTag();
        sizeTag.putDouble("X", size.x());
        sizeTag.putDouble("Y", size.y());
        sizeTag.putDouble("Z", size.z());
        tag.put("Size", sizeTag);
        CompoundTag offsetTag = new CompoundTag();
        offsetTag.putDouble("X", offset.x());
        offsetTag.putDouble("Y", offset.y());
        offsetTag.putDouble("Z", offset.z());
        tag.put("Offset", offsetTag);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        if (this.getBlockState().getBlock() instanceof CopycatConfigurableBlock configurable) {
            CompoundTag sizeTag = tag.getCompound("Size");
            setSize(new Vec3(sizeTag.getDouble("X"), sizeTag.getDouble("Y"), sizeTag.getDouble("Z")));
            CompoundTag offsetTag = tag.getCompound("Offset");
            setOffset(new Vec3(offsetTag.getDouble("X"), offsetTag.getDouble("Y"), offsetTag.getDouble("Z")));
        }
    }

    public Vec3 getOffset() {
        return offset;
    }

    public void setOffset(Vec3 offset) {
        this.offset = offset;
        if (getBlockState().getBlock() instanceof CopycatConfigurableBlock configurableBlock) {
            configurableBlock.setOffset(offset);
        }
        if (this.level != null) {
            if (!this.level.isClientSide()) {
                this.notifyUpdate();
            } else {
                this.redraw();
            }
        }
    }

    public void setSize(Vec3 size) {
        this.size = size;
        if (getBlockState().getBlock() instanceof CopycatConfigurableBlock configurableBlock) {
            configurableBlock.setSize(size);
        }
        if (this.level != null) {
            if (!this.level.isClientSide()) {
                this.notifyUpdate();
            } else {
                this.redraw();
            }
        }
    }

    public void redraw() {
        if (!this.isVirtual()) {
            cc$requestModelDataUpdate(this);
        }

        if (this.hasLevel()) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 16);
            this.level.getChunkSource().getLightEngine().checkBlock(this.worldPosition);
        }

    }

    @ExpectPlatform
    public static void cc$requestModelDataUpdate(CopycatBlockEntity instance) {

    }

    public Vec3 getSize() {
        return size;
    }
}

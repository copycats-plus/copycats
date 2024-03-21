package com.copycatsplus.copycats.content.copycat.configurable_block;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CopycatConfigurableBlock extends CopycatBlock {

    private Vec3 size;
    private Vec3 offset;

    public CopycatConfigurableBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState());
        this.size = new Vec3(16, 16, 16);
        this.offset = new Vec3(0, 0, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, BlockPos blockPos1, BlockState blockState) {
        return false;
    }


    @Override
    public void attack(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            withBlockEntityDo(level, pos, copycatBlockEntity -> {
                if (copycatBlockEntity instanceof CopycatConfigurableBlockBlockEntity) {
                    ((CopycatConfigurableBlockBlockEntity) copycatBlockEntity).adjustSize(player);
                }
            });
        }
        super.attack(state, level, pos, player);
    }

    @Override
    public BlockEntityType<? extends CopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.CONFIGURABLE_BLOCK_ENTITY.get();
    }

    public Vec3 getSize() {
        return this.size;
    }

    public Vec3 getOffset() {
        return this.offset;
    }

    public void setSize(Vec3 size) {
        this.size = size;
    }

    public void setOffset(Vec3 offset) {
        this.offset = offset;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.block();
    }
}

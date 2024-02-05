package com.copycatsplus.copycats.content.copycat.pressure_plate;

import com.copycatsplus.copycats.content.copycat.ICopycatWithWrappedBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.PressurePlateBlock.POWERED;
import static net.minecraft.world.level.block.PressurePlateBlock.TOUCH_AABB;

public class CopycatWoodenPressurePlate extends CopycatBlock implements ICopycatWithWrappedBlock<WrappedPressurePlate.Wood> {

    public static WrappedPressurePlate.Wood pressurePlate;

    public CopycatWoodenPressurePlate(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(POWERED, Boolean.FALSE));
    }

    @Override
    public WrappedPressurePlate.Wood getWrappedBlock() {
        return pressurePlate;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(POWERED));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pressurePlate.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int i = this.getSignalForState(pState);
        if (i > 0) {
            this.checkPressed(null, pLevel, pPos, pState, i);
        }

    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide) {
            int i = this.getSignalForState(pState);
            if (i == 0) {
                this.checkPressed(pEntity, pLevel, pPos, pState, i);
            }

        }
    }

    protected int getSignalForState(BlockState pState) {
        return pState.getValue(POWERED) ? 15 : 0;
    }

    protected BlockState setSignalForState(BlockState pState, int pStrength) {
        return pState.setValue(POWERED, pStrength > 0);
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return pressurePlate.isSignalSource(pState);
    }

    protected int getSignalStrength(Level pLevel, BlockPos pPos) {
        Class<? extends Entity> oclass1 = switch (pressurePlate.sensitivity) {
            case EVERYTHING -> Entity.class;
            case MOBS -> LivingEntity.class;
            default -> throw new IncompatibleClassChangeError();
        };
        return getEntityCount(pLevel, TOUCH_AABB.move(pPos), oclass1) > 0 ? 15 : 0;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        pressurePlate.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    /**
     * Notify block and block below of changes
     */
    protected void updateNeighbours(Level pLevel, BlockPos pPos) {
        pLevel.updateNeighborsAt(pPos, this);
        pLevel.updateNeighborsAt(pPos.below(), this);
    }

    private void checkPressed(@Nullable Entity pEntity, Level pLevel, BlockPos pPos, BlockState pState, int pCurrentSignal) {
        int i = this.getSignalStrength(pLevel, pPos);
        boolean flag = pCurrentSignal > 0;
        boolean flag1 = i > 0;
        if (pCurrentSignal != i) {
            BlockState blockstate = this.setSignalForState(pState, i);
            pLevel.setBlock(pPos, blockstate, 2);
            this.updateNeighbours(pLevel, pPos);
            pLevel.setBlocksDirty(pPos, pState, blockstate);
        }

        if (!flag1 && flag) {
            pLevel.playSound((Player)null, pPos, pressurePlate.type.pressurePlateClickOff(), SoundSource.BLOCKS);
            pLevel.gameEvent(pEntity, GameEvent.BLOCK_DEACTIVATE, pPos);
        } else if (flag1 && !flag) {
            pLevel.playSound((Player)null, pPos, pressurePlate.type.pressurePlateClickOn(), SoundSource.BLOCKS);
            pLevel.gameEvent(pEntity, GameEvent.BLOCK_ACTIVATE, pPos);
        }

        if (flag1) {
            pLevel.scheduleTick(new BlockPos(pPos), this, 20);
        }

    }

    public static int getEntityCount(Level pLevel, net.minecraft.world.phys.AABB pBox, Class<? extends Entity> pEntityClass) {
        return pLevel.getEntitiesOfClass(pEntityClass, pBox, EntitySelector.NO_SPECTATORS.and((p_289691_) -> {
            return !p_289691_.isIgnoringBlockTriggers();
        })).size();
    }
}

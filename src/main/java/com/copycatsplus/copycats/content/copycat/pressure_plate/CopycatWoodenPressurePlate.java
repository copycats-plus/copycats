package com.copycatsplus.copycats.content.copycat.pressure_plate;

import com.copycatsplus.copycats.content.copycat.ICopycatWithWrappedBlock;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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

import static net.minecraft.world.level.block.ButtonBlock.POWERED;
import static net.minecraft.world.level.block.PressurePlateBlock.TOUCH_AABB;
import static net.minecraft.world.level.block.PressurePlateBlock.getEntityCount;

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
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    protected int getSignalForState(BlockState pState) {
        return pState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pressurePlate.getDirectSignal(pBlockState, pBlockAccess, pPos, pSide);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        int i = getSignalForState(pState);
        if (i > 0) {
            checkPressed(null, pLevel, pPos, pState, i);
        }

    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pIsMoving && !pState.is(pNewState.getBlock())) {
            if (getSignalForState(pState) > 0) {
                updateNeighbours(pLevel, pPos);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pLevel.isClientSide) {
            int i = getSignalForState(pState);
            if (i == 0) {
                checkPressed(pEntity, pLevel, pPos, pState, i);
            }

        }
    }

    private void checkPressed(@Nullable Entity pEntity, Level pLevel, BlockPos pPos, BlockState pState, int pCurrentSignal) {
        int i = this.getSignalStrength(pLevel, pPos);
        boolean flag = pCurrentSignal > 0;
        boolean flag1 = i > 0;
        if (pCurrentSignal != i) {
            BlockState blockstate = this.setSignalForState(pState, i);
            pLevel.setBlock(pPos, blockstate, Block.UPDATE_CLIENTS);
            updateNeighbours(pLevel, pPos);
            pLevel.setBlocksDirty(pPos, pState, blockstate);
        }

        if (!flag1 && flag) {
            pLevel.playSound(null, pPos, pressurePlate.type.pressurePlateClickOff(), SoundSource.BLOCKS);
            pLevel.gameEvent(pEntity, GameEvent.BLOCK_DEACTIVATE, pPos);
        } else if (flag1 && !flag) {
            pLevel.playSound(null, pPos, pressurePlate.type.pressurePlateClickOn(), SoundSource.BLOCKS);
            pLevel.gameEvent(pEntity, GameEvent.BLOCK_ACTIVATE, pPos);
        }

        if (flag1) {
            pLevel.scheduleTick(pPos, this, pressurePlate.getPressedTime());
        }

    }

    protected int getSignalStrength(Level pLevel, BlockPos pPos) {
        Class<? extends Entity> oclass1;
        switch (pressurePlate.sensitivity) {
            case EVERYTHING:
                oclass1 = Entity.class;
                break;
            case MOBS:
                oclass1 = LivingEntity.class;
                break;
            default:
                throw new IncompatibleClassChangeError();
        }

        Class oclass = oclass1;
        return getEntityCount(pLevel, TOUCH_AABB.move(pPos), oclass) > 0 ? 15 : 0;
    }

    protected BlockState setSignalForState(BlockState pState, int pStrength) {
        return pState.setValue(POWERED, pStrength > 0);
    }

    protected void updateNeighbours(Level pLevel, BlockPos pPos) {
        pLevel.updateNeighborsAt(pPos, this);
        pLevel.updateNeighborsAt(pPos.below(), this);
    }
}

package com.copycatsplus.copycats.content.copycat.wall;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

import static net.minecraft.core.Direction.Axis;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatWallBlock extends WallBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, IStateType {

    public CopycatWallBlock(Properties properties) {
        super(properties);
    }

    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return true;
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level, BlockState state, BlockEntityType<S> type) {
        return null;
    }

    @Override
    public InteractionResult use(BlockState state,
                                 Level level,
                                 BlockPos pos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult hit) {
        return InteractionUtils.sequential(
                () -> ICopycatBlock.super.use(state, level, pos, player, hand, hit),
                () -> super.use(state, level, pos, player, hand, hit)
        );
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        ICopycatBlock.super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        ICopycatBlock.super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        ICopycatBlock.super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public Class<CCCopycatBlockEntity> getBlockEntityClass() {
        return CCCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CCCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT.get();
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face,
                                             BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this) || !state.is(this)) return true;

        boolean isCross = true;
        for (Direction direction : Iterate.horizontalDirections) {
            if (toState.getValue(byDirection(direction)) == WallSide.NONE) {
                isCross = false;
                break;
            }
        }
        return isCross;
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this)) return false;

        long sideCount = Arrays.stream(Iterate.horizontalDirections).filter(s -> state.getValue(byDirection(s)) != WallSide.NONE).count();
        if (sideCount > 2)
            return false;
        if (sideCount == 2 && (state.getValue(NORTH_WALL) != state.getValue(SOUTH_WALL) || state.getValue(EAST_WALL) != state.getValue(WEST_WALL))) {
            return false;
        }

        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction face = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) {
            if (diff.distManhattan(Vec3i.ZERO) > 2) return false;
            if (diff.getY() == 0) return false;
            Direction horizontalDiff = Direction.fromAxisAndDirection(diff.getX() == 0 ? Axis.Z : Axis.X,
                    (diff.getX() + diff.getZ() > 0) ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
            if (diff.getY() > 0) {
                if (state.getValue(byDirection(horizontalDiff)) != WallSide.TALL) return false;
                if (toState.getValue(byDirection(horizontalDiff.getOpposite())) == WallSide.NONE) return false;
            } else {
                if (state.getValue(byDirection(horizontalDiff)) == WallSide.NONE) return false;
                if (toState.getValue(byDirection(horizontalDiff.getOpposite())) != WallSide.TALL) return false;
            }
            return true;
        } else if (face == Direction.DOWN || face == Direction.UP) {
            return canConnectVertically(state) && canConnectVertically(toState);
        } else {
            if (state.getValue(WallBlock.UP)) return false;
            if (state.getValue(byDirection(face)) == WallSide.NONE) return false;
            return true;
        }
    }

    private boolean canConnectVertically(BlockState state) {
        if (!state.getValue(WallBlock.UP)) return false;
        for (Direction direction : Iterate.horizontalDirections) {
            WallSide side = state.getValue(byDirection(direction));
            if (side != WallSide.NONE) return false;
        }
        return true;
    }

    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }

    public boolean hidesNeighborFace(BlockGetter level,
                                     BlockPos pos,
                                     BlockState state,
                                     BlockState neighborState,
                                     Direction dir) {
        return ICopycatBlock.hidesNeighborFace(level, pos, state, neighborState, dir);
    }

    public static EnumProperty<WallSide> byDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH_WALL;
            case SOUTH -> SOUTH_WALL;
            case WEST -> WEST_WALL;
            case EAST -> EAST_WALL;
            default -> throw new IllegalArgumentException("Vertical directions not supported");
        };
    }
}

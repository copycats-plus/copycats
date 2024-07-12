package com.copycatsplus.copycats.content.copycat.stairs;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.content.copycat.vertical_stairs.CopycatVerticalStairBlock;
import com.copycatsplus.copycats.foundation.copycat.CCCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.ICustomCTBlocking;
import com.copycatsplus.copycats.foundation.copycat.IStateType;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

import static net.minecraft.core.Direction.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatStairsBlock extends StairBlock implements ICopycatBlock, IBE<CCCopycatBlockEntity>, ICustomCTBlocking, IStateType {

    public CopycatStairsBlock(Properties properties) {
        super(Blocks.OAK_PLANKS.defaultBlockState(), properties);
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
        boolean flipped = state.getValue(HALF) == Half.TOP;
        Direction facing = state.getValue(StairBlock.FACING);
        BlockState toState = reader.getBlockState(toPos);
        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }

        if (CopycatVerticalStairBlock.isStairs(toState)) {
            return false;
        } else {
            if (diff.getY() == 0) {
                // if target is level with this block,
                // only allows it to connect if it's adjacent to a full face of this block
                StairsShape shape = state.getValue(SHAPE);
                int fullCount = 0;
                if (diff.getX() != 0) {
                    FaceShape faceShape = getFaceShape(state, fromAxisAndDirection(Axis.X, directionOf(diff.getX())));
                    if (faceShape.isFull())
                        fullCount++;
                    else if ((shape == StairsShape.OUTER_LEFT || shape == StairsShape.OUTER_RIGHT) && diff.getZ() != 0) {
                        if (diff.getX() > 0 && faceShape.topNegative && faceShape.bottomNegative || diff.getX() < 0 && faceShape.topPositive && faceShape.bottomPositive)
                            fullCount++;
                    }
                }
                if (diff.getZ() != 0) {
                    FaceShape faceShape = getFaceShape(state, fromAxisAndDirection(Axis.Z, directionOf(diff.getZ())));
                    if (faceShape.isFull())
                        fullCount++;
                    else if ((shape == StairsShape.OUTER_LEFT || shape == StairsShape.OUTER_RIGHT) && diff.getX() != 0) {
                        if (diff.getZ() > 0 && faceShape.topNegative && faceShape.bottomNegative || diff.getZ() < 0 && faceShape.topPositive && faceShape.bottomPositive)
                            fullCount++;
                    }
                }
                return fullCount < Mth.abs(diff.getX()) + Mth.abs(diff.getZ());
            } else {
                // if target is not level with this block,
                // only allow connections below the base of this block
                return (diff.getY() > 0) != flipped;
            }
        }
    }

    @Override
    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        BlockPos diff = toPos.subtract(fromPos);
        if (diff.equals(Vec3i.ZERO)) {
            return true;
        }
        Direction side = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());

        if (side != null) {
            FaceShape sideShape = getFaceShape(state, side);
            if (!sideShape.canConnect()) return false;
            if (CopycatVerticalStairBlock.isStairs(toState)) {
                if (!sideShape.equals(getFaceShape(toState, side.getOpposite()))) return false;
            } else {
                if (!sideShape.isFull()) return false;
            }
        }

        return true;
    }

    @Override
    public Optional<Boolean> isCTBlocked(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos connectingPos, BlockPos blockingPos, Direction face) {
        return CCBlocks.COPYCAT_VERTICAL_STAIRS.get().isCTBlocked(reader, state, pos, connectingPos, blockingPos, face);
    }

    @Override
    public Optional<Boolean> blockCTTowards(BlockAndTintGetter reader, BlockState state, BlockPos pos, BlockPos ctPos, BlockPos connectingPos, Direction face) {
        return CCBlocks.COPYCAT_VERTICAL_STAIRS.get().blockCTTowards(reader, state, pos, ctPos, connectingPos, face);
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

    private static AxisDirection directionOf(int value) {
        return value >= 0 ? AxisDirection.POSITIVE : AxisDirection.NEGATIVE;
    }

    /**
     * Return the area of the face that is at the edge of the block.
     */
    public static FaceShape getFaceShape(BlockState state, Direction face) {
        if (state.getBlock() instanceof CopycatVerticalStairBlock) {
            return CopycatVerticalStairBlock.getFaceShape(state, face);
        }
        boolean top = state.getValue(StairBlock.HALF) == Half.TOP;
        Direction facing = state.getValue(StairBlock.FACING);
        StairsShape shape = state.getValue(StairBlock.SHAPE);
        if (!top && face == DOWN) return new FaceShape().fillAll();
        if (top && face == UP) return new FaceShape().fillAll();

        FaceShape faceShape = new FaceShape();

        switch (shape) {
            case STRAIGHT -> {
                if (!top && face == UP || top && face == DOWN)
                    return faceShape.fillTop().rotate(facing.toYRot());
                faceShape.fillRow(top);
                if (face == facing) return faceShape.fillRow(!top);
                if (face == facing.getOpposite()) return faceShape;
                return faceShape.fillRow(!top, facing.getAxisDirection());
            }
            case INNER_LEFT -> {
                if (!top && face == UP || top && face == DOWN)
                    return faceShape.fillTop().fillBottom(AxisDirection.POSITIVE).rotate(facing.toYRot());
                faceShape.fillRow(top);
                if (face == facing) return faceShape.fillRow(!top);
                if (face == facing.getOpposite())
                    return faceShape.fillRow(!top, facing.getCounterClockWise().getAxisDirection());
                if (face == facing.getCounterClockWise()) return faceShape.fillRow(!top);
                if (face == facing.getClockWise())
                    return faceShape.fillRow(!top, facing.getAxisDirection());
            }
            case INNER_RIGHT -> {
                if (!top && face == UP || top && face == DOWN)
                    return faceShape.fillTop().fillBottom(AxisDirection.NEGATIVE).rotate(facing.toYRot());
                faceShape.fillRow(top);
                if (face == facing) return faceShape.fillRow(!top);
                if (face == facing.getOpposite())
                    return faceShape.fillRow(!top, facing.getClockWise().getAxisDirection());
                if (face == facing.getClockWise()) return faceShape.fillRow(!top);
                if (face == facing.getCounterClockWise())
                    return faceShape.fillRow(!top, facing.getAxisDirection());
            }
            case OUTER_LEFT -> {
                if (!top && face == UP || top && face == DOWN)
                    return faceShape.fillTop(AxisDirection.POSITIVE).rotate(facing.toYRot());
                faceShape.fillRow(top);
                if (face == facing) return faceShape.fillRow(!top, facing.getCounterClockWise().getAxisDirection());
                if (face == facing.getOpposite())
                    return faceShape;
                if (face == facing.getCounterClockWise()) return faceShape.fillRow(!top, facing.getAxisDirection());
                if (face == facing.getClockWise())
                    return faceShape;
            }
            case OUTER_RIGHT -> {
                if (!top && face == UP || top && face == DOWN)
                    return faceShape.fillTop(AxisDirection.NEGATIVE).rotate(facing.toYRot());
                faceShape.fillRow(top);
                if (face == facing) return faceShape.fillRow(!top, facing.getClockWise().getAxisDirection());
                if (face == facing.getOpposite())
                    return faceShape;
                if (face == facing.getClockWise()) return faceShape.fillRow(!top, facing.getAxisDirection());
                if (face == facing.getCounterClockWise())
                    return faceShape;
            }
        }
        return faceShape;
    }

    public static class FaceShape {
        public boolean topNegative;
        public boolean topPositive;
        public boolean bottomNegative;
        public boolean bottomPositive;

        public FaceShape fillTop() {
            topNegative = topPositive = true;
            return this;
        }

        public FaceShape fillColumn(AxisDirection direction) {
            switch (direction) {
                case POSITIVE -> topPositive = bottomPositive = true;
                case NEGATIVE -> topNegative = bottomNegative = true;
            }
            return this;
        }

        public FaceShape fillNegative() {
            topNegative = bottomNegative = true;
            return this;
        }

        public FaceShape fillPositive() {
            topPositive = bottomPositive = true;
            return this;
        }

        public FaceShape fillLeft(Direction relativeTo) {
            return fillColumn(relativeTo.getClockWise().getAxisDirection());
        }

        public FaceShape fillRight(Direction relativeTo) {
            return fillColumn(relativeTo.getCounterClockWise().getAxisDirection());
        }

        public FaceShape fillTop(AxisDirection direction) {
            switch (direction) {
                case POSITIVE -> topPositive = true;
                case NEGATIVE -> topNegative = true;
            }
            return this;
        }

        public FaceShape fillBottom() {
            bottomNegative = bottomPositive = true;
            return this;
        }

        public FaceShape fillBottom(AxisDirection direction) {
            switch (direction) {
                case POSITIVE -> bottomPositive = true;
                case NEGATIVE -> bottomNegative = true;
            }
            return this;
        }

        public FaceShape fillRow(boolean top) {
            if (top) return fillTop();
            return fillBottom();
        }

        public FaceShape fillRow(boolean top, AxisDirection direction) {
            if (top) return fillTop(direction);
            return fillBottom(direction);
        }

        public FaceShape fillAll() {
            return fillTop().fillBottom();
        }

        public FaceShape rotate(float angle) {
            return rotate((int) angle);
        }

        public FaceShape rotate(int angle) {
            angle = angle % 360;
            if (angle < 0) angle += 360;
            return switch (angle) {
                case 90 -> set(topNegative, bottomNegative, topPositive, bottomPositive);
                case 180 -> set(topPositive, topNegative, bottomPositive, bottomNegative);
                case 270 -> set(bottomPositive, topPositive, bottomNegative, topNegative);
                default -> this;
            };
        }

        public FaceShape set(boolean bottomNegative, boolean bottomPositive, boolean topNegative, boolean topPositive) {
            this.bottomNegative = bottomNegative;
            this.bottomPositive = bottomPositive;
            this.topNegative = topNegative;
            this.topPositive = topPositive;
            return this;
        }

        public int countBlocks() {
            int count = 0;
            if (bottomNegative) count++;
            if (bottomPositive) count++;
            if (topNegative) count++;
            if (topPositive) count++;
            return count;
        }

        public boolean canConnect() {
            return countBlocks() >= 3;
        }

        public boolean isFull() {
            return countBlocks() == 4;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof FaceShape shape)) return false;
            return shape.bottomNegative == this.bottomNegative && shape.bottomPositive == this.bottomPositive &&
                    shape.topNegative == this.topNegative && shape.topPositive == this.topPositive;
        }
    }
}

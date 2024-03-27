package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.MathHelper;
import com.copycatsplus.copycats.content.copycat.base.CTWaterloggedCopycatBlock;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.OctahedralGroup;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class CopycatByteBlock extends CTWaterloggedCopycatBlock implements ISpecialBlockItemRequirement {
    public static BooleanProperty TOP_NE = BooleanProperty.create("top_northeast");
    public static BooleanProperty TOP_NW = BooleanProperty.create("top_northwest");
    public static BooleanProperty TOP_SE = BooleanProperty.create("top_southeast");
    public static BooleanProperty TOP_SW = BooleanProperty.create("top_southwest");
    public static BooleanProperty BOTTOM_NE = BooleanProperty.create("bottom_northeast");
    public static BooleanProperty BOTTOM_NW = BooleanProperty.create("bottom_northwest");
    public static BooleanProperty BOTTOM_SE = BooleanProperty.create("bottom_southeast");
    public static BooleanProperty BOTTOM_SW = BooleanProperty.create("bottom_southwest");
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;

    public CopycatByteBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(TOP_NE, false)
                .setValue(TOP_NW, false)
                .setValue(TOP_SE, false)
                .setValue(TOP_SW, false)
                .setValue(BOTTOM_NE, false)
                .setValue(BOTTOM_NW, false)
                .setValue(BOTTOM_SE, false)
                .setValue(BOTTOM_SW, false)
        );
        this.shapesCache = this.getShapeForEachState(CopycatByteBlock::calculateMultiFaceShape);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW));
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return true;
    }


    public boolean canConnectTexturesToward(BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        if (!toState.is(this)) return false;
        return false;
    }

    @Nullable
    @Override
    public BlockState getConnectiveMaterial(BlockAndTintGetter reader, BlockState otherState, Direction face, BlockPos fromPos, BlockPos toPos) {
        return (canConnectTexturesToward(reader, fromPos, toPos, reader.getBlockState(fromPos)) ? getMaterial(reader, toPos) : null);
    }

    @Override
    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    @Override
    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return true;
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        for (Byte bite : allBytes) {
            if (pState.getValue(byByte(bite))) {
                int offsetX = bite.x ? 8 : 0;
                int offsetY = bite.y ? 8 : 0;
                int offsetZ = bite.z ? 8 : 0;
                shape = Shapes.or(shape, Block.box(offsetX, offsetY, offsetZ, offsetX + 8, offsetY + 8, offsetZ + 8));
            }
        }
        return shape;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Objects.requireNonNull(this.shapesCache.get(pState));
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return switch (pType) {
            case LAND -> true;

            default -> false;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        assert stateForPlacement != null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        Vec3 bias = Vec3.atLowerCornerOf(context.getClickedFace().getNormal()).scale(1 / 16f);
        Vec3 biasedLocation = context.getClickLocation().add(bias);
        if (!MathHelper.blockPosContaining(biasedLocation).equals(context.getClickedPos())) {
            biasedLocation = clampToBlockPos(biasedLocation, context.getClickedPos());
        }
        Byte bite = getByteFromVec(biasedLocation, context.getClickedPos());
        if (state.is(this)) {
            if (!state.getValue(byByte(bite)))
                return state.setValue(byByte(bite), true);
            else {
                bite = bite.relative(context.getClickedFace());
                if (!state.getValue(byByte(bite)))
                    return state.setValue(byByte(bite), true);
                else {
                    Copycats.LOGGER.warn("Can't figure out where to place a byte! Please file an issue if you see this.");
                    return state;
                }
            }
        } else {
            return stateForPlacement.setValue(byByte(bite), true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        ItemStack itemstack = pUseContext.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        Vec3 bias = Vec3.atLowerCornerOf(pUseContext.getClickedFace().getNormal()).scale(1 / 16f);
        Vec3 biasedLocation = pUseContext.getClickLocation().add(bias);
        if (!MathHelper.blockPosContaining(biasedLocation).equals(pUseContext.getClickedPos())) {
            biasedLocation = clampToBlockPos(biasedLocation, pUseContext.getClickedPos());
        }
        Byte bite = getByteFromVec(biasedLocation, pUseContext.getClickedPos());
        return !pState.getValue(byByte(bite));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        int byteCount = 0;
        for (Byte bite : allBytes) {
            if (state.getValue(byByte(bite))) byteCount++;
        }
        if (byteCount <= 1) return super.onSneakWrenched(state, context);

        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        Vec3 bias = Vec3.atLowerCornerOf(context.getClickedFace().getNormal()).scale(-1 / 16f);
        Byte bite = getByteFromVec(context.getClickLocation().add(bias), context.getClickedPos());
        if (world instanceof ServerLevel) {
            if (player != null && !player.isCreative()) {
                List<ItemStack> drops = Block.getDrops(defaultBlockState().setValue(byByte(bite), true), (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                for (ItemStack drop : drops) {
                    player.getInventory().placeItemBackInInventory(drop);
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, state.setValue(byByte(bite), false).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return new ItemRequirement(
                ItemRequirement.ItemUseType.CONSUME,
                new ItemStack(asItem(), (int) allBytes.stream().filter(b -> state.getValue(byByte(b))).count())
        );
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
        if (pRotation == Rotation.CLOCKWISE_90) {
            return mapBytes(pState, bite -> bite(!bite.z, bite.y, bite.x));
        } else if (pRotation == Rotation.CLOCKWISE_180) {
            return mapBytes(pState, bite -> bite(!bite.x, bite.y, !bite.z));
        } else if (pRotation == Rotation.COUNTERCLOCKWISE_90) {
            return mapBytes(pState, bite -> bite(bite.z, bite.y, !bite.x));
        }
        return pState;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, Mirror pMirror) {
        boolean invertX = pMirror.rotation() == OctahedralGroup.INVERT_X;
        boolean invertZ = pMirror.rotation() == OctahedralGroup.INVERT_Z;
        return mapBytes(pState, bite -> bite(invertX != bite.x, bite.y, invertZ != bite.z));
    }

    public static Vec3 clampToBlockPos(Vec3 vec, BlockPos pos) {
        return new Vec3(
                Mth.clamp(vec.x, pos.getX(), pos.getX() + 1),
                Mth.clamp(vec.y, pos.getY(), pos.getY() + 1),
                Mth.clamp(vec.z, pos.getZ(), pos.getZ() + 1)
        );
    }

    public static Byte getByteFromVec(Vec3 vec, BlockPos pos) {
        vec = vec.subtract(pos.getX(), pos.getY(), pos.getZ());
        return bite(vec.x > 0.5, vec.y > 0.5, vec.z > 0.5);
    }

    private BlockState mapBytes(BlockState state, Function<Byte, Byte> mapper) {
        BlockState blockstate = state;

        for (Byte bite : allBytes) {
            blockstate = blockstate.setValue(byByte(mapper.apply(bite)), state.getValue(byByte(bite)));
        }

        return blockstate;
    }

    public static final List<Byte> allBytes;

    static {
        allBytes = new ArrayList<>(8);
        for (boolean x : Iterate.falseAndTrue) {
            for (boolean y : Iterate.falseAndTrue) {
                for (boolean z : Iterate.falseAndTrue) {
                    allBytes.add(bite(x, y, z));
                }
            }
        }
    }

    public static Byte bite(boolean x, boolean y, boolean z) {
        return new Byte(x, y, z);
    }

    public static BooleanProperty byByte(Byte bite) {
        return byByte(bite.x, bite.y, bite.z);
    }

    public static BooleanProperty byByte(boolean x, boolean y, boolean z) {
        if (y) {
            if (x) {
                if (z) {
                    return TOP_SE;
                } else {
                    return TOP_NE;
                }
            } else {
                if (z) {
                    return TOP_SW;
                } else {
                    return TOP_NW;
                }
            }
        } else {
            if (x) {
                if (z) {
                    return BOTTOM_SE;
                } else {
                    return BOTTOM_NE;
                }
            } else {
                if (z) {
                    return BOTTOM_SW;
                } else {
                    return BOTTOM_NW;
                }
            }
        }
    }

    public record Byte(boolean x, boolean y, boolean z) {
        public Byte copy() {
            return new Byte(x, y, z);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this)
                return true;
            if (obj instanceof Byte other) {
                return this.x == other.x && this.y == other.y && this.z == other.z;
            }
            return false;
        }

        public Byte set(Direction.Axis axis, boolean value) {
            return switch (axis) {
                case X -> new Byte(value, y, z);
                case Y -> new Byte(x, value, z);
                case Z -> new Byte(x, y, value);
            };
        }

        public boolean get(Direction.Axis axis) {
            return switch (axis) {
                case X -> x;
                case Y -> y;
                case Z -> z;
            };
        }

        public Byte relative(Direction direction) {
            return set(direction.getAxis(), !get(direction.getAxis()));
        }

        @Override
        public int hashCode() {
            return i(x) + i(y) << 1 + i(z) << 2;
        }

        private static int i(boolean b) {
            return b ? 1 : 0;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }
    }
}

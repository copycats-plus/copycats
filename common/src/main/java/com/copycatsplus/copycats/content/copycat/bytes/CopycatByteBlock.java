package com.copycatsplus.copycats.content.copycat.bytes;

import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.content.copycat.base.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.content.copycat.base.multistate.WaterloggedMultiStateCopycatBlock;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.OctahedralGroup;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CopycatByteBlock extends WaterloggedMultiStateCopycatBlock {
    public static BooleanProperty TOP_NE = BooleanProperty.create("top_northeast");
    public static BooleanProperty TOP_NW = BooleanProperty.create("top_northwest");
    public static BooleanProperty TOP_SE = BooleanProperty.create("top_southeast");
    public static BooleanProperty TOP_SW = BooleanProperty.create("top_southwest");
    public static BooleanProperty BOTTOM_NE = BooleanProperty.create("bottom_northeast");
    public static BooleanProperty BOTTOM_NW = BooleanProperty.create("bottom_northwest");
    public static BooleanProperty BOTTOM_SE = BooleanProperty.create("bottom_southeast");
    public static BooleanProperty BOTTOM_SW = BooleanProperty.create("bottom_southwest");
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;

    public static final List<Byte> allBytes;
    public static final Map<String, Byte> byteMap;

    static {
        allBytes = new ArrayList<>(8);
        for (boolean x : Iterate.falseAndTrue) {
            for (boolean y : Iterate.falseAndTrue) {
                for (boolean z : Iterate.falseAndTrue) {
                    allBytes.add(bite(x, y, z));
                }
            }
        }
        byteMap = allBytes.stream().collect(Collectors.toMap(b -> byByte(b).getName(), b -> b));
    }

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
    public int maxMaterials() {
        return 8;
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(2, 2, 2);
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(TOP_NE.getName())) return state.getValue(TOP_NE);
        if (property.equals(TOP_NW.getName())) return state.getValue(TOP_NW);
        if (property.equals(TOP_SE.getName())) return state.getValue(TOP_SE);
        if (property.equals(TOP_SW.getName())) return state.getValue(TOP_SW);
        if (property.equals(BOTTOM_NE.getName())) return state.getValue(BOTTOM_NE);
        if (property.equals(BOTTOM_NW.getName())) return state.getValue(BOTTOM_NW);
        if (property.equals(BOTTOM_SE.getName())) return state.getValue(BOTTOM_SE);
        if (property.equals(BOTTOM_SW.getName())) return state.getValue(BOTTOM_SW);
        return false;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW).stream().map(BooleanProperty::getName).collect(Collectors.toSet());
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return byByte(hitLocation.getX() > 0, hitLocation.getY() > 0, hitLocation.getZ() > 0).getName();
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        Byte bite = byteMap.get(property);
        return new Vec3i(bite.x ? 1 : 0, bite.y ? 1 : 0, bite.z ? 1 : 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(TOP_NE, TOP_NW, TOP_SE, TOP_SW, BOTTOM_NE, BOTTOM_NW, BOTTOM_SE, BOTTOM_SW));
    }


    @Override
    public boolean isIgnoredConnectivitySide(String property, BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState toState = reader.getBlockState(toPos);
        return !toState.is(this);
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        BlockState toState = reader.getBlockState(toPos);
        return toState.is(this);
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
        VoxelShape shapeOverride = multiPlatformGetShape(pState, pLevel, pPos, pContext);
        if (shapeOverride != null) return shapeOverride;
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
        if (!BlockPos.containing(biasedLocation).equals(context.getClickedPos())) {
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
        if (!BlockPos.containing(biasedLocation).equals(pUseContext.getClickedPos())) {
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
            if (player != null) {
                List<ItemStack> drops = Block.getDrops(defaultBlockState().setValue(byByte(bite), true), (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                withBlockEntityDo(world, pos, ufte -> {
                    String property = byByte(bite).getName();
                    drops.add(ufte.getMaterialItemStorage().getMaterialItem(property).consumedItem());
                    ufte.setMaterial(property, AllBlocks.COPYCAT_BASE.getDefaultState());
                    ufte.setConsumedItem(property, ItemStack.EMPTY);
                });
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, state.setValue(byByte(bite), false).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
        pState = super.rotate(pState, pRotation);
        return mapBytes(pState, bite -> bite.rotate(pRotation));
    }

    @Override
    public void rotate(@NotNull BlockState state, @NotNull MultiStateCopycatBlockEntity be, Rotation rotation) {
        be.getMaterialItemStorage().remapStorage(key -> byByte(byteMap.get(key).rotate(rotation)).getName());
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState pState, Mirror pMirror) {
        pState = super.mirror(pState, pMirror);
        return mapBytes(pState, bite -> bite.mirror(pMirror));
    }

    @Override
    public void mirror(@NotNull BlockState state, @NotNull MultiStateCopycatBlockEntity be, Mirror mirror) {
        be.getMaterialItemStorage().remapStorage(key -> byByte(byteMap.get(key).mirror(mirror)).getName());
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

        public Byte rotate(Rotation rotation) {
            if (rotation == Rotation.CLOCKWISE_90) {
                return new Byte(!this.z, this.y, this.x);
            } else if (rotation == Rotation.CLOCKWISE_180) {
                return new Byte(!this.x, this.y, !this.z);
            } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
                return new Byte(this.z, this.y, !this.x);
            } else {
                return this;
            }
        }

        public Byte mirror(Mirror mirror) {
            boolean invertX = mirror.rotation() == OctahedralGroup.INVERT_X;
            boolean invertZ = mirror.rotation() == OctahedralGroup.INVERT_Z;
            return new Byte(invertX != this.x, this.y, invertZ != this.z);
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

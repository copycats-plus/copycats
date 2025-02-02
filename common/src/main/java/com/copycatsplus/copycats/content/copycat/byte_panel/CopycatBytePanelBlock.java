package com.copycatsplus.copycats.content.copycat.byte_panel;

import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.google.common.collect.ImmutableMap;
import com.mojang.math.OctahedralGroup;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

import static com.copycatsplus.copycats.utility.MathUtils.clampToGrid;
import static com.copycatsplus.copycats.utility.MathUtils.replaceAxis;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatBytePanelBlock extends WaterloggedMultiStateCopycatBlock implements ISpecialBlockItemRequirement {
    public static BooleanProperty BOTTOM_LEFT = BooleanProperty.create("bottom_left");
    public static BooleanProperty BOTTOM_RIGHT = BooleanProperty.create("bottom_right");
    public static BooleanProperty TOP_LEFT = BooleanProperty.create("top_left");
    public static BooleanProperty TOP_RIGHT = BooleanProperty.create("top_right");
    public static DirectionProperty FACING = BlockStateProperties.FACING;
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;

    public CopycatBytePanelBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(BOTTOM_LEFT, false)
                .setValue(BOTTOM_RIGHT, false)
                .setValue(TOP_LEFT, false)
                .setValue(TOP_RIGHT, false)
                .setValue(FACING, Direction.DOWN)
        );
        this.shapesCache = this.getShapeForEachState(CopycatBytePanelBlock::calculateMultiFaceShape);
    }

    @Override
    public String defaultProperty() {
        return BOTTOM_LEFT.getName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(2, 2, 2).relative(state.getValue(FACING).getAxis(), -1);
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        if (property.equals(BOTTOM_LEFT.getName())) return state.getValue(BOTTOM_LEFT);
        if (property.equals(BOTTOM_RIGHT.getName())) return state.getValue(BOTTOM_RIGHT);
        if (property.equals(TOP_LEFT.getName())) return state.getValue(TOP_LEFT);
        if (property.equals(TOP_RIGHT.getName())) return state.getValue(TOP_RIGHT);
        return false;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT).stream().map(BooleanProperty::getName).collect(Collectors.toSet());
    }

    @Override
    public int getColorIndex(String property) {
        if (property.equals(BOTTOM_LEFT.getName())) return 0;
        if (property.equals(BOTTOM_RIGHT.getName())) return 1;
        if (property.equals(TOP_LEFT.getName())) return 1;
        if (property.equals(TOP_RIGHT.getName())) return 0;
        throw new RuntimeException("Invalid property: " + property);
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        Direction blockFacing = state.getValue(FACING);
        Direction horizontal = getHorizontal(blockFacing);
        Direction vertical = getVertical(blockFacing);
        int horizontalHit = Mth.clamp(hitLocation.get(horizontal.getAxis()), 0, 1);
        if (horizontal.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
            horizontalHit = 1 - horizontalHit;
        int verticalHit = Mth.clamp(hitLocation.get(vertical.getAxis()), 0, 1);
        if (vertical.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
            verticalHit = 1 - verticalHit;
        return getProperty(horizontalHit, verticalHit);
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        Direction blockFacing = state.getValue(FACING);
        Direction horizontal = getHorizontal(blockFacing);
        Direction vertical = getVertical(blockFacing);
        Vector2i vector = getVector(property);
        return Vec3i.ZERO
                .relative(horizontal.getAxis(), horizontal.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 1 - vector.x : vector.x)
                .relative(vertical.getAxis(), vertical.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 1 - vector.y : vector.y);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT, FACING));
    }

    private static VoxelShape calculateMultiFaceShape(BlockState pState) {
        VoxelShape shape = Shapes.empty();
        Direction facing = pState.getValue(FACING);
        CopycatBytePanelBlock block = (CopycatBytePanelBlock) pState.getBlock();
        Vec3 size = replaceAxis(new Vec3(8, 8, 8), facing.getAxis(), 3);
        Direction horizontal = getHorizontal(facing);
        Direction vertical = getVertical(facing);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (block.partExists(pState, getProperty(i, j))) {
                    double h = horizontal.getAxisDirection() == Direction.AxisDirection.POSITIVE ? i * 8 : (8 - i * 8);
                    double v = vertical.getAxisDirection() == Direction.AxisDirection.POSITIVE ? j * 8 : (8 - j * 8);
                    double d = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 13 : 0;
                    Vec3 min = replaceAxis(replaceAxis(replaceAxis(Vec3.ZERO, horizontal.getAxis(), h), vertical.getAxis(), v), facing.getAxis(), d);
                    Vec3 max = min.add(size);
                    shape = Shapes.joinUnoptimized(shape, Block.box(min.x, min.y, min.z, max.x, max.y, max.z), BooleanOp.OR);
                }
            }
        }
        return shape.optimize();
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
        if (stateForPlacement == null) return null;
        BlockPos blockPos = context.getClickedPos();
        BlockState state = context.getLevel().getBlockState(blockPos);
        BlockState interactionState = state.is(this) ? state : stateForPlacement.setValue(FACING, context.getClickedFace().getOpposite());
        String property = getPropertyFromInteraction(interactionState, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), false);
        if (state.is(this)) {
            if (!state.getValue(fromProperty(property)))
                return state.setValue(fromProperty(property), true);
            else {
                return state;
            }
        } else {
            return stateForPlacement.setValue(FACING, context.getClickedFace().getOpposite()).setValue(fromProperty(property), true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        ItemStack itemstack = context.getItemInHand();
        if (!itemstack.is(this.asItem())) return false;
        String property = getPropertyFromInteraction(state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), false);
        return !state.getValue(fromProperty(property));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        onWrenched(state, context);
        int byteCount = 0;
        for (String property : storageProperties()) {
            if (state.getValue(fromProperty(property))) byteCount++;
        }
        if (byteCount <= 1) return super.onSneakWrenched(state, context);

        String property = getPropertyFromInteraction(state, context.getLevel(), context.getClickedPos(), context.getClickLocation(), context.getClickedFace(), true);
        if (context.getLevel() instanceof ServerLevel world) {
            Player player = context.getPlayer();
            BlockPos pos = context.getClickedPos();
            if (player != null) {
                List<ItemStack> drops = Block.getDrops(defaultBlockState().setValue(fromProperty(property), true), (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand());
                if (!player.isCreative()) {
                    for (ItemStack drop : drops) {
                        player.getInventory().placeItemBackInInventory(drop);
                    }
                }
            }
            BlockPos up = pos.relative(Direction.UP);
            world.setBlockAndUpdate(pos, state.setValue(fromProperty(property), false).updateShape(Direction.UP, world.getBlockState(up), world, pos, up));
            playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        return IMultiStateCopycatBlock.getRequiredItemsForParts(state, BOTTOM_LEFT, BOTTOM_RIGHT, TOP_LEFT, TOP_RIGHT);
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

    private static BlockState flipFaceVertical(BlockState state) {
        return state
                .setValue(TOP_LEFT, state.getValue(BOTTOM_LEFT))
                .setValue(TOP_RIGHT, state.getValue(BOTTOM_RIGHT))
                .setValue(BOTTOM_LEFT, state.getValue(TOP_LEFT))
                .setValue(BOTTOM_RIGHT, state.getValue(TOP_RIGHT));
    }

    private static void flipFaceVertical(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(BOTTOM_LEFT.getName())) return TOP_LEFT.getName();
            if (key.equals(BOTTOM_RIGHT.getName())) return TOP_RIGHT.getName();
            if (key.equals(TOP_LEFT.getName())) return BOTTOM_LEFT.getName();
            if (key.equals(TOP_RIGHT.getName())) return BOTTOM_RIGHT.getName();
            return key;
        });
    }

    private static BlockState flipFaceHorizontal(BlockState state) {
        return state
                .setValue(BOTTOM_LEFT, state.getValue(BOTTOM_RIGHT))
                .setValue(BOTTOM_RIGHT, state.getValue(BOTTOM_LEFT))
                .setValue(TOP_LEFT, state.getValue(TOP_RIGHT))
                .setValue(TOP_RIGHT, state.getValue(TOP_LEFT));
    }

    private static void flipFaceHorizontal(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(BOTTOM_RIGHT.getName())) return BOTTOM_LEFT.getName();
            if (key.equals(BOTTOM_LEFT.getName())) return BOTTOM_RIGHT.getName();
            if (key.equals(TOP_RIGHT.getName())) return TOP_LEFT.getName();
            if (key.equals(TOP_LEFT.getName())) return TOP_RIGHT.getName();
            return key;
        });
    }

    private static BlockState rotateFaceCounterClockwise(BlockState state) {
        return state
                .setValue(BOTTOM_LEFT, state.getValue(TOP_LEFT))
                .setValue(TOP_LEFT, state.getValue(TOP_RIGHT))
                .setValue(TOP_RIGHT, state.getValue(BOTTOM_RIGHT))
                .setValue(BOTTOM_RIGHT, state.getValue(BOTTOM_LEFT));
    }

    private static void rotateFaceCounterClockwise(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(TOP_LEFT.getName())) return BOTTOM_LEFT.getName();
            if (key.equals(TOP_RIGHT.getName())) return TOP_LEFT.getName();
            if (key.equals(BOTTOM_RIGHT.getName())) return TOP_RIGHT.getName();
            if (key.equals(BOTTOM_LEFT.getName())) return BOTTOM_RIGHT.getName();
            return key;
        });
    }

    private static BlockState rotateFaceClockwise(BlockState state) {
        return state
                .setValue(BOTTOM_LEFT, state.getValue(BOTTOM_RIGHT))
                .setValue(BOTTOM_RIGHT, state.getValue(TOP_RIGHT))
                .setValue(TOP_RIGHT, state.getValue(TOP_LEFT))
                .setValue(TOP_LEFT, state.getValue(BOTTOM_LEFT));
    }

    private static void rotateFaceClockwise(IMultiStateCopycatBlockEntity be) {
        be.getMaterialItemStorage().remapStorage(key -> {
            if (key.equals(BOTTOM_RIGHT.getName())) return BOTTOM_LEFT.getName();
            if (key.equals(TOP_RIGHT.getName())) return BOTTOM_RIGHT.getName();
            if (key.equals(TOP_LEFT.getName())) return TOP_RIGHT.getName();
            if (key.equals(BOTTOM_LEFT.getName())) return TOP_LEFT.getName();
            return key;
        });
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        // I am also extremely lost, but I don't see any way to simplify this
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            Direction facing = state.getValue(FACING);
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Y) {
                state = flipFaceVertical(state);
                if (facing.getAxis().isVertical()) {
                    state = state.setValue(FACING, facing.getOpposite());
                }
            } else {
                if (facing.getAxis().isHorizontal() && transform.mirror.rotation().inverts(facing.getAxis())) {
                    state = flipFaceHorizontal(state).setValue(FACING, facing.getOpposite());
                } else if (facing.getAxis().isHorizontal()) {
                    state = flipFaceHorizontal(state);
                } else if (transform.mirror.rotation().inverts(Axis.X)) {
                    state = flipFaceHorizontal(state);
                } else if (transform.mirror.rotation().inverts(Axis.Z)) {
                    state = flipFaceVertical(state);
                } else {
                    state = flipFaceVertical(state).setValue(FACING, facing.getOpposite());
                }
            }
        }
        if (transform.rotationAxis != null && transform.rotation != Rotation.NONE) {
            Direction facing = state.getValue(FACING);
            if (transform.rotationAxis.isVertical() && facing.getAxis().isHorizontal()) {
                state = state.setValue(FACING, transform.rotateFacing(facing));
            } else if (transform.rotationAxis == facing.getAxis()) {
                if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                    for (int i = 0; i < transform.rotation.ordinal(); i++)
                        state = rotateFaceClockwise(state);
                } else {
                    for (int i = 0; i < transform.rotation.ordinal(); i++)
                        state = rotateFaceCounterClockwise(state);
                }
            } else if (facing.getAxis().isVertical()) {
                if (transform.rotation == Rotation.CLOCKWISE_180) {
                    if (transform.rotationAxis == Axis.X) {
                        state = flipFaceVertical(state);
                    } else {
                        state = flipFaceHorizontal(state);
                    }
                    state = flipFaceVertical(state);
                } else if (transform.rotationAxis == Axis.X) {
                    if (facing == Direction.DOWN && transform.rotation == Rotation.CLOCKWISE_90 || facing == Direction.UP && transform.rotation == Rotation.COUNTERCLOCKWISE_90) {
                        state = state.setValue(FACING, Direction.SOUTH);
                    } else {
                        state = flipFaceHorizontal(flipFaceVertical(state)).setValue(FACING, Direction.NORTH);
                    }
                } else {
                    if (facing == Direction.DOWN) {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            state = rotateFaceCounterClockwise(state).setValue(FACING, Direction.WEST);
                        } else {
                            state = rotateFaceClockwise(state).setValue(FACING, Direction.EAST);
                        }
                    } else {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            state = rotateFaceCounterClockwise(state).setValue(FACING, Direction.EAST);
                        } else {
                            state = rotateFaceClockwise(state).setValue(FACING, Direction.WEST);
                        }
                    }
                }
            } else {
                if (transform.rotation == Rotation.CLOCKWISE_180) {
                    state = flipFaceVertical(state).setValue(FACING, facing.getOpposite());
                } else if (transform.rotationAxis == Axis.X) {
                    if (facing == Direction.SOUTH) {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            state = state.setValue(FACING, Direction.UP);
                        } else {
                            state = state.setValue(FACING, Direction.DOWN);
                        }
                    } else {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            state = flipFaceHorizontal(flipFaceVertical(state)).setValue(FACING, Direction.DOWN);
                        } else {
                            state = flipFaceHorizontal(flipFaceVertical(state)).setValue(FACING, Direction.UP);
                        }
                    }
                } else {
                    if (facing == Direction.EAST) {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            state = rotateFaceCounterClockwise(state).setValue(FACING, Direction.DOWN);
                        } else {
                            state = rotateFaceClockwise(state).setValue(FACING, Direction.UP);
                        }
                    } else {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            state = rotateFaceCounterClockwise(state).setValue(FACING, Direction.UP);
                        } else {
                            state = rotateFaceClockwise(state).setValue(FACING, Direction.DOWN);
                        }
                    }
                }
            }
        }
        return state;
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        if (transform.mirror != null && transform.mirror != Mirror.NONE) {
            Direction facing = state.getValue(FACING);
            if (transform.mirror.rotation() == OctahedralGroup.INVERT_Y) {
                flipFaceVertical(be);
            } else {
                if (facing.getAxis().isHorizontal() && transform.mirror.rotation().inverts(facing.getAxis())) {
                    flipFaceHorizontal(be);
                } else if (facing.getAxis().isHorizontal()) {
                    flipFaceHorizontal(be);
                } else if (transform.mirror.rotation().inverts(Axis.X)) {
                    flipFaceHorizontal(be);
                } else if (transform.mirror.rotation().inverts(Axis.Z)) {
                    flipFaceVertical(be);
                } else {
                    flipFaceVertical(be);
                }
            }
        }
        if (transform.rotationAxis != null && transform.rotation != Rotation.NONE) {
            Direction facing = state.getValue(FACING);
            if (!transform.rotationAxis.isVertical() || !facing.getAxis().isHorizontal()) {
                if (transform.rotationAxis == facing.getAxis()) {
                    if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                        for (int i = 0; i < transform.rotation.ordinal(); i++)
                            rotateFaceClockwise(be);
                    } else {
                        for (int i = 0; i < transform.rotation.ordinal(); i++)
                            rotateFaceCounterClockwise(be);
                    }
                } else if (facing.getAxis().isVertical()) {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        if (transform.rotationAxis == Axis.X) {
                            flipFaceVertical(be);
                        } else {
                            flipFaceHorizontal(be);
                        }
                        flipFaceVertical(be);
                    } else if (transform.rotationAxis == Axis.X) {
                        if ((facing != Direction.UP || transform.rotation != Rotation.CLOCKWISE_90) && (facing != Direction.DOWN || transform.rotation != Rotation.COUNTERCLOCKWISE_90)) {
                            flipFaceVertical(be);
                            flipFaceHorizontal(be);
                        }
                    } else {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            rotateFaceCounterClockwise(be);
                        } else {
                            rotateFaceClockwise(be);
                        }
                    }
                } else {
                    if (transform.rotation == Rotation.CLOCKWISE_180) {
                        flipFaceVertical(be);
                    } else if (transform.rotationAxis == Axis.X) {
                        if (facing != Direction.SOUTH) {
                            flipFaceVertical(be);
                            flipFaceHorizontal(be);
                        }
                    } else {
                        if (transform.rotation == Rotation.CLOCKWISE_90) {
                            rotateFaceCounterClockwise(be);
                        } else {
                            rotateFaceClockwise(be);
                        }
                    }
                }
            }
        }
    }

    public static BooleanProperty fromProperty(String property) {
        if (property.equals(BOTTOM_LEFT.getName())) return BOTTOM_LEFT;
        if (property.equals(BOTTOM_RIGHT.getName())) return BOTTOM_RIGHT;
        if (property.equals(TOP_LEFT.getName())) return TOP_LEFT;
        if (property.equals(TOP_RIGHT.getName())) return TOP_RIGHT;
        throw new RuntimeException("Invalid property: " + property);
    }

    public static String getProperty(int horizontal, int vertical) {
        if (horizontal == 0 && vertical == 0) return BOTTOM_RIGHT.getName();
        if (horizontal == 1 && vertical == 0) return BOTTOM_LEFT.getName();
        if (horizontal == 0 && vertical == 1) return TOP_RIGHT.getName();
        if (horizontal == 1 && vertical == 1) return TOP_LEFT.getName();
        throw new RuntimeException("Invalid horizontal and vertical values: " + horizontal + ", " + vertical);
    }

    public static Vector2i getVector(String property) {
        if (property.equals(BOTTOM_RIGHT.getName())) return new Vector2i(0, 0);
        if (property.equals(BOTTOM_LEFT.getName())) return new Vector2i(1, 0);
        if (property.equals(TOP_RIGHT.getName())) return new Vector2i(0, 1);
        if (property.equals(TOP_LEFT.getName())) return new Vector2i(1, 1);
        throw new RuntimeException("Invalid property: " + property);
    }

    public static Direction getHorizontal(Direction facing) {
        return switch (facing) {
            case DOWN -> Direction.EAST;
            case UP -> Direction.EAST;
            case NORTH -> Direction.WEST;
            case SOUTH -> Direction.EAST;
            case EAST -> Direction.NORTH;
            case WEST -> Direction.SOUTH;
        };
    }

    public static Direction getVertical(Direction facing) {
        return switch (facing) {
            case DOWN -> Direction.SOUTH;
            case UP -> Direction.NORTH;
            case NORTH -> Direction.UP;
            case SOUTH -> Direction.UP;
            case EAST -> Direction.UP;
            case WEST -> Direction.UP;
        };
    }
}

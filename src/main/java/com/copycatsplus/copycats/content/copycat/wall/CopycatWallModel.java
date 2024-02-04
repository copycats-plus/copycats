package com.copycatsplus.copycats.content.copycat.wall;

import com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel;
import com.simibubi.create.content.decoration.copycat.CopycatModel;
import com.simibubi.create.foundation.utility.Iterate;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.copycatsplus.copycats.content.copycat.ISimpleCopycatModel.MutableCullFace.*;

public class CopycatWallModel extends CopycatModel implements ISimpleCopycatModel {

    public CopycatWallModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected void emitBlockQuadsInner(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext renderContext, BlockState material, CullFaceRemovalData cullFaceRemovalData, OcclusionData occlusionData) {
        BakedModel model = getModelOf(material);
        SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));
        // Use a mesh to defer quad emission since quads cannot be emitted inside a transform
        MeshBuilder meshBuilder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();

        renderContext.pushTransform(quad -> {
            CopycatRenderContext context = context(quad, emitter);
            if (cullFaceRemovalData.shouldRemove(quad.cullFace())) {
                quad.cullFace(null);
            } else if (occlusionData.isOccluded(quad.cullFace())) {
                // Add quad to mesh and do not render original quad to preserve quad render order
                assembleQuad(context);
                return false;
            }
            boolean pole = state.getValue(WallBlock.UP);
            if (pole) {
                // Assemble piece by piece if the central pole exists

                // Assemble the central pole
                for (Direction direction : Iterate.horizontalDirections) {
                    assemblePiece(context, (int) direction.toYRot(), false,
                            vec3(4, 0, 4),
                            aabb(4, 16, 4),
                            cull(SOUTH | EAST)
                    );
                }

                // Assemble the sides
                for (Direction direction : Iterate.horizontalDirections) {
                    int rot = (int) direction.toYRot();
                    switch (state.getValue(CopycatWallBlock.byDirection(direction))) {
                        case NONE -> {
                            continue;
                        }
                        case LOW -> {
                            assemblePiece(context, rot, false,
                                    vec3(5, 0, 12),
                                    aabb(3, 7, 4),
                                    cull(UP | NORTH | EAST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(8, 0, 12),
                                    aabb(3, 7, 4).move(13, 0, 0),
                                    cull(UP | NORTH | WEST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(5, 7, 12),
                                    aabb(3, 7, 4).move(0, 9, 0),
                                    cull(DOWN | NORTH | EAST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(8, 7, 12),
                                    aabb(3, 7, 4).move(13, 9, 0),
                                    cull(DOWN | NORTH | WEST)
                            );
                        }
                        case TALL -> {
                            assemblePiece(context, rot, false,
                                    vec3(5, 0, 12),
                                    aabb(3, 16, 4),
                                    cull(NORTH | EAST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(8, 0, 12),
                                    aabb(3, 16, 4).move(13, 0, 0),
                                    cull(NORTH | WEST)
                            );
                        }
                    }
                }
            } else {
                // Use special logic if the central pole does not exist

                boolean tall = false;
                Map<Direction, WallSide> sides = new HashMap<>();
                for (Direction direction : Iterate.horizontalDirections) {
                    WallSide wall = state.getValue(CopycatWallBlock.byDirection(direction));
                    sides.put(direction, wall);
                    if (wall == WallSide.TALL) tall = true;
                }

                // Special case: A straight panel
                if (sides.get(Direction.SOUTH) == sides.get(Direction.NORTH) &&
                        sides.get(Direction.EAST) == sides.get(Direction.WEST) &&
                        (sides.get(Direction.NORTH) == WallSide.NONE || sides.get(Direction.EAST) == WallSide.NONE) &&
                        (sides.get(Direction.NORTH) != WallSide.NONE || sides.get(Direction.EAST) != WallSide.NONE)) {
                    int rot = sides.get(Direction.SOUTH) == WallSide.NONE ? 90 : 0;

                    if (!tall) {
                        assemblePiece(context, rot, false,
                                vec3(5, 0, 0),
                                aabb(3, 7, 16),
                                cull(UP | EAST)
                        );
                        assemblePiece(context, rot, false,
                                vec3(8, 0, 0),
                                aabb(3, 7, 16).move(13, 0, 0),
                                cull(UP | WEST)
                        );
                        assemblePiece(context, rot, false,
                                vec3(5, 7, 0),
                                aabb(3, 7, 16).move(0, 9, 0),
                                cull(DOWN | EAST)
                        );
                        assemblePiece(context, rot, false,
                                vec3(8, 7, 0),
                                aabb(3, 7, 16).move(13, 9, 0),
                                cull(DOWN | WEST)
                        );
                    } else {
                        assemblePiece(context, rot, false,
                                vec3(5, 0, 0),
                                aabb(3, 16, 16).move(0, 0, 0),
                                cull(EAST)
                        );
                        assemblePiece(context, rot, false,
                                vec3(8, 0, 0),
                                aabb(3, 16, 16).move(13, 0, 0),
                                cull(WEST)
                        );
                    }

                    return false;
                }

                // Assemble the center if needed
                Direction extendSide = null;
                long sideCount = sides.values().stream().filter(s -> s != WallSide.NONE).count();
                if (sideCount == 1) {
                    extendSide = sides.entrySet().stream().filter(s -> s.getValue() != WallSide.NONE).findFirst().map(Map.Entry::getKey).orElse(null);
                } else {
                    for (Direction direction : Iterate.horizontalDirections) {
                        int rot = (int) direction.toYRot();
                        if (tall) {
                            boolean cullCurrent = sides.get(direction.getOpposite()) == WallSide.TALL;
                            boolean cullAdjacent = sides.get(direction.getClockWise()) == WallSide.TALL;
                            assemblePiece(context, rot, false,
                                    vec3(5, 0, 5),
                                    aabb(3, 16, 3).move(0, 0, 0),
                                    cull(SOUTH | EAST | (cullCurrent ? NORTH : 0) | (cullAdjacent ? WEST : 0))
                            );
                        } else {
                            boolean cullCurrent = sides.get(direction.getOpposite()) != WallSide.NONE;
                            boolean cullAdjacent = sides.get(direction.getClockWise()) != WallSide.NONE;
                            assemblePiece(context, rot, false,
                                    vec3(5, 0, 5),
                                    aabb(3, 7, 3).move(0, 0, 0),
                                    cull(UP | SOUTH | EAST | (cullCurrent ? NORTH : 0) | (cullAdjacent ? WEST : 0))
                            );
                            assemblePiece(context, rot, false,
                                    vec3(5, 7, 5),
                                    aabb(3, 7, 3).move(0, 9, 0),
                                    cull(DOWN | SOUTH | EAST | (cullCurrent ? NORTH : 0) | (cullAdjacent ? WEST : 0))
                            );
                        }
                    }
                }

                // Assemble the sides
                // One side will extend to the center
                for (Direction direction : Iterate.horizontalDirections) {
                    int rot = (int) direction.toYRot();
                    boolean extend = extendSide == direction;
                    boolean cullEnd = !extend;

                    switch (sides.get(direction)) {
                        case NONE -> {
                            continue;
                        }
                        case LOW -> {
                            assemblePiece(context, rot, false,
                                    vec3(5, 0, extend ? 5 : 11),
                                    aabb(3, 7, extend ? 11 : 5).move(0, 0, 0),
                                    cull(UP | (cullEnd ? NORTH : 0) | EAST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(8, 0, extend ? 5 : 11),
                                    aabb(3, 7, extend ? 11 : 5).move(13, 0, 0),
                                    cull(UP | (cullEnd ? NORTH : 0) | WEST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(5, 7, extend ? 5 : 11),
                                    aabb(3, 7, extend ? 11 : 5).move(0, 9, 0),
                                    cull(DOWN | (cullEnd ? NORTH : 0) | EAST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(8, 7, extend ? 5 : 11),
                                    aabb(3, 7, extend ? 11 : 5).move(13, 9, 0),
                                    cull(DOWN | (cullEnd ? NORTH : 0) | WEST)
                            );
                        }
                        case TALL -> {
                            assemblePiece(context, rot, false,
                                    vec3(5, 0, extend ? 5 : 11),
                                    aabb(3, 16, extend ? 11 : 5).move(0, 0, 0),
                                    cull((cullEnd ? NORTH : 0) | EAST)
                            );
                            assemblePiece(context, rot, false,
                                    vec3(8, 0, extend ? 5 : 11),
                                    aabb(3, 16, extend ? 11 : 5).move(13, 0, 0),
                                    cull((cullEnd ? NORTH : 0) | WEST)
                            );
                        }
                    }
                }
            }
            return false;
        });
        model.emitBlockQuads(blockView, material, pos, randomSupplier, renderContext);
        renderContext.popTransform();
        renderContext.meshConsumer().accept(meshBuilder.build());
    }

}

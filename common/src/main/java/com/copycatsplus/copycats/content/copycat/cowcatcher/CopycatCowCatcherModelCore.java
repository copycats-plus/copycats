package com.copycatsplus.copycats.content.copycat.cowcatcher;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.content.copycat.base.model.assembly.MutableCullFace.*;

public class CopycatCowCatcherModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        AssemblyTransform transform = t -> t.rotateY(0);

        if (state.getValue(CopycatCowcatcherBlock.HALF) == DoubleBlockHalf.LOWER) {
            //Right Segment
            context.assemblePiece(transform,
                    vec3(-3.326180001999518, 1.25, 13.746561915190497),
                    aabb(2.9999999999999964, 2.0, 14.0),
                    cull(0),
                    rotate(
                            pivot(8.27683999800048, 14.75, 5.996521915190495),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(17.54740188638617, 1.25, 3.4274315555951116),
                    aabb(2.9999999999999964, 2.0, 3.0),
                    cull(0),
                    rotate(
                            pivot(29.15042188638617, 14.75, -14.889701886386167),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(17.54740188638617, 0.25, 3.4274315555951116),
                    aabb(2.9999999999999964, 1.0, 3.0),
                    cull(0),
                    rotate(
                            pivot(29.150421886386166, 14.75, -14.889701886386167),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(14.71897476163998, 0.25, 5.255858680341301),
                    aabb(2.9999999999999964, 1.0, 3.0),
                    cull(0),
                    rotate(
                            pivot(26.321994761639978, 13.75, -12.061274761639977),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(19.37582867395757, 0.25, 6.255858343166514),
                    aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                    cull(0),
                    rotate(
                            pivot(31.978848673957568, 13.75, -12.061275098814765),
                            angle(0, -45, 0)
                    )
            );

            //Left Edge Piece
            context.assemblePiece(transform,
                    vec3(15.230133936099513, 8.729744152022255, 11.06072),
                    aabb(2.0, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(2.666943936099514, 27.729744152022253, 4.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(15.230133936099516, 8.729744152022255, 13.06072),
                    aabb(2.0, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(2.6669439360995177, 27.729744152022253, 6.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(15.006453233159084, 8.287426213744254, 13.06072),
                    aabb(2.0000000000000018, 13.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(2.443263233159085, 23.043505279196683, 6.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(14.488069178913037, 8.18431323488797, 11.06072),
                    aabb(2.0, 13.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(1.924879178913038, 24.29499509347544, 4.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(20.36402114941208, -4.146886911008693, 13.06072),
                    aabb(2.0, 13.5, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.80083114941208, 10.109192154443736, 6.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(19.845637140785453, -4.25, 11.06072),
                    aabb(2.0, 13.5, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.282447140785454, 11.36068185858747, 4.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(20.58770198921077, -4.204569303135761, 11.06072),
                    aabb(2.0, 14.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.024511989210772, 14.795430696864237, 4.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(20.58770198921077, -4.204569303135761, 13.06072),
                    aabb(2.0, 14.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.024511989210772, 14.795430696864237, 6.017389999999999),
                            angle(0, 0, 22.5)
                    )
            );

            //Middle
            context.assemblePiece(transform,
                    vec3(8.525939974517824, -1.3120600000000024, -0.6912300000000036),
                    aabb(1.0, 13.000000000000002, 2.0000000000000133),
                    cull(0),
                    rotate(
                            pivot(9.008549974517823, 16.599999999999998, 7.767390000000011),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.525939974517824, -1.3120600000000024, -0.6912300000000036),
                    aabb(2.0, 13.000000000000002, 2.0000000000000133),
                    cull(0),
                    rotate(
                            pivot(8.008549974517823, 16.599999999999998, 7.767390000000011),
                            angle(22.5, 0, 0)
                    )
            );

            //Left Segment
            context.assemblePiece(transform,
                    vec3(-6.375828673957567, 1.25, 6.255858343166514),
                    aabb(2.9999999999999964, 2.0, 3.0000000000000018),
                    cull(0),
                    rotate(
                            pivot(-15.978848673957568, 14.75, -12.061275098814765),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-4.254508330397925, 0.25, 8.377178686726156),
                    aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                    cull(0),
                    rotate(
                            pivot(-13.857528330397926, 13.75, -9.939954755255123),
                            angle(0, 45, 0)
                    )
            );

            //Right Segment
            context.assemblePiece(transform,
                    vec3(3.52613, 25.25, 5.524789999999999),
                    aabb(2.0, 1.5, 15.0),
                    cull(UP | NORTH | EAST),
                    rotate(
                            pivot(8.043330000000001, 12.75, 6.017390000000001),
                            angle(0, -45, 0)
                    )
            );

            //Left Segment
            context.assemblePiece(transform,
                    vec3(9.059656437626906, 26.75, 6.9390035623730935),
                    aabb(2.0, 1.5, 15.000000000000002),
                    cull(DOWN | NORTH | EAST),
                    rotate(
                            pivot(6.542456437626905, 15.25, 7.431603562373095),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(10.47387, 26.75, 5.524789999999999),
                    aabb(2.0, 1.5, 15.0),
                    cull(DOWN | NORTH | WEST),
                    rotate(
                            pivot(7.956669999999999, 15.25, 6.017390000000001),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(10.47387, 25.25, 5.524789999999999),
                    aabb(2.0, 1.5, 15.0),
                    cull(NORTH | WEST),
                    rotate(
                            pivot(7.956669999999999, 12.75, 6.017390000000001),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.059656437626906, 25.25, 6.9390035623730935),
                    aabb(2.0, 1.5, 15.000000000000002),
                    cull(NORTH | EAST),
                    rotate(
                            pivot(6.542456437626905, 12.75, 7.431603562373095),
                            angle(0, 45, 0)
                    )
            );

            //Back Segment
            context.assemblePiece(transform,
                    vec3(7.9740600000000015, 0.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(3.956669999999999, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(7.9740600000000015, 1.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(3.956669999999999, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(7.9740600000000015, 0.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(3.956669999999999, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(7.9740600000000015, 1.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(3.956669999999999, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(10.974060000000001, 1.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(5.956669999999999, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(10.974060000000001, 1.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(5.956669999999999, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(10.974060000000001, 0.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(5.956669999999999, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(10.974060000000001, 0.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(5.956669999999999, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            //Right Segment
            context.assemblePiece(transform,
                    vec3(-5.735194059144945, 1.25, 13.999999999999996),
                    aabb(2.999999999999993, 2.0, 14.000000000000004),
                    cull(0),
                    rotate(
                            pivot(5.867825940855051, 14.75, 12.64817665945521),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-5.735194059144945, 0.25, 13.999999999999996),
                    aabb(2.999999999999993, 1.0, 14.000000000000004),
                    cull(0),
                    rotate(
                            pivot(5.867825940855051, 13.75, 12.64817665945521),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-3.326180001999518, 0.25, 13.746561915190497),
                    aabb(2.9999999999999964, 1.0, 14.0),
                    cull(0),
                    rotate(
                            pivot(8.27683999800048, 13.75, 5.996521915190495),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-5.735194059144945, 1.25, 0.9999999999999964),
                    aabb(2.999999999999993, 2.0, 13.000000000000004),
                    cull(0),
                    rotate(
                            pivot(5.867825940855051, 14.75, 12.64817665945521),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-5.735194059144945, 0.25, 0.9999999999999964),
                    aabb(2.999999999999993, 1.0, 13.000000000000004),
                    cull(0),
                    rotate(
                            pivot(5.867825940855051, 13.75, 12.64817665945521),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-3.326180001999518, 0.25, 0.7465619151904974),
                    aabb(2.9999999999999964, 1.0, 13.0),
                    cull(0),
                    rotate(
                            pivot(8.27683999800048, 13.75, 5.996521915190495),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-3.326180001999518, 1.25, 0.7465619151904974),
                    aabb(2.9999999999999964, 2.0, 13.0),
                    cull(0),
                    rotate(
                            pivot(8.27683999800048, 14.75, 5.996521915190495),
                            angle(0, -45, 0)
                    )
            );

            //Left Segment
            context.assemblePiece(transform,
                    vec3(16.32618000199952, 1.25, 0.7465619151904974),
                    aabb(2.9999999999999964, 2.0, 13.0),
                    cull(0),
                    rotate(
                            pivot(7.723160001999521, 14.75, 5.996521915190495),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(16.32618000199952, 1.25, 13.746561915190497),
                    aabb(2.9999999999999964, 2.0, 14.0),
                    cull(0),
                    rotate(
                            pivot(7.723160001999521, 14.75, 5.996521915190495),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(16.32618000199952, 0.25, 13.746561915190497),
                    aabb(2.9999999999999964, 1.0, 14.0),
                    cull(0),
                    rotate(
                            pivot(7.723160001999521, 13.75, 5.996521915190495),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(16.32618000199952, 0.25, 0.7465619151904974),
                    aabb(2.9999999999999964, 1.0, 13.0),
                    cull(0),
                    rotate(
                            pivot(7.723160001999521, 13.75, 5.996521915190495),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(18.735194059144952, 0.25, 0.9999999999999964),
                    aabb(2.999999999999993, 1.0, 13.000000000000004),
                    cull(0),
                    rotate(
                            pivot(10.13217405914495, 13.75, 12.64817665945521),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(18.735194059144952, 0.25, 13.999999999999996),
                    aabb(2.999999999999993, 1.0, 14.000000000000004),
                    cull(0),
                    rotate(
                            pivot(10.13217405914495, 13.75, 12.64817665945521),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(18.735194059144952, 1.25, 13.999999999999996),
                    aabb(2.999999999999993, 2.0, 14.000000000000004),
                    cull(0),
                    rotate(
                            pivot(10.13217405914495, 14.75, 12.64817665945521),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(18.735194059144952, 1.25, 0.9999999999999964),
                    aabb(2.999999999999993, 2.0, 13.000000000000004),
                    cull(0),
                    rotate(
                            pivot(10.13217405914495, 14.75, 12.64817665945521),
                            angle(0, 45, 0)
                    )
            );

            //Back Segment
            context.assemblePiece(transform,
                    vec3(4.974059548645016, 0.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(11.991449548645019, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(4.974059548645016, 1.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(11.991449548645019, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(4.974059548645016, 0.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(11.991449548645019, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(4.974059548645016, 1.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(11.991449548645019, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-10.025940451354984, 1.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.991449548645019, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-10.025940451354984, 1.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.991449548645019, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-10.025940451354984, 0.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.991449548645019, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-10.025940451354984, 0.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.991449548645019, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            //Right Edge Piece
            context.assemblePiece(transform,
                    vec3(-1.230133936099513, 8.729744152022255, 11.06072),
                    aabb(2.0, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(13.333056063900486, 27.729744152022253, 4.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-1.2301339360995165, 8.729744152022255, 13.06072),
                    aabb(2.0, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(13.333056063900482, 27.729744152022253, 6.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-1.0064532331590854, 8.287426213744254, 13.06072),
                    aabb(2.0000000000000018, 13.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(13.556736766840915, 23.043505279196683, 6.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-0.4880691789130367, 8.18431323488797, 11.06072),
                    aabb(2.0, 13.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(14.075120821086962, 24.29499509347544, 4.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-6.364021149412078, -4.146886911008693, 13.06072),
                    aabb(2.0, 13.5, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.19916885058792, 10.109192154443736, 6.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-5.845637140785453, -4.25, 11.06072),
                    aabb(2.0, 13.5, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.717552859214546, 11.36068185858747, 4.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-6.587701989210771, -4.204569303135761, 11.06072),
                    aabb(2.0, 14.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.975488010789228, 14.795430696864237, 4.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-6.587701989210771, -4.204569303135761, 13.06072),
                    aabb(2.0, 14.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.975488010789228, 14.795430696864237, 6.017389999999999),
                            angle(0, 0, -22.5)
                    )
            );

            //Middle
            context.assemblePiece(transform,
                    vec3(12.275939999999999, 11.20525656763491, 5.232649532511285),
                    aabb(2.0, 12.699999999999998, 1.0),
                    cull(0),
                    rotate(
                            pivot(12.793330000000001, 14.11731656763491, 12.691269532511285),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(12.275939999999999, 11.58794, 3.308769999999999),
                    aabb(2.0, 12.7, 2.0),
                    cull(0),
                    rotate(
                            pivot(12.793330000000001, 14.5, 11.767389999999999),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.275939974517824, -1.5620600000000024, 4.05877000000001),
                    aabb(2.0, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(12.758549974517823, 16.349999999999998, 12.517389999999974),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(13.275939974517824, -1.5620600000000024, 4.05877000000001),
                    aabb(1.0000000000000018, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(13.758549974517823, 16.349999999999998, 12.517389999999974),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.275939999999999, 11.205256567634912, 5.232649532511285),
                    aabb(1.0, 12.7, 1.0),
                    cull(0),
                    rotate(
                            pivot(11.793330000000001, 14.117316567634912, 12.691269532511285),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.275939999999999, 11.58794, 3.308769999999999),
                    aabb(1.0, 12.7, 2.0),
                    cull(0),
                    rotate(
                            pivot(11.793330000000001, 14.5, 11.767389999999999),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.27594, -1.9447434323650903, 5.982649532511296),
                    aabb(1.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(11.793330000000001, 15.96731656763491, 13.441269532511242),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(12.275939999999999, -1.9447434323650903, 5.982649532511296),
                    aabb(2.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(12.793330000000001, 15.96731656763491, 13.441269532511242),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(17.27593999999997, 11.20525656763491, 9.782649532511272),
                    aabb(2.0, 12.699999999999998, 1.0),
                    cull(0),
                    rotate(
                            pivot(17.793329999999973, 14.11731656763491, 17.241269532511332),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(17.27593999999997, 11.58794, 7.858769999999989),
                    aabb(2.0, 12.7, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(17.793329999999973, 14.5, 16.317390000000046),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(16.275939974517797, -1.5620600000000024, 8.60877),
                    aabb(2.0, 13.000000000000002, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(17.758549974517795, 16.349999999999998, 17.067390000000003),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(18.275939974517797, -1.5620600000000024, 8.60877),
                    aabb(1.0, 13.000000000000002, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(18.758549974517795, 16.349999999999998, 17.067390000000003),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(16.27593999999997, 11.205256567634912, 9.782649532511272),
                    aabb(1.0, 12.7, 1.0),
                    cull(0),
                    rotate(
                            pivot(16.793329999999973, 14.117316567634912, 17.241269532511332),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(16.27593999999997, 11.58794, 7.858769999999989),
                    aabb(1.0, 12.7, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(16.793329999999973, 14.5, 16.317390000000046),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(16.27593999999997, -1.9447434323650903, 10.532649532511282),
                    aabb(1.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(16.793329999999973, 15.96731656763491, 17.99126953251129),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(17.27593999999997, -1.9447434323650903, 10.532649532511282),
                    aabb(2.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(17.793329999999973, 15.96731656763491, 17.99126953251129),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(1.7240600000000015, 11.20525656763491, 5.232649532511285),
                    aabb(2.0, 12.699999999999998, 1.0),
                    cull(0),
                    rotate(
                            pivot(3.206669999999999, 14.11731656763491, 12.691269532511285),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(1.7240600000000015, 11.58794, 3.308769999999999),
                    aabb(2.0, 12.7, 2.0),
                    cull(0),
                    rotate(
                            pivot(3.206669999999999, 14.5, 11.767389999999999),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(2.724060025482176, -1.5620600000000024, 4.05877000000001),
                    aabb(2.0, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(3.2414500254821768, 16.349999999999998, 12.517389999999974),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(1.7240600254821743, -1.5620600000000024, 4.05877000000001),
                    aabb(1.0000000000000018, 13.000000000000002, 2.0),
                    cull(0),
                    rotate(
                            pivot(2.2414500254821768, 16.349999999999998, 12.517389999999974),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(3.7240600000000015, 11.205256567634912, 5.232649532511285),
                    aabb(1.0, 12.7, 1.0),
                    cull(0),
                    rotate(
                            pivot(4.206669999999999, 14.117316567634912, 12.691269532511285),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(3.7240600000000015, 11.58794, 3.308769999999999),
                    aabb(1.0, 12.7, 2.0),
                    cull(0),
                    rotate(
                            pivot(4.206669999999999, 14.5, 11.767389999999999),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(3.7240599999999997, -1.9447434323650903, 5.982649532511296),
                    aabb(1.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(4.206669999999999, 15.96731656763491, 13.441269532511242),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(1.7240600000000015, -1.9447434323650903, 5.982649532511296),
                    aabb(2.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(3.206669999999999, 15.96731656763491, 13.441269532511242),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-3.27593999999997, 11.20525656763491, 9.782649532511272),
                    aabb(2.0, 12.699999999999998, 1.0),
                    cull(0),
                    rotate(
                            pivot(-1.7933299999999726, 14.11731656763491, 17.241269532511332),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-3.27593999999997, 11.58794, 7.858769999999989),
                    aabb(2.0, 12.7, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(-1.7933299999999726, 14.5, 16.317390000000046),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.2759399745177973, -1.5620600000000024, 8.60877),
                    aabb(2.0, 13.000000000000002, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(-1.7585499745177948, 16.349999999999998, 17.067390000000003),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-3.2759399745177973, -1.5620600000000024, 8.60877),
                    aabb(1.0, 13.000000000000002, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(-2.758549974517795, 16.349999999999998, 17.067390000000003),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-1.27593999999997, 11.205256567634912, 9.782649532511272),
                    aabb(1.0, 12.7, 1.0),
                    cull(0),
                    rotate(
                            pivot(-0.7933299999999726, 14.117316567634912, 17.241269532511332),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-1.27593999999997, 11.58794, 7.858769999999989),
                    aabb(1.0, 12.7, 1.9999999999999964),
                    cull(0),
                    rotate(
                            pivot(-0.7933299999999726, 14.5, 16.317390000000046),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-1.27593999999997, -1.9447434323650903, 10.532649532511282),
                    aabb(1.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(-0.7933299999999726, 15.96731656763491, 17.99126953251129),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-3.27593999999997, -1.9447434323650903, 10.532649532511282),
                    aabb(2.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(-1.7933299999999726, 15.96731656763491, 17.99126953251129),
                            angle(22.5, 0, 0)
                    )
            );

            //Back Segment
            context.assemblePiece(transform,
                    vec3(6.5, 25.75, 12.31072),
                    aabb(2.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 25.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 26.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.5, 26.75, 12.31072),
                    aabb(2.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.5, 25.75, 14.31072),
                    aabb(2.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 25.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 26.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.5, 26.75, 14.31072),
                    aabb(2.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 26.75, 14.31072),
                    aabb(1.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 25.75, 14.31072),
                    aabb(1.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 26.75, 12.31072),
                    aabb(1.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 25.75, 12.31072),
                    aabb(1.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 25.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 26.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 25.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 26.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            //Left Segment
            context.assemblePiece(transform,
                    vec3(5.52613, 26.25, 3.5247900000000003),
                    aabb(2.0, 2.0, 1.9999999999999991),
                    cull(0),
                    rotate(
                            pivot(8.043330000000001, 14.75, 6.017390000000001),
                            angle(0, -45, 0)
                    )
            );

            //Middle
            context.assemblePiece(transform,
                    vec3(8.524060000000002, 11.83794, -1.441230000000001),
                    aabb(1.0, 12.7, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.00667, 14.75, 7.017390000000001),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.524060000000002, 11.455256567634912, 0.4826495325112852),
                    aabb(1.0, 12.7, 1.0),
                    cull(0),
                    rotate(
                            pivot(9.00667, 14.367316567634912, 7.941269532511287),
                            angle(22.5, 0, 0)
                    )
            );

            //Left Segment
            context.assemblePiece(transform,
                    vec3(-4.547401886386165, 0.25, 3.4274315555951116),
                    aabb(2.9999999999999964, 1.0, 3.0),
                    cull(0),
                    rotate(
                            pivot(-13.150421886386166, 14.75, -14.889701886386167),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-1.718974761639977, 0.25, 5.255858680341301),
                    aabb(2.9999999999999964, 1.0, 3.0),
                    cull(0),
                    rotate(
                            pivot(-10.321994761639978, 13.75, -12.061274761639977),
                            angle(0, 45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-6.375828673957567, 0.25, 6.255858343166514),
                    aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                    cull(0),
                    rotate(
                            pivot(-15.978848673957568, 13.75, -12.061275098814765),
                            angle(0, 45, 0)
                    )
            );

            //Right Segment
            context.assemblePiece(transform,
                    vec3(19.37582867395757, 1.25, 6.255858343166514),
                    aabb(2.9999999999999964, 2.0, 3.0000000000000018),
                    cull(0),
                    rotate(
                            pivot(31.978848673957568, 14.75, -12.061275098814765),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(17.25450833039793, 0.25, 8.377178686726156),
                    aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                    cull(0),
                    rotate(
                            pivot(29.857528330397926, 13.75, -9.939954755255123),
                            angle(0, -45, 0)
                    )
            );

            //Left Segment
            context.assemblePiece(transform,
                    vec3(6.940343541299672, 26.25, 2.1105764587003293),
                    aabb(2.0, 2.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.457543541299671, 14.75, 4.6031764587003305),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(5.526129978926576, 26.25, 0.6963628963272344),
                    aabb(2.0, 2.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.043329978926575, 14.75, 3.1889628963272356),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(4.111916437626906, 26.25, 2.1105764376269054),
                    aabb(2.0, 2.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(6.629116437626905, 14.75, 4.603176437626907),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.940343541299672, 25.25, 2.1105764587003293),
                    aabb(2.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.457543541299671, 13.75, 4.6031764587003305),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(5.52613, 25.25, 3.5247900000000003),
                    aabb(2.0, 1.0, 1.9999999999999991),
                    cull(0),
                    rotate(
                            pivot(8.043330000000001, 13.75, 6.017390000000001),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(4.111916437626906, 25.25, 2.1105764376269054),
                    aabb(2.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(6.629116437626905, 13.75, 4.603176437626907),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(5.526129978926576, 25.25, 0.6963628963272344),
                    aabb(2.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.043329978926575, 13.75, 3.1889628963272356),
                            angle(0, -45, 0)
                    )
            );

            //Middle
            context.assemblePiece(transform,
                    vec3(6.524060000000002, 11.83794, -1.441230000000001),
                    aabb(2.0, 12.7, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.00667, 14.75, 7.017390000000001),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.524060000000002, 11.45525656763491, 0.4826495325112852),
                    aabb(2.0, 12.699999999999998, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.00667, 14.36731656763491, 7.941269532511287),
                            angle(22.5, 0, 0)
                    )
            );

            //Back Segment
            context.assemblePiece(transform,
                    vec3(5.0259399999999985, 0.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(12.043330000000001, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(5.0259399999999985, 1.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(12.043330000000001, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(5.0259399999999985, 0.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(12.043330000000001, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(5.0259399999999985, 1.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(12.043330000000001, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-9.974060000000001, 1.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(10.043330000000001, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-9.974060000000001, 1.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(10.043330000000001, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-9.974060000000001, 0.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(10.043330000000001, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-9.974060000000001, 0.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(10.043330000000001, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            //Right Segment
            context.assemblePiece(transform,
                    vec3(17.239752887321657, 1.25, 8.362423243649886),
                    aabb(3.0, 2.0, 3.0000000000000018),
                    cull(0),
                    rotate(
                            pivot(29.842772887321658, 14.75, -9.95471019833139),
                            angle(0, -45, 0)
                    )
            );

            //Back Segment
            context.assemblePiece(transform,
                    vec3(8.025940451354984, 0.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(4.0085504513549814, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.025940451354984, 1.75, 12.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(4.0085504513549814, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.025940451354984, 0.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(4.0085504513549814, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.025940451354984, 1.75, 14.06072),
                    aabb(3.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(4.0085504513549814, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.025940451354984, 1.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(6.0085504513549814, 14.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.025940451354984, 1.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(6.0085504513549814, 14.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.025940451354984, 0.75, 14.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(6.0085504513549814, 13.75, 6.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(11.025940451354984, 0.75, 12.06072),
                    aabb(15.0, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(6.0085504513549814, 13.75, 4.017389999999999),
                            angle(0, 0, 0)
                    )
            );

            //Middle
            context.assemblePiece(transform,
                    vec3(6.52594, -1.6947434323650903, 1.2326495325112958),
                    aabb(1.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(7.043330000000001, 16.21731656763491, 8.691269532511297),
                            angle(22.5, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(7.5259399999999985, -1.6947434323650903, 1.2326495325112958),
                    aabb(2.0, 13.000000000000002, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.043330000000001, 16.21731656763491, 8.691269532511297),
                            angle(22.5, 0, 0)
                    )
            );

            //Back Segment
            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 26.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 25.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.5, 25.75, 12.31072),
                    aabb(2.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 25.75, 12.31072),
                    aabb(1.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 25.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 14.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 25.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(-2.4999999738502545, 26.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(7.224500026149748, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 25.75, 14.31072),
                    aabb(1.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.5, 25.75, 14.31072),
                    aabb(2.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 25.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 14.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 26.75, 14.31072),
                    aabb(9.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(9.499999973850251, 26.75, 12.31072),
                    aabb(9.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.775499973850252, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 26.75, 12.31072),
                    aabb(1.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(8.5, 26.75, 14.31072),
                    aabb(1.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(9.593330000000002, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.5, 26.75, 14.31072),
                    aabb(2.0000000000000036, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 15.75, 7.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(6.5, 26.75, 12.31072),
                    aabb(2.0000000000000036, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(8.593330000000002, 15.75, 5.267390000000001),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(2.5000000261497455, 25.75, 7.31072),
                    aabb(11.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(14.224500026149748, 14.75, 0.2673900000000007),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(2.5000000261497455, 25.75, 9.31072),
                    aabb(11.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(14.224500026149748, 14.75, 2.2673900000000007),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(2.5000000261497455, 26.75, 7.31072),
                    aabb(11.000000000000004, 1.0, 2.0),
                    cull(0),
                    rotate(
                            pivot(14.224500026149748, 15.75, 0.2673900000000007),
                            angle(0, 0, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(2.5000000261497455, 26.75, 9.31072),
                    aabb(11.000000000000004, 1.0, 1.0),
                    cull(0),
                    rotate(
                            pivot(14.224500026149748, 15.75, 2.2673900000000007),
                            angle(0, 0, 0)
                    )
            );

            //Right Segment
            context.assemblePiece(transform,
                    vec3(4.940343562373094, 26.75, 6.9390035623730935),
                    aabb(2.0, 1.5, 15.000000000000002),
                    cull(DOWN),
                    rotate(
                            pivot(9.457543562373095, 15.25, 7.431603562373095),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(3.52613, 26.75, 5.524789999999999),
                    aabb(2.0, 1.5, 15.0),
                    cull(DOWN | NORTH | EAST),
                    rotate(
                            pivot(8.043330000000001, 15.25, 6.017390000000001),
                            angle(0, -45, 0)
                    )
            );

            context.assemblePiece(transform,
                    vec3(4.940343562373094, 25.25, 6.9390035623730935),
                    aabb(2.0, 1.5, 15.000000000000002),
                    cull(UP | NORTH | WEST),
                    rotate(
                            pivot(9.457543562373095, 12.75, 7.431603562373095),
                            angle(0, -45, 0)
                    )
            );
        }
    }
}

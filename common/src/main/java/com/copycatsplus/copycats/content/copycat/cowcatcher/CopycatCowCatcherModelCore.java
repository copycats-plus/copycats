package com.copycatsplus.copycats.content.copycat.cowcatcher;

import com.copycatsplus.copycats.content.copycat.base.model.CopycatModelCore;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext;
import net.minecraft.world.level.block.state.BlockState;

import static com.copycatsplus.copycats.content.copycat.base.model.assembly.CopycatRenderContext.*;

public class CopycatCowCatcherModelCore extends CopycatModelCore {

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        AssemblyTransform transform = t -> t.rotateY(0);
        context.assemblePiece(transform,
                vec3(-3.326180001999518, -10.5, 17.746561915190497),
                aabb(2.9999999999999964, 2.0, 14.0),
                cull(0),
                rotate(
                        pivot(8.27683999800048, 3, 9.996521915190495),
                        angle(0, -45, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(17.54740188638617, -10.5, 7.427431555595112),
                aabb(2.9999999999999964, 2.0, 3.0),
                cull(0),
                rotate(
                        pivot(29.15042188638617, 3, -10.889701886386167),
                        angle(0, -45, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(17.54740188638617, -11.5, 7.427431555595112),
                aabb(2.9999999999999964, 1.0, 3.0),
                cull(0),
                rotate(
                        pivot(29.150421886386166, 3, -10.889701886386167),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(14.71897476163998, -11.5, 9.255858680341301),
                aabb(2.9999999999999964, 1.0, 3.0),
                cull(0),
                rotate(
                        pivot(26.321994761639978, 2, -8.061274761639977),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(19.37582867395757, -11.5, 10.255858343166514),
                aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                cull(0),
                rotate(
                        pivot(31.978848673957568, 2, -8.061275098814765),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(15.230133936099513, -3.0202558479777455, 15.06072),
                aabb(2.0, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(2.666943936099514, 15.979744152022253, 8.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(15.230133936099516, -3.0202558479777455, 17.06072),
                aabb(2.0, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(2.6669439360995177, 15.979744152022253, 10.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(15.006453233159084, -3.4625737862557466, 17.06072),
                aabb(2.0000000000000018, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(2.443263233159085, 11.293505279196683, 10.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(14.488069178913037, -3.56568676511203, 15.06072),
                aabb(2.0, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(1.924879178913038, 12.544995093475439, 8.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(20.36402114941208, -15.896886911008693, 17.06072),
                aabb(2.0, 13.5, 2.0),
                cull(0),
                rotate(
                        pivot(7.80083114941208, -1.6408078455562647, 10.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(19.845637140785453, -16, 15.06072),
                aabb(2.0, 13.5, 2.0),
                cull(0),
                rotate(
                        pivot(7.282447140785454, -0.38931814141253085, 8.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(20.58770198921077, -15.954569303135761, 15.06072),
                aabb(2.0, 14.0, 2.0),
                cull(0),
                rotate(
                        pivot(8.024511989210772, 3.045430696864237, 8.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(20.58770198921077, -15.954569303135761, 17.06072),
                aabb(2.0, 14.0, 2.0),
                cull(0),
                rotate(
                        pivot(8.024511989210772, 3.045430696864237, 10.017389999999999),
                        angle(0, 0, 22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(3.52613, 14.5, 9.52479),
                aabb(2.0, 2.0, 15.0),
                cull(0),
                rotate(
                        pivot(8.043330000000001, 3, 10.01739),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.525939974517824, -14.91206, 2.558769999999999),
                aabb(1.0, 16.0, 2.0),
                cull(0),
                rotate(
                        pivot(9.008549974517823, 3, 11.01739),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.525939974517824, -14.91206, 2.558769999999999),
                aabb(2.0, 16.0, 2.0),
                cull(0),
                rotate(
                        pivot(8.008549974517823, 3, 11.01739),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-6.375828673957567, -10.5, 10.255858343166514),
                aabb(2.9999999999999964, 2.0, 3.0000000000000018),
                cull(0),
                rotate(
                        pivot(-15.978848673957568, 3, -8.061275098814765),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-4.254508330397925, -11.5, 12.377178686726156),
                aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                cull(0),
                rotate(
                        pivot(-13.857528330397926, 2, -5.939954755255123),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.940343562373094, 14.5, 10.939003562373093),
                aabb(2.0, 2.0, 15.000000000000002),
                cull(0),
                rotate(
                        pivot(9.457543562373095, 3, 11.431603562373095),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(3.52613, 13.5, 9.52479),
                aabb(2.0, 1.0, 15.0),
                cull(0),
                rotate(
                        pivot(8.043330000000001, 1, 10.01739),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.940343562373094, 13.5, 10.939003562373093),
                aabb(2.0, 1.0, 15.000000000000002),
                cull(0),
                rotate(
                        pivot(9.457543562373095, 1, 11.431603562373095),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.47387, 13.5, 7.52479),
                aabb(2.0, 1.0, 1.9999999999999991),
                cull(0),
                rotate(
                        pivot(7.95667, 2, 10.01739),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.059656437626906, 14.5, 10.939003562373093),
                aabb(2.0, 2.0, 15.000000000000002),
                cull(0),
                rotate(
                        pivot(6.542456437626905, 3, 11.431603562373095),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(10.47387, 14.5, 9.52479),
                aabb(2.0, 2.0, 15.0),
                cull(0),
                rotate(
                        pivot(7.956669999999999, 3, 10.01739),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(10.47387, 13.5, 9.52479),
                aabb(2.0, 1.0, 15.0),
                cull(0),
                rotate(
                        pivot(7.956669999999999, 1, 10.01739),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.059656437626906, 13.5, 10.939003562373093),
                aabb(2.0, 1.0, 15.000000000000002),
                cull(0),
                rotate(
                        pivot(6.542456437626905, 1, 11.431603562373095),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(7.9740600000000015, -11, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(3.956669999999999, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(7.9740600000000015, -10, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(3.956669999999999, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(7.9740600000000015, -11, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(3.956669999999999, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(7.9740600000000015, -10, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(3.956669999999999, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(10.974060000000001, -10, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(5.956669999999999, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(10.974060000000001, -10, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(5.956669999999999, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(10.974060000000001, -11, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(5.956669999999999, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(10.974060000000001, -11, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(5.956669999999999, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-5.735194059144945, -10.5, 17.999999999999996),
                aabb(2.999999999999993, 2.0, 14.000000000000004),
                cull(0),
                rotate(
                        pivot(5.867825940855051, 3, 16.64817665945521),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-5.735194059144945, -11.5, 17.999999999999996),
                aabb(2.999999999999993, 1.0, 14.000000000000004),
                cull(0),
                rotate(
                        pivot(5.867825940855051, 2, 16.64817665945521),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-3.326180001999518, -11.5, 17.746561915190497),
                aabb(2.9999999999999964, 1.0, 14.0),
                cull(0),
                rotate(
                        pivot(8.27683999800048, 2, 9.996521915190495),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-5.735194059144945, -10.5, 4.9999999999999964),
                aabb(2.999999999999993, 2.0, 13.000000000000004),
                cull(0),
                rotate(
                        pivot(5.867825940855051, 3, 16.64817665945521),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-5.735194059144945, -11.5, 4.9999999999999964),
                aabb(2.999999999999993, 1.0, 13.000000000000004),
                cull(0),
                rotate(
                        pivot(5.867825940855051, 2, 16.64817665945521),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-3.326180001999518, -11.5, 4.746561915190497),
                aabb(2.9999999999999964, 1.0, 13.0),
                cull(0),
                rotate(
                        pivot(8.27683999800048, 2, 9.996521915190495),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-3.326180001999518, -10.5, 4.746561915190497),
                aabb(2.9999999999999964, 2.0, 13.0),
                cull(0),
                rotate(
                        pivot(8.27683999800048, 3, 9.996521915190495),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.32618000199952, -10.5, 4.746561915190497),
                aabb(2.9999999999999964, 2.0, 13.0),
                cull(0),
                rotate(
                        pivot(7.723160001999521, 3, 9.996521915190495),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.32618000199952, -10.5, 17.746561915190497),
                aabb(2.9999999999999964, 2.0, 14.0),
                cull(0),
                rotate(
                        pivot(7.723160001999521, 3, 9.996521915190495),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.32618000199952, -11.5, 17.746561915190497),
                aabb(2.9999999999999964, 1.0, 14.0),
                cull(0),
                rotate(
                        pivot(7.723160001999521, 2, 9.996521915190495),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.32618000199952, -11.5, 4.746561915190497),
                aabb(2.9999999999999964, 1.0, 13.0),
                cull(0),
                rotate(
                        pivot(7.723160001999521, 2, 9.996521915190495),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(18.735194059144952, -11.5, 4.9999999999999964),
                aabb(2.999999999999993, 1.0, 13.000000000000004),
                cull(0),
                rotate(
                        pivot(10.13217405914495, 2, 16.64817665945521),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(18.735194059144952, -11.5, 17.999999999999996),
                aabb(2.999999999999993, 1.0, 14.000000000000004),
                cull(0),
                rotate(
                        pivot(10.13217405914495, 2, 16.64817665945521),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(18.735194059144952, -10.5, 17.999999999999996),
                aabb(2.999999999999993, 2.0, 14.000000000000004),
                cull(0),
                rotate(
                        pivot(10.13217405914495, 3, 16.64817665945521),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(18.735194059144952, -10.5, 4.9999999999999964),
                aabb(2.999999999999993, 2.0, 13.000000000000004),
                cull(0),
                rotate(
                        pivot(10.13217405914495, 3, 16.64817665945521),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.974059548645016, -11, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(11.991449548645019, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.974059548645016, -10, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(11.991449548645019, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.974059548645016, -11, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(11.991449548645019, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.974059548645016, -10, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(11.991449548645019, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-10.025940451354984, -10, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.991449548645019, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-10.025940451354984, -10, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.991449548645019, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-10.025940451354984, -11, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.991449548645019, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-10.025940451354984, -11, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.991449548645019, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-1.230133936099513, -3.0202558479777455, 15.06072),
                aabb(2.0, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(13.333056063900486, 15.979744152022253, 8.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(-1.2301339360995165, -3.0202558479777455, 17.06072),
                aabb(2.0, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(13.333056063900482, 15.979744152022253, 10.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(-1.0064532331590854, -3.4625737862557466, 17.06072),
                aabb(2.0000000000000018, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(13.556736766840915, 11.293505279196683, 10.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(-0.4880691789130367, -3.56568676511203, 15.06072),
                aabb(2.0, 13.0, 2.0),
                cull(0),
                rotate(
                        pivot(14.075120821086962, 12.544995093475439, 8.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(-6.364021149412078, -15.896886911008693, 17.06072),
                aabb(2.0, 13.5, 2.0),
                cull(0),
                rotate(
                        pivot(8.19916885058792, -1.6408078455562647, 10.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(-5.845637140785453, -16, 15.06072),
                aabb(2.0, 13.5, 2.0),
                cull(0),
                rotate(
                        pivot(8.717552859214546, -0.38931814141253085, 8.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(-6.587701989210771, -15.954569303135761, 15.06072),
                aabb(2.0, 14.0, 2.0),
                cull(0),
                rotate(
                        pivot(7.975488010789228, 3.045430696864237, 8.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(-6.587701989210771, -15.954569303135761, 17.06072),
                aabb(2.0, 14.0, 2.0),
                cull(0),
                rotate(
                        pivot(7.975488010789228, 3.045430696864237, 10.017389999999999),
                        angle(0, 0, -22.5)
                )
        );


        context.assemblePiece(transform,
                vec3(12.275939999999999, 0.45525656763491007, 9.232649532511285),
                aabb(2.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(12.793330000000001, 2.3673165676349104, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(12.275939999999999, 0.8379399999999997, 7.308769999999999),
                aabb(2.0, 11.0, 2.0),
                cull(0),
                rotate(
                        pivot(12.793330000000001, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.275939974517824, -15.16206, 7.308769999999999),
                aabb(2.0, 16.0, 2.0),
                cull(0),
                rotate(
                        pivot(12.758549974517823, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(13.275939974517824, -15.16206, 7.308769999999999),
                aabb(1.0000000000000018, 16.0, 2.0),
                cull(0),
                rotate(
                        pivot(13.758549974517823, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.275939999999999, 0.45525656763491185, 9.232649532511285),
                aabb(1.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(11.793330000000001, 2.367316567634912, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.275939999999999, 0.8379399999999997, 7.308769999999999),
                aabb(1.0, 11.0, 2.0),
                cull(0),
                rotate(
                        pivot(11.793330000000001, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.27594, -15.544743432365088, 9.232649532511285),
                aabb(1.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(11.793330000000001, 2.367316567634912, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(12.275939999999999, -15.544743432365088, 9.232649532511285),
                aabb(2.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(12.793330000000001, 2.367316567634912, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(17.27593999999997, 0.45525656763491007, 13.782649532511272),
                aabb(2.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(17.793329999999973, 2.3673165676349104, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(17.27593999999997, 0.8379399999999997, 11.85876999999999),
                aabb(2.0, 11.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(17.793329999999973, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.275939974517797, -15.16206, 11.85876999999999),
                aabb(2.0, 16.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(17.758549974517795, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(18.275939974517797, -15.16206, 11.85876999999999),
                aabb(1.0, 16.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(18.758549974517795, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.27593999999997, 0.45525656763491185, 13.782649532511272),
                aabb(1.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(16.793329999999973, 2.367316567634912, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.27593999999997, 0.8379399999999997, 11.85876999999999),
                aabb(1.0, 11.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(16.793329999999973, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(16.27593999999997, -15.544743432365088, 13.782649532511272),
                aabb(1.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(16.793329999999973, 2.367316567634912, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(17.27593999999997, -15.544743432365088, 13.782649532511272),
                aabb(2.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(17.793329999999973, 2.367316567634912, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(1.7240600000000015, 0.45525656763491007, 9.232649532511285),
                aabb(2.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(3.206669999999999, 2.3673165676349104, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(1.7240600000000015, 0.8379399999999997, 7.308769999999999),
                aabb(2.0, 11.0, 2.0),
                cull(0),
                rotate(
                        pivot(3.206669999999999, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(2.724060025482176, -15.16206, 7.308769999999999),
                aabb(2.0, 16.0, 2.0),
                cull(0),
                rotate(
                        pivot(3.2414500254821768, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(1.7240600254821743, -15.16206, 7.308769999999999),
                aabb(1.0000000000000018, 16.0, 2.0),
                cull(0),
                rotate(
                        pivot(2.2414500254821768, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(3.7240600000000015, 0.45525656763491185, 9.232649532511285),
                aabb(1.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(4.206669999999999, 2.367316567634912, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(3.7240600000000015, 0.8379399999999997, 7.308769999999999),
                aabb(1.0, 11.0, 2.0),
                cull(0),
                rotate(
                        pivot(4.206669999999999, 2.75, 15.767389999999999),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(3.7240599999999997, -15.544743432365088, 9.232649532511285),
                aabb(1.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(4.206669999999999, 2.367316567634912, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(1.7240600000000015, -15.544743432365088, 9.232649532511285),
                aabb(2.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(3.206669999999999, 2.367316567634912, 16.691269532511285),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-3.27593999999997, 0.45525656763491007, 13.782649532511272),
                aabb(2.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(-1.7933299999999726, 2.3673165676349104, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-3.27593999999997, 0.8379399999999997, 11.85876999999999),
                aabb(2.0, 11.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(-1.7933299999999726, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.2759399745177973, -15.16206, 11.85876999999999),
                aabb(2.0, 16.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(-1.7585499745177948, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-3.2759399745177973, -15.16206, 11.85876999999999),
                aabb(1.0, 16.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(-2.758549974517795, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-1.27593999999997, 0.45525656763491185, 13.782649532511272),
                aabb(1.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(-0.7933299999999726, 2.367316567634912, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-1.27593999999997, 0.8379399999999997, 11.85876999999999),
                aabb(1.0, 11.0, 1.9999999999999964),
                cull(0),
                rotate(
                        pivot(-0.7933299999999726, 2.75, 20.317390000000046),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-1.27593999999997, -15.544743432365088, 13.782649532511272),
                aabb(1.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(-0.7933299999999726, 2.367316567634912, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-3.27593999999997, -15.544743432365088, 13.782649532511272),
                aabb(2.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(-1.7933299999999726, 2.367316567634912, 21.241269532511332),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.5, 14, 16.31072),
                aabb(2.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.499999973850251, 14, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.499999973850251, 15, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.5, 15, 16.31072),
                aabb(2.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.5, 14, 18.31072),
                aabb(2.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.499999973850251, 14, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.499999973850251, 15, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.5, 15, 18.31072),
                aabb(2.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.5, 15, 18.31072),
                aabb(1.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.5, 14, 18.31072),
                aabb(1.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.5, 15, 16.31072),
                aabb(1.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.5, 14, 16.31072),
                aabb(1.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 14, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 15, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 14, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 15, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.52613, 14.5, 7.52479),
                aabb(2.0, 2.0, 1.9999999999999991),
                cull(0),
                rotate(
                        pivot(8.043330000000001, 3, 10.01739),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.474060000000001, 1.0879399999999997, 2.558769999999999),
                aabb(1.0, 11.0, 2.0),
                cull(0),
                rotate(
                        pivot(8.956669999999999, 3, 11.01739),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.474060000000001, 0.7052565676349118, 4.482649532511285),
                aabb(1.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(8.956669999999999, 2.617316567634912, 11.941269532511287),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-4.547401886386165, -11.5, 7.427431555595112),
                aabb(2.9999999999999964, 1.0, 3.0),
                cull(0),
                rotate(
                        pivot(-13.150421886386166, 3, -10.889701886386167),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-1.718974761639977, -11.5, 9.255858680341301),
                aabb(2.9999999999999964, 1.0, 3.0),
                cull(0),
                rotate(
                        pivot(-10.321994761639978, 2, -8.061274761639977),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-6.375828673957567, -11.5, 10.255858343166514),
                aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                cull(0),
                rotate(
                        pivot(-15.978848673957568, 2, -8.061275098814765),
                        angle(0, 45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(19.37582867395757, -10.5, 10.255858343166514),
                aabb(2.9999999999999964, 2.0, 3.0000000000000018),
                cull(0),
                rotate(
                        pivot(31.978848673957568, 3, -8.061275098814765),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(17.25450833039793, -11.5, 12.377178686726156),
                aabb(2.9999999999999964, 1.0, 3.0000000000000018),
                cull(0),
                rotate(
                        pivot(29.857528330397926, 2, -5.939954755255123),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.940343541299672, 14.5, 6.110576458700329),
                aabb(2.0, 2.0, 2.0),
                cull(0),
                rotate(
                        pivot(9.457543541299671, 3, 8.60317645870033),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.526129978926576, 14.5, 4.696362896327234),
                aabb(2.0, 2.0, 2.0),
                cull(0),
                rotate(
                        pivot(8.043329978926575, 3, 7.188962896327236),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.111916437626906, 14.5, 6.110576437626905),
                aabb(2.0, 2.0, 2.0),
                cull(0),
                rotate(
                        pivot(6.629116437626905, 3, 8.603176437626907),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.940343541299672, 13.5, 6.110576458700329),
                aabb(2.0, 1.0, 2.0),
                cull(0),
                rotate(
                        pivot(9.457543541299671, 2, 8.60317645870033),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.52613, 13.5, 7.52479),
                aabb(2.0, 1.0, 1.9999999999999991),
                cull(0),
                rotate(
                        pivot(8.043330000000001, 2, 10.01739),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(4.111916437626906, 13.5, 6.110576437626905),
                aabb(2.0, 1.0, 2.0),
                cull(0),
                rotate(
                        pivot(6.629116437626905, 2, 8.603176437626907),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.526129978926576, 13.5, 4.696362896327234),
                aabb(2.0, 1.0, 2.0),
                cull(0),
                rotate(
                        pivot(8.043329978926575, 2, 7.188962896327236),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.4740600000000015, 1.0879399999999997, 2.558769999999999),
                aabb(2.0, 11.0, 2.0),
                cull(0),
                rotate(
                        pivot(7.956669999999999, 3, 11.01739),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.4740600000000015, 0.7052565676349101, 4.482649532511285),
                aabb(2.0, 11.0, 1.0),
                cull(0),
                rotate(
                        pivot(7.956669999999999, 2.6173165676349104, 11.941269532511287),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.0259399999999985, -11, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(12.043330000000001, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.0259399999999985, -10, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(12.043330000000001, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.0259399999999985, -11, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(12.043330000000001, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(5.0259399999999985, -10, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(12.043330000000001, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-9.974060000000001, -10, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(10.043330000000001, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-9.974060000000001, -10, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(10.043330000000001, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-9.974060000000001, -11, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(10.043330000000001, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-9.974060000000001, -11, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(10.043330000000001, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(17.239752887321657, -10.5, 12.362423243649886),
                aabb(3.0, 2.0, 3.0000000000000018),
                cull(0),
                rotate(
                        pivot(29.842772887321658, 3, -5.954710198331392),
                        angle(0, -45, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.025940451354984, -11, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(4.0085504513549814, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.025940451354984, -10, 16.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(4.0085504513549814, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.025940451354984, -11, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(4.0085504513549814, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.025940451354984, -10, 18.06072),
                aabb(3.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(4.0085504513549814, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.025940451354984, -10, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(6.0085504513549814, 3, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.025940451354984, -10, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(6.0085504513549814, 3, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.025940451354984, -11, 18.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(6.0085504513549814, 2, 10.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(11.025940451354984, -11, 16.06072),
                aabb(15.0, 1, 2.0),
                cull(0),
                rotate(
                        pivot(6.0085504513549814, 2, 8.017389999999999),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.52594, -15.294743432365088, 4.482649532511285),
                aabb(1.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(7.043330000000001, 2.617316567634912, 11.941269532511287),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(7.5259399999999985, -15.294743432365088, 4.482649532511285),
                aabb(2.0, 16.0, 1.0),
                cull(0),
                rotate(
                        pivot(8.043330000000001, 2.617316567634912, 11.941269532511287),
                        angle(22.5, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 15, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 14, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.5, 14, 16.31072),
                aabb(2.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.5, 14, 16.31072),
                aabb(1.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.499999973850251, 14, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 3, 9.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 14, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(-2.4999999738502545, 15, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(7.224500026149748, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(8.5, 14, 18.31072),
                aabb(1.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(6.5, 14, 18.31072),
                aabb(2.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.499999973850251, 14, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 3, 11.26739),
                        angle(0, 0, 0)
                )
        );


        context.assemblePiece(transform,
                vec3(9.499999973850251, 15, 18.31072),
                aabb(9.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(9.499999973850251, 15, 16.31072),
                aabb(9.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.775499973850252, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8.5, 15, 16.31072),
                aabb(1.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(8.5, 15, 18.31072),
                aabb(1.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(9.593330000000002, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(6.5, 15, 18.31072),
                aabb(2.0000000000000036, 1, 1.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 4, 11.26739),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(6.5, 15, 16.31072),
                aabb(2.0000000000000036, 1, 2.0),
                cull(0),
                rotate(
                        pivot(8.593330000000002, 4, 9.26739),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(2.5000000261497455, 14, 11.31072),
                aabb(11.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(14.224500026149748, 3, 4.267390000000001),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(2.5000000261497455, 14, 13.31072),
                aabb(11.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(14.224500026149748, 3, 6.267390000000001),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(2.5000000261497455, 15, 11.31072),
                aabb(11.000000000000004, 1, 2.0),
                cull(0),
                rotate(
                        pivot(14.224500026149748, 4, 4.267390000000001),
                        angle(0, 0, 0)
                )
        );

        context.assemblePiece(transform,
                vec3(2.5000000261497455, 15, 13.31072),
                aabb(11.000000000000004, 1, 1.0),
                cull(0),
                rotate(
                        pivot(14.224500026149748, 4, 6.267390000000001),
                        angle(0, 0, 0)
                )
        );
    }
}

package com.copycatsplus.copycats.mixin.copycat;

import com.simibubi.create.content.decoration.copycat.CopycatModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Random;

@Mixin(value = CopycatModel.class, remap = false)
public interface CopycatModelAccessor {
    @Invoker
    List<BakedQuad> callGetCroppedQuads(BlockState state, Direction side, Random rand,
                                        BlockState material, IModelData wrappedData);
}

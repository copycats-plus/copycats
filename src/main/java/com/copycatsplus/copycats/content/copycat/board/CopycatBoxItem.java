package com.copycatsplus.copycats.content.copycat.board;

import com.copycatsplus.copycats.CCBlocks;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock.byDirection;

public class CopycatBoxItem extends BlockItem {

    public CopycatBoxItem(Properties builder) {
        super(CCBlocks.COPYCAT_BOARD.get(), builder);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "item.copycats.copycat_box";
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab pGroup, @NotNull NonNullList<ItemStack> pItems) {
    }

    @Override
    public void registerBlocks(@NotNull Map<Block, Item> map, @NotNull Item self) {
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, @NotNull Level world, Player player, @NotNull ItemStack stack, @NotNull BlockState state) {
        for (Direction direction : Iterate.directions) {
            state = state.setValue(byDirection(direction), true);
        }
        world.setBlockAndUpdate(pos, state);
        return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
    }

}

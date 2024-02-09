package com.copycatsplus.copycats.datagen;

import com.copycatsplus.copycats.content.copycat.board.CopycatBoardBlock;
import com.copycatsplus.copycats.content.copycat.bytes.CopycatByteBlock;
import com.simibubi.create.foundation.utility.Iterate;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class CCLootGen {
    public static void lootForLayers(RegistrateBlockLootTables lt, Block block) {
        lootForLayers(lt, block, BlockStateProperties.LAYERS);
    }

    public static void lootForLayers(RegistrateBlockLootTables lt, Block block, IntegerProperty property) {
        LootTable.Builder builder = LootTable.lootTable();
        for (int i = 1; i <= 8; i++) {
            builder.withPool(
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .when(ExplosionCondition.survivesExplosion())
                            .when(LootItemBlockStatePropertyCondition
                                    .hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, i)))
                            .add(LootItem.lootTableItem(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(i))))
            );
        }
        lt.add(block, builder);
    }

    public static void lootForBytes(RegistrateBlockLootTables lt, Block block) {
        LootTable.Builder builder = LootTable.lootTable();
        for (CopycatByteBlock.Byte bite : CopycatByteBlock.allBytes) {
            builder.withPool(
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .when(ExplosionCondition.survivesExplosion())
                            .when(LootItemBlockStatePropertyCondition
                                    .hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CopycatByteBlock.byByte(bite), true)))
                            .add(LootItem.lootTableItem(block))
            );
        }
        lt.add(block, builder);
    }

    public static void lootForDirections(RegistrateBlockLootTables lt, Block block) {
        LootTable.Builder builder = LootTable.lootTable();
        for (Direction direction : Iterate.directions) {
            builder.withPool(
                    LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .when(ExplosionCondition.survivesExplosion())
                            .when(LootItemBlockStatePropertyCondition
                                    .hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CopycatBoardBlock.byDirection(direction), true)))
                            .add(LootItem.lootTableItem(block))
            );
        }
        lt.add(block, builder);
    }
}

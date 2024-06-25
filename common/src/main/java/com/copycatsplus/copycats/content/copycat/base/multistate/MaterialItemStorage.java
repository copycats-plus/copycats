package com.copycatsplus.copycats.content.copycat.base.multistate;

import com.copycatsplus.copycats.utility.ItemUtils;
import com.copycatsplus.copycats.utility.NBTUtils;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MaterialItemStorage {

    private Map<String, MaterialItem> storage;
    private int maxStorage;

    private MaterialItemStorage(int maxStorage, Set<String> properties) {
        storage = new HashMap<>(maxStorage);
        this.maxStorage = maxStorage;
        for (String property : properties) {
            storage.put(property, new MaterialItem(AllBlocks.COPYCAT_BASE.getDefaultState(), ItemStack.EMPTY));
        }
    }

    public static MaterialItemStorage create(int maxStorage, Set<String> properties) {
        return new MaterialItemStorage(maxStorage, properties);
    }

    public void storeMaterialItem(String property, MaterialItem materialItem) {
        storage.put(property, materialItem);
    }

    public @Nullable MaterialItem getMaterialItem(String property) {
        return storage.get(property);
    }

    public Set<String> getAllProperties() {
        return storage.keySet();
    }

    public Set<BlockState> getAllMaterials() {
        return storage.values().stream().map(MaterialItem::material).collect(Collectors.toSet());
    }

    public List<ItemStack> getAllConsumedItems() {
        return storage.values().stream().map(MaterialItem::consumedItem).dropWhile(itemStack -> itemStack.equals(ItemStack.EMPTY)).collect(Collectors.toList());
    }

    public Map<String, BlockState> getMaterialMap() {
        return storage.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> s.getValue().material));
    }

    public boolean hasCustomMaterial(String property) {
        return !storage.get(property).material().is(AllBlocks.COPYCAT_BASE.get());
    }

    public void remapStorage(Function<String, String> keyMapper) {
        Map<String, MaterialItem> newStorage = new HashMap<>();
        storage.forEach((key, materialItem) -> newStorage.put(keyMapper.apply(key), materialItem));
        storage = newStorage;
    }

    public CompoundTag serialize() {
        CompoundTag root = new CompoundTag();
        storage.forEach((key, materialItem) -> root.put(key, materialItem.serialize()));
        return root;
    }

    public CompoundTag serializeSafe() {
        CompoundTag root = new CompoundTag();
        storage.forEach((key, materialItem) -> root.put(key, materialItem.serializeSafe()));
        return root;
    }

    public boolean deserialize(CompoundTag tag) {
        AtomicBoolean anyUpdated = new AtomicBoolean(false);
        tag.getAllKeys().forEach(key -> {
            MaterialItem newVersion = MaterialItem.deserialize(tag.getCompound(key));
            MaterialItem oldVersion = storage.put(key, newVersion);
            if (oldVersion != null &&
                    (newVersion.material() != oldVersion.material() || newVersion.enableCT() != oldVersion.enableCT()) &&
                    !anyUpdated.get()) {
                anyUpdated.set(true);
            }
        });
        return anyUpdated.get();
    }

    public static class MaterialItem {

        public static MaterialItem EMPTY = new MaterialItem(AllBlocks.COPYCAT_BASE.getDefaultState(), ItemStack.EMPTY);

        private BlockState material;
        private ItemStack consumedItem;
        private boolean enableCT;

        public MaterialItem(BlockState material, ItemStack consumedItem) {
            this(material, consumedItem, true);
        }

        public MaterialItem(BlockState material, ItemStack consumedItem, boolean enableCT) {
            this.material = material;
            this.consumedItem = consumedItem;
            this.enableCT = enableCT;
        }

        public CompoundTag serialize() {
            CompoundTag root = new CompoundTag();
            root.put("material", NbtUtils.writeBlockState(material));
            root.put("consumedItem", NBTUtils.serializeStack(consumedItem));
            root.putBoolean("enableCT", enableCT);
            return root;
        }

        public CompoundTag serializeSafe() {
            CompoundTag root = new CompoundTag();
            root.put("material", NbtUtils.writeBlockState(material));
            ItemStack stackEmpty = consumedItem.copy();
            stackEmpty.setTag(null);
            root.put("consumedItem", NBTUtils.serializeStack(stackEmpty));
            root.putBoolean("enableCT", enableCT);
            return root;
        }

        public static MaterialItem deserialize(CompoundTag tag) {
            return new MaterialItem(
                    NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("material")),
                    ItemStack.of(tag.getCompound("consumedItem")),
                    !tag.contains("enableCT") || tag.getBoolean("enableCT")
            );
        }

        public BlockState material() {
            return material;
        }

        public ItemStack consumedItem() {
            return consumedItem;
        }

        public boolean enableCT() {
            return enableCT;
        }

        public void setMaterial(BlockState material) {
            this.material = material;
        }

        public void setConsumedItem(ItemStack stack) {
            consumedItem = ItemUtils.copyStackWithSize(stack, 1);
        }

        public void setEnableCT(boolean enableCT) {
            this.enableCT = enableCT;
        }
    }

}

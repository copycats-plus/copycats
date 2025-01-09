package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.copycatsplus.copycats.utility.ItemUtils;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stores copycat data, including material, consumed item and CT toggle, for each part of a multi-state copycat.
 */
public class MaterialItemStorage {

    private Map<String, MaterialItem> storage;

    private MaterialItemStorage(Set<String> properties) {
        storage = new HashMap<>(properties.size());
        for (String property : properties) {
            storage.put(property, new MaterialItem(AllBlocks.COPYCAT_BASE.getDefaultState(), ItemStack.EMPTY));
        }
    }

    public static MaterialItemStorage create(Set<String> properties) {
        return new MaterialItemStorage(properties);
    }

    /**
     * Replace the material item for a specific property.
     */
    public void storeMaterialItem(String property, MaterialItem materialItem) {
        storage.put(property, materialItem);
    }

    /**
     * Get the material item for a specific property.
     */
    public @Nullable MaterialItem getMaterialItem(String property) {
        return storage.get(property);
    }

    /**
     * Get all properties stored in this storage, regardless of whether they have a custom material.
     */
    public Set<String> getAllProperties() {
        return storage.keySet();
    }

    /**
     * Get all materials stored in this storage, regardless of whether they have a custom material or whether they exist according to the copycat's block state.
     */
    public Set<BlockState> getAllMaterials() {
        return storage.values().stream().map(MaterialItem::material).collect(Collectors.toSet());
    }

    /**
     * Get all material items stored in this storage, regardless of whether they have a custom material or whether they exist according to the copycat's block state.
     */
    public Set<MaterialItem> getAllMaterialItems() {
        return new HashSet<>(storage.values());
    }

    /**
     * Get all consumed items stored in this storage. Empty stacks are not included.
     */
    public List<ItemStack> getAllConsumedItems() {
        return storage.values().stream().map(MaterialItem::consumedItem).filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    /**
     * Get a map of all properties and their corresponding materials stored in this storage.
     */
    public Map<String, BlockState> getMaterialMap() {
        return storage.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> s.getValue().material));
    }

    /**
     * Check if a specific property has a custom material.
     */
    public boolean hasCustomMaterial(String property) {
        return storage.get(property).hasCustomMaterial();
    }

    /**
     * Modify the keys of all stored materials using the provided key mapper.
     *
     * @param keyMapper A function which accepts a key and returns a new key where the corresponding material should be stored.
     */
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

    /**
     * Stores copycat data for a single part of a multi-state copycat.
     */
    public static class MaterialItem {

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
            root.put("consumedItem", ItemUtils.serializeNBT(consumedItem));
            root.putBoolean("enableCT", enableCT);
            return root;
        }

        public CompoundTag serializeSafe() {
            CompoundTag root = new CompoundTag();
            root.put("material", NbtUtils.writeBlockState(material));
            ItemStack stackEmpty = consumedItem.copy();
            stackEmpty.setTag(null);
            root.put("consumedItem", ItemUtils.serializeNBT(stackEmpty));
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

        public boolean hasCustomMaterial() {
            return !material.is(AllBlocks.COPYCAT_BASE.get());
        }
    }

}

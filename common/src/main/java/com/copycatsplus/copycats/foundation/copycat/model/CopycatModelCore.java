package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.config.CCConfigs;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatModelPart;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.utility.BlockUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Function;

/**
 * Block-specific but platform-independent model generation logic for copycats.
 */
public abstract class CopycatModelCore implements CopycatModelPart {

    protected static final ModelEntry SUPER = new ModelEntry("super", null, null, EntryType.STATIC);

    /**
     * Model key for the copied material in simple copycats.
     */
    public static final String MATERIAL_KEY = "material";
    protected final ModelEntry MATERIAL = new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, EntryType.COPYCAT);

    /**
     * Whether this model core should render enhanced models.
     */
    public boolean enhanced = true;

    /**
     * Whether to colorize copycat base models.
     */
    public boolean colorize = false;

    /**
     * Register all models required to render this copycat.
     * <p>
     * By default, the only required model is the one of the copied material.
     * This should be sufficient for most simple copycats. Only override this method if the copycat is multi-state or
     * if extra models are required.
     *
     * @param entries The list to register the models to.
     */
    public void registerModels(List<ModelEntry> entries) {
        entries.add(MATERIAL);
    }

    /**
     * Helper method to register models for all copied materials in a multi-state copycat.
     * <p>
     * When implementing a model core for multi-state copycats, override {@link CopycatModelCore#registerModels}
     * and call this method without calling super.
     *
     * @param entries   The list to register the models to.
     * @param block     The multi-state copycat that this model core is meant for.
     * @param isKinetic Whether the copycat is kinetic.
     */
    protected final void registerForMultiState(List<ModelEntry> entries, IMultiStateCopycatBlock block, boolean isKinetic) {
        for (String property : block.storageProperties()) {
            registerMultiStatePart(entries, property, isKinetic);
        }
    }

    /**
     * Helper method to register a model for one part of a multi-state copycat.
     * <p>
     * When implementing a model core for a kinetic multi-state copycat, override {@link CopycatModelCore#registerModels}
     * and call this method for the specific part of the copycat that this model core is meant for.
     *
     * @param entries   The list to register the models to.
     * @param property  The storage property of the copycat that this model core is meant for.
     * @param isKinetic Whether the copycat is kinetic.
     */
    protected final void registerMultiStatePart(List<ModelEntry> entries, String property, boolean isKinetic) {
        entries.add(new ModelEntry(property, (state, mat) -> getModelOf(mat), this, isKinetic ? EntryType.KINETIC_COPYCAT : EntryType.COPYCAT));
    }

    /**
     * Called before rendering to gather external data, such as configs, and prepare the model core for rendering.
     * <p>
     * This method is likely to be called on the render thread and might be called multiple times in each render.
     */
    public void prepareForRender() {
        enhanced = CCConfigs.client().useEnhancedModels.get();
        colorize = CCConfigs.client().colorizeMultiStates.get();
    }

    /**
     * For a default implementation of {@link CopycatModelCore#registerModels}, this method is called to assemble quads
     * from the copied material, with the key being equal to {@link CopycatModelCore#MATERIAL_KEY}.
     * <p>
     * For multi-state copycats using {@link CopycatModelCore#registerForMultiState}, this method is called for each
     * storage property of the copycat.
     *
     * @param key      The key of the model provided by the context. For simple copycats, the key of the copied material is {@link CopycatModelCore#MATERIAL_KEY}. For multi-state copycats, the keys come from the storage properties of the copycat.
     * @param state    The block state of the copycat.
     * @param context  The context to assemble the quads with.
     * @param material The block state of the copied material. This matches the key provided for both simple and multi-state copycats.
     */
    @Override
    public abstract void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material);

    /**
     * Helper method to get the model of a block state.
     *
     * @param state The block state to get the model of.
     * @return The baked model of the block state.
     */
    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(state);
    }

    /**
     * Helper method to copy block state properties from the copycat to the material.
     * This is useful for copycats that allow blocks similar to themselves to be used as materials.
     */
    public static MaterialMapper updatePropertiesIfMatch(Class<?> clazz) {
        return (state, mat) -> {
            if (mat == null)
                return null;
            if (clazz.isInstance(mat.getBlock())) {
                return BlockUtils.tryCopyProperties(state, mat);
            }
            return mat;
        };
    }

    /**
     * Create a platform-specific {@link BakedModel} implementation for a copycat which wraps the original model and
     * renders with the provided core.
     *
     * @param original The original model to wrap.
     * @param core     The core to render the model with.
     */
    @ExpectPlatform
    @NotNull
    public static BakedModel createModel(BakedModel original, CopycatModelCore core) {
        //noinspection DataFlowIssue
        return null;
    }

    /**
     * Create a platform-specific {@link BakedModel} implementation for a copycat which wraps the original model and
     * renders with the provided core.
     * <p>
     * The model is configured for kinetic rendering by disabling ambient occlusion and applying a model filter
     * to selectively render parts of a multi-state copycat.
     *
     * @param original The original model to wrap.
     * @param core     The core to render the model with.
     */
    @ExpectPlatform
    @NotNull
    public static BakedModel createKineticModel(BakedModel original, CopycatModelCore core) {
        //noinspection DataFlowIssue
        return null;
    }

    /**
     * A core that renders the original model and additional kinetic models when needed.
     * <p>
     * Should be used for copycats with purely kinetic parts.
     */
    public static CopycatModelCore kinetic(CopycatModelCore... cores) {
        return new CopycatModelCore() {
            @Override
            public void registerModels(List<ModelEntry> entries) {
                for (CopycatModelCore core : cores) {
                    core.registerModels(entries);
                }
                entries.add(SUPER);
            }

            @Override
            public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
            }
        };
    }

    /**
     * A model core that requires extra data for rendering. The stored data is thread-local and should not be retained between renders.
     *
     * @param <T> The type of data required for rendering.
     */
    public static abstract class WithData<T> extends CopycatModelCore {
        private final ThreadLocal<T> data = new ThreadLocal<>();

        /**
         * Set the data required for rendering. This is intended to be called around the same time as
         * {@link CopycatModelCore#prepareForRender}, which guarantees that the data is set before model getters are invoked.
         */
        public void setData(T data) {
            this.data.set(data);
        }

        /**
         * Get the data required for rendering. The data is thread-local and should not be retained between renders.
         */
        public T getData() {
            return this.data.get();
        }
    }

    /**
     * A model entry to be rendered by a {@link CopycatModelCore}.
     *
     * @param key            A custom key to identify the model entry during rendering.
     * @param model          A getter that returns a {@link BakedModel} to be rendered for this entry, invoked for each render. Set to null to render the original model as specified by the copycat's block state file.
     * @param part           A {@link CopycatModelPart} to assemble the model quads with. Set to null if the model should be renderekd without modifications.
     * @param materialMapper A function to modify the block state of the material at render time. Use {@link MaterialMapper#IDENTITY} if no modifications are needed.
     * @param type           The type of the model entry, which determines how the model is rendered.
     */
    @ParametersAreNonnullByDefault
    public record ModelEntry(String key, @Nullable ModelGetter model, @Nullable CopycatModelPart part,
                             MaterialMapper materialMapper,
                             EntryType type) {
        public ModelEntry(String key, @Nullable ModelGetter model, @Nullable CopycatModelPart part, EntryType type) {
            this(key, model, part, MaterialMapper.IDENTITY, type);
        }
    }

    public enum EntryType {
        /**
         * A static model that is rendered into the terrain mesh without needing the copycat material.
         */
        STATIC(false, false),
        /**
         * A copycat model that requires copycat material and is rendered into the terrain mesh.
         */
        COPYCAT(true, false),
        /**
         * A static model that is rendered into virtual environments, such as placement helpers or kinetic renderers.
         */
        KINETIC(false, true),
        /**
         * A copycat model that requires copycat material and is rendered into virtual environments.
         */
        KINETIC_COPYCAT(true, true);

        private final boolean useCopycatLogic;
        private final boolean onlyWhenVirtual;

        EntryType(boolean useCopycatLogic, boolean onlyWhenVirtual) {
            this.useCopycatLogic = useCopycatLogic;
            this.onlyWhenVirtual = onlyWhenVirtual;
        }

        /**
         * Specifies that this model requires a copycat material and should use it for occlusion, culling and connected textures instead of the copycat's own block state.
         */
        public boolean useCopycatLogic() {
            return useCopycatLogic;
        }

        /**
         * Specifies that this model should only be rendered when the rendering environment is virtual, such as in placement helpers or kinetic renderers.
         */
        public boolean onlyWhenVirtual() {
            return onlyWhenVirtual;
        }
    }

    /**
     * A functional interface to get the {@link BakedModel} when rendering a {@link ModelEntry}. For model cores with
     * extra data, the data should be set before invoking this getter, so that the getter is safe to access the data.
     */
    @FunctionalInterface
    public interface ModelGetter {
        ModelGetter MATERIAL = (state, material) -> getModelOf(material);

        /**
         * Get the model for the given block state and material.
         *
         * @param state    The block state of the copycat.
         * @param material The block state of the copied material.
         */
        BakedModel getModel(BlockState state, BlockState material);
    }

    /**
     * A functional interface to map the block state of the material at render time.
     */
    @FunctionalInterface
    public interface MaterialMapper {
        MaterialMapper IDENTITY = (state, material) -> material;

        /**
         * Modifies the block state of the material at render time. Use {@link MaterialMapper#IDENTITY} if no modifications are needed.
         *
         * @param state    The block state of the copycat.
         * @param material The block state of the copied material.
         */
        BlockState map(BlockState state, BlockState material);
    }
}

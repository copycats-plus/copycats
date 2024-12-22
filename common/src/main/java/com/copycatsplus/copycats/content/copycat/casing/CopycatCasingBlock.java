package com.copycatsplus.copycats.content.copycat.casing;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.MultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import com.jozufozu.flywheel.util.Lazy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatCasingBlock extends CasingBlock implements IMultiStateCopycatBlock, IBE<MultiStateCopycatBlockEntity> {

    public static final Lazy<Map<Block, Block>> ACCEPTED_CASINGS = Lazy.of(() -> Map.of(
            AllBlocks.ANDESITE_CASING.get(), CCBlocks.WRAPPED_ANDESITE_CASING.get(),
            AllBlocks.BRASS_CASING.get(), CCBlocks.WRAPPED_BRASS_CASING.get(),
            AllBlocks.COPPER_CASING.get(), CCBlocks.WRAPPED_COPPER_CASING.get(),
            AllBlocks.RAILWAY_CASING.get(), CCBlocks.WRAPPED_RAILWAY_CASING.get(),
            AllBlocks.REFINED_RADIANCE_CASING.get(), CCBlocks.WRAPPED_REFINED_RADIANCE_CASING.get(),
            AllBlocks.SHADOW_STEEL_CASING.get(), CCBlocks.WRAPPED_SHADOW_STEEL_CASING.get()
    ));

    // create a reverse lookup of ACCEPTED_CASINGS
    public static final Lazy<Map<Block, Block>> REVERSE_ACCEPTED_CASINGS = Lazy.of(() -> ACCEPTED_CASINGS.get().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));

    public CopycatCasingBlock(Properties properties) {
        super(properties);
    }

    @javax.annotation.Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<S> p_153214_) {
        return null;
    }

    @Override
    public String defaultProperty() {
        return Part.OUTER.getSerializedName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(Part.OUTER.getSerializedName(), Part.INNER.getSerializedName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(Part.INNER.getSerializedName()) ? 1 : 0;
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        return true;
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return new Vec3i(0, 0, 0);
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        int edgeCount = 0;
        for (Direction.Axis axis : Iterate.axes) {
            if (Math.abs(unscaledHit.get(axis) - 0.5) > 6 / 16.0) {
                edgeCount++;
            }
        }
        return edgeCount > 1 ? Part.OUTER.getSerializedName() : Part.INNER.getSerializedName();
    }

    @Override
    public String getPropertyFromRender(String renderingProperty, BlockState state, BlockGetter level, Vec3i vector, BlockPos blockPos) {
        return renderingProperty;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(
                () -> IMultiStateCopycatBlock.super.onWrenched(state, context),
                () -> super.onWrenched(state, context)
        );
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return InteractionUtils.sequential(
                () -> IMultiStateCopycatBlock.super.onSneakWrenched(state, context),
                () -> super.onSneakWrenched(state, context)
        );
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionUtils.sequential(
                () -> IMultiStateCopycatBlock.super.use(state, level, pos, player, hand, hit),
                () -> super.use(state, level, pos, player, hand, hit)
        );
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        IMultiStateCopycatBlock.super.setPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        IMultiStateCopycatBlock.super.onRemove(state, world, pos, newState, isMoving, super::onRemove);
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
        IMultiStateCopycatBlock.super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {
        // no-op
    }

    @Override
    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos, BlockState toState) {
        return false;
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        return true;
    }

    @Override
    public Optional<Boolean> shapeCanOccludeNeighbor(BlockGetter level, BlockPos pos, BlockState state, BlockPos neighborPos, Direction dir) {
        return Optional.of(true);
    }

    @Override
    public Class<MultiStateCopycatBlockEntity> getBlockEntityClass() {
        return MultiStateCopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MultiStateCopycatBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.MULTI_STATE_COPYCAT.get();
    }

    public enum Part implements StringRepresentable {
        INNER, OUTER;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}

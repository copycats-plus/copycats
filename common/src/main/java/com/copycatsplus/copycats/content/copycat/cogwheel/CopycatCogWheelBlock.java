package com.copycatsplus.copycats.content.copycat.cogwheel;

import com.copycatsplus.copycats.CCBlockEntityTypes;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlock;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.utility.InteractionUtils;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.decoration.bracket.BracketBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Locale;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatCogWheelBlock extends CogWheelBlock implements IMultiStateCopycatBlock {
    protected CopycatCogWheelBlock(boolean large, Properties properties) {
        super(large, properties);
    }

    public static CopycatCogWheelBlock small(Properties properties) {
        return new CopycatCogWheelBlock(false, properties);
    }

    public static CopycatCogWheelBlock large(Properties properties) {
        return new CopycatCogWheelBlock(true, properties);
    }

    @Override
    public String defaultProperty() {
        return Part.COGWHEEL.getSerializedName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return new Vec3i(1, 1, 1);
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(Part.COGWHEEL.getSerializedName(), Part.SHAFT.getSerializedName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(Part.COGWHEEL.getSerializedName()) ? 1 : 0;
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
        Axis axis = state.getValue(AXIS);
        double offset = unscaledHit.get(axis);
        if (offset > 6 / 16.0 && offset < 10 / 16.0)
            return Part.COGWHEEL.getSerializedName();
        return Part.SHAFT.getSerializedName();
    }

    @Override
    public boolean canToggleCT(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        return false;
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

    @Nullable
    @Override
    public BlockState getAcceptedBlockState(String property, Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (item.getItem() instanceof BlockItem bi) {
            if (bi.getBlock() instanceof BracketBlock) return null;
            if (bi.getBlock() instanceof ShaftBlock && !(bi.getBlock() instanceof ICopycatBlock))
                return property.equals(Part.SHAFT.getSerializedName()) ? bi.getBlock().defaultBlockState() : null;
            if (bi.getBlock() instanceof CogWheelBlock cogwheelBlock && !(bi.getBlock() instanceof ICopycatBlock))
                return property.equals(Part.COGWHEEL.getSerializedName()) && cogwheelBlock.isLargeCog() == this.isLargeCog() ? bi.getBlock().defaultBlockState() : null;
        }

        return IMultiStateCopycatBlock.super.getAcceptedBlockState(pLevel, pPos, item, face);
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
        return true;
    }

    @Override
    public boolean canConnectTexturesToward(String property, BlockAndTintGetter reader, BlockPos fromPos, BlockPos toPos, BlockState state) {
        if (property.equals(Part.COGWHEEL.getSerializedName()))
            return false;
        Vec3i diff = toPos.subtract(fromPos);
        Direction face = Direction.fromDelta(diff.getX(), diff.getY(), diff.getZ());
        if (face == null) return false;
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public BlockEntityType<? extends CopycatCogWheelBlockEntity> getBlockEntityType() {
        return CCBlockEntityTypes.COPYCAT_COGWHEEL.get();
    }

    public enum Part implements StringRepresentable {
        SHAFT, COGWHEEL;

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}

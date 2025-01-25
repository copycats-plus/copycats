package com.copycatsplus.copycats.content.copycat.half_stairs;

import com.copycatsplus.copycats.CCBlockStateProperties;
import com.copycatsplus.copycats.CCBlockStateProperties.SideType;
import com.copycatsplus.copycats.foundation.copycat.multistate.IMultiStateCopycatBlockEntity;
import com.copycatsplus.copycats.foundation.copycat.multistate.WaterloggedMultiStateCopycatBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import java.util.Set;

public class CopycatHalfStairsBlock extends WaterloggedMultiStateCopycatBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<SideType> SIDE_TYPE = CCBlockStateProperties.SIDE_TYPE;

    public CopycatHalfStairsBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(SIDE_TYPE, SideType.LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, SIDE_TYPE));
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return state;
    }

    @Override
    public String defaultProperty() {
        return SideType.LEFT.getSerializedName();
    }

    @Override
    public Vec3i vectorScale(BlockState state) {
        return null;
    }

    @Override
    public Set<String> storageProperties() {
        return Set.of(SideType.LEFT.getSerializedName(), SideType.RIGHT.getSerializedName());
    }

    @Override
    public int getColorIndex(String property) {
        return property.equals(SideType.LEFT.getSerializedName()) ? 0 : 1;
    }

    @Override
    public boolean partExists(BlockState state, String property) {
        SideType sideType = state.getValue(SIDE_TYPE);
        if (property.equals(SideType.LEFT.getSerializedName())) {
            return sideType == SideType.BOTH || sideType == SideType.LEFT;
        } else if (property.equals(SideType.RIGHT.getSerializedName())) {
            return sideType == SideType.BOTH || sideType == SideType.RIGHT;
        }
        return false;
    }

    @Override
    public Vec3i getVectorFromProperty(BlockState state, String property) {
        return null;
    }

    @Override
    public String getPropertyFromInteraction(BlockState state, BlockGetter level, Vec3i hitLocation, BlockPos blockPos, Direction facing, Vec3 unscaledHit) {
        return "";
    }

    @Override
    public void transformStorage(BlockState state, IMultiStateCopycatBlockEntity be, StructureTransform transform) {

    }
}

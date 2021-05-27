package io.github.projectet.dmlSimulacrum.block;

import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SimulationChamber extends HorizontalFacingBlock implements BlockEntityProvider {

    BlockState state;

    public SimulationChamber(Settings settings) {
        super(settings);
        state = stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new SimulationChamberEntity();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return state.with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        super.onUse(state, world, pos, player, hand, hit);
        player.sendMessage(new LiteralText(String.valueOf(((SimulationChamberEntity) world.getBlockEntity(pos)).getEnergy())), true);
        return ActionResult.SUCCESS;
    }
}

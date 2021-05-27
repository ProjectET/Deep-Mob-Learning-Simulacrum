package io.github.projectet.dmlSimulacrum.block;

import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SimulationChamber extends Block implements BlockEntityProvider {
    public SimulationChamber(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new SimulationChamberEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        super.onUse(state, world, pos, player, hand, hit);
        player.sendMessage(new LiteralText(String.valueOf(((SimulationChamberEntity) world.getBlockEntity(pos)).getEnergy())), true);
        return ActionResult.SUCCESS;
    }
}

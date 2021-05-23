package io.github.projectet.dmlSimulacrum.block;

import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
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


}

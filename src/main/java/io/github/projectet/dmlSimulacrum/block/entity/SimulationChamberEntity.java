package io.github.projectet.dmlSimulacrum.block.entity;

import dev.nathanpb.dml.DataModel;
import dev.technici4n.fasttransferlib.api.Simulation;
import dev.technici4n.fasttransferlib.api.energy.EnergyIo;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class SimulationChamberEntity extends BlockEntity implements EnergyIo, Tickable, InventoryProvider {

    private Double energyAmount = 0.0;

    public SimulationChamberEntity(BlockEntityType<?> type) {
        super(type);
    }

    public SimulationChamberEntity() {
        this(dmlSimulacrum.SIMULATION_CHAMBER_ENTITY);
    }

    @Override
    public double getEnergy() {
        if (energyAmount != null) return energyAmount;
        else return 0.0;
    }

    @Override
    public double getEnergyCapacity() {
        return 2000000;
    }

    @Override
    public boolean supportsInsertion() {
        return true;
    }

    @Override
    public double insert(double amount, Simulation simulation) {

        return EnergyIo.super.insert(amount, simulation);
    }

    @Override
    public boolean supportsExtraction() {
        return false;
    }

    @Override
    public void tick() {
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return null;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        energyAmount = tag.getDouble("energy");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("energy", energyAmount);
        return tag;
    }

    /*private void updateSimulationText(ItemStack stack) {
        String[] lines = new String[]{
                "> Launching runtime",
                "v1.4.7",
                "> Iteration #" + (DataModel.getTotalSimulationCount(stack) + 1) + " started",
                "> Loading model from chip memory",
                "> Assessing threat level",
                "> Engaged enemy",
                "> Pristine procurement",
                byproductSuccess ? "succeeded" : "failed",
                "> Processing results",
                "..."
        };

    }*/
}

package io.github.projectet.dmlSimulacrum.block.entity;

import dev.technici4n.fasttransferlib.api.Simulation;
import dev.technici4n.fasttransferlib.api.energy.EnergyIo;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.util.Animation;
import io.github.projectet.dmlSimulacrum.util.DataModelUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SimulationChamberEntity extends BlockEntity implements EnergyIo, Tickable, InventoryProvider, NamedScreenHandlerFactory, PropertyDelegateHolder {

    private Double energyAmount = 0.0;
    private boolean isCrafting = false;
    private boolean byproductSuccess = false;
    private int ticks = 0;

    private HashMap<String, String> simulationText = new HashMap<>();
    private HashMap<String, Animation> simulationAnimation = new HashMap<>();

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
        return 2000000.0;
    }

    @Override
    public boolean supportsInsertion() {
        return true;
    }

    @Override
    public double insert(double amount, Simulation simulation) {
        double inserted = energyAmount + amount;
        if(inserted > getEnergyCapacity()) {
            if (!simulation.isSimulating()) {
                energyAmount = getEnergyCapacity();
            }
            return inserted - getEnergyCapacity();
        }
        else {
            if (!simulation.isSimulating()) {
                energyAmount += amount;
            }
            return 0.0;
        }
    }

    @Override
    public boolean supportsExtraction() {
        return false;
    }

    @Override
    public void tick() {
        if(!world.isClient) {
            ticks++;
        }
    }

    @Override
    public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
        return null;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        energyAmount = tag.getDouble("energy");
        byproductSuccess = tag.getBoolean("byproductSuccess");
        isCrafting = tag.getBoolean("isCrafting");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("energy", energyAmount);
        tag.putBoolean("byproductSuccess", byproductSuccess);
        tag.putBoolean("isCrafting", isCrafting);
        return tag;
    }

    private void updateSimulationText(ItemStack stack) {
        String[] lines = new String[]{
                "> Launching runtime",
                "v1.4.7",
                "> Iteration #" + (DataModelUtil.getSimulationCount(stack) + 1) + " started",
                "> Loading model from chip memory",
                "> Assessing threat level",
                "> Engaged enemy",
                "> Pristine procurement",
                byproductSuccess ? "succeeded" : "failed",
                "> Processing results",
                "..."
        };

    }

    @Override
    public Text getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }

    @Override
    public PropertyDelegate getPropertyDelegate() {
        return null;
    }
}

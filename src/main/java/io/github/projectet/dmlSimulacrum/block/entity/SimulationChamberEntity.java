package io.github.projectet.dmlSimulacrum.block.entity;

import dev.technici4n.fasttransferlib.api.Simulation;
import dev.technici4n.fasttransferlib.api.energy.EnergyIo;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreenHandler;
import io.github.projectet.dmlSimulacrum.inventory.ImplementedInventory;
import io.github.projectet.dmlSimulacrum.util.Animation;
import io.github.projectet.dmlSimulacrum.util.DataModelUtil;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class SimulationChamberEntity extends BlockEntity implements EnergyIo, Tickable, ImplementedInventory, ExtendedScreenHandlerFactory, BlockEntityClientSerializable {

    private Double energyAmount = 0.0;
    private boolean isCrafting = false;
    private boolean byproductSuccess = false;
    public int ticks = 0;
    public int percentDone = 0;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

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
            if(ticks % 5 == 0) {
                sync();
            }
        }
    }

    public void updateState() {
        BlockState state = world.getBlockState(getPos());
        world.updateListeners(getPos(), state, state, 3);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        energyAmount = tag.getDouble("energy");
        byproductSuccess = tag.getBoolean("byproductSuccess");
        isCrafting = tag.getBoolean("isCrafting");
        Inventories.fromTag(tag, inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("energy", energyAmount);
        tag.putBoolean("byproductSuccess", byproductSuccess);
        tag.putBoolean("isCrafting", isCrafting);
        Inventories.toTag(tag, inventory);
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

/*    private void startSimulation() {
        isCrafting = true;
        currentDataModelType = DataModel.getMobMetaData(getDataModel()).getKey();
        mobMetaData = MobMetaFactory.createMobMetaData(currentDataModelType);
        ItemStack oldInput = getPolymerClay();
        ItemStack newInput = new ItemStack(Registry.polymerClay, oldInput.getCount() - 1);
        polymer.setStackInSlot(0, newInput);
        resetAnimations();
    }

    private void finishSimulation(boolean abort) {
        resetAnimations();
        percentDone = 0;
        isCrafting = false;
        // Only decrease input and increase output if not aborted, and only if on the server's TE
        if(!abort && !world.isRemote) {
            DataModel.increaseSimulationCount(getDataModel());

            ItemStack oldOutput = getLiving();
            ItemStack newOutput = mobMetaData.getLivingMatterStack(oldOutput.getCount() + 1);
            lOutput.setStackInSlot(0, newOutput);

            if(byproductSuccess) {
                // If Byproduct roll was successful
                byproductSuccess = false;
                ItemStack oldPristine = getPristine();
                ItemStack newPristine = mobMetaData.getPristineMatterStack(oldPristine.getCount() + 1);

                pOutput.setStackInSlot(0, newPristine);
            }

            updateState();
        }
    }

    private boolean canStartSimulation() {
        return hasEnergyForSimulation() && canContinueSimulation() && !outputIsFull() && !pristineIsFull() && hasPolymerClay();
    }

    private boolean canContinueSimulation() {
        return hasDataModel() && DataModel.getTier(getDataModel()) != 0;
    }

    public boolean hasEnergyForSimulation() {
        if(hasDataModel()) {
            int ticksPerSimulation = 300;
            return getEnergy() > (ticksPerSimulation * DataModel.getSimulationTickCost(getDataModel()));
        } else {
            return false;
        }
    }*/

    @Override
    public Text getDisplayName() {
        return new LiteralText("");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SimulationChamberScreenHandler(syncId, inv, this, this);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(getPos());
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(world.getBlockState(pos), tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }
}

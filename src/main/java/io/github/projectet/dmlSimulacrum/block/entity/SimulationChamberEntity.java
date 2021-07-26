package io.github.projectet.dmlSimulacrum.block.entity;

import dev.nathanpb.dml.item.ItemDataModel;
import dev.nathanpb.dml.item.ItemPristineMatter;
import dev.technici4n.fasttransferlib.api.Simulation;
import dev.technici4n.fasttransferlib.api.energy.EnergyIo;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreen;
import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreenHandler;
import io.github.projectet.dmlSimulacrum.inventory.ImplementedInventory;
import io.github.projectet.dmlSimulacrum.item.ItemMatter;
import io.github.projectet.dmlSimulacrum.item.ItemPolymerClay;
import io.github.projectet.dmlSimulacrum.util.Animation;
import io.github.projectet.dmlSimulacrum.util.Constants;
import io.github.projectet.dmlSimulacrum.util.DataModelUtil;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Random;

public class SimulationChamberEntity extends BlockEntity implements EnergyIo, Tickable, ImplementedInventory, ExtendedScreenHandlerFactory, BlockEntityClientSerializable, Constants, SidedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
    public int ticks = 0;
    public int percentDone = 0;
    private Double energyAmount = 0.0;
    private boolean isCrafting = false;
    private boolean byproductSuccess = false;
    private String currentDataModelType = "";

    private HashMap<String, String> simulationText = new HashMap<>();
    private HashMap<String, Animation> simulationAnimations = new HashMap<>();

    public PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return energyAmount.intValue();
        }

        @Override
        public void set(int index, int value) {
            energyAmount = (double) value;
        }

        @Override
        public int size() {
            return 1;
        }
    };

    public SimulationChamberEntity(BlockEntityType<?> type) {
        super(type);
    }

    public SimulationChamberEntity() {
        this(dmlSimulacrum.SIMULATION_CHAMBER_ENTITY);
    }

    private static boolean dataModelMatchesOutput(ItemStack stack, ItemStack output) {
        Item livingMatter = dataModel.get(DataModelUtil.getEntityCategory(stack).toString()).getMatter();
        return Registry.ITEM.getId(livingMatter).equals(Registry.ITEM.getId(output.getItem()));
    }

    private static boolean dataModelMatchesPristine(ItemStack stack, ItemStack pristine) {
        Item pristineMatter = dataModel.get(DataModelUtil.getEntityCategory(stack).toString()).getPristine();
        return Registry.ITEM.getId(pristineMatter).equals(Registry.ITEM.getId(pristine.getItem()));
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
        ticks++;
        if(!world.isClient) {
            if(!isCrafting()) {
                if(canStartSimulation()) {
                    startSimulation();
                }
            } else {
                if (!canContinueSimulation() || dataModelTypeChanged()) {
                    finishSimulation(true);
                    return;
                }

                updateSimulationText(getDataModel());

                if (percentDone == 0) {
                    Random rand = new Random();
                    int num = rand.nextInt(100);
                    int chance = dmlSimulacrum.config.Pristine_Chance.entries.get(DataModelUtil.getTier(getDataModel()).toString());
                    byproductSuccess = num <= SimulationChamberScreen.ensureRange(chance, 1, 100);
                }

                int energyTickCost = dmlSimulacrum.config.Energy_Cost.entries.get(currentDataModelType);
                energyAmount = energyAmount - (double) energyTickCost;

                if (ticks % ((20 * 15) / 100) == 0) {
                    percentDone++;
                }

                // Notify while crafting every other second, this is done more frequently when the container is open
                if (ticks % (20 * 2) == 0) {
                    updateState();
                }
            }

            if(percentDone == 100) {
                finishSimulation(false);
                return;
            }

            sync();
        }
    }

    public void updateState() {
        BlockState state = world.getBlockState(getPos());
        world.updateListeners(getPos(), state, state, 3);
    }

    public boolean isCrafting() {
        return isCrafting;
    }

    private boolean dataModelTypeChanged() {
        return !currentDataModelType.equals(DataModelUtil.getEntityCategory(getDataModel()).toString());
    }

    public CompoundTag createTagFromSimText() {
        CompoundTag tag = new CompoundTag();
        simulationText.forEach(tag::putString);
        return tag;
    }

    public void getSimTextfromTag(CompoundTag tag) {
        simulationText.forEach((key, text) -> simulationText.put(key, tag.getString(key)));
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        energyAmount = tag.getDouble("energy");
        byproductSuccess = tag.getBoolean("byproductSuccess");
        isCrafting = tag.getBoolean("isCrafting");
        percentDone = tag.getInt("percentDone");
        currentDataModelType = tag.getString("currentDataModelType");
        getSimTextfromTag(tag.getCompound("simulationText"));
        Inventories.fromTag(tag, inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putDouble("energy", energyAmount);
        tag.putBoolean("byproductSuccess", byproductSuccess);
        tag.putBoolean("isCrafting", isCrafting);
        tag.putInt("percentDone", percentDone);
        tag.putString("currentDataModelType", currentDataModelType);
        tag.put("simulationText", createTagFromSimText());
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

        String resultPrefix = byproductSuccess ? "§a" : "§c";

        Animation aLine1 = getAnimation("simulationProgressLine1");
        Animation aLine1Version = getAnimation("simulationProgressLine1Version");

        Animation aLine2 = getAnimation("simulationProgressLine2");

        Animation aLine3 = getAnimation("simulationProgressLine3");
        Animation aLine4 = getAnimation("simulationProgressLine4");
        Animation aLine5 = getAnimation("simulationProgressLine5");

        Animation aLine6 = getAnimation("simulationProgressLine6");
        Animation aLine6Result = getAnimation("simulationProgressLine6Result");

        Animation aLine7 = getAnimation("simulationProgressLine7");
        Animation aLine8 = getAnimation("blinkingDots1");

        simulationText.put("simulationProgressLine1", animate(lines[0], aLine1, null, 1, false));
        simulationText.put("simulationProgressLine1Version", "§6" + animate(lines[1], aLine1Version, aLine1, 1, false) + "§r");

        simulationText.put("simulationProgressLine2", animate(lines[2], aLine2, aLine1Version, 1, false));

        simulationText.put("simulationProgressLine3", animate(lines[3], aLine3, aLine2, 2, false));
        simulationText.put("simulationProgressLine4", animate(lines[4], aLine4, aLine3, 1, false));
        simulationText.put("simulationProgressLine5", animate(lines[5], aLine5, aLine4, 2, false));

        simulationText.put("simulationProgressLine6", animate(lines[6], aLine6, aLine5, 2, false));
        simulationText.put("simulationProgressLine6Result", resultPrefix + animate(lines[7], aLine6Result, aLine6, 2, false) + "§r");

        simulationText.put("simulationProgressLine7", animate(lines[8], aLine7, aLine6Result, 1, false));
        simulationText.put("blinkingDots1", animate(lines[9], aLine8, aLine7, 8, true));
    }

    private String animate(String string, Animation anim, @Nullable Animation precedingAnim, int delayInTicks, boolean loop) {
        if(precedingAnim != null) {
            if (precedingAnim.hasFinished()) {
                return anim.animate(string, delayInTicks, world.getLevelProperties().getTime(), loop);
            } else {
                return "";
            }
        }
        return  anim.animate(string, delayInTicks, world.getLevelProperties().getTime(), loop);
    }

    private Animation getAnimation(String key) {
        if(simulationAnimations.containsKey(key)) {
            return simulationAnimations.get(key);
        } else {
            simulationAnimations.put(key, new Animation());
            return simulationAnimations.get(key);
        }
    }

    public String getSimulationText(String key) {
        if(simulationText.containsKey(key)) {
            return simulationText.get(key);
        } else {
            simulationText.put(key, "");
            return simulationText.get(key);
        }
    }

    private void startSimulation() {
        isCrafting = true;
        currentDataModelType = DataModelUtil.getEntityCategory(getDataModel()).toString();
        inventory.get(1).setCount(getPolymerClay().getCount() - 1);
        resetAnimations();
    }

    private void finishSimulation(boolean abort) {
        resetAnimations();
        percentDone = 0;
        isCrafting = false;
        // Only decrease input and increase output if not aborted, and only if on the server's TE
        if(!abort && !world.isClient) {
            DataModelUtil.updateSimulationCount(getDataModel());
            DataModelUtil.updateTierCount(getDataModel());

            if(inventory.get(2).getItem() instanceof ItemMatter) inventory.get(2).setCount(getLiving().getCount() + 1);
            else inventory.set(2, new ItemStack(dataModel.get(currentDataModelType).getMatter(), 1));

            if(byproductSuccess) {
                // If Byproduct roll was successful
                byproductSuccess = false;
                if(inventory.get(3).getItem() instanceof ItemPristineMatter) inventory.get(3).increment(1);
                else inventory.set(3, new ItemStack(dataModel.get(currentDataModelType).getPristine(), 1));
            }

            updateState();
        }
    }

    private boolean canStartSimulation() {
        return hasEnergyForSimulation() && canContinueSimulation() && !outputIsFull() && !pristineIsFull() && hasPolymerClay();
    }

    private boolean canContinueSimulation() {
        return hasDataModel() && !DataModelUtil.getTier(getDataModel()).toString().equalsIgnoreCase("faulty");
    }

    public boolean hasEnergyForSimulation() {
        if(hasDataModel()) {
            int ticksPerSimulation = 300;
            return getEnergy() > (ticksPerSimulation * dmlSimulacrum.config.Energy_Cost.entries.get(DataModelUtil.getEntityCategory(getDataModel()).toString()));
        } else {
            return false;
        }
    }

    public void resetAnimations() {
        simulationAnimations = new HashMap<>();
        simulationText = new HashMap<>();
    }

    public ItemStack getDataModel() {
        return getStack(DATA_MODEL_SLOT);
    }

    private ItemStack getPolymerClay() {
        return getStack(INPUT_SLOT);
    }

    private ItemStack getLiving() {
        return getStack(OUTPUT_SLOT);
    }

    private ItemStack getPristine() {
        return getStack(PRISTINE_SLOT);
    }

    public boolean hasDataModel() {
        return getDataModel().getItem() instanceof ItemDataModel;
    }

    public boolean hasPolymerClay() {
        ItemStack stack = getPolymerClay();
        return stack.getItem() instanceof ItemPolymerClay && stack.getCount() > 0;
    }

    public boolean outputIsFull() {
        ItemStack stack = getLiving();
        if(stack.isEmpty()) {
            return false;
        }

        boolean stackLimitReached = stack.getCount() == getLiving().getMaxCount();
        boolean outputMatches = dataModelMatchesOutput(getDataModel(), getLiving());

        return stackLimitReached || !outputMatches;
    }

    public boolean pristineIsFull() {
        ItemStack stack = getPristine();
        if(stack.isEmpty()) {
            return false;
        }

        boolean stackLimitReached = stack.getCount() == inventory.get(3).getMaxCount();
        boolean outputMatches = dataModelMatchesPristine(getDataModel(), getPristine());

        return stackLimitReached || !outputMatches;
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("Simulation Chamber");
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

    @Override
    public int[] getAvailableSlots(Direction side) {
        switch(side) {
            case UP:
                return new int[] {DATA_MODEL_SLOT, INPUT_SLOT};
            default:
                return new int[] {OUTPUT_SLOT, PRISTINE_SLOT};
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        if (dir == Direction.UP) {
            switch (slot) {
                case DATA_MODEL_SLOT:
                    return stack.getItem() instanceof ItemDataModel && DataModelUtil.getEntityCategory(stack) != null;
                case INPUT_SLOT:
                    return stack.getItem() instanceof ItemPolymerClay;
                default:
                    return false;
            }
        }
        else return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        if(dir != Direction.UP) {
            switch (slot) {
                case OUTPUT_SLOT:
                case PRISTINE_SLOT:
                    return true;
                default:
                    return false;
            }
        }
        else return false;
    }
}

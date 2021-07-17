package io.github.projectet.dmlSimulacrum.gui;

import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.inventory.SlotSimulationChamber;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class SimulationChamberScreenHandler extends ScreenHandler {
    public static final int DATA_MODEL_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int PRISTINE_SLOT = 3;
    private final Inventory inventory;
    private PlayerEntity player;

    public static final ScreenHandlerType<SimulationChamberScreenHandler> SCS_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(dmlSimulacrum.id("simulation"), SimulationChamberScreenHandler::new);

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(4));
    }

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(SCS_HANDLER_TYPE, syncId);
        checkSize(inventory, 4);
        this.inventory = inventory;
        player = playerInventory.player;
        addSlots();
        addInventorySlots();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addSlots() {
        this.addSlot(new SlotSimulationChamber(inventory, DATA_MODEL_SLOT, -13, 1));
        this.addSlot(new SlotSimulationChamber(inventory, INPUT_SLOT, 176, 7));
        this.addSlot(new SlotSimulationChamber(inventory, OUTPUT_SLOT, 196, 7));
        this.addSlot(new SlotSimulationChamber(inventory, PRISTINE_SLOT, 186, 27));
    }

    private void addInventorySlots() {
        // Bind actionbar
        for (int row = 0; row < 9; row++) {
            int index = row;
            Slot slot = new Slot(player.inventory, index, 36 + row * 18, 211);
            addSlot(slot);
        }

        // 3 Top rows, starting with the bottom one
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                int x = 36 + column * 18;
                int y = 153 + row * 18;
                int index = column + row * 9 + 9;
                Slot slot = new Slot(player.inventory, index, x, y);
                addSlot(slot);
            }
        }
    }
}

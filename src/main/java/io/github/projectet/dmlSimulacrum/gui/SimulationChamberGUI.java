package io.github.projectet.dmlSimulacrum.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;

public class SimulationChamberGUI extends SyncedGuiDescription {
    public SimulationChamberGUI(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory) {
        super(type, syncId, playerInventory);
    }

    public SimulationChamberGUI(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory blockInventory, PropertyDelegate propertyDelegate) {
        super(type, syncId, playerInventory, blockInventory, propertyDelegate);
    }
}

package io.github.projectet.dmlSimulacrum.gui;

import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

public class SimulationChamberScreenHandler extends ScreenHandler {

    public static final ScreenHandlerType<SimulationChamberScreenHandler> SCS_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(dmlSimulacrum.id("simulation"), SimulationChamberScreenHandler::new);

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3));
    }

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(SCS_HANDLER_TYPE, syncId);
        checkSize(inventory, 3);

        this.addSlot(new Slot(inventory, 0, 80, 35));

        int m;
        int l;
        //The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}

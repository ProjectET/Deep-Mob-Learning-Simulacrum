package io.github.projectet.dmlSimulacrum.inventory;

import dev.nathanpb.dml.item.ItemDataModel;
import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreenHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SlotSimulationChamber extends Slot {
    private final int index;

    public SlotSimulationChamber(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.index = index;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        Item item = stack.getItem();
        switch(index) {
            case SimulationChamberScreenHandler.DATA_MODEL_SLOT:
                return !stack.isEmpty() && item instanceof ItemDataModel;
            case SimulationChamberScreenHandler.INPUT_SLOT:
                return !stack.isEmpty() && stack.getItem().getName().equals("Clay Polymer");
            default:
                return false;
        }
    }
}

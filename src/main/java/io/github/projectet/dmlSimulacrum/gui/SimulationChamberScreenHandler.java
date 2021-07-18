package io.github.projectet.dmlSimulacrum.gui;

import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.inventory.SlotSimulationChamber;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class SimulationChamberScreenHandler extends ScreenHandler {
    public static final int DATA_MODEL_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int PRISTINE_SLOT = 3;
    private Inventory inventory;
    private PlayerEntity player;
    private SimulationChamberEntity blockEntity;
    private BlockPos blockPos;

    public static final ScreenHandlerType<SimulationChamberScreenHandler> SCS_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(dmlSimulacrum.id("simulation"), SimulationChamberScreenHandler::new);

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        this(syncId, playerInventory, new SimpleInventory(4));
        this.blockPos = packetByteBuf.readBlockPos();
        this.blockEntity = ((SimulationChamberEntity) playerInventory.player.world.getBlockEntity(blockPos));
    }

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(SCS_HANDLER_TYPE, syncId);
        blockPos = BlockPos.ORIGIN;
        this.inventory = inventory;
        this.player = playerInventory.player;
        checkSize(inventory, 4);
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
            Slot slot = new Slot(player.inventory, row, 36 + row * 18, 211);
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

    public SimulationChamberEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = slots.size() - player.inventory.main.size();

            if (index < containerSlots) {
                if (!insertItem(itemstack1, containerSlots, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!insertItem(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemstack1);
        }

        blockEntity.markDirty();
        this.player.inventory.markDirty();
        return itemstack;
    }
}

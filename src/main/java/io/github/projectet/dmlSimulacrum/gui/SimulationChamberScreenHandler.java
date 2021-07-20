package io.github.projectet.dmlSimulacrum.gui;

import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.inventory.SlotSimulationChamber;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimulationChamberScreenHandler extends ScreenHandler {
    public static final int DATA_MODEL_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int PRISTINE_SLOT = 3;
    private Inventory inventory;
    private final PlayerEntity player;
    private SimulationChamberEntity blockEntity;
    public BlockPos blockPos;
    private World world;

    public static final ScreenHandlerType<SimulationChamberScreenHandler> SCS_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(dmlSimulacrum.id("simulation"), SimulationChamberScreenHandler::new);

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf packetByteBuf) {
        super(SCS_HANDLER_TYPE, syncId);
        this.blockPos = packetByteBuf.readBlockPos();
        this.blockEntity = ((SimulationChamberEntity) playerInventory.player.getEntityWorld().getBlockEntity(blockPos));
        this.inventory = blockEntity;
        this.player = playerInventory.player;
        this.world = this.player.world;;
        checkSize(inventory, 4);
        addSlots();
        addInventorySlots();
    }

    public SimulationChamberScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, SimulationChamberEntity blockEntity) {
        this(syncId, playerInventory, PacketByteBufs.create().writeBlockPos(blockEntity.getPos()));
        this.inventory = inventory;
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

    @Override
    public void sendContentUpdates() {
        super.sendContentUpdates();
        if(!world.isClient) {
            // Update the tile every tick while container is open
            blockEntity.updateState();
        }
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (index < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
}

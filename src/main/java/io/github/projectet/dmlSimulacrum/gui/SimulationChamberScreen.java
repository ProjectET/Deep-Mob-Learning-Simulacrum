package io.github.projectet.dmlSimulacrum.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.util.Animation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class SimulationChamberScreen extends HandledScreen<SimulationChamberScreenHandler> {

    private static final int WIDTH =  232;
    private static final int HEIGHT = 230;
    public static final Identifier GUI = dmlSimulacrum.id( "textures/gui/simulation_chamber_base.png");
    public static final Identifier defaultGUI = dmlSimulacrum.id("textures/gui/default_gui.png");
    private final SimulationChamberEntity blockEntity;
    private double energy;
    private final double maxEnergy;
    private HashMap<String, Animation> animationList;
    private ItemStack currentDataModel = ItemStack.EMPTY;
    private TextRenderer renderer;
    private World world;

    public SimulationChamberScreen(SimulationChamberScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = WIDTH;
        this.backgroundHeight = HEIGHT;
        this.world = inventory.player.getEntityWorld();
        this.blockEntity = handler.getBlockEntity();
        this.energy = handler.getEnergy();
        this.maxEnergy = blockEntity.getEnergyCapacity();
        this.animationList = new HashMap<>();
        this.renderer = MinecraftClient.getInstance().textRenderer;
    }

    public static int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        DecimalFormat f = new DecimalFormat("0.#");
        int x2 = x + 8;
        int spacing = 12;
        int x2Start = x2 - 3;
        //Main Chamber GUI
        MinecraftClient.getInstance().getTextureManager().bindTexture(GUI);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(matrices, x2, y, 0, 0, 216, 141);

        //Energy Bar Rendering
        int energyBarHeight = ensureRange((int) ( energy / (blockEntity.getEnergyCapacity() - 64) * 87), 0, 87);
        int energyBarOffset = 87 - energyBarHeight;
        drawTexture(matrices, x2 - 22, y, 0, 141, 18, 18);
        drawTexture(matrices, x2 + 203,  y + 48 + energyBarOffset, 25, 141, 7, energyBarHeight);

        //Player Inventory GUI
        MinecraftClient.getInstance().getTextureManager().bindTexture(defaultGUI);
        drawTexture(matrices, x2 + 20, y + 145, 0, 0, 176, 90);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }
}

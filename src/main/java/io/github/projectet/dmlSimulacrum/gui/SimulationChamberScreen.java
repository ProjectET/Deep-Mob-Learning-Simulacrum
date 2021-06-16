package io.github.projectet.dmlSimulacrum.gui;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SimulationChamberScreen extends HandledScreen<SimulationChamberScreenHandler> {

    private static final int WIDTH =  232;
    private static final int HEIGHT = 230;
    public static final Identifier GUI = new Identifier("dmlSimulacrum", "textures/gui/simulation_chamber_base.png");

    public SimulationChamberScreen(SimulationChamberScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.client.getTextureManager().bindTexture(GUI);
        drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
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

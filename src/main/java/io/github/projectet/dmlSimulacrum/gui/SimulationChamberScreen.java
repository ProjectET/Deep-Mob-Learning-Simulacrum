package io.github.projectet.dmlSimulacrum.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.nathanpb.dml.enums.DataModelTier;
import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import io.github.projectet.dmlSimulacrum.util.Animation;
import io.github.projectet.dmlSimulacrum.util.DataModelUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Environment(EnvType.CLIENT)
public class SimulationChamberScreen extends HandledScreen<SimulationChamberScreenHandler> {

    private static final int WIDTH =  232;
    private static final int HEIGHT = 230;
    public static final Identifier GUI = dmlSimulacrum.id( "textures/gui/simulation_chamber_base.png");
    public static final Identifier defaultGUI = dmlSimulacrum.id("textures/gui/default_gui.png");
    private double energy;
    private final double maxEnergy;
    private HashMap<String, Animation> animationList;
    private ItemStack currentDataModel = ItemStack.EMPTY;
    private TextRenderer renderer;
    SimulationChamberEntity blockEntity;
    private World world;

    public SimulationChamberScreen(SimulationChamberScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = WIDTH;
        this.backgroundHeight = HEIGHT;
        this.blockEntity = (SimulationChamberEntity) MinecraftClient.getInstance().world.getBlockEntity(handler.blockPos);
        this.energy = blockEntity.getEnergy();
        this.maxEnergy = blockEntity.getEnergyCapacity();
        this.animationList = new HashMap<>();
        this.world = blockEntity.getWorld();
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
        int y2Start = y - 3;
        //Main Chamber GUI
        MinecraftClient.getInstance().getTextureManager().bindTexture(GUI);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(matrices, x2, y, 0, 0, 216, 141);

        //Energy Bar Rendering
        int energyBarHeight = ensureRange((int) ( energy / (maxEnergy - 64) * 87), 0, 87);
        int energyBarOffset = 87 - energyBarHeight;
        drawTexture(matrices, x2 - 22, y, 0, 141, 18, 18);
        drawTexture(matrices, x2 + 203,  y + 48 + energyBarOffset, 25, 141, 7, energyBarHeight);

        String[] lines;

        if(!blockEntity.hasDataModel()) {
            lines = new String[] {"Please insert a data model", "to begin the simulation"};

            Animation a1 = getAnimation("pleaseInsert1");
            Animation a2 = getAnimation("pleaseInsert2");

            animateString(matrices, lines[0], a1, null, 1, false, x2 + 10, y2Start + spacing, 0xFFFFFF);
            animateString(matrices, lines[1], a2, a1, 1, false, x2Start + 10, y2Start + (spacing * 2), 0xFFFFFF);

        } else if(DataModelUtil.getTier(blockEntity.getDataModel()).toString().equalsIgnoreCase("faulty")) {

            lines = new String[] {"Insufficient data in model", "please insert a basic model", "or better "};

            Animation insufData = getAnimation("insufData1");
            Animation insufData2 = getAnimation("insufData2");
            Animation insufData3 = getAnimation("insufData3");

            animateString(matrices, lines[0], insufData, null, 1, false, x2 + 10, y2Start + spacing, 0xFFFFFF);
            animateString(matrices, lines[1], insufData2, insufData, 1, false,  x2 + 10, y2Start + (spacing * 2), 0xFFFFFF);
            animateString(matrices, lines[2], insufData3, insufData2, 1, false,  x2 + 10, y2Start + (spacing * 3), 0xFFFFFF);

        } else {
            // Draw current data model data
            if(DataModelUtil.getTier(blockEntity.getDataModel()).toString().equals(DataModelTier.SELF_AWARE.toString())) {
                drawTexture(matrices, x2 + 6,  y + 48, 18, 141, 7, 87);
            } else {
                int collectedData = DataModelUtil.getTierCount(blockEntity.getDataModel());
                int tierRoof = DataModelUtil.getTierRoof(blockEntity.getDataModel());

                int experienceBarHeight = (int) (((float) collectedData / tierRoof * 87));
                int experienceBarOffset = 87 - experienceBarHeight;
                drawTexture(matrices, x2 + 6,  y + 48 + experienceBarOffset, 18, 141, 7, experienceBarHeight);
            }

            drawStringWithShadow(matrices, renderer, "Tier: " + DataModelUtil.getTier(blockEntity.getDataModel()), x2 + 10, y2Start + spacing, 0xFFFFFF);
            drawStringWithShadow(matrices, renderer, "Iterations: " + f.format(DataModelUtil.getSimulationCount(blockEntity.getDataModel())), x2 + 10, y2Start + spacing * 2, 0xFFFFFF);
            drawStringWithShadow(matrices, renderer, "Pristine chance: " + dmlSimulacrum.config.Pristine_Chance.entries.get(DataModelUtil.getTier(blockEntity.getDataModel()).toString()) + "%", x2 + 10, y2Start + spacing * 3, 0xFFFFFF);
        }

        // Draw player inventory
        MinecraftClient.getInstance().getTextureManager().bindTexture(defaultGUI);
        drawTexture(matrices, x2 + 20, y + 145, 0, 0, 176, 90);


        drawConsoleText(matrices, x2, y, spacing);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int x = mouseX - guiLeft;
        int y = mouseY - guiTop;

        NumberFormat f = NumberFormat.getNumberInstance(Locale.ENGLISH);
        List<String> tooltip = new ArrayList<>();

        if(47 <= y && y < 135) {
            if(13 <= x && x < 22) {
                // Tooltip for data model data bar
                if(blockEntity.hasDataModel()) {
                    if(!DataModelUtil.getTier(blockEntity.getDataModel()).toString().equals(DataModelTier.SELF_AWARE.name())) {
                        tooltip.add(DataModelUtil.getTierCount(blockEntity.getDataModel()) + "/" + DataModelUtil.getTierRoof(blockEntity.getDataModel()) + " Data collected");
                    } else {
                        tooltip.add("This data model has reached the max tier.");
                    }
                } else {
                    tooltip.add("Machine is missing a data model");
                }
                drawHoveringText(tooltip, x + 2, y + 2);
            } else if(211 <= x && x < 220) {
                // Tooltip for energy
                tooltip.add(f.format(energy) + "/" + f.format(maxEnergy) + " E");
                if(tile.hasDataModel()) {
                    MobMetaData data = DataModel.getMobMetaData(tile.getDataModel());
                    tooltip.add("Simulations with current data model drains " + f.format(data.getSimulationTickCost()) + "E/t");
                }
                drawHoveringText(tooltip, x - 90, y - 16);
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private Animation getAnimation(String key) {
        if(animationList.containsKey(key)) {
            return animationList.get(key);
        } else {
            animationList.put(key, new Animation());
            return animationList.get(key);
        }
    }

    private void animateString(MatrixStack matrices, String string, Animation anim, Animation precedingAnim, int delay, boolean loop, int left, int top, int color) {
        if(precedingAnim != null) {
            if (precedingAnim.hasFinished()) {
                String result = anim.animate(string, delay, world.getLevelProperties().getTime(), loop);
                drawStringWithShadow(matrices, renderer, result, left, top, color);
            } else {
                return;
            }
        }
        String result = anim.animate(string, delay, world.getLevelProperties().getTime(), loop);
        drawStringWithShadow(matrices, renderer, result, left, top, color);
    }

    private void drawConsoleText(MatrixStack matrices, int left, int top, int spacing) {
        String[] lines;

        if(!blockEntity.hasDataModel() || DataModelUtil.getTier(blockEntity.getDataModel()).toString().equalsIgnoreCase("faulty")) {
            animateString(matrices,"_", getAnimation("blinkingUnderline"), null, 16, true, left + 21, top + 49, 0xFFFFFF);

        } else if(!blockEntity.hasPolymerClay() && !blockEntity.isCrafting()) {
            lines = new String[] {"Cannot begin simulation", "Missing polymer medium", "_"};
            Animation a1 = getAnimation("inputSlotEmpty1");
            Animation a2 = getAnimation("inputSlotEmpty2");
            Animation a3 = getAnimation("blinkingUnderline1");

            animateString(matrices, lines[0], a1, null, 1, false, left + 21, top + 51, 0xFFFFFF);
            animateString(matrices, lines[1], a2, a1, 1, false, left + 21, top + 51 + spacing, 0xFFFFFF);
            animateString(matrices, lines[2], a3, a2, 16, true, left + 21, top + 51 + (spacing * 2), 0xFFFFFF);

        } else if(!hasEnergy() && !blockEntity.isCrafting()) {
            lines = new String[] {"Cannot begin simulation", "System energy levels critical", "_"};
            Animation a1 = getAnimation("lowEnergy1");
            Animation a2 = getAnimation("lowEnergy2");
            Animation a3 = getAnimation("blinkingUnderline2");

            animateString(matrices, lines[0], a1, null, 1, false, left + 21, top + 51, 0xFFFFFF);
            animateString(matrices, lines[1], a2, a1, 1, false, left + 21, top + 51 + spacing, 0xFFFFFF);
            animateString(matrices, lines[2], a3, a2, 16, true, left + 21, top + 51 + (spacing * 2), 0xFFFFFF);
        } else if(blockEntity.outputIsFull() || blockEntity.pristineIsFull()) {
            lines = new String[] {"Cannot begin simulation", "Output or pristine buffer is full", "_"};
            Animation a1 = getAnimation("outputSlotFilled1");
            Animation a2 = getAnimation("outputSlotFilled2");
            Animation a3 = getAnimation("blinkingUnderline3");

            animateString(matrices, lines[0], a1, null, 1, false, left + 21, top + 51, 0xFFFFFF);
            animateString(matrices, lines[1], a2, a1, 1, false, left + 21, top + 51 + spacing, 0xFFFFFF);
            animateString(matrices, lines[2], a3, a2, 16, true, left + 21, top + 51 + (spacing * 2), 0xFFFFFF);
        } else if(blockEntity.isCrafting()) {
            drawStringWithShadow(matrices, renderer, blockEntity.percentDone + "%", left + 176, top + 123, 0x62D8FF);

            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine1"), left + 21, top + 51, 0xFFFFFF);
            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine1Version"), left + 124, top + 51, 0xFFFFFF);

            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine2"), left + 21, top + 51 + spacing, 0xFFFFFF);

            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine3"), left + 21, top + 51 + (spacing * 2), 0xFFFFFF);
            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine4"), left + 21, top + 51 + (spacing * 3), 0xFFFFFF);
            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine5"), left + 21, top + 51 + (spacing * 4), 0xFFFFFF);

            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine6"), left + 21, top + 51 + (spacing * 5), 0xFFFFFF);
            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine6Result"), left + 140, top + 51 + (spacing * 5), 0xFFFFFF);

            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("simulationProgressLine7"), left + 21, top + 51 + (spacing * 6), 0xFFFFFF);
            drawStringWithShadow(matrices, renderer, blockEntity.getSimulationText("blinkingDots1"), left + 128, top + 51 + (spacing * 6), 0xFFFFFF);
        } else {
            animateString(matrices, "_", getAnimation("blinkingUnderline"), null, 16, true, left + 21, top + 49, 0xFFFFFF);
        }
    }

    private boolean dataModelChanged() {
        if(ItemStack.areItemsEqual(currentDataModel, blockEntity.getDataModel())) {
            return false;
        } else {
            this.currentDataModel = blockEntity.getDataModel();
            return true;
        }
    }
}

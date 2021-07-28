package io.github.projectet.dmlSimulacrum;

import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreen;
import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class dmlSimulacrumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(SimulationChamberScreenHandler.SCS_HANDLER_TYPE, SimulationChamberScreen::new);

        /*ItemTooltipCallback.EVENT.register((item, player, context, lines) -> {
            if (item.getItem() == Items.EXPERIENCE_BOTTLE) {
                lines.add(new TranslatableText("hi"));
            }
        });*/
    }
}

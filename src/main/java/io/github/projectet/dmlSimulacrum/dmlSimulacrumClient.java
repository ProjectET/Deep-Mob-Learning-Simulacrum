package io.github.projectet.dmlSimulacrum;

import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class dmlSimulacrumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(dmlSimulacrum.SCS_HANDLER_TYPE, SimulationChamberScreen::new);
    }
}

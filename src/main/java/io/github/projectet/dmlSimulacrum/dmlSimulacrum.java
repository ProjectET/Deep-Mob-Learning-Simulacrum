package io.github.projectet.dmlSimulacrum;

import io.github.projectet.dmlSimulacrum.block.SimulationChamber;
import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class dmlSimulacrum implements ModInitializer {

    public static final Block SIMULATION_CHAMBER = new SimulationChamber(FabricBlockSettings.of(Material.METAL));
    public static final BlockEntityType<SimulationChamberEntity> SIMULATION_CHAMBER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("Simulation_Chamber_Entity"), BlockEntityType.Builder.create(SimulationChamberEntity::new, SIMULATION_CHAMBER).build(null));

    final static String MOD_ID = "dmlSimulacrum";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {

    }

}

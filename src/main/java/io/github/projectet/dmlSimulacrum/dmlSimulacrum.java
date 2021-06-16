package io.github.projectet.dmlSimulacrum;

import dev.technici4n.fasttransferlib.api.energy.EnergyApi;
import io.github.projectet.dmlSimulacrum.block.SimulationChamber;
import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import io.github.projectet.dmlSimulacrum.gui.SimulationChamberScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class dmlSimulacrum implements ModInitializer {

    public static final Block SIMULATION_CHAMBER = new SimulationChamber(FabricBlockSettings.of(Material.METAL));
    public static BlockEntityType<SimulationChamberEntity> SIMULATION_CHAMBER_ENTITY;

    public final static String MOD_ID = "dmlsimulacrum";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static final ScreenHandlerType<SimulationChamberScreenHandler> SCS_HANDLER_TYPE;

    static {
        SCS_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(id("simulation"), SimulationChamberScreenHandler::new);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, id("simulation_chamber"), SIMULATION_CHAMBER);
        Registry.register(Registry.ITEM, id("simulation_chamber"), new BlockItem(SIMULATION_CHAMBER, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        SIMULATION_CHAMBER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("simulation_chamber_entity"), BlockEntityType.Builder.create(SimulationChamberEntity::new, SIMULATION_CHAMBER).build(null));
        EnergyApi.SIDED.registerSelf(SIMULATION_CHAMBER_ENTITY);

    }

}

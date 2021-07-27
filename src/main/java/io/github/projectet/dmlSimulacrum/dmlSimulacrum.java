package io.github.projectet.dmlSimulacrum;

import dev.technici4n.fasttransferlib.api.energy.EnergyApi;
import io.github.projectet.dmlSimulacrum.block.SimulationChamber;
import io.github.projectet.dmlSimulacrum.block.entity.SimulationChamberEntity;
import io.github.projectet.dmlSimulacrum.config.Config;
import io.github.projectet.dmlSimulacrum.item.Items;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class dmlSimulacrum implements ModInitializer {

    public static final Block SIMULATION_CHAMBER = new SimulationChamber(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).hardness(4f).resistance(3000f));
    public static BlockEntityType<SimulationChamberEntity> SIMULATION_CHAMBER_ENTITY;

    public final static String MOD_ID = "dmlsimulacrum";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static Config config;

    @Override
    public void onInitialize() {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        testConfig();
        config = AutoConfig.getConfigHolder(Config.class).getConfig();

        Registry.register(Registry.BLOCK, id("simulation_chamber"), SIMULATION_CHAMBER);
        Registry.register(Registry.ITEM, id("simulation_chamber"), new BlockItem(SIMULATION_CHAMBER, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

        SIMULATION_CHAMBER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("simulation_chamber_entity"), FabricBlockEntityTypeBuilder.create(SimulationChamberEntity::new, SIMULATION_CHAMBER).build());
        EnergyApi.SIDED.registerSelf(SIMULATION_CHAMBER_ENTITY);
        Items.Register();
    }

    public static void testConfig() {
        Config.PristineChance StaticPristine = new Config.PristineChance();
        HashMap<String, Integer> map = AutoConfig.getConfigHolder(Config.class).getConfig().Pristine_Chance.entries;
        map.forEach((K, V) -> {
            if(!inRange(V, 1, 100)) {
                map.put(K, StaticPristine.entries.get(K));
            }
        });
        AutoConfig.getConfigHolder(Config.class).getConfig().Pristine_Chance.entries = map;

        Config.PristineChance StaticCost = new Config.PristineChance();
        HashMap<String, Integer> cost = AutoConfig.getConfigHolder(Config.class).getConfig().Energy_Cost.entries;
        cost.forEach((K, V) -> {
            if(!inRange(V, 0, 6666)) {
                cost.put(K, StaticCost.entries.get(K));
            }
        });
        AutoConfig.getConfigHolder(Config.class).getConfig().Energy_Cost.entries = cost;
    }

    public static boolean inRange(int input, int min, int max) {
        return ((input >= min) && (input <= max));
    }
}

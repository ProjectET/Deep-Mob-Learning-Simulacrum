package io.github.projectet.dmlSimulacrum.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.HashMap;
import java.util.Map;

@me.shedaniel.autoconfig.annotation.Config(name = "dml-simulacrum")
public class Config implements ConfigData{

    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Category("default")
    @ConfigEntry.Gui.TransitiveObject
    public MatterXP Matter_XP = new MatterXP();

    @ConfigEntry.Gui.Excluded
    public PristineChance Pristine_Chance = new PristineChance();

    @ConfigEntry.Gui.Excluded
    public EnergyCost Energy_Cost = new EnergyCost();

    @me.shedaniel.autoconfig.annotation.Config(name = "matter_xp")
    public static class MatterXP implements ConfigData {
        @ConfigEntry.BoundedDiscrete(min = 1, max = 999)
        public int OverworldMatterXP = 10;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 999)
        public int HellishMatterXP = 14;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 999)
        public int ExtraMatterXP = 20;
    }

    public static class PristineChance {
        public HashMap<String, Integer> entries = new HashMap<>(Map.ofEntries(
                Map.entry("BASIC", 5),
                Map.entry("ADVANCED", 11),
                Map.entry("SUPERIOR", 24),
                Map.entry("SELF_AWARE", 42)
        ));
    }

    public static class EnergyCost {
        public HashMap<String, Integer> entries = new HashMap<>(Map.ofEntries(
            Map.entry("NETHER", 300),
                Map.entry("SLIMY", 160),
                Map.entry("OVERWORLD", 100),
                Map.entry("ZOMBIE", 300),
                Map.entry("SKELETON", 80),
                Map.entry("END", 512),
                Map.entry("GHOST", 372),
                Map.entry("ILLAGER", 412),
                Map.entry("OCEAN", 160)
        ));
    }
}

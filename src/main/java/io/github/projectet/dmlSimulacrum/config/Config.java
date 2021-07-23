package io.github.projectet.dmlSimulacrum.config;

import io.github.projectet.dmlSimulacrum.util.NumberRange;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.HashMap;
import java.util.Map;

@me.shedaniel.autoconfig.annotation.Config(name = "dml-simulacrum")
public class Config implements ConfigData{

    @ConfigEntry.Gui.RequiresRestart
    public MatterXP Matter_XP = new MatterXP();

    @ConfigEntry.Gui.RequiresRestart
    public PristineChance Pristine_Chance = new PristineChance();

    @ConfigEntry.Gui.RequiresRestart
    @ConfigEntry.Gui.Tooltip(count = 2)
    public EnergyCost Energy_Cost = new EnergyCost();

    @me.shedaniel.autoconfig.annotation.Config(name = "Matter XP")
    public static class MatterXP {
        public int OverworldMatterXP = 10;
        public int HellishMatterXP = 14;
        public int ExtraMatterXP = 20;
    }

    @me.shedaniel.autoconfig.annotation.Config(name = "Pristine Chance")
    public static class PristineChance {
        public HashMap<String, NumberRange> entries = new HashMap<>(Map.ofEntries(
                Map.entry("BASIC", new NumberRange(5)),
                Map.entry("ADVANCED", new NumberRange(11)),
                Map.entry("SUPERIOR", new NumberRange(24)),
                Map.entry("SELF_AWARE", new NumberRange(42))
        ));
    }

    @me.shedaniel.autoconfig.annotation.Config(name = "Energy Cost")
    public static class EnergyCost {
        public HashMap<String, NumberRange> entries = new HashMap<>(Map.ofEntries(
            Map.entry("NETHER", new NumberRange(300, 0, 6666)),
                Map.entry("SLIMY", new NumberRange(160, 0, 6666)),
                Map.entry("OVERWORLD", new NumberRange(100, 0, 6666)),
                Map.entry("ZOMBIE", new NumberRange(300, 0, 6666)),
                Map.entry("SKELETON", new NumberRange(80, 0, 6666)),
                Map.entry("END", new NumberRange(512, 0, 6666)),
                Map.entry("GHOST", new NumberRange(372, 0, 6666)),
                Map.entry("ILLAGER", new NumberRange(412, 0, 6666)),
                Map.entry("OCEAN", new NumberRange(160, 0, 6666))
        ));
    }
}

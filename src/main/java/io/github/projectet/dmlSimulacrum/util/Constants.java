package io.github.projectet.dmlSimulacrum.util;

import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public interface Constants {
    //Simulation Chamber index slots.
    int DATA_MODEL_SLOT = 0;
    int INPUT_SLOT = 1;
    int OUTPUT_SLOT = 2;
    int PRISTINE_SLOT = 3;

    HashMap<String, DataModelUtil.DataModel2Matter> dataModel = new HashMap<>(Map.ofEntries(
            Map.entry("NETHER", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_nether")), Registry.ITEM.get(dmlSimulacrum.id("hellish_matter")))),
            Map.entry("SLIMY", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_slimy")), Registry.ITEM.get(dmlSimulacrum.id("overworld_matter")))),
            Map.entry("OVERWORLD", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_overworld")), Registry.ITEM.get(dmlSimulacrum.id("overworld_matter")))),
            Map.entry("ZOMBIE", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_zombie")), Registry.ITEM.get(dmlSimulacrum.id("overworld_matter")))),
            Map.entry("SKELETON", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_skeleton")), Registry.ITEM.get(dmlSimulacrum.id("overworld_matter")))),
            Map.entry("END", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_end")), Registry.ITEM.get(dmlSimulacrum.id("extraterrestrial_matter")))),
            Map.entry("GHOST", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_ghost")), Registry.ITEM.get(dmlSimulacrum.id("hellish_matter")))),
            Map.entry("ILLAGER", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_illager")), Registry.ITEM.get(dmlSimulacrum.id("overworld_matter")))),
            Map.entry("OCEAN", new DataModelUtil.DataModel2Matter(Registry.ITEM.get(new Identifier("dml-refabricated", "pristine_matter_ocean")), Registry.ITEM.get(dmlSimulacrum.id("overworld_matter"))))
    ));
}

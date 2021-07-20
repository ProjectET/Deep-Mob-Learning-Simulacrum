package io.github.projectet.dmlSimulacrum.item;

import io.github.projectet.dmlSimulacrum.dmlSimulacrum;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class Items {

    public static void Register() {
        Registry.register(Registry.ITEM, dmlSimulacrum.id("polymer_clay"), POLYMER_CLAY);
    }

    public static final Item POLYMER_CLAY = new Item(new FabricItemSettings().maxCount(64).group(ItemGroup.MATERIALS));
    public static final Item OVERWORLD_MATTER = new Item(new FabricItemSettings().maxCount(64));
    public static final Item HELLISH_MATTER = new Item(new FabricItemSettings().maxCount(64));
    public static final Item EXTRATERRESTRIAL_MATTER = new Item(new FabricItemSettings().maxCount(64));
}

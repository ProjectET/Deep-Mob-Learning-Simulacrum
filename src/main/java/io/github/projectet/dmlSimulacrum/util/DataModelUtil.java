package io.github.projectet.dmlSimulacrum.util;

import dev.nathanpb.dml.data.DataModelDataKt;
import dev.nathanpb.dml.enums.EntityCategory;
import net.minecraft.item.ItemStack;

public class DataModelUtil {
    public static void updateSimulationCount(ItemStack stack) {
        int i = DataModelDataKt.getDataModel(stack).getTag().getInt("simulationCount");
        i++;
        DataModelDataKt.getDataModel(stack).getTag().putInt("simulationCount", i);
    }

    public static int getSimulationCount(ItemStack stack) {
        return DataModelDataKt.getDataModel(stack).getTag().getInt("simulationCount");
    }

    public static EntityCategory getEntityCategory(ItemStack stack) {
        return DataModelDataKt.getDataModel(stack).getCategory();
    }

    public static int getTierCount(ItemStack stack) {
        return DataModelDataKt.getDataModel(stack).getDataAmount();
    }
}

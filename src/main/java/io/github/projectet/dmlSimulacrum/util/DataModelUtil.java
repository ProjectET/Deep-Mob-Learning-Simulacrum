package io.github.projectet.dmlSimulacrum.util;

import dev.nathanpb.dml.data.DataModelDataKt;
import dev.nathanpb.dml.enums.DataModelTier;
import dev.nathanpb.dml.enums.EntityCategory;
import dev.nathanpb.dml.item.ItemDataModel;

import net.minecraft.item.ItemStack;

public class DataModelUtil {
    public static void updateSimulationCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            int i = DataModelDataKt.getDataModel(stack).getTag().getInt("simulationCount") + 1;
            DataModelDataKt.getDataModel(stack).getTag().putInt("simulationCount", i);
        }
    }

    public static int getSimulationCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            return DataModelDataKt.getDataModel(stack).getTag().getInt("simulationCount");
        }
        else {
            return 0;
        }
    }

    public static EntityCategory getEntityCategory(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            return DataModelDataKt.getDataModel(stack).getCategory();
        }
        else {
            return null;
        }
    }

    public static int getTierCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            return DataModelDataKt.getDataModel(stack).getDataAmount();
        }
        else {
            return 0;
        }
    }

    public static DataModelTier getTier(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            return DataModelDataKt.getDataModel(stack).tier();
        }
        else {
            return null;
        }
    }
}

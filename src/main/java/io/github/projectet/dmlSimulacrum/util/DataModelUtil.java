package io.github.projectet.dmlSimulacrum.util;

import dev.nathanpb.dml.data.DataModelDataKt;
import dev.nathanpb.dml.enums.EntityCategory;
import dev.nathanpb.dml.item.ItemDataModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class DataModelUtil {
    public static CompoundTag putSimulationCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            CompoundTag tag = stack.getTag();
            tag.putInt("simulationCount", 0);
            return tag;
        }
        else return stack.getTag();
    }

    public static void updateSimulationCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            int i = stack.getTag().getInt("simulationCount");
            i++;
            stack.getTag().putInt("simulationCount", i);
        }
    }

    public static int getSimulationCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            return stack.getTag().getInt("simulationCount");
        }
        else return 0;
    }

    public static EntityCategory getEntityCategory(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            return DataModelDataKt.getDataModel(stack).getCategory();
        }
        else return null;
    }

    public static int getTierCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            return DataModelDataKt.getDataModel(stack).getDataAmount();
        }
        else return 0;
    }
}

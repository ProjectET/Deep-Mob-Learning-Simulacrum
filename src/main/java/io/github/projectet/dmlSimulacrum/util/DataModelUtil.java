package io.github.projectet.dmlSimulacrum.util;

import dev.nathanpb.dml.DeepMobLearningKt;
import dev.nathanpb.dml.data.DataModelDataKt;
import dev.nathanpb.dml.enums.DataModelTier;
import dev.nathanpb.dml.enums.EntityCategory;
import dev.nathanpb.dml.item.ItemDataModel;
import dev.nathanpb.dml.ModConfig;

import dev.nathanpb.dml.item.ItemPristineMatter;
import io.github.projectet.dmlSimulacrum.item.ItemMatter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class DataModelUtil {
    public static void updateSimulationCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            int i = getSimulationCount(stack) + 1;
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

    public static void updateTierCount(ItemStack stack) {
        if(stack.getItem() instanceof ItemDataModel) {
            DataModelDataKt.getDataModel(stack).setDataAmount(getTierCount(stack) + 1);
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

    public static int getTierRoof(ItemStack stack) {
        if (stack.getItem() instanceof ItemDataModel) {
            ModConfig config = DeepMobLearningKt.getConfig();
            switch (getTier(stack).toString()) {
                case "FAULTY" -> {
                    return config.getDataModel().getBasicDataRequired();
                }
                case "BASIC" -> {
                    return config.getDataModel().getAdvancedDataRequired();
                }
                case "ADVANCED" -> {
                    return config.getDataModel().getSuperiorDataRequired();
                }
                case "SUPERIOR" -> {
                    return config.getDataModel().getSelfAwareDataRequired();
                }
            }
        }
        return 0;
    }

    public static Text getFormattedTier(ItemStack stack) {
        switch (getTier(stack).toString()) {
            case "FAULTY" -> {
                return new LiteralText("Faulty").formatted(Formatting.GRAY);
            }
            case "BASIC" -> {
                return new LiteralText("Basic").formatted(Formatting.GREEN);
            }
            case "ADVANCED" -> {
                return new LiteralText("Advanced").formatted(Formatting.BLUE);
            }
            case "SUPERIOR" -> {
                return new LiteralText("Superior").formatted(Formatting.LIGHT_PURPLE);
            }
            case "SELF_AWARE" -> {
                return new LiteralText("Self Aware").formatted(Formatting.GOLD);
            }
            default -> {
                return new LiteralText("Invalid Item");
            }
        }
    }

    public static class DataModel2Matter {
        ItemPristineMatter pristine;
        ItemMatter matter;

        DataModel2Matter(Item pristine, Item matter) {
            this.pristine = (ItemPristineMatter) pristine;
            this.matter = (ItemMatter) matter;
        }

        public ItemMatter getMatter() {
            return matter;
        }

        public ItemPristineMatter getPristine() {
            return pristine;
        }
    }
}

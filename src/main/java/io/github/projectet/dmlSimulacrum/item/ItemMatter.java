package io.github.projectet.dmlSimulacrum.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemMatter extends Item {
    public ItemMatter(Settings settings) {
        super(settings);
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        int count = 1;
        if(user.isSneaking()) {
            count = stack.getCount();
            stack.decrement(count);
        }
        else {
            stack.decrement(1);
            if(user.world.isClient) {
                user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.1f, (float) Math.random());
            }
        }
        user.addExperience(count * 20);
        return TypedActionResult.consume(stack);
    }
}

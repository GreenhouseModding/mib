package dev.greenhouseteam.mib.item;

import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.registry.MibComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MibInstrumentItem extends Item {
    public MibInstrumentItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return ItemInstrument.playInstrument(player, stack, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if (!stack.has(MibComponents.INSTRUMENT))
            return 60;
        return stack.get(MibComponents.INSTRUMENT).maxUseDuration();
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int useTicksRemaining) {
        super.releaseUsing(stack, level, entity, useTicksRemaining);
        if (entity instanceof Player player)
            player.getCooldowns().addCooldown(stack.getItem(), 40);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player)
            player.getCooldowns().addCooldown(stack.getItem(), 40);
        return super.finishUsingItem(stack, level, entity);
    }
}

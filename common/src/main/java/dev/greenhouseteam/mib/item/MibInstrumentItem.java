package dev.greenhouseteam.mib.item;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.registry.MibComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class MibInstrumentItem extends Item {
    private static final List<MibInstrumentItem> INSTRUMENT_ITEMS = new ArrayList<>();

    public MibInstrumentItem(Properties properties) {
        super(properties);
        INSTRUMENT_ITEMS.add(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return ItemInstrument.playInstrument(player, stack, hand);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int useTicksRemaining) {
        if (!stack.has(MibComponents.INSTRUMENT))
            return;
        Mib.getHelper().invokeTickEvents(level, entity, stack, useTicksRemaining);
        if (useTicksRemaining % 20 == 0) {
            entity.gameEvent(GameEvent.INSTRUMENT_PLAY, entity);
            if (stack.has(MibComponents.INSTRUMENT) && stack.get(MibComponents.INSTRUMENT).animation().isPresent() && !stack.get(MibComponents.INSTRUMENT).animation().get().handsToSwing().isEmpty())
                stack.get(MibComponents.INSTRUMENT).animation().get().handsToSwing().forEach(hand -> entity.swing(hand, true));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        if (!stack.has(MibComponents.INSTRUMENT))
            return 40;
        return Mib.getHelper().getInstrumentUseDuration(stack, entity, stack.get(MibComponents.INSTRUMENT).maxUseDuration());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        if (stack.has(MibComponents.INSTRUMENT) && stack.get(MibComponents.INSTRUMENT).animation().isPresent() && stack.get(MibComponents.INSTRUMENT).animation().get().useAnim() != null)
            return stack.get(MibComponents.INSTRUMENT).animation().get().useAnim();
        return super.getUseAnimation(stack);
    }

    public static void applyCooldownToAllInstruments(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof MibInstrumentItem item && INSTRUMENT_ITEMS.contains(item))
                player.getCooldowns().addCooldown(item, Mib.getHelper().getInstrumentCooldown(stack, player, 40));
        }
        if (player.getOffhandItem().getItem() instanceof MibInstrumentItem item && INSTRUMENT_ITEMS.contains(item))
            player.getCooldowns().addCooldown(item, Mib.getHelper().getInstrumentCooldown(player.getOffhandItem(), player, 40));
    }
}

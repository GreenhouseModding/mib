package dev.greenhouseteam.mib.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MibInstrumentEvents {
    public static final Event<CooldownCallback> COOLDOWN = EventFactory.createArrayBacked(CooldownCallback.class, (listeners) ->  (stack, entity, original) -> {
        for (CooldownCallback listener : listeners)
            if (listener.getCooldown(stack, entity, original) != original)
                return listener.getCooldown(stack, entity, original);
        return original;
    });


    public static final Event<TickCallback> TICK = EventFactory.createArrayBacked(TickCallback.class, (listeners) ->  (level, entity, stack, useTicksRemaining) -> {
        for (TickCallback listener : listeners)
            listener.onTick(level, entity, stack, useTicksRemaining);
    });

    public static final Event<UseDurationCallback> USE_DURATION = EventFactory.createArrayBacked(UseDurationCallback.class, (listeners) ->  (stack, entity, original) -> {
        for (UseDurationCallback listener : listeners)
            if (listener.getUseDuration(stack, entity, original) != original)
                return listener.getUseDuration(stack, entity, original);
        return original;
    });

    @FunctionalInterface
    public interface CooldownCallback {
        int getCooldown(ItemStack stack, LivingEntity entity, int original);
    }

    @FunctionalInterface
    public interface TickCallback {
        void onTick(Level level, LivingEntity entity, ItemStack stack, int useTicksRemaining);
    }

    @FunctionalInterface
    public interface UseDurationCallback {
        /**
         * Allows modification of the use duration.
         * Called when an item has finished being used, whether that be releasing the item, or running out of use time.
         *
         * @param stack     The instrument stack.
         * @param entity    The entity using the instrument.
         * @param original  The original use duration.
         * @return          A new use duration, return the original if you
         *                  don't want to cancel. I'd advise mixins if you need
         *                  to modify on a non cancelling level.
         */
        int getUseDuration(ItemStack stack, LivingEntity entity, int original);
    }
}

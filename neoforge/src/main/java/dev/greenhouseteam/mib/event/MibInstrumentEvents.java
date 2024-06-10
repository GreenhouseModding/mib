package dev.greenhouseteam.mib.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.NeoForge;

public class MibInstrumentEvents {
    public static class ApplyUsageSlownessCallback extends Event implements IModBusEvent, ICancellableEvent {
        private final ItemStack stack;
        private final LivingEntity entity;
        private boolean current;

        public ApplyUsageSlownessCallback(ItemStack stack, LivingEntity entity, boolean original) {
            this.stack = stack;
            this.entity = entity;
            this.current = original;
        }

        public ItemStack getStack() {
            return stack;
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public boolean getValue() {
            return current;
        }

        public void setValue(boolean value) {
            current = value;
        }

        public static boolean post(ItemStack stack, LivingEntity entity, boolean original) {
            return NeoForge.EVENT_BUS.post(new ApplyUsageSlownessCallback(stack, entity, original)).getValue();
        }
    }

    public static class CooldownEvent extends Event implements IModBusEvent, ICancellableEvent {
        private final ItemStack stack;
        private final LivingEntity entity;
        private int current;

        public CooldownEvent(ItemStack stack, LivingEntity entity, int original) {
            this.stack = stack;
            this.entity = entity;
            this.current = original;
        }

        public ItemStack getStack() {
            return stack;
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public int getValue() {
            return current;
        }

        public void setValue(int value) {
            current = value;
        }

        public static int post(ItemStack stack, LivingEntity entity, int original) {
            return NeoForge.EVENT_BUS.post(new CooldownEvent(stack, entity, original)).getValue();
        }
    }

    public static class TickEvent extends Event implements IModBusEvent {
        private final Level level;
        private final ItemStack stack;
        private final LivingEntity entity;
        private final int useTicksRemaining;

        public TickEvent(Level level, LivingEntity entity, ItemStack stack, int useTicksRemaining) {
            this.level = level;
            this.entity = entity;
            this.stack = stack;
            this.useTicksRemaining = useTicksRemaining;
        }

        public Level getLevel() {
            return level;
        }

        public ItemStack getStack() {
            return stack;
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public int getUseTicksRemaining() {
            return useTicksRemaining;
        }

        public static void post(Level level, LivingEntity entity, ItemStack stack, int useTicksRemaining) {
            NeoForge.EVENT_BUS.post(new TickEvent(level, entity, stack, useTicksRemaining));
        }
    }

    public static class UseDurationEvent extends Event implements IModBusEvent, ICancellableEvent {
        private final ItemStack stack;
        private final LivingEntity entity;
        private int current;

        public UseDurationEvent(ItemStack stack, LivingEntity entity, int original) {
            this.stack = stack;
            this.entity = entity;
            this.current = original;
        }

        public ItemStack getStack() {
            return stack;
        }

        public LivingEntity getEntity() {
            return entity;
        }

        public int getValue() {
            return current;
        }

        public void setValue(int value) {
            current = value;
        }

        public static int post(ItemStack stack, LivingEntity entity, int original) {
            return NeoForge.EVENT_BUS.post(new UseDurationEvent(stack, entity, original)).getValue();
        }
    }
}

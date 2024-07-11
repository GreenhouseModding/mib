package dev.greenhouseteam.mib.platform;

import dev.greenhouseteam.mib.event.MibInstrumentEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class MibPlatformHelperNeoForge implements MibPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return new RegistryBuilder<>(registryKey).create();
    }

    @Override
    public int getInstrumentCooldown(ItemStack stack, LivingEntity entity, int original) {
        return MibInstrumentEvents.UseDurationEvent.post(stack, entity, original);
    }

    @Override
    public void invokeTickEvents(Level level, LivingEntity entity, ItemStack stack, int useTicksRemaining) {
        MibInstrumentEvents.TickEvent.post(level, entity, stack, useTicksRemaining);
    }

    @Override
    public int getInstrumentUseDuration(ItemStack stack, LivingEntity entity, int original) {
        return MibInstrumentEvents.UseDurationEvent.post(stack, entity, original);
    }

    @Override
    public boolean shouldApplyUseSlowness(ItemStack stack, LivingEntity entity, boolean original) {
        return MibInstrumentEvents.ApplyUsageSlownessCallback.post(stack, entity, original);
    }

    @Override
    public void sendTrackingClientboundPacket(CustomPacketPayload payload, Entity entity) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, payload);
        if (entity instanceof ServerPlayer player)
            PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void sendTrackingClientboundPacket(CustomPacketPayload payload, ServerLevel level, BlockPos pos) {
        PacketDistributor.sendToPlayersTrackingChunk(level, level.getChunk(pos).getPos(), payload);
    }
}
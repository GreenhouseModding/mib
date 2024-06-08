package dev.greenhouseteam.mib.platform;

import dev.greenhouseteam.mib.event.MibInstrumentEvents;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collection;

public class MibPlatformHelperFabric implements MibPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }

    @Override
    public int getInstrumentCooldown(ItemStack stack, LivingEntity entity, int original) {
        return MibInstrumentEvents.COOLDOWN.invoker().getCooldown(stack, entity, original);
    }

    @Override
    public void invokeTickEvents(Level level, LivingEntity entity, ItemStack stack, int useTicksRemaining) {
        MibInstrumentEvents.TICK.invoker().onTick(level, entity, stack, useTicksRemaining);
    }

    @Override
    public int getInstrumentUseDuration(ItemStack stack, LivingEntity entity, int original) {
        return MibInstrumentEvents.USE_DURATION.invoker().getUseDuration(stack, entity, original);
    }

    @Override
    public void sendTrackingClientboundPacket(CustomPacketPayload payload, Entity entity) {
        Collection<ServerPlayer> players = PlayerLookup.tracking(entity);
        for (ServerPlayer other : players) {
            ServerPlayNetworking.send(other, payload);
        }
        if (entity instanceof ServerPlayer player && !players.contains(player))
            ServerPlayNetworking.send(player, payload);
    }
}

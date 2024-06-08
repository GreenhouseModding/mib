package dev.greenhouseteam.mib.platform;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

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
    public void sendTrackingClientboundPacket(CustomPacketPayload payload, Entity entity) {
        Collection<ServerPlayer> players = PlayerLookup.tracking(entity);
        for (ServerPlayer other : players) {
            ServerPlayNetworking.send(other, payload);
        }
        if (entity instanceof ServerPlayer player && !players.contains(player))
            ServerPlayNetworking.send(player, payload);
    }
}

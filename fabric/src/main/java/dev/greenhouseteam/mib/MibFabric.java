package dev.greenhouseteam.mib;

import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.network.clientbound.StartPlayingClientboundPacket;
import dev.greenhouseteam.mib.registry.MibComponents;
import dev.greenhouseteam.mib.registry.MibItems;
import dev.greenhouseteam.mib.registry.MibRegistries;
import dev.greenhouseteam.mib.registry.MibSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.Registry;

public class MibFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        registerContents();
        registerNetwork();
    }

    public static void registerContents() {
        MibComponents.registerAll(Registry::register);
        MibItems.registerAll(Registry::register);
        MibSoundEvents.registerAll(Registry::register);

        DynamicRegistries.registerSynced(MibRegistries.SOUND_SET, MibSoundSet.DIRECT_CODEC);
    }

    public static void registerNetwork() {
        PayloadTypeRegistry.playS2C().register(StartPlayingClientboundPacket.TYPE, StartPlayingClientboundPacket.STREAM_CODEC);
    }
}

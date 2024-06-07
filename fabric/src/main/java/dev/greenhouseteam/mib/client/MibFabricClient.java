package dev.greenhouseteam.mib.client;

import dev.greenhouseteam.mib.network.clientbound.StartPlayingClientboundPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class MibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StartPlayingClientboundPacket.TYPE, (packet, context) -> packet.handle());
    }
}

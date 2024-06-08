package dev.greenhouseteam.mib.client;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.client.util.ClientUtil;
import dev.greenhouseteam.mib.network.clientbound.StartPlayingClientboundPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.mixin.object.builder.client.ModelPredicateProviderRegistryAccessor;

public class MibFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StartPlayingClientboundPacket.TYPE, (packet, context) -> packet.handle());
        ModelPredicateProviderRegistryAccessor.callRegister(Mib.asResource("playing"), ClientUtil::createPropertyFunction);
    }
}

package dev.greenhouseteam.mib.client;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.client.util.MibClientUtil;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class MibNeoForgeClient {
    @EventBusSubscriber(modid = Mib.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemProperties.registerGeneric(Mib.asResource("playing"), MibClientUtil::createPropertyFunction);
        }
    }
}

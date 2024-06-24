package dev.greenhouseteam.mib;

import dev.greenhouseteam.mib.network.clientbound.StartPlayingClientboundPacket;
import dev.greenhouseteam.mib.platform.MibPlatformHelperNeoForge;
import dev.greenhouseteam.mib.registry.MibItems;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.Map;

@Mod(Mib.MOD_ID)
public class MibNeoForge {

    public MibNeoForge(IEventBus eventBus) {
        Mib.setHelper(new MibPlatformHelperNeoForge());
    }

    @EventBusSubscriber(modid = Mib.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
            event.registrar("1.0.0")
                    .playToClient(StartPlayingClientboundPacket.TYPE, StartPlayingClientboundPacket.STREAM_CODEC, (packet, context) -> packet.handle());
        }

        @SubscribeEvent
        public static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
            addAfterAnyItem(event, Items.GOAT_HORN, MibItems.ACOUSTIC_GUITAR);
            addAfter(event, MibItems.ACOUSTIC_GUITAR, MibItems.COPPER_GOAT_HORN);
            addAfter(event, MibItems.COPPER_GOAT_HORN, MibItems.FANTASY_TRUMPET);
            addAfter(event, MibItems.FANTASY_TRUMPET, MibItems.FLUTE);
            addAfter(event, MibItems.FLUTE, MibItems.HARPSICHORD);
            addAfter(event, MibItems.HARPSICHORD, MibItems.KEYBOARD);
            addAfter(event, MibItems.KEYBOARD, MibItems.SAXOPHONE);
        }

        private static void addAfterAnyItem(BuildCreativeModeTabContentsEvent event, Item startItem, Item newItem) {
            if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                ItemStack startStack = null;
                for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : event.getEntries()) {
                    if (entry.getKey().is(startItem)) {
                        startStack = entry.getKey();
                    }
                }
                event.getEntries().putAfter(startStack, new ItemStack(newItem), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }

        private static void addAfter(BuildCreativeModeTabContentsEvent event, Item startItem, Item newItem) {
            if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                ItemStack startStack = null;
                for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : event.getEntries()) {
                    if (entry.getKey().is(startItem)) {
                        startStack = entry.getKey();
                        break;
                    }
                }
                event.getEntries().putAfter(startStack, new ItemStack(newItem), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }
}
package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.command.argument.NoteArgumentType;
import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = Mib.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MibRegistryEvents {
    @SubscribeEvent
    public static void registerContent(RegisterEvent event) {
        register(event, MibDataComponents::registerAll);
        register(event, MibInstrumentAnimations::registerAll);
        register(event, MibItems::registerAll);
        register(event, MibSoundEvents::registerAll);
        event.register(Registries.COMMAND_ARGUMENT_TYPE, Mib.asResource("note"), () -> ArgumentTypeInfos.registerByClass(NoteArgumentType.class, SingletonArgumentInfo.contextFree(NoteArgumentType::note)));
    }

    private static <T> void register(RegisterEvent event, Consumer<RegistrationCallback<T>> consumer) {
        consumer.accept((registry, id, value) ->
                event.register(registry.key(), id, () -> value));
    }

    @SubscribeEvent
    public static void createNewRegistries(NewRegistryEvent event) {
        event.register(MibRegistries.INSTRUMENT_ANIMATIONS);
    }

    @SubscribeEvent
    public static void createNewDataPackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(MibRegistries.SOUND_SET, MibSoundSet.DIRECT_CODEC, MibSoundSet.DIRECT_CODEC);
    }
}

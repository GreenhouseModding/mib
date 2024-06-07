package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

public class MibComponents {
    public static final DataComponentType<ItemInstrument> INSTRUMENT = DataComponentType.<ItemInstrument>builder()
            .persistent(ItemInstrument.CODEC)
            .networkSynchronized(ItemInstrument.STREAM_CODEC)
            .build();

    public static void registerAll(RegistrationCallback<DataComponentType<?>> callback) {
        callback.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Mib.asResource("instrument"), INSTRUMENT);
    }
}

package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class MibSoundEvents {
    public static final SoundEvent EASTERN_TRUMPET_START = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.eastern_trumpet_start"));
    public static final SoundEvent EASTERN_TRUMPET_LOOP = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.eastern_trumpet_loop"));

    public static void registerAll(RegistrationCallback<SoundEvent> callback) {
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.eastern_trumpet_start"), EASTERN_TRUMPET_START);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.eastern_trumpet_loop"), EASTERN_TRUMPET_LOOP);
    }
}

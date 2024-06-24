package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public class MibSoundEvents {
    public static final SoundEvent ACOUSTIC_GUITAR = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.acoustic_guitar"));
    public static final SoundEvent COPPER_GOAT_HORN_START = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.copper_goat_horn_start"));
    public static final SoundEvent COPPER_GOAT_HORN_LOOP = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.copper_goat_horn_loop"));
    public static final SoundEvent FANTASY_TRUMPET_START = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.fantasy_trumpet_start"));
    public static final SoundEvent FANTASY_TRUMPET_LOOP = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.fantasy_trumpet_loop"));
    public static final SoundEvent FLUTE_START = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.flute_start"));
    public static final SoundEvent FLUTE_LOOP = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.flute_loop"));
    public static final SoundEvent HARPSICHORD = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.harpsichord"));
    public static final SoundEvent KEYBOARD = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.keyboard"));
    public static final SoundEvent SAXOPHONE_START = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.saxophone_start"));
    public static final SoundEvent SAXOPHONE_LOOP = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.saxophone_loop"));
    public static final SoundEvent VIOLIN_START = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.violin_start"));
    public static final SoundEvent VIOLIN_LOOP = SoundEvent.createVariableRangeEvent(Mib.asResource("mib.instrument.violin_loop"));

    public static void registerAll(RegistrationCallback<SoundEvent> callback) {
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.acoustic_guitar"), ACOUSTIC_GUITAR);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.copper_goat_horn_loop"), COPPER_GOAT_HORN_LOOP);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.copper_goat_horn_start"), COPPER_GOAT_HORN_START);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.fantasy_trumpet_loop"), FANTASY_TRUMPET_LOOP);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.fantasy_trumpet_start"), FANTASY_TRUMPET_START);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.flute_loop"), FLUTE_LOOP);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.flute_start"), FLUTE_START);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.harpsichord"), HARPSICHORD);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.keyboard"), KEYBOARD);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.saxophone_loop"), SAXOPHONE_LOOP);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.saxophone_start"), SAXOPHONE_START);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.violin_loop"), VIOLIN_LOOP);
        callback.register(BuiltInRegistries.SOUND_EVENT, Mib.asResource("mib.instrument.violin_start"), VIOLIN_START);
    }
}
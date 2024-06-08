package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.data.animation.InstrumentAnimation;
import dev.greenhouseteam.mib.data.animation.SwingMainhandInstrumentAnimation;
import dev.greenhouseteam.mib.data.animation.SwingOffhandInstrumentAnimation;
import dev.greenhouseteam.mib.data.animation.TootInstrumentAnimation;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;

public class MibInstrumentAnimations {

    public static void registerAll(RegistrationCallback<InstrumentAnimation> callback) {
        callback.register(MibRegistries.INSTRUMENT_ANIMATIONS, Mib.asResource("swing_mainhand"), SwingMainhandInstrumentAnimation.INSTANCE);
        callback.register(MibRegistries.INSTRUMENT_ANIMATIONS, Mib.asResource("swing_offhand"), SwingOffhandInstrumentAnimation.INSTANCE);
        callback.register(MibRegistries.INSTRUMENT_ANIMATIONS, Mib.asResource("toot"), TootInstrumentAnimation.INSTANCE);
    }
}

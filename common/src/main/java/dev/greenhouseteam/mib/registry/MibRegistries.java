package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.data.animation.InstrumentAnimation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MibRegistries {
    public static final ResourceKey<Registry<MibSoundSet>> SOUND_SET = ResourceKey.createRegistryKey(Mib.asResource("sound_set"));
    public static final ResourceKey<Registry<InstrumentAnimation>> INSTRUMENT_ANIMATION = ResourceKey.createRegistryKey(Mib.asResource("instrument_animation"));

    public static final Registry<InstrumentAnimation> INSTRUMENT_ANIMATIONS = Mib.getHelper().createRegistry(INSTRUMENT_ANIMATION);
}

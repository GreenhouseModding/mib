package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.data.MibSoundSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class MibRegistries {
    public static final ResourceKey<Registry<MibSoundSet>> SOUND_SET = ResourceKey.createRegistryKey(Mib.asResource("sound_set"));
}

package dev.greenhouseteam.mib.mixin.client;

import com.google.common.collect.Multimap;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
    @Accessor("instanceToChannel")
    Map<SoundInstance, ChannelAccess.ChannelHandle> mib$getInstanceToChannel();

    @Accessor("instanceBySource")
    Multimap<SoundSource, SoundInstance> mib$getInstanceBySource();

    @Accessor("tickingSounds")
    List<TickableSoundInstance> mib$getTickingSounds();
}

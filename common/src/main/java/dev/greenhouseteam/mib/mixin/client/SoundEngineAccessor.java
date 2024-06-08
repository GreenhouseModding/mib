package dev.greenhouseteam.mib.mixin.client;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
    @Accessor("soundDeleteTime")
    Map<SoundInstance, Integer> mib$getSoundDeleteTime();

    @Accessor("tickCount")
    int mib$getTickCount();

    @Accessor("instanceToChannel")
    Map<SoundInstance, ChannelAccess.ChannelHandle> mib$getInstanceToChannel();
}

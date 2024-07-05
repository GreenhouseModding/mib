package dev.greenhouseteam.mib.mixin.client;

import dev.greenhouseteam.mib.client.sound.MibSoundInstance;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Final private SoundManager soundManager;

    @Shadow public abstract DeltaTracker getTimer();

    @Shadow public abstract boolean isPaused();

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 2))
    private void mib$bypassTicksWithMibSounds(boolean value, CallbackInfo ci) {
        if (isPaused())
            return;
        for (MibSoundInstance soundInstance : ((SoundEngineAccessor)((SoundManagerAccessor)soundManager).mib$getSoundEngine()).mib$getInstanceToChannel().keySet().stream()
                .filter(inst -> inst instanceof MibSoundInstance).map(instance -> (MibSoundInstance)instance).toList()) {
            soundInstance.bypassingTick(getTimer());
        }
    }
}

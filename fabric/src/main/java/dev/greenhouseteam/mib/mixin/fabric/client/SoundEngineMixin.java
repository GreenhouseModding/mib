package dev.greenhouseteam.mib.mixin.fabric.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import dev.greenhouseteam.mib.client.sound.MibSoundInstance;
import dev.greenhouseteam.mib.client.util.MibClientUtil;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Run before other mixins as we are removing the pitch limit.
@Mixin(value = SoundEngine.class, priority = 500)
public class SoundEngineMixin {

    @ModifyReturnValue(method = "calculatePitch", at = @At("RETURN"))
    private float mib$unrestrainPitch(float original, SoundInstance instance) {
        if (instance instanceof MibSoundInstance)
            return instance.getPitch();
        return original;
    }

    @Unique
    private static MibSoundInstance mib$soundInstance;

    @Inject(method = "play", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundBufferLibrary;getCompleteBuffer(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/concurrent/CompletableFuture;"))
    private void captureSoundBuffer(SoundInstance instance, CallbackInfo ci) {
        if (instance instanceof MibSoundInstance soundInstance)
            mib$soundInstance = soundInstance;
    }

    @Inject(method = "method_19752", at = @At("HEAD"))
    private static void captureSoundBuffer(SoundBuffer buffer, Channel channel, CallbackInfo ci) {
        if (mib$soundInstance != null && !mib$soundInstance.hasPlayedLoop())
            mib$soundInstance.setBuffer(buffer);
        mib$soundInstance = null;
    }
}

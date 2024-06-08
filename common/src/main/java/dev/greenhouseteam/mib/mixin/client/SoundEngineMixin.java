package dev.greenhouseteam.mib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.greenhouseteam.mib.client.sound.UnrestrainedPitchSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Run before other mixins as we are removing the pitch limit.
@Mixin(value = SoundEngine.class, priority = 500)
public class SoundEngineMixin {

    @ModifyReturnValue(method = "calculatePitch", at = @At("RETURN"))
    private float mib$unrestrainPitch(float original, SoundInstance instance) {
        if (instance instanceof UnrestrainedPitchSoundInstance)
            return instance.getPitch();
        return original;
    }
}

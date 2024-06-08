package dev.greenhouseteam.mib.mixin.client;

import com.mojang.blaze3d.audio.SoundBuffer;
import dev.greenhouseteam.mib.access.SoundBufferAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

@Mixin(SoundBuffer.class)
public class SoundBufferMixin implements SoundBufferAccess {
    @Unique
    private ByteBuffer mib$capturedData;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mib$captureData(ByteBuffer data, AudioFormat format, CallbackInfo ci) {
        mib$capturedData = data;
    }

    @Override
    public ByteBuffer mib$getData() {
        return mib$capturedData;
    }
}

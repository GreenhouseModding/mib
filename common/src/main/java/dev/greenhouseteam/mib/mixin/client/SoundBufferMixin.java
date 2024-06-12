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
    private int mib$bufferSize;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mib$captureBufferSize(ByteBuffer buffer, AudioFormat format, CallbackInfo ci) {
        mib$bufferSize = buffer.capacity();
    }

    @Override
    public int mib$getByteAmount() {
        return mib$bufferSize;
    }
}

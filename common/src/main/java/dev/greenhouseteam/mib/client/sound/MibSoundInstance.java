package dev.greenhouseteam.mib.client.sound;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.audio.SoundBuffer;
import dev.greenhouseteam.mib.access.SoundBufferAccess;
import dev.greenhouseteam.mib.client.util.MibClientUtil;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.mixin.client.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioFormat;
import java.util.Objects;
import java.util.function.Predicate;

public class MibSoundInstance extends AbstractTickableSoundInstance {
    @Nullable
    protected final LivingEntity living;
    protected final Predicate<LivingEntity> stopPredicate;
    protected final ExtendedSound extendedSound;
    protected boolean shouldPlayLoop;
    protected boolean shouldFade = false;
    protected boolean shouldPlayStopSound = true;
    protected float initialVolume;
    protected float tickDuration = Float.MAX_VALUE;
    @Nullable
    protected MibSoundInstance loopSound = null;

    private SoundBuffer buffer;

    protected MibSoundInstance(@Nullable LivingEntity living, double x, double y, double z, Predicate<LivingEntity> stopPredicate,
                            SoundEvent sound, ExtendedSound extendedSound,
                            float volume, float pitch, boolean isLooping,
                            boolean shouldPlayLoop) {
        super(sound, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.living = living;
        this.x = x;
        this.y = y;
        this.z = z;
        this.stopPredicate = stopPredicate;
        this.extendedSound = extendedSound;
        this.volume = volume;
        this.initialVolume = volume;
        this.pitch = pitch;
        this.looping = isLooping;
        this.shouldPlayLoop = shouldPlayLoop;
    }

    public static MibSoundInstance createBlockPosDependent(BlockPos blockPos, ExtendedSound extendedSound, float volume, float pitch) {
        Vec3 pos = blockPos.getCenter();
        return new MibSoundInstance(null, pos.x, pos.y, pos.z,
                p -> false, extendedSound.sounds().start().value(), extendedSound, volume, pitch, false, true);
    }

    public static MibSoundInstance createPosDependent(Vec3 pos, ExtendedSound extendedSound, float volume, float pitch) {
        return new MibSoundInstance(null, pos.x, pos.y, pos.z,
                p -> false, extendedSound.sounds().start().value(), extendedSound, volume, pitch, false, true);
    }

    public static MibSoundInstance createEntityDependent(LivingEntity living, ItemStack stack,
                                                  ExtendedSound extendedSound,
                                                  float volume, float pitch) {
        return new MibSoundInstance(living, living.getX(), living.getY(), living.getZ(),
                p -> !p.isUsingItem() || p.getUseItem() != stack, extendedSound.sounds().start().value(), extendedSound, volume, pitch, false, true);
    }

    public static MibSoundInstance createPosDependentStopSound(Vec3 pos,
                                                               ExtendedSound extendedSound,
                                                               float volume, float pitch) {
        if (extendedSound.sounds().stop().isEmpty())
            throw new RuntimeException("Could not create stop sound from an ExtendedSound without one.");
        return new MibSoundInstance(null, pos.x(), pos.y(), pos.z(), entity -> false, extendedSound.sounds().stop().get().value(), extendedSound, volume, pitch, false, false);
    }

    public static MibSoundInstance createEntityDependentStopSound(LivingEntity living, Predicate<LivingEntity> stopPredicate,
                                                                  ExtendedSound extendedSound,
                                                                  float volume, float pitch) {
        if (extendedSound.sounds().stop().isEmpty())
            throw new RuntimeException("Could not create stop sound from an ExtendedSound without one.");
        return new MibSoundInstance(living, living.getX(), living.getY(), living.getZ(), stopPredicate, extendedSound.sounds().stop().get().value(), extendedSound, volume, pitch, false, false);
    }

    public void bypassingTick(long ticks, DeltaTracker delta) {
        if (!shouldPlayLoop && living != null && stopPredicate.test(living) && !shouldFade) {
            if (shouldPlayStopSound && extendedSound.sounds().stop().isPresent()) {
                var instance = new MibSoundInstance(living, x, y, z, stopPredicate, extendedSound.sounds().stop().get().value(), extendedSound, volume, pitch, false, false);
                Minecraft.getInstance().getSoundManager().play(instance);
            }

            if (extendedSound.fadeSpeed().isPresent())
                shouldFade = true;
            else
                stopAndClear();
            return;
        }

        if (shouldPlayLoop && (extendedSound.fadeSpeed().isPresent() || living == null || !stopPredicate.test(living)) && getTickDuration(ticks, delta) - 0.2 <= ((float)ticks + delta.getGameTimeDeltaTicks()) && extendedSound.sounds().loop().isPresent()) {
            shouldPlayLoop = false;
            shouldPlayStopSound = false;
            stopAndClear();
            var instance = new MibSoundInstance(living, x, y, z, stopPredicate, extendedSound.sounds().loop().get().value(), extendedSound, volume, pitch, true, false);
            Minecraft.getInstance().getSoundManager().play(instance);
            loopSound = instance;
            return;
        }

        if (shouldFade && extendedSound.fadeSpeed().isPresent()) {
            volume = Math.clamp(volume - (extendedSound.fadeSpeed().get().sample(random) * pitch * initialVolume), 0.0F, 1.0F);
            if (volume < 0.005F)
                stopAndClear();
        }

        if (living != null) {
            this.x = living.getX();
            this.y = living.getY();
            this.z = living.getZ();
        }
    }

    protected SoundBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(SoundBuffer buffer) {
        this.buffer = buffer;
    }

    public void stopAndClear() {
        ((AbstractTickableSoundInstanceAccessor)this).mib$setStopped(true);
        looping = false;
        shouldPlayLoop = false;
        SoundEngine engine = ((SoundManagerAccessor)Minecraft.getInstance().getSoundManager()).mib$getSoundEngine();
        engine.stop(this);
        if (loopSound != null)
            loopSound.stopAndClear();
    }

    public float getTickDuration(long ticks, DeltaTracker delta) {
        if (tickDuration != Float.MAX_VALUE)
            return tickDuration;
        SoundBuffer buffer = getBuffer();
        if (buffer == null)
             return Float.MAX_VALUE;
        AudioFormat format = ((SoundBufferAccessor)buffer).mib$getFormat();
        float audioLength = ((((SoundBufferAccess)buffer).mib$getByteAmount() / pitch) / format.getFrameSize() / format.getFrameRate());
        tickDuration = (float)ticks + delta.getGameTimeDeltaTicks() + (audioLength * 20);
        return tickDuration;
    }

    public boolean shouldPlayLoop() {
        return shouldPlayLoop;
    }

    @Override
    public void tick() {
        // No-op
    }
}

package dev.greenhouseteam.mib.client.sound;

import com.mojang.blaze3d.audio.SoundBuffer;
import dev.greenhouseteam.mib.access.SoundBufferAccess;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.mixin.client.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioFormat;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MibSoundInstance extends AbstractTickableSoundInstance {
    @Nullable
    protected final Entity entity;
    protected final BiPredicate<Entity, Float> stopPredicate;
    protected final ExtendedSound extendedSound;
    protected boolean shouldPlayLoop;
    protected boolean shouldFade;
    protected boolean shouldPlayStopSound = true;
    protected float initialVolume;
    protected float durationBeforeLoop;
    @Nullable
    protected MibSoundInstance loopSound = null;
    protected float elapsedTicks;

    private SoundBuffer buffer;

    protected MibSoundInstance(@Nullable Entity entity, double x, double y, double z, BiPredicate<Entity, Float> stopPredicate,
                               SoundEvent sound, ExtendedSound extendedSound,
                               float volume, float pitch, boolean isLooping,
                               boolean shouldPlayLoop, boolean shouldFade,
                               float elapsedTicks) {
        super(sound, SoundSource.RECORDS, SoundInstance.createUnseededRandom());
        this.entity = entity;
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
        this.shouldFade = shouldFade;
        this.elapsedTicks = elapsedTicks;
        this.durationBeforeLoop = Float.MAX_VALUE;
    }

    public static MibSoundInstance createBlockPosDependent(BlockPos blockPos, ExtendedSound extendedSound, float volume, float pitch, float duration) {
        Vec3 pos = blockPos.getCenter();
        return new MibSoundInstance(null, pos.x, pos.y, pos.z,
                (e, t) -> duration < t, extendedSound.sounds().start().value(), extendedSound, volume, pitch, false, true, false, 0);
    }

    public static MibSoundInstance createPosDependent(Vec3 pos, ExtendedSound extendedSound, float volume, float pitch, float duration) {
        return new MibSoundInstance(null, pos.x, pos.y, pos.z,
                (e, t) -> duration < t, extendedSound.sounds().start().value(), extendedSound, volume, pitch, false, true, false, 0);
    }

    public static MibSoundInstance createFromEntityUsage(LivingEntity living, ItemStack stack,
                                                         ExtendedSound extendedSound,
                                                         float volume, float pitch) {
        return new MibSoundInstance(living, living.getX(), living.getY(), living.getZ(),
                (e, t) -> e instanceof LivingEntity l && (!l.isUsingItem() || l.getUseItem() != stack), extendedSound.sounds().start().value(), extendedSound, volume, pitch, false, true, false, 0);
    }

    public static MibSoundInstance createEntityDependentUnbound(Entity entity, ExtendedSound extendedSound,
                                                                float volume, float pitch, float duration) {
        return new MibSoundInstance(entity, entity.getX(), entity.getY(), entity.getZ(),
                (e, t) -> getDurationBeforeFade(duration, extendedSound) < t, extendedSound.sounds().start().value(), extendedSound, volume, pitch, false, true, false, 0);
    }

    private static float getDurationBeforeFade(float duration, ExtendedSound sound) {
        if (sound.fadeSpeed().isEmpty())
            return duration;
        return duration - (sound.fadeSpeed().get().sample(Minecraft.getInstance().level.getRandom()) / 1.0F);
    }

    public static MibSoundInstance createPosDependentStopSound(Vec3 pos,
                                                               ExtendedSound extendedSound,
                                                               float volume, float pitch) {
        if (extendedSound.sounds().stop().isEmpty())
            throw new RuntimeException("Could not create stop sound from an ExtendedSound without one.");
        return new MibSoundInstance(null, pos.x(), pos.y(), pos.z(), (e, t) -> false, extendedSound.sounds().stop().get().value(), extendedSound, volume, pitch, false, false, false, 0);
    }

    public static MibSoundInstance createEntityDependentStopSound(Entity living, Predicate<Entity> stopPredicate,
                                                                  ExtendedSound extendedSound,
                                                                  float volume, float pitch) {
        if (extendedSound.sounds().stop().isEmpty())
            throw new RuntimeException("Could not create stop sound from an ExtendedSound without one.");
        return new MibSoundInstance(living, living.getX(), living.getY(), living.getZ(), (e, t) -> stopPredicate.test(e), extendedSound.sounds().stop().get().value(), extendedSound, volume, pitch, false, false, false, 0);
    }

    public void bypassingTick(DeltaTracker delta) {
        elapsedTicks += delta.getGameTimeDeltaTicks();

        if (!shouldPlayLoop && (stopPredicate != null && stopPredicate.test(entity, elapsedTicks)) && !shouldFade) {
            stopOrFadeOut();
            return;
        }

        if (!shouldFade && shouldPlayLoop && (extendedSound.fadeSpeed().isPresent() || entity == null || !stopPredicate.test(entity, elapsedTicks)) && getTickDuration((long) elapsedTicks) - delta.getGameTimeDeltaTicks() < elapsedTicks) {
            shouldPlayLoop = false;
            shouldPlayStopSound = false;
            stopAndClear();
            if (extendedSound.sounds().loop().isPresent()) {
                var instance = new MibSoundInstance(entity, x, y, z, stopPredicate, extendedSound.sounds().loop().get().value(), extendedSound, volume, pitch, true, false, shouldFade, elapsedTicks);
                Minecraft.getInstance().getSoundManager().queueTickingSound(instance);
                loopSound = instance;
            }
            return;
        }

        if (shouldFade && extendedSound.fadeSpeed().isPresent()) {
            volume = Math.clamp(volume - (extendedSound.fadeSpeed().get().sample(random) * pitch * initialVolume), 0.0F, 1.0F);
            if (volume < 0.005F)
                stopAndClear();
        }

        if (entity != null) {
            this.x = entity.getX();
            this.y = entity.getY();
            this.z = entity.getZ();
        }
    }

    protected SoundBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(SoundBuffer buffer) {
        this.buffer = buffer;
    }

    public void stopOrFadeOut() {
        if (shouldPlayStopSound && extendedSound.sounds().stop().isPresent()) {
            var instance = new MibSoundInstance(entity, x, y, z, stopPredicate, extendedSound.sounds().stop().get().value(), extendedSound, volume, pitch, false, false, false, 0);
            Minecraft.getInstance().getSoundManager().queueTickingSound(instance);
        }

        if (extendedSound.fadeSpeed().isPresent()) {
            shouldFade = true;
            if (loopSound != null) {
                loopSound.shouldFade = true;
            }
            return;
        }
        stopAndClear();
    }

    public void stopAndClear() {
        stop();
        shouldPlayLoop = false;
        if (loopSound != null)
            loopSound.stopAndClear();
        Minecraft.getInstance().getSoundManager().stop(this);
    }

    public float getTickDuration(long ticks) {
        if (durationBeforeLoop != Float.MAX_VALUE)
            return durationBeforeLoop;
        SoundBuffer buffer = getBuffer();
        if (buffer == null)
             return Float.MAX_VALUE;
        AudioFormat format = ((SoundBufferAccessor)buffer).mib$getFormat();
        float audioLength = ((((SoundBufferAccess)buffer).mib$getByteAmount() / pitch) / format.getFrameSize() / format.getFrameRate());
        durationBeforeLoop = (float)ticks + (audioLength * 20);
        return durationBeforeLoop;
    }

    public boolean shouldPlayLoop() {
        return shouldPlayLoop;
    }

    @Override
    public void tick() {
        // No-op
    }
}

package dev.greenhouseteam.mib.client.sound;

import com.mojang.blaze3d.audio.SoundBuffer;
import dev.greenhouseteam.mib.access.SoundBufferAccess;
import dev.greenhouseteam.mib.client.util.MibClientUtil;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.mixin.client.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioFormat;
import java.util.function.Predicate;

public class MibSoundInstance extends AbstractTickableSoundInstance implements UnrestrainedPitchSoundInstance {
    @Nullable
    protected final LivingEntity living;
    protected final Predicate<LivingEntity> stopPredicate;
    protected final ExtendedSound extendedSound;
    protected boolean hasPlayedLoop;
    protected boolean shouldFade;
    protected boolean shouldPlayStopSound;
    protected float initialVolume;
    protected int startLoopTicks = Integer.MIN_VALUE;

    public MibSoundInstance(double x, double y, double z,
                            SoundEvent sound, ExtendedSound extendedSound,
                            float volume, float pitch, boolean isLooping, boolean shouldFade) {
        this(null, x, y, z, p -> true, sound, extendedSound, volume, pitch, isLooping, true, shouldFade);
    }

    public MibSoundInstance(LivingEntity living, ItemStack stack,
                            SoundEvent sound, ExtendedSound extendedSound,
                            float volume, float pitch, boolean isLooping, boolean shouldFade) {
        this(living, living.getX(), living.getY(), living.getZ(), p -> !p.isUsingItem() || p.getUseItem() != stack, sound, extendedSound, volume, pitch, isLooping, true, shouldFade);
    }

    public MibSoundInstance(@Nullable LivingEntity living, double x, double y, double z, Predicate<LivingEntity> stopPredicate,
                            SoundEvent sound, ExtendedSound extendedSound,
                            float volume, float pitch, boolean isLooping,
                            boolean shouldPlayLoopSound, boolean shouldFade) {
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
        this.hasPlayedLoop = !shouldPlayLoopSound;
        this.shouldPlayStopSound = true;
        this.shouldFade = shouldFade;
    }

    @Override
    public void tick() {
        if (isStopped())
            return;

        SoundEngineAccessor soundEngine = ((SoundEngineAccessor) ((SoundManagerAccessor)Minecraft.getInstance().getSoundManager()).mib$getSoundEngine());

        if (hasPlayedLoop && living != null && stopPredicate.test(living)) {
            if (shouldPlayStopSound && extendedSound.sounds().stop().isPresent())
                Minecraft.getInstance().getSoundManager().play(new MibSoundInstance(living, x, y, z, stopPredicate, extendedSound.sounds().stop().get().value(), extendedSound, volume, pitch, false, false, false));
            stop();
            return;
        }

        if (!hasPlayedLoop && getOrCalculateStartSoundStop() <= soundEngine.mib$getTickCount() && extendedSound.sounds().loop().isPresent()) {
            hasPlayedLoop = true;
            shouldPlayStopSound = false;
            Minecraft.getInstance().getSoundManager().queueTickingSound(new MibSoundInstance(living, x, y, z, stopPredicate, extendedSound.sounds().loop().get().value(), extendedSound, volume, pitch, true, false, true));
            return;
        }

        if (living != null) {
            this.x = living.getX();
            this.y = living.getY();
            this.z = living.getZ();
        }

        if (shouldFade && extendedSound.fadeSpeed().isPresent())
            volume = Math.clamp(volume - (extendedSound.fadeSpeed().get().sample(random) * pitch * initialVolume), 0.0F, 1.0F);
    }

    protected int getOrCalculateStartSoundStop() {
        if (startLoopTicks != Integer.MIN_VALUE)
            return startLoopTicks;

        SoundEngineAccessor soundEngine = ((SoundEngineAccessor) ((SoundManagerAccessor)Minecraft.getInstance().getSoundManager()).mib$getSoundEngine());
        if (!soundEngine.mib$getInstanceToChannel().containsKey(this))
            return 0;
        SoundBuffer buffer = MibClientUtil.getSoundBuffer();
        if (buffer == null)
            return Integer.MAX_VALUE;
        AudioFormat format = ((SoundBufferAccessor)buffer).mib$getFormat();
        float nonTickValue = ((float)((SoundBufferAccess)buffer).mib$getData().capacity() / format.getFrameSize()) / format.getFrameRate();
        startLoopTicks = soundEngine.mib$getTickCount() + (int) (nonTickValue * 20 / pitch) - 2;
        MibClientUtil.clearSoundBuffer();
        return startLoopTicks;
    }
}

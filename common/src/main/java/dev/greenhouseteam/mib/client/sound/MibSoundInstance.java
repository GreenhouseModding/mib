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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.AudioFormat;
import java.util.function.Predicate;

public class MibSoundInstance extends AbstractTickableSoundInstance implements UnrestrainedPitchSoundInstance {
    @Nullable
    protected final Player player;
    protected final Predicate<Player> stopPredicate;
    protected final ExtendedSound extendedSound;
    protected boolean hasPlayedLoop;
    protected boolean shouldPlayStopSound;
    protected int startLoopTicks = Integer.MIN_VALUE;

    public MibSoundInstance(double x, double y, double z, SoundEvent sound,
                            ExtendedSound extendedSound, SoundSource source,
                            float volume, float pitch, boolean isLooping) {
        this(null, x, y, z, p -> true, sound, extendedSound, source, volume, pitch, isLooping, true);
    }

    public MibSoundInstance(Player player, ItemStack stack, SoundEvent sound,
                            ExtendedSound extendedSound, SoundSource source,
                            float volume, float pitch, boolean isLooping) {
        this(player, player.getX(), player.getY(), player.getZ(), p -> !p.isUsingItem() || p.getUseItem() != stack, sound, extendedSound, source, volume, pitch, isLooping, true);
    }

    protected MibSoundInstance(@Nullable Player player, double x, double y, double z, Predicate<Player> stopPredicate, SoundEvent sound,
                            ExtendedSound extendedSound, SoundSource source,
                            float volume, float pitch, boolean isLooping,
                            boolean shouldPlayLoopSound) {
        super(sound, source, SoundInstance.createUnseededRandom());
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.stopPredicate = stopPredicate;
        this.extendedSound = extendedSound;
        this.volume = volume;
        this.pitch = pitch;
        this.looping = isLooping;
        this.hasPlayedLoop = !shouldPlayLoopSound;
        this.shouldPlayStopSound = true;
    }

    @Override
    public void tick() {
        if (isStopped())
            return;

        SoundEngineAccessor soundEngine = ((SoundEngineAccessor) ((SoundManagerAccessor)Minecraft.getInstance().getSoundManager()).mib$getSoundEngine());

        if (hasPlayedLoop && player != null && stopPredicate.test(player)) {
            if (shouldPlayStopSound && extendedSound.sounds().stop().isPresent())
                Minecraft.getInstance().getSoundManager().play(new MibSoundInstance(player, x, y, z, stopPredicate, extendedSound.sounds().stop().get().value(), extendedSound, source, this.volume, this.pitch, false, false));
            stop();
            return;
        }

        if (!hasPlayedLoop && getOrCalculateStartSoundStop() <= soundEngine.mib$getTickCount() && extendedSound.sounds().loop().isPresent()) {
            hasPlayedLoop = true;
            shouldPlayStopSound = false;
            Minecraft.getInstance().getSoundManager().queueTickingSound(new MibSoundInstance(player, x, y, z, stopPredicate, extendedSound.sounds().loop().get().value(), extendedSound, source, volume, pitch, true, false));
            return;
        }

        if (player != null) {
            this.x = player.getX();
            this.y = player.getY();
            this.z = player.getZ();
        }
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
        float nonTickValue = (((SoundBufferAccess)buffer).mib$getData().capacity() / format.getFrameSize()) / format.getFrameRate();
        startLoopTicks = soundEngine.mib$getTickCount() + (int) (nonTickValue * 20 / pitch) - 2;
        MibClientUtil.clearSoundBuffer();
        return startLoopTicks;
    }
}

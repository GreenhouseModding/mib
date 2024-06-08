package dev.greenhouseteam.mib.client.sound;

import com.mojang.blaze3d.audio.SoundBuffer;
import dev.greenhouseteam.mib.access.PlayerAccess;
import dev.greenhouseteam.mib.access.SoundBufferAccess;
import dev.greenhouseteam.mib.client.util.MibClientUtil;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.mixin.client.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
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
        this(null, x, y, z, p -> true, sound, extendedSound, source, volume, pitch, isLooping);
    }

    public MibSoundInstance(Player player, ItemStack stack, SoundEvent sound,
                            ExtendedSound extendedSound, SoundSource source,
                            float volume, float pitch, boolean isLooping) {
        this(player, player.getX(), player.getY(), player.getZ(), p -> !p.isUsingItem() || p.getUseItem() != stack, sound, extendedSound, source, volume, pitch, isLooping);
    }

    public MibSoundInstance(@Nullable Player player, double x, double y, double z, Predicate<Player> stopPredicate, SoundEvent sound,
                            ExtendedSound extendedSound, SoundSource source,
                            float volume, float pitch, boolean isLooping) {
        super(sound, source, SoundInstance.createUnseededRandom());
        if (player != null)
            ((PlayerAccess)player).mib$setCurrentSoundInstance(this);
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.stopPredicate = stopPredicate;
        this.extendedSound = extendedSound;
        this.volume = volume;
        this.pitch = pitch;
        this.looping = isLooping;
        this.hasPlayedLoop = isLooping;
        this.shouldPlayStopSound = true;
    }

    @Override
    public void tick() {
        SoundEngineAccessor soundEngine = ((SoundEngineAccessor) ((SoundManagerAccessor)Minecraft.getInstance().getSoundManager()).mib$getSoundEngine());
        if (!hasPlayedLoop && getOrCalculateStartSoundStop() <= soundEngine.mib$getTickCount() && extendedSound.sounds().loop().isPresent()) {
            hasPlayedLoop = true;
            shouldPlayStopSound = false;
            Minecraft.getInstance().getSoundManager().queueTickingSound(new MibSoundInstance(player, x, y, z, stopPredicate, extendedSound.sounds().loop().get().value(), extendedSound, source, volume, pitch, true));
        }

        if (!hasPlayedLoop && getOrCalculateStartSoundStop() <= soundEngine.mib$getTickCount() || (hasPlayedLoop && player != null && stopPredicate.test(player))) {
            if (shouldPlayStopSound && extendedSound.sounds().stop().isPresent())
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(extendedSound.sounds().stop().get().value(), this.volume, this.pitch));
            ((PlayerAccess)player).mib$setCurrentSoundInstance(null);
            stop();
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
        startLoopTicks = soundEngine.mib$getSoundDeleteTime().get(this) - 20 + (int) (nonTickValue * 20 / pitch);
        MibClientUtil.clearSoundBuffer();
        return startLoopTicks;
    }
}

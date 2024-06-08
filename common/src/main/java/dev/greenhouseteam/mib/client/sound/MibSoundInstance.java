package dev.greenhouseteam.mib.client.sound;

import dev.greenhouseteam.mib.access.PlayerAccess;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.mixin.client.SoundEngineAccessor;
import dev.greenhouseteam.mib.mixin.client.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MibSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;
    private final ItemStack stack;
    private final ExtendedSound extendedSound;
    private boolean hasPlayedLoop;

    public MibSoundInstance(Player player, ItemStack stack, SoundEvent sound,
                            ExtendedSound extendedSound, SoundSource source,
                            float volume, float pitch, boolean isLooping) {
        super(sound, source, SoundInstance.createUnseededRandom());
        this.player = player;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
        ((PlayerAccess)this.player).mib$setCurrentSoundInstance(this);
        this.stack = stack;
        this.extendedSound = extendedSound;
        this.volume = volume;
        this.pitch = pitch;
        this.looping = isLooping;
        this.hasPlayedLoop = isLooping;
    }

    @Override
    public void tick() {
        if (extendedSound.looping() && hasPlayedLoop && (!player.isUsingItem() || player.getUseItem() != stack || ((PlayerAccess)this.player).mib$getSoundInstance() != this)) {
            if (extendedSound.stopSound().isPresent())
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(extendedSound.stopSound().get().value(), this.volume, this.pitch));
            stop();
            return;
        }

        SoundEngineAccessor soundEngine = ((SoundEngineAccessor) ((SoundManagerAccessor)Minecraft.getInstance().getSoundManager()).mib$getSoundEngine());
        if (extendedSound.looping() && !hasPlayedLoop && soundEngine.mib$getSoundDeleteTime().containsKey(this) && soundEngine.mib$getSoundDeleteTime().get(this) - 20 + extendedSound.durationBeforeLoop() <= soundEngine.mib$tickCount()) {
            this.hasPlayedLoop = true;
            Minecraft.getInstance().getSoundManager().queueTickingSound(new MibSoundInstance(player, stack, extendedSound.loopSound().orElse(extendedSound.startSound()).value(), extendedSound, source, this.volume, this.pitch, true));
            return;
        }

        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }
}

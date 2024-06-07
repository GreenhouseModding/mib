package dev.greenhouseteam.mib.client.util;

import dev.greenhouseteam.mib.client.sound.MibSoundInstance;
import dev.greenhouseteam.mib.data.ExtendedSound;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ClientUtil {
    public static void queueSound(Player player, InteractionHand hand, ExtendedSound extendedSound, float volume, float pitch) {
        Minecraft.getInstance().getSoundManager().play(new MibSoundInstance(
                player,
                player.getItemInHand(hand),
                extendedSound.startSound().value(),
                extendedSound,
                player.getSoundSource(),
                volume,
                pitch,
                false)
        );
    }
}

package dev.greenhouseteam.mib.client.util;

import dev.greenhouseteam.mib.client.sound.MibSoundInstance;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.item.MibInstrumentItem;
import dev.greenhouseteam.mib.registry.MibComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ClientUtil {
    public static void queueSound(Player player, InteractionHand hand, ExtendedSound extendedSound, float volume, float pitch) {
        Minecraft.getInstance().getSoundManager().play(new MibSoundInstance(
                player,
                player.getItemInHand(hand),
                extendedSound.sounds().start().value(),
                extendedSound,
                player.getSoundSource(),
                volume,
                pitch,
                false)
        );
    }

    public static float createPropertyFunction(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return stack.getItem() instanceof MibInstrumentItem && stack.has(MibComponents.INSTRUMENT) && entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
    }
}

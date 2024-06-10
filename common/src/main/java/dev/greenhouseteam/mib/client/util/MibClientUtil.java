package dev.greenhouseteam.mib.client.util;

import com.mojang.blaze3d.audio.SoundBuffer;
import dev.greenhouseteam.mib.client.sound.MibSoundInstance;
import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.data.animation.FluteInstrumentAnimation;
import dev.greenhouseteam.mib.item.MibInstrumentItem;
import dev.greenhouseteam.mib.registry.MibComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MibClientUtil {
    public static void queueSound(Player player, InteractionHand hand, ExtendedSound extendedSound, float volume, float pitch) {
        Minecraft.getInstance().getSoundManager().play(new MibSoundInstance(
                player,
                player.getItemInHand(hand),
                extendedSound.sounds().start().value(),
                extendedSound,
                volume,
                pitch,
                false,
                false)
        );
    }

    public static float createPropertyFunction(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return stack.getItem() instanceof MibInstrumentItem && stack.has(MibComponents.INSTRUMENT) && entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
    }

    public static boolean poseArms(AbstractClientPlayer player, ItemStack stack, boolean left, ModelPart mainArm, ModelPart offArm, HumanoidModel<AbstractClientPlayer> model) {
        if (stack.has(MibComponents.INSTRUMENT) && player.isUsingItem() && player.getUseItem() == stack) {
            ItemInstrument component = stack.get(MibComponents.INSTRUMENT);
            if (component.animation().isPresent() && component.animation().get() instanceof FluteInstrumentAnimation) {
                // TODO: Test this.
                mainArm.xRot = Mth.clamp(model.head.xRot, -1.2F, 1.2F) - 1.65F;
                mainArm.yRot = (float) (model.head.yRot + (left ? Math.PI / 6.0F : -Math.PI / 6.0F));
                offArm.xRot = Mth.clamp(model.head.xRot, -1.2F, 1.2F) - 1.75F;
                offArm.yRot = (float) (model.head.yRot + (left ? Math.PI / 16.0F : -Math.PI / 16.0F));
                return true;
            }
        }

        return false;
    }

    private static SoundBuffer capturedBuffer;

    public static void captureSoundBuffer(SoundBuffer buffer) {
        capturedBuffer = buffer;
    }

    public static SoundBuffer getSoundBuffer() {
        return capturedBuffer;
    }

    public static void clearSoundBuffer() {
        capturedBuffer = null;
    }
}

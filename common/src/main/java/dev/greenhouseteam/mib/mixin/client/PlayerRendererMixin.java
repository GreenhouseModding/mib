package dev.greenhouseteam.mib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.registry.MibComponents;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @ModifyExpressionValue(method = "setModelProperties", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel$ArmPose;isTwoHanded()Z"))
    private boolean mib$modifyToTwoHanded(boolean original, AbstractClientPlayer player) {
        if (player.getItemInHand(player.getUsedItemHand()).has(MibComponents.INSTRUMENT)) {
            ItemInstrument component = player.getItemInHand(player.getUsedItemHand()).get(MibComponents.INSTRUMENT);
            if (component.animation().isPresent() && component.animation().get().isTwoHanded())
                return true;
        }
        return original;
    }
}

package dev.greenhouseteam.mib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.item.MibInstrumentItem;
import dev.greenhouseteam.mib.registry.MibComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Shadow public abstract boolean isUsingItem();

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean mib$should(boolean original) {
        if (isUsingItem() && getUseItem().getItem() instanceof MibInstrumentItem &&
                getUseItem().has(MibComponents.INSTRUMENT))
            return Mib.getHelper().shouldApplyUseSlowness(getUseItem(), this, false);
        return original;
    }
}

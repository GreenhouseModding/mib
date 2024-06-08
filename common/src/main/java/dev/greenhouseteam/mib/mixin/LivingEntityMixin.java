package dev.greenhouseteam.mib.mixin;

import dev.greenhouseteam.mib.Mib;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow protected ItemStack useItem;

    @Inject(method = "stopUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setLivingEntityFlag(IZ)V"))
    private void mib$setInstrumentCooldown(CallbackInfo ci) {
        if ((LivingEntity)(Object)this instanceof Player player)
            player.getCooldowns().addCooldown(useItem.getItem(), Mib.getHelper().getInstrumentCooldown(useItem, player, 40));
    }
}

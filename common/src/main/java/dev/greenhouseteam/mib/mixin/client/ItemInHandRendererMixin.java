package dev.greenhouseteam.mib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.greenhouseteam.mib.item.MibInstrumentItem;
import dev.greenhouseteam.mib.registry.MibComponents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @ModifyReturnValue(method = "evaluateWhichHandsToRender", at = @At(value = "RETURN", ordinal = 0))
    private static ItemInHandRenderer.HandRenderSelection mib$renderInstrumentHandInMainhand(ItemInHandRenderer.HandRenderSelection original, LocalPlayer player) {
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof MibInstrumentItem && player.getUseItem().has(MibComponents.INSTRUMENT)) {
            ItemStack stack = player.getUseItem();
            if (stack.get(MibComponents.INSTRUMENT).animation().isPresent() && stack.get(MibComponents.INSTRUMENT).animation().get().isTwoHanded())
                return player.getUsedItemHand() == InteractionHand.OFF_HAND ? ItemInHandRenderer.HandRenderSelection.RENDER_OFF_HAND_ONLY : ItemInHandRenderer.HandRenderSelection.RENDER_MAIN_HAND_ONLY;
        }
        return original;
    }
}

package dev.greenhouseteam.mib.mixin.client;

import dev.greenhouseteam.mib.client.util.MibClientUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart rightArm;

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void mib$poseRightArmWhenPlayingInstruments(T entity, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer player))
            return;
        if (MibClientUtil.poseArms(player, entity.getItemInHand(entity.getUsedItemHand()), false, rightArm, leftArm, (HumanoidModel<AbstractClientPlayer>) (Object) this))
            ci.cancel();
    }

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    private void mib$poseLeftArmWhenPlayingInstruments(T entity, CallbackInfo ci) {
        if (!(entity instanceof AbstractClientPlayer player))
            return;

        if (MibClientUtil.poseArms(player, entity.getItemInHand(entity.getUsedItemHand()), true, leftArm, rightArm, (HumanoidModel<AbstractClientPlayer>) (Object) this))
            ci.cancel();
    }
}

package dev.greenhouseteam.mib.data.animation;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public interface InstrumentAnimation  {
    @Nullable
    UseAnim useAnim();

    EnumSet<InteractionHand> handsToSwing();
}

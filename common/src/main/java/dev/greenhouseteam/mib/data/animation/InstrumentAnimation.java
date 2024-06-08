package dev.greenhouseteam.mib.data.animation;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public interface InstrumentAnimation  {
    @Nullable
    default UseAnim useAnim() {
        return null;
    }

    default EnumSet<InteractionHand> handsToSwing() {
        return EnumSet.noneOf(InteractionHand.class);
    }

    default boolean isTwoHanded() {
        return false;
    }
}

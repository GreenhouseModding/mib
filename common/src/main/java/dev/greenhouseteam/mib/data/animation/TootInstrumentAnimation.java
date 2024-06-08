package dev.greenhouseteam.mib.data.animation;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class TootInstrumentAnimation implements InstrumentAnimation {
    public static final InstrumentAnimation INSTANCE = new TootInstrumentAnimation();

    protected TootInstrumentAnimation() {}

    @Override
    public @Nullable UseAnim useAnim() {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public EnumSet<InteractionHand> handsToSwing() {
        return EnumSet.noneOf(InteractionHand.class);
    }
}

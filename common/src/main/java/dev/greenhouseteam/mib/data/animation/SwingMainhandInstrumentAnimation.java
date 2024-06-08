package dev.greenhouseteam.mib.data.animation;

import net.minecraft.world.InteractionHand;

import java.util.EnumSet;

public class SwingMainhandInstrumentAnimation implements InstrumentAnimation {
    public static final InstrumentAnimation INSTANCE = new SwingMainhandInstrumentAnimation();

    protected SwingMainhandInstrumentAnimation() {}

    @Override
    public EnumSet<InteractionHand> handsToSwing() {
        return EnumSet.of(InteractionHand.MAIN_HAND);
    }
}

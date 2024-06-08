package dev.greenhouseteam.mib.data.animation;

import net.minecraft.world.InteractionHand;

import java.util.EnumSet;

public class SwingOffhandInstrumentAnimation implements InstrumentAnimation {
    public static final InstrumentAnimation INSTANCE = new SwingOffhandInstrumentAnimation();

    protected SwingOffhandInstrumentAnimation() {}

    @Override
    public EnumSet<InteractionHand> handsToSwing() {
        return EnumSet.of(InteractionHand.OFF_HAND);
    }
}

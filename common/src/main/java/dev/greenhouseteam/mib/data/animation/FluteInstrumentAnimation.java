package dev.greenhouseteam.mib.data.animation;

public class FluteInstrumentAnimation implements InstrumentAnimation {
    public static final InstrumentAnimation INSTANCE = new FluteInstrumentAnimation();

    protected FluteInstrumentAnimation() {}

    public boolean isTwoHanded() {
        return true;
    }
}

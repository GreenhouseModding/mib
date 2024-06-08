package dev.greenhouseteam.mib.data.animation;

import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;

public class TootInstrumentAnimation implements InstrumentAnimation {
    public static final InstrumentAnimation INSTANCE = new TootInstrumentAnimation();

    protected TootInstrumentAnimation() {}

    @Override
    public @Nullable UseAnim useAnim() {
        return UseAnim.TOOT_HORN;
    }
}

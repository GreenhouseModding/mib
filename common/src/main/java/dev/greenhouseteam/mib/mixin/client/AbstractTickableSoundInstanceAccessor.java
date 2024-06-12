package dev.greenhouseteam.mib.mixin.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractTickableSoundInstance.class)
public interface AbstractTickableSoundInstanceAccessor {
    @Accessor("stopped")
    void mib$setStopped(boolean value);
}

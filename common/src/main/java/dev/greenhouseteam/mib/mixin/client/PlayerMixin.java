package dev.greenhouseteam.mib.mixin.client;

import dev.greenhouseteam.mib.access.PlayerAccess;
import dev.greenhouseteam.mib.client.sound.MibSoundInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements PlayerAccess {
    @Unique
    private MibSoundInstance mib$soundInstance;

    @Override
    public MibSoundInstance mib$getSoundInstance() {
        return mib$soundInstance;
    }

    @Override
    public void mib$setCurrentSoundInstance(MibSoundInstance instance) {
        mib$soundInstance = instance;
    }
}

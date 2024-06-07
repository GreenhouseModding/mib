package dev.greenhouseteam.mib.access;

import dev.greenhouseteam.mib.client.sound.MibSoundInstance;

public interface PlayerAccess {
    MibSoundInstance mib$getSoundInstance();
    void mib$setCurrentSoundInstance(MibSoundInstance instance);
}

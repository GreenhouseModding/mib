package dev.greenhouseteam.mib;

import dev.greenhouseteam.mib.platform.MibPlatformHelperFabric;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class MibFabricPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        Mib.setHelper(new MibPlatformHelperFabric());
    }
}

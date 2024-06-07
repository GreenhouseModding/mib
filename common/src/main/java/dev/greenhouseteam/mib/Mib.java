package dev.greenhouseteam.mib;

import dev.greenhouseteam.mib.platform.MibPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mib {

    public static final String MOD_ID = "mib";
    public static final String MOD_NAME = "Mib";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);


    private static MibPlatformHelper helper;

    public static void init() {
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static MibPlatformHelper getHelper() {
        return helper;
    }

    public static void setHelper(MibPlatformHelper helper) {
        Mib.helper = helper;
    }
}
package dev.greenhouseteam.mib.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public interface MibPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    <T> Registry<T> createRegistry(ResourceKey<Registry<T>> registryKey);

    int getInstrumentCooldown(ItemStack stack, LivingEntity entity, int original);

    void invokeTickEvents(Level level, LivingEntity entity, ItemStack stack, int useTicksRemaining);

    int getInstrumentUseDuration(ItemStack stack, LivingEntity entity, int original);

    boolean shouldApplyUseSlowness(ItemStack stack, LivingEntity entity, boolean original);

    void sendTrackingClientboundPacket(CustomPacketPayload payload, Entity entity);

    void sendTrackingClientboundPacket(CustomPacketPayload payload, ServerLevel level, BlockPos pos);
}
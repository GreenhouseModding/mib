package dev.greenhouseteam.mib.network.clientbound;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.client.util.ClientUtil;
import dev.greenhouseteam.mib.data.ExtendedSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public record StartPlayingClientboundPacket(int entityId, boolean offhand, ExtendedSound extendedSound, float volume, float pitch) implements CustomPacketPayload {
    public static final ResourceLocation ID = Mib.asResource("start_playing");
    public static final Type<StartPlayingClientboundPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, StartPlayingClientboundPacket> STREAM_CODEC = StreamCodec.of(StartPlayingClientboundPacket::write, StartPlayingClientboundPacket::new);

    public StartPlayingClientboundPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean(), ExtendedSound.STREAM_CODEC.decode(buf), buf.readFloat(), buf.readFloat());
    }

    public static void write(RegistryFriendlyByteBuf buf, StartPlayingClientboundPacket packet) {
         buf.writeInt(packet.entityId);
         buf.writeBoolean(packet.offhand);
         ExtendedSound.STREAM_CODEC.encode(buf, packet.extendedSound);
         buf.writeFloat(packet.volume);
         buf.writeFloat(packet.pitch);
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId);
            if (!(entity instanceof Player player))
                return;
            InteractionHand hand = offhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            ClientUtil.queueSound(player, hand, extendedSound, volume, pitch);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

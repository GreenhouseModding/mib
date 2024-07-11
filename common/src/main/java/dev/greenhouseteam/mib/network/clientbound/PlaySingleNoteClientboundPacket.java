package dev.greenhouseteam.mib.network.clientbound;

import com.mojang.datafixers.util.Either;
import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.client.util.MibClientUtil;
import dev.greenhouseteam.mib.data.ExtendedSound;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record PlaySingleNoteClientboundPacket(Either<Integer, Vec3> entityIdOrPos, ExtendedSound extendedSound, float volume, float pitch, float duration) implements CustomPacketPayload {
    public static final ResourceLocation ID = Mib.asResource("play_single_note");
    public static final Type<PlaySingleNoteClientboundPacket> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, PlaySingleNoteClientboundPacket> STREAM_CODEC = StreamCodec.of(PlaySingleNoteClientboundPacket::write, PlaySingleNoteClientboundPacket::new);

    public PlaySingleNoteClientboundPacket(RegistryFriendlyByteBuf buf) {
        this(ByteBufCodecs.either(ByteBufCodecs.INT, ByteBufCodecs.fromCodec(Vec3.CODEC)).decode(buf), ExtendedSound.STREAM_CODEC.decode(buf), buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void write(RegistryFriendlyByteBuf buf, PlaySingleNoteClientboundPacket packet) {
        ByteBufCodecs.either(ByteBufCodecs.INT, ByteBufCodecs.fromCodec(Vec3.CODEC)).encode(buf, packet.entityIdOrPos);
        ExtendedSound.STREAM_CODEC.encode(buf, packet.extendedSound);
        buf.writeFloat(packet.volume);
        buf.writeFloat(packet.pitch);
        buf.writeFloat(packet.duration);
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            entityIdOrPos.ifLeft(id -> MibClientUtil.queueSingleNoteOnEntity(Minecraft.getInstance().level.getEntity(id), extendedSound, volume, pitch, duration))
                    .ifRight(pos -> MibClientUtil.queueSingleNoteAtPos(pos, extendedSound, volume, pitch, duration));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

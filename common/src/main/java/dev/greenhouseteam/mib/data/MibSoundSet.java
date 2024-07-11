package dev.greenhouseteam.mib.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.network.clientbound.PlaySingleNoteClientboundPacket;
import dev.greenhouseteam.mib.network.clientbound.StartPlayingClientboundPacket;
import dev.greenhouseteam.mib.registry.MibRegistries;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MibSoundSet {
    public static final Codec<MibSoundSet> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.unboundedMap(NoteWithOctave.CODEC, ExtendedSound.CODEC.listOf()).fieldOf("sounds").forGetter(mibInstrument -> mibInstrument.octaveKeyToSoundMap)
    ).apply(inst, MibSoundSet::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, MibSoundSet> DIRECT_STREAM_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.fromCodec(NoteWithOctave.CODEC), ExtendedSound.STREAM_CODEC.apply(ByteBufCodecs.list())).map(MibSoundSet::new, mibSoundSet -> new Object2ObjectOpenHashMap<>(mibSoundSet.octaveKeyToSoundMap));

    public static final Codec<Holder<MibSoundSet>> CODEC = RegistryFileCodec.create(MibRegistries.SOUND_SET, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MibSoundSet>> STREAM_CODEC = ByteBufCodecs.holder(MibRegistries.SOUND_SET, DIRECT_STREAM_CODEC);

    private final Map<NoteWithOctave, List<ExtendedSound>> octaveKeyToSoundMap;

    public MibSoundSet(Map<NoteWithOctave, List<ExtendedSound>> map) {
        this.octaveKeyToSoundMap = map;
    }

    public int playSoundWithLength(Collection<ServerPlayer> players, Entity entity, MibNote note, int octave, float volume, float length) {
        return playSoundWithLength(players, Either.left(entity), note, octave, volume, length);
    }

    public int playSoundWithLength(Collection<ServerPlayer> players, Vec3 pos, MibNote note, int octave, float volume, float length) {
        return playSoundWithLength(players, Either.right(pos), note, octave, volume, length);
    }

    public int playSoundWithLength(Collection<ServerPlayer> players, Either<Entity, Vec3> entityOrPos, MibNote note, int octave, float volume, float duration) {
        NoteWithOctave keyWithOctave = new NoteWithOctave(note, octave);
        var sound = getSound(keyWithOctave, volume);
        if (sound == null)
            return 0;
        NoteWithOctave closest = getClosestDefined(keyWithOctave);

        Vec3 pos = entityOrPos.map(Entity::position, p -> p);
        var entityIdOrPos = entityOrPos.mapLeft(Entity::getId);
        float pitch = keyWithOctave.getPitchFromNote(closest);

        double range = Mth.square(sound.sounds().start().value().getRange(volume));
        int i = 0;
        for (ServerPlayer player : players) {
            double xDiff = pos.x - player.getX();
            double yDiff = pos.y - player.getY();
            double zDiff = pos.z - player.getZ();
            double diffSqr = xDiff * xDiff + yDiff * yDiff + zDiff * zDiff;
            if (diffSqr > range)
                continue;
            Mib.getHelper().sendTrackingClientboundPacket(new PlaySingleNoteClientboundPacket(entityIdOrPos, sound, volume, pitch, duration), player);
            ++i;
        }
        return i;
    }

    public void playSound(LivingEntity player, InteractionHand hand, MibNote note, int octave, float volume) {
        NoteWithOctave keyWithOctave = new NoteWithOctave(note, octave);
        var sound = getSound(keyWithOctave, volume);
        if (sound == null)
            return;
        NoteWithOctave closest = getClosestDefined(keyWithOctave);
        Mib.getHelper().sendTrackingClientboundPacket(new StartPlayingClientboundPacket(player.getId(), hand == InteractionHand.OFF_HAND, sound, volume, keyWithOctave.getPitchFromNote(closest)), player);
    }

    public NoteWithOctave getClosestDefined(NoteWithOctave keyWithOctave) {
        if (octaveKeyToSoundMap.containsKey(keyWithOctave))
            return keyWithOctave;
        NoteWithOctave withOctave = octaveKeyToSoundMap.keySet().stream().filter(v -> v.getValue() < keyWithOctave.getValue()).min(Comparator.comparingInt(value -> value.getValue() - keyWithOctave.getValue())).orElse(null);
        if (withOctave != null)
            return withOctave;
        return octaveKeyToSoundMap.keySet().stream().filter(v -> v.getValue() > keyWithOctave.getValue()).min(Comparator.comparingInt(value -> value.getValue() - keyWithOctave.getValue())).orElse(null);
    }

    @Nullable
    public ExtendedSound getSound(NoteWithOctave keyWithOctave, float volume) {
        ExtendedSound soundEvent = null;
        if (octaveKeyToSoundMap.containsKey(keyWithOctave))
            soundEvent = octaveKeyToSoundMap.get(keyWithOctave).stream().filter(sound -> sound.isVolumeWithinRange(volume)).min(Comparator.comparingDouble(value -> value.volumeComparison(volume))).orElse(null);

        if (soundEvent == null)
            soundEvent = octaveKeyToSoundMap.get(getClosestDefined(keyWithOctave)).stream().filter(sound -> sound.isVolumeWithinRange(volume)).min(Comparator.comparingDouble(value -> value.volumeComparison(volume))).orElse(null);

        return soundEvent;
    }
}

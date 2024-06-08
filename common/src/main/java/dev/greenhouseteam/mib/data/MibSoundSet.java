package dev.greenhouseteam.mib.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.network.clientbound.StartPlayingClientboundPacket;
import dev.greenhouseteam.mib.registry.MibRegistries;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MibSoundSet {
    public static final Codec<MibSoundSet> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.unboundedMap(KeyWithOctave.CODEC, ExtendedSound.CODEC.listOf()).fieldOf("sounds").forGetter(mibInstrument -> mibInstrument.octaveKeyToSoundMap)
    ).apply(inst, MibSoundSet::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, MibSoundSet> DIRECT_STREAM_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.fromCodec(KeyWithOctave.CODEC), ExtendedSound.STREAM_CODEC.apply(ByteBufCodecs.list())).map(MibSoundSet::new, mibSoundSet -> new Object2ObjectOpenHashMap<>(mibSoundSet.octaveKeyToSoundMap));

    public static final Codec<Holder<MibSoundSet>> CODEC = RegistryFileCodec.create(MibRegistries.SOUND_SET, DIRECT_CODEC);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MibSoundSet>> STREAM_CODEC = ByteBufCodecs.holder(MibRegistries.SOUND_SET, DIRECT_STREAM_CODEC);

    private final Map<KeyWithOctave, List<ExtendedSound>> octaveKeyToSoundMap;

    public MibSoundSet(Map<KeyWithOctave, List<ExtendedSound>> map) {
        this.octaveKeyToSoundMap = map;
    }

    public void playSound(Player player, InteractionHand hand, Key key, int octave, float velocity) {
        KeyWithOctave keyWithOctave = new KeyWithOctave(key, octave);
        var sound = getSound(keyWithOctave, velocity);
        if (sound == null)
            return;
        KeyWithOctave closest = getClosestDefined(keyWithOctave);
        Mib.getHelper().sendTrackingClientboundPacket(new StartPlayingClientboundPacket(player.getId(), hand == InteractionHand.OFF_HAND, sound, velocity, keyWithOctave.getPitchFromNote(closest)), player);
    }

    public KeyWithOctave getClosestDefined(KeyWithOctave keyWithOctave) {
        if (octaveKeyToSoundMap.containsKey(keyWithOctave))
            return keyWithOctave;
        KeyWithOctave withOctave = octaveKeyToSoundMap.keySet().stream().filter(v -> v.getValue() < keyWithOctave.getValue()).min(Comparator.comparingInt(value -> value.getValue() - keyWithOctave.getValue())).orElse(null);
        if (withOctave != null)
            return withOctave;
        return octaveKeyToSoundMap.keySet().stream().filter(v -> v.getValue() > keyWithOctave.getValue()).min(Comparator.comparingInt(value -> value.getValue() - keyWithOctave.getValue())).orElse(null);
    }

    @Nullable
    public ExtendedSound getSound(KeyWithOctave keyWithOctave, float velocity) {
        ExtendedSound soundEvent = null;
        if (octaveKeyToSoundMap.containsKey(keyWithOctave))
            soundEvent = octaveKeyToSoundMap.get(keyWithOctave).stream().filter(sound -> sound.isVelocityWithinRange(velocity)).min(Comparator.comparingDouble(value -> value.velocityComparison(velocity))).orElse(null);

        if (soundEvent == null)
            soundEvent = octaveKeyToSoundMap.get(getClosestDefined(keyWithOctave)).stream().filter(sound -> sound.isVelocityWithinRange(velocity)).min(Comparator.comparingDouble(value -> value.velocityComparison(velocity))).orElse(null);

        return soundEvent;
    }
}

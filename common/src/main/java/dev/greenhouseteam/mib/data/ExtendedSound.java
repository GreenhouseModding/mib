package dev.greenhouseteam.mib.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.greenhouseteam.mib.util.FloatRange;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.FloatProvider;

import java.util.Optional;
import java.util.function.Function;

public record ExtendedSound(Sounds sounds,
                            Optional<FloatProvider> pitch,
                            Optional<FloatRange> volumeRange,
                            int durationBeforeLoop,
                            boolean looping) {

    public ExtendedSound(Sounds sounds, int durationBeforeLoop, boolean looping) {
        this(sounds, Optional.empty(), Optional.empty(), durationBeforeLoop, looping);
    }

    public static final Codec<ExtendedSound> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Sounds.CODEC.fieldOf("sounds").forGetter(ExtendedSound::sounds),
            FloatProvider.codec(0.0F, 1.0F).optionalFieldOf("pitch").forGetter(ExtendedSound::pitch),
            FloatRange.codec(0.0F, 1.0F).optionalFieldOf("volume_range").forGetter(ExtendedSound::volumeRange),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("duration_before_loop", 20).forGetter(ExtendedSound::durationBeforeLoop),
            Codec.BOOL.optionalFieldOf("loop", true).forGetter(ExtendedSound::looping)
    ).apply(inst, ExtendedSound::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtendedSound> STREAM_CODEC = StreamCodec.composite(
            Sounds.STREAM_CODEC,
            ExtendedSound::sounds,
            ByteBufCodecs.optional(ByteBufCodecs.fromCodec(FloatProvider.CODEC)),
            ExtendedSound::pitch,
            ByteBufCodecs.optional(ByteBufCodecs.fromCodec(FloatRange.codec(0.0F, 1.0F))),
            ExtendedSound::volumeRange,
            ByteBufCodecs.INT,
            ExtendedSound::durationBeforeLoop,
            ByteBufCodecs.BOOL,
            ExtendedSound::looping,
            ExtendedSound::new
    );

    public boolean isVolumeWithinRange(float value) {
        return volumeRange.isEmpty() || volumeRange.get().isWithinRange(value);
    }

    public double volumeComparison(float value) {
        if (volumeRange.isEmpty())
            return Double.MAX_VALUE;
        return volumeRange.get().min() - value;
    }

    public record Sounds(Holder<SoundEvent> start,
                         Optional<Holder<SoundEvent>> loop,
                         Optional<Holder<SoundEvent>> stop) {
        public static final Codec<Sounds> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
                SoundEvent.CODEC.fieldOf("start").forGetter(Sounds::start),
                SoundEvent.CODEC.optionalFieldOf("loop").forGetter(Sounds::loop),
                SoundEvent.CODEC.optionalFieldOf("stop").forGetter(Sounds::stop)
        ).apply(inst, Sounds::new));
        public static final Codec<Sounds> CODEC = Codec.either(SoundEvent.CODEC, DIRECT_CODEC)
                .xmap(either -> either.map(holder -> new Sounds(holder, Optional.empty(), Optional.empty()), Function.identity()), Either::right);

        public static final StreamCodec<RegistryFriendlyByteBuf, Sounds> STREAM_CODEC = StreamCodec.composite(
                SoundEvent.STREAM_CODEC,
                Sounds::start,
                ByteBufCodecs.optional(SoundEvent.STREAM_CODEC),
                Sounds::loop,
                ByteBufCodecs.optional(SoundEvent.STREAM_CODEC),
                Sounds::stop,
                Sounds::new
        );

        public Sounds(Holder<SoundEvent> start) {
            this(start, Optional.empty(), Optional.empty());
        }

        public Sounds(Holder<SoundEvent> start, Holder<SoundEvent> loop) {
            this(start, Optional.of(loop), Optional.empty());
        }
    }
}
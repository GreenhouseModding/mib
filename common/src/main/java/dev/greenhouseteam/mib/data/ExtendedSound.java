package dev.greenhouseteam.mib.data;

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

public record ExtendedSound(Holder<SoundEvent> startSound,
                            Optional<Holder<SoundEvent>> loopSound,
                            Optional<Holder<SoundEvent>> stopSound,
                            Optional<FloatProvider> pitch,
                            Optional<FloatRange> velocityRange,
                            int durationBeforeLoop,
                            boolean looping) {

    public ExtendedSound(Holder<SoundEvent> startSound, Optional<Holder<SoundEvent>> loopSound, Optional<Holder<SoundEvent>> stopSound, int durationBeforeLoop, boolean looping) {
        this(startSound, loopSound, stopSound, Optional.empty(), Optional.empty(), durationBeforeLoop, looping);
    }

    public ExtendedSound(Holder<SoundEvent> startSound, int durationBeforeLoop, boolean looping) {
        this(startSound, Optional.empty(), Optional.empty(), durationBeforeLoop, looping);
    }

    public ExtendedSound(Holder<SoundEvent> startSound, Holder<SoundEvent> loopSound, int durationBeforeLoop, boolean looping) {
        this(startSound, Optional.of(loopSound), Optional.empty(), durationBeforeLoop, looping);
    }

    public ExtendedSound(Holder<SoundEvent> startSound, Holder<SoundEvent> loopSound, Holder<SoundEvent> stopSound, int durationBeforeLoop, boolean looping) {
        this(startSound, Optional.of(loopSound), Optional.of(stopSound), durationBeforeLoop, looping);
    }

    public static final Codec<ExtendedSound> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            SoundEvent.CODEC.fieldOf("start_sound").forGetter(ExtendedSound::startSound),
            SoundEvent.CODEC.optionalFieldOf("loop_sound").forGetter(ExtendedSound::loopSound),
            SoundEvent.CODEC.optionalFieldOf("stop_sound").forGetter(ExtendedSound::stopSound),
            FloatProvider.codec(0.0F, 1.0F).optionalFieldOf("pitch").forGetter(ExtendedSound::pitch),
            FloatRange.codec(0.0F, 1.0F).optionalFieldOf("velocity_range").forGetter(ExtendedSound::velocityRange),
            Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("duration_before_loop", 20).forGetter(ExtendedSound::durationBeforeLoop),
            Codec.BOOL.optionalFieldOf("loop", true).forGetter(ExtendedSound::looping)
    ).apply(inst, ExtendedSound::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ExtendedSound> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ExtendedSound decode(RegistryFriendlyByteBuf buf) {
            return new ExtendedSound(
                    SoundEvent.STREAM_CODEC.decode(buf),
                    ByteBufCodecs.optional(SoundEvent.STREAM_CODEC).decode(buf),
                    ByteBufCodecs.optional(SoundEvent.STREAM_CODEC).decode(buf),
                    ByteBufCodecs.optional(ByteBufCodecs.fromCodec(FloatProvider.CODEC)).decode(buf),
                    ByteBufCodecs.optional(ByteBufCodecs.fromCodec(FloatRange.codec(0.0F, 1.0F))).decode(buf),
                    ByteBufCodecs.INT.decode(buf),
                    ByteBufCodecs.BOOL.decode(buf)
            );
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ExtendedSound sound) {
            SoundEvent.STREAM_CODEC.encode(buf, sound.startSound);
            ByteBufCodecs.optional(SoundEvent.STREAM_CODEC).encode(buf, sound.loopSound);
            ByteBufCodecs.optional(SoundEvent.STREAM_CODEC).encode(buf, sound.stopSound);
            ByteBufCodecs.optional(ByteBufCodecs.fromCodec(FloatProvider.CODEC)).encode(buf, sound.pitch);
            ByteBufCodecs.optional(ByteBufCodecs.fromCodec(FloatRange.codec(0.0F, 1.0F))).encode(buf, sound.velocityRange);
            ByteBufCodecs.INT.encode(buf, sound.durationBeforeLoop);
            ByteBufCodecs.BOOL.encode(buf, sound.looping);
        }
    };

    public boolean isVelocityWithinRange(float value) {
        return velocityRange.isEmpty() || velocityRange.get().isWithinRange(value);
    }

    public double velocityComparison(float value) {
        if (velocityRange.isEmpty())
            return Double.MAX_VALUE;
        return velocityRange.get().min() - value;
    }
}
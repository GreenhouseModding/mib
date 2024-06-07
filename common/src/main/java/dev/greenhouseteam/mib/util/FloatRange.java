package dev.greenhouseteam.mib.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FloatRange(float min, float max) {
    public static Codec<FloatRange> codec(float minBounds, float maxBounds) {
        Codec<FloatRange> codec = RecordCodecBuilder.create(inst -> inst.group(
                Codec.FLOAT.optionalFieldOf("min", minBounds).forGetter(FloatRange::min),
                Codec.FLOAT.optionalFieldOf("max", maxBounds).forGetter(FloatRange::max)
        ).apply(inst, FloatRange::new));
        return codec.validate(floatRange -> {
            if (floatRange.min >= minBounds && floatRange.max <= maxBounds)
                return DataResult.success(floatRange);
            return DataResult.error(() -> "Float Range not within range. Must not be smaller than " + minBounds + ", or larger than " + maxBounds + ".");
        });
    }

    public boolean isWithinRange(float value) {
        return value > min && value <= max;
    }
}

package dev.greenhouseteam.mib.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;

public record KeyWithOctave(Key key, int octave) {

    public static final int DEFAULT_OCTAVE = 3;
    private static final int MIN_OCTAVE = 1;
    private static final int MAX_OCTAVE = 5;

    public static final KeyWithOctave DEFAULT = new KeyWithOctave(Key.C, DEFAULT_OCTAVE);

    public static final Codec<KeyWithOctave> CODEC = Codec.STRING.xmap(string -> {
        if (!string.contains("([0-9]+)"))
            return new KeyWithOctave(Key.getKey(string), DEFAULT_OCTAVE);
        return new KeyWithOctave(Key.getKey(string), Mth.clamp(Character.getNumericValue(string.charAt(2)), MIN_OCTAVE, MAX_OCTAVE));
    }, keyWithPitch -> keyWithPitch.key.getSerializedName() + keyWithPitch.octave);

    public int getValue() {
        return key.ordinal() + ((octave - 1) * 12);
    }

    public float getPitchFromNote() {
        return getPitchFromNote(DEFAULT);
    }

    public float getPitchFromNote(KeyWithOctave startingKey) {
        return (float)Math.pow(2.0, (((double)getValue()) - startingKey.getValue()) / 12);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyWithOctave other))
            return false;
        return other.octave == octave && other.key == key;
    }
}
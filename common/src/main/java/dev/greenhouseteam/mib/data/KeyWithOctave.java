package dev.greenhouseteam.mib.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.Mth;

import java.util.Optional;

public record KeyWithOctave(Key key, int octave) {

    public static final int DEFAULT_OCTAVE = 3;
    private static final int MIN_OCTAVE = 1;
    private static final int MAX_OCTAVE = 5;

    public static final KeyWithOctave DEFAULT = new KeyWithOctave(Key.C, DEFAULT_OCTAVE);

    public static final Codec<KeyWithOctave> CODEC = Codec.STRING.flatXmap(string -> {
        if (!string.matches(".[0-9]") && !string.matches("..[0-9]"))
            return resultOrError(string, Optional.of(DEFAULT_OCTAVE));
        return resultOrError(string, Optional.empty());
        }, octaveKey -> DataResult.success(octaveKey.key.getSerializedName() + octaveKey.octave));

    private static DataResult<KeyWithOctave> resultOrError(String string, Optional<Integer> octave) {
        if (string.length() > 3)
            return DataResult.error(() -> "Keys may not have more than 3 characters.");
        boolean isSharp = string.length() >= 2 && string.charAt(1) == '#';
        try {
            KeyWithOctave keyWithOctave = new KeyWithOctave(Key.getKey(isSharp ? string.substring(0, 2) : string.substring(0, 1)), Mth.clamp(octave.orElseGet(() -> Character.getNumericValue(isSharp ? string.charAt(2) : string.charAt(1))), MIN_OCTAVE, MAX_OCTAVE));
            return DataResult.success(keyWithOctave);
        } catch (Exception ignored) {
        }
        return DataResult.error(() -> "Could not get key from '" + string + "'. Must be one of: " + Key.buildValuesString() + ", optionally with an octave ranging from 1-5 at the end.");
    }

    public int getValue() {
        return key.ordinal() + ((octave - 1) * 12);
    }

    public float getPitchFromNote() {
        return getPitchFromNote(DEFAULT);
    }

    public float getPitchFromNote(KeyWithOctave startingKey) {
        return (float)Math.pow(2.0, (((double)getValue()) - startingKey.getValue()) / 12);
    }

    public static KeyWithOctave fromInt(int value) {
        int currentOctave = 1;
        for (int i = 0; i < Key.values().length * 5; ++i) {
            if (i == value)
                return new KeyWithOctave(Key.values()[i % 12], currentOctave);
            if (i % 12 == 11)
                ++currentOctave;
        }
        throw new RuntimeException("Int must be within a range of 0-55.");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyWithOctave other))
            return false;
        return other.octave == octave && other.key == key;
    }
}
package dev.greenhouseteam.mib.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Key implements StringRepresentable {
    C("c"),
    C_SHARP("c#"),
    D("d"),
    D_SHARP("d#"),
    E("e"),
    F("f"),
    F_SHARP("f#"),
    G("g"),
    G_SHARP("g#"),
    A("a"),
    A_SHARP("a#"),
    B("b");

    Key(String name) {
        this.name = name;
    }

    public static final Codec<Key> CODEC = StringRepresentable.fromEnum(Key::values);

    private final String name;

    @Override
    @NotNull
    public String getSerializedName() {
        return name;
    }

    public static Key getKey(String str) {
        for (Key key : Key.values())
            if (str.contains(key.name)) {
                return key;
            }
        throw new IllegalStateException("Could not find key '" + str + "'.");
    }

}

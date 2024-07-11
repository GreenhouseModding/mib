package dev.greenhouseteam.mib.data;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum MibNote implements StringRepresentable {
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

    MibNote(String name) {
        this.name = name;
    }

    private final String name;

    @Override
    @NotNull
    public String getSerializedName() {
        return name;
    }

    public static MibNote getNote(String str) {
        for (MibNote key : MibNote.values())
            if (str.toLowerCase().equals(key.name))
                return key;
        throw new IllegalStateException("Could not find note '" + str + "'. Must be one of " + buildValuesString() + ".");
    }

    public static String buildValuesString() {
        StringBuilder builder = new StringBuilder("[ ");
        for (int i = 0; i < MibNote.values().length; ++i) {
            if (i != 0)
                builder.append(", ");
            MibNote key = MibNote.values()[i];
            builder.append(key.getSerializedName());
        }
        builder.append(" ]");
        return builder.toString();
    }

}

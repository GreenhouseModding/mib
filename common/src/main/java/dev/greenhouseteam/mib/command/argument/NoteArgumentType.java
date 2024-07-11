package dev.greenhouseteam.mib.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.greenhouseteam.mib.data.MibNote;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class NoteArgumentType implements ArgumentType<MibNote> {
    private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST = new DynamicCommandExceptionType(p -> Component.translatable("argument.mib.note.unknown", p));

    private NoteArgumentType() {}

    public static NoteArgumentType note() {
        return new NoteArgumentType();
    }

    public static MibNote getNote(CommandContext<CommandSourceStack> context, String argument) throws CommandSyntaxException {
        return context.getArgument(argument, MibNote.class);
    }

    public static boolean isAllowedInUnquotedString(final char c) {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z'
                || c == '_' || c == '-'
                || c == '.' || c == '+'
                || c == '#';
    }

    @Override
    public MibNote parse(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead() && isAllowedInUnquotedString(reader.peek()))
            reader.skip();
        String string = reader.getString().substring(start, reader.getCursor());
        try {
            return MibNote.getNote(string);
        } catch (IllegalStateException ex) {
            throw ERROR_DOESNT_EXIST.createWithContext(reader, string);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        for (MibNote note : MibNote.values()) {
            builder.suggest(note.getSerializedName());
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.stream(MibNote.values()).map(MibNote::getSerializedName).toList();
    }
}

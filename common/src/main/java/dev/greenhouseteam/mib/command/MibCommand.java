package dev.greenhouseteam.mib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.datafixers.util.Either;
import dev.greenhouseteam.mib.command.argument.NoteArgumentType;
import dev.greenhouseteam.mib.data.MibNote;
import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.data.NoteWithOctave;
import dev.greenhouseteam.mib.mixin.ResourceKeyArgumentAccessor;
import dev.greenhouseteam.mib.registry.MibRegistries;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MibCommand {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> mibNode = Commands
                .literal("mib")
                .build();

        // TODO: Clean this up, maybe separate into different nodes.
        LiteralCommandNode<CommandSourceStack> playNode = Commands
                .literal("play")
                .then(Commands.argument("soundSet", ResourceKeyArgument.key(MibRegistries.SOUND_SET))
                        .then(Commands.argument("note", NoteArgumentType.note())
                                .executes(context -> playNote(context, false, false, false))
                                .then(Commands.argument("octave", IntegerArgumentType.integer(1, 4))
                                        .executes(context -> playNote(context, true, false, false))
                                        .then(Commands.argument("volume", FloatArgumentType.floatArg(0.0F))
                                                .executes(context -> playNote(context, true, true, false))
                                                .then(Commands.argument("length", IntegerArgumentType.integer())
                                                        .executes(context -> playNote(context, true, true, true)))))))
                .build();

        mibNode.addChild(playNode);

        dispatcher.getRoot().addChild(mibNode);
    }

    private static final DynamicCommandExceptionType ERROR_INVALID_SOUND_SET = new DynamicCommandExceptionType(
            p -> Component.translatableEscape("commands.mib.play.sound_set.invalid", p)
    );

    public static int playNote(CommandContext<CommandSourceStack> context, boolean withOctave, boolean withVolume, boolean withLength) throws CommandSyntaxException {
        MibNote note = NoteArgumentType.getNote(context, "note");
        int octave = NoteWithOctave.DEFAULT_OCTAVE;
        if (withOctave)
            octave = IntegerArgumentType.getInteger(context, "octave");

        Holder.Reference<MibSoundSet> soundSet = ResourceKeyArgumentAccessor.mib$invokeResolveKey(context, "soundSet", MibRegistries.SOUND_SET, ERROR_INVALID_SOUND_SET);

        float volume = 1.0F;
        if (withVolume)
            volume = FloatArgumentType.getFloat(context, "volume");

        float length = 20;
        if (withLength)
            length = IntegerArgumentType.getInteger(context, "length");

        Either<Entity, Vec3> entityIdOrPos;

        if (context.getSource().getEntity() != null)
            entityIdOrPos = Either.left(context.getSource().getEntity());
        else
            entityIdOrPos = Either.right(context.getSource().getPosition());

        soundSet.value().playSoundWithLength(context.getSource().getLevel(), entityIdOrPos, note, octave, volume, length);

        return 1;
    }
}

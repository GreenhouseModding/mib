package dev.greenhouseteam.mib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
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
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class MibCommand {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> mibNode = Commands
                .literal("mib")
                .requires(stack -> stack.hasPermission(2))
                .build();

        // TODO: Clean this up, maybe separate into different nodes.
        LiteralCommandNode<CommandSourceStack> playNode = Commands
                .literal("play")
                .build();

        ArgumentCommandNode<CommandSourceStack, ResourceKey<MibSoundSet>> soundSetNode = Commands
                .argument("soundSet", ResourceKeyArgument.key(MibRegistries.SOUND_SET))
                .build();

        ArgumentCommandNode<CommandSourceStack, MibNote> noteNode = Commands
                .argument("note", NoteArgumentType.note())
                .executes(context -> playNote(context, NoteWithOctave.DEFAULT_OCTAVE, 1.0F, 20, getCallingPlayerAsCollection(context.getSource().getPlayer()), Either.right(context.getSource().getPosition())))
                .build();

        ArgumentCommandNode<CommandSourceStack, Integer> octaveNode = Commands
                .argument("octave", IntegerArgumentType.integer(1, 4))
                .executes(context -> playNote(context, IntegerArgumentType.getInteger(context, "octave"), 1.0F, 20, getCallingPlayerAsCollection(context.getSource().getPlayer()), Either.right(context.getSource().getPosition())))
                .build();

        ArgumentCommandNode<CommandSourceStack, Float> volumeNode = Commands
                .argument("volume", FloatArgumentType.floatArg(0.0F))
                .executes(context -> playNote(context, IntegerArgumentType.getInteger(context, "octave"), FloatArgumentType.getFloat(context, "volume"), 20, getCallingPlayerAsCollection(context.getSource().getPlayer()), Either.right(context.getSource().getPosition())))
                .build();

        ArgumentCommandNode<CommandSourceStack, Integer> lengthNode = Commands
                .argument("length", IntegerArgumentType.integer())
                .executes(context -> playNote(context, IntegerArgumentType.getInteger(context, "octave"), FloatArgumentType.getFloat(context, "volume"), IntegerArgumentType.getInteger(context, "length"), getCallingPlayerAsCollection(context.getSource().getPlayer()), Either.right(context.getSource().getPosition())))
                .build();

        ArgumentCommandNode<CommandSourceStack, EntitySelector> targetsNode = Commands
                .argument("targets", EntityArgument.players())
                .executes(context -> playNote(context, IntegerArgumentType.getInteger(context, "octave"), FloatArgumentType.getFloat(context, "volume"), IntegerArgumentType.getInteger(context, "length"), EntityArgument.getPlayers(context, "targets"), Either.right(context.getSource().getPosition())))
                .build();

        LiteralCommandNode<CommandSourceStack> posNode = Commands
                .literal("pos")
                .build();

        LiteralCommandNode<CommandSourceStack> entityNode = Commands
                .literal("entity")
                .build();

        ArgumentCommandNode<CommandSourceStack, Coordinates> originPosNode = Commands
                .argument("origin", Vec3Argument.vec3())
                .executes(context -> playNote(context, IntegerArgumentType.getInteger(context, "octave"), FloatArgumentType.getFloat(context, "volume"), IntegerArgumentType.getInteger(context, "length"), EntityArgument.getPlayers(context, "targets"), Either.right(Vec3Argument.getVec3(context, "origin"))))
                .build();

        ArgumentCommandNode<CommandSourceStack, EntitySelector> originEntityNode = Commands
                .argument("origin", EntityArgument.entity())
                .executes(context -> playNote(context, IntegerArgumentType.getInteger(context, "octave"), FloatArgumentType.getFloat(context, "volume"), IntegerArgumentType.getInteger(context, "length"), EntityArgument.getPlayers(context, "targets"), Either.left(EntityArgument.getEntity(context, "origin"))))
                .build();


        entityNode.addChild(originEntityNode);
        posNode.addChild(originPosNode);
        targetsNode.addChild(entityNode);
        targetsNode.addChild(posNode);

        lengthNode.addChild(targetsNode);

        volumeNode.addChild(lengthNode);
        octaveNode.addChild(volumeNode);
        noteNode.addChild(octaveNode);
        soundSetNode.addChild(noteNode);
        playNode.addChild(soundSetNode);

        mibNode.addChild(playNode);

        dispatcher.getRoot().addChild(mibNode);
    }

    private static Collection<ServerPlayer> getCallingPlayerAsCollection(@Nullable ServerPlayer player) {
        return player != null ? List.of(player) : List.of();
    }

    private static final DynamicCommandExceptionType ERROR_INVALID_SOUND_SET = new DynamicCommandExceptionType(
            p -> Component.translatableEscape("commands.mib.play.sound_set.invalid", p)
    );

    public static int playNote(CommandContext<CommandSourceStack> context, int octave, float volume, float length, Collection<ServerPlayer> targets, Either<Entity, Vec3> entityOrPos) throws CommandSyntaxException {
        MibNote note = NoteArgumentType.getNote(context, "note");
        Holder.Reference<MibSoundSet> soundSet = ResourceKeyArgumentAccessor.mib$invokeResolveKey(context, "soundSet", MibRegistries.SOUND_SET, ERROR_INVALID_SOUND_SET);

        soundSet.value().playSoundWithLength(targets, entityOrPos, note, octave, volume, length);

        return 1;
    }
}

package dev.greenhouseteam.mib.mixin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ResourceKeyArgument.class)
public interface ResourceKeyArgumentAccessor {
    @Invoker("resolveKey")
    static <T> Holder.Reference<T> mib$invokeResolveKey(CommandContext<CommandSourceStack> context, String argument, ResourceKey<Registry<T>> registryKey, DynamicCommandExceptionType exception) {
        throw new RuntimeException("");
    }
}

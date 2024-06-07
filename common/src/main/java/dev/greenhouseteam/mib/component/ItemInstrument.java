package dev.greenhouseteam.mib.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.greenhouseteam.mib.data.KeyWithOctave;
import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.registry.MibComponents;
import dev.greenhouseteam.mib.registry.MibRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.ItemStack;

public record ItemInstrument(EitherHolder<MibSoundSet> sound, KeyWithOctave defaultKey, int maxUseDuration) {
    public static final Codec<ItemInstrument> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            EitherHolder.codec(MibRegistries.SOUND_SET, MibSoundSet.CODEC).fieldOf("sound").forGetter(ItemInstrument::sound),
            KeyWithOctave.CODEC.optionalFieldOf("default_key", KeyWithOctave.DEFAULT).forGetter(ItemInstrument::defaultKey),
            Codec.INT.optionalFieldOf("max_use_duration", 60).forGetter(ItemInstrument::maxUseDuration)
    ).apply(inst, ItemInstrument::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInstrument> STREAM_CODEC = StreamCodec.composite(
            EitherHolder.streamCodec(MibRegistries.SOUND_SET, MibSoundSet.STREAM_CODEC),
            ItemInstrument::sound,
            ByteBufCodecs.fromCodec(KeyWithOctave.CODEC),
            ItemInstrument::defaultKey,
            ByteBufCodecs.INT,
            ItemInstrument::maxUseDuration,
            ItemInstrument::new
    );

    public ItemInstrument(ResourceKey<MibSoundSet> key) {
        this(new EitherHolder<>(key), KeyWithOctave.DEFAULT, 60);
    }

    public ItemInstrument(ResourceKey<MibSoundSet> key, KeyWithOctave defaultKey) {
        this(new EitherHolder<>(key), defaultKey, 60);
    }

    public static InteractionResultHolder<ItemStack> playInstrument(Player player, ItemStack stack, InteractionHand hand) {
        if (!stack.has(MibComponents.INSTRUMENT))
            return InteractionResultHolder.pass(stack);

        ItemInstrument instrumentComponent = stack.get(MibComponents.INSTRUMENT);
        var instrument = instrumentComponent.sound().unwrap(player.level().registryAccess());
        if (instrument.isEmpty() || !instrument.get().isBound())
            return InteractionResultHolder.pass(stack);

        player.startUsingItem(hand);
        if (!player.level().isClientSide())
            instrument.get().value().playSound(player, hand, instrumentComponent.defaultKey.key(), instrumentComponent.defaultKey.octave(), 1.0F);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        return InteractionResultHolder.consume(stack);
    }
}

package dev.greenhouseteam.mib.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.greenhouseteam.mib.data.KeyWithOctave;
import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.data.animation.InstrumentAnimation;
import dev.greenhouseteam.mib.registry.MibDataComponents;
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

import java.util.Optional;

public record ItemInstrument(EitherHolder<MibSoundSet> sound, KeyWithOctave defaultKey, int maxUseDuration, Optional<InstrumentAnimation> animation) {
    public static final Codec<ItemInstrument> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            EitherHolder.codec(MibRegistries.SOUND_SET, MibSoundSet.CODEC).fieldOf("sound").forGetter(ItemInstrument::sound),
            KeyWithOctave.CODEC.optionalFieldOf("default_key", KeyWithOctave.DEFAULT).forGetter(ItemInstrument::defaultKey),
            Codec.INT.optionalFieldOf("max_use_duration", 60).forGetter(ItemInstrument::maxUseDuration),
            MibRegistries.INSTRUMENT_ANIMATIONS.byNameCodec().optionalFieldOf("animation").forGetter(ItemInstrument::animation)
    ).apply(inst, ItemInstrument::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInstrument> STREAM_CODEC = StreamCodec.composite(
            EitherHolder.streamCodec(MibRegistries.SOUND_SET, MibSoundSet.STREAM_CODEC),
            ItemInstrument::sound,
            ByteBufCodecs.fromCodec(KeyWithOctave.CODEC),
            ItemInstrument::defaultKey,
            ByteBufCodecs.INT,
            ItemInstrument::maxUseDuration,
            ByteBufCodecs.optional(ByteBufCodecs.registry(MibRegistries.INSTRUMENT_ANIMATION)),
            ItemInstrument::animation,
            ItemInstrument::new
    );

    public ItemInstrument(ResourceKey<MibSoundSet> key) {
        this(new EitherHolder<>(key), KeyWithOctave.DEFAULT, 40, Optional.empty());
    }

    public ItemInstrument(ResourceKey<MibSoundSet> key, KeyWithOctave defaultKey) {
        this(new EitherHolder<>(key), defaultKey, 40, Optional.empty());
    }

    public ItemInstrument(ResourceKey<MibSoundSet> key, KeyWithOctave defaultKey, InstrumentAnimation animation) {
        this(new EitherHolder<>(key), defaultKey, 40, Optional.of(animation));
    }

    public ItemInstrument(ResourceKey<MibSoundSet> key, int maxUseDuration) {
        this(new EitherHolder<>(key), KeyWithOctave.DEFAULT, maxUseDuration, Optional.empty());
    }

    public ItemInstrument(ResourceKey<MibSoundSet> key, int maxUseDuration, InstrumentAnimation animation) {
        this(new EitherHolder<>(key), KeyWithOctave.DEFAULT, maxUseDuration, Optional.of(animation));
    }

    public ItemInstrument(ResourceKey<MibSoundSet> key, InstrumentAnimation animation) {
        this(new EitherHolder<>(key), KeyWithOctave.DEFAULT, 40, Optional.of(animation));
    }

    public static InteractionResultHolder<ItemStack> playInstrument(Player player, ItemStack stack, InteractionHand hand) {
        if (!stack.has(MibDataComponents.INSTRUMENT))
            return InteractionResultHolder.pass(stack);

        ItemInstrument instrumentComponent = stack.get(MibDataComponents.INSTRUMENT);
        var instrument = instrumentComponent.sound().unwrap(player.level().registryAccess());
        if (instrument.isEmpty())
            return InteractionResultHolder.pass(stack);

        player.startUsingItem(hand);
        if (!player.level().isClientSide())
            instrument.get().value().playSound(player, hand, instrumentComponent.defaultKey.key(), instrumentComponent.defaultKey.octave(), 1.0F);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        return InteractionResultHolder.consume(stack);
    }
}

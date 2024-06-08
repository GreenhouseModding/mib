package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.data.Key;
import dev.greenhouseteam.mib.data.KeyWithOctave;
import dev.greenhouseteam.mib.data.animation.TootInstrumentAnimation;
import dev.greenhouseteam.mib.item.MibInstrumentItem;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class MibItems {
    public static final Item EASTERN_TRUMPET = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.EASTERN_TRUMPET, new KeyWithOctave(Key.G_SHARP, 3), TootInstrumentAnimation.INSTANCE)));
    public static final Item KEYBOARD = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.KEYBOARD, 1)));

    public static void registerAll(RegistrationCallback<Item> callback) {
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("eastern_trumpet"), EASTERN_TRUMPET);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("keyboard"), KEYBOARD);
    }
}

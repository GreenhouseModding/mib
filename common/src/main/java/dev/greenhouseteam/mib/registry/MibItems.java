package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.data.Key;
import dev.greenhouseteam.mib.data.KeyWithOctave;
import dev.greenhouseteam.mib.data.animation.FluteInstrumentAnimation;
import dev.greenhouseteam.mib.data.animation.TootInstrumentAnimation;
import dev.greenhouseteam.mib.item.MibInstrumentItem;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class MibItems {
    public static final Item FANTASY_TRUMPET = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.FANTASY_TRUMPET, new KeyWithOctave(Key.G_SHARP, 3), TootInstrumentAnimation.INSTANCE)));
    public static final Item FLUTE = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.FLUTE, new KeyWithOctave(Key.C, 4), FluteInstrumentAnimation.INSTANCE)));
    public static final Item KEYBOARD = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.KEYBOARD, 1)));

    public static void registerAll(RegistrationCallback<Item> callback) {
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("fantasy_trumpet"), FANTASY_TRUMPET);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("flute"), FLUTE);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("keyboard"), KEYBOARD);
    }
}

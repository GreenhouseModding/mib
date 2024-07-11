package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import dev.greenhouseteam.mib.component.ItemInstrument;
import dev.greenhouseteam.mib.data.MibNote;
import dev.greenhouseteam.mib.data.NoteWithOctave;
import dev.greenhouseteam.mib.data.animation.FluteInstrumentAnimation;
import dev.greenhouseteam.mib.data.animation.SwingOffhandInstrumentAnimation;
import dev.greenhouseteam.mib.data.animation.TootInstrumentAnimation;
import dev.greenhouseteam.mib.item.MibInstrumentItem;
import dev.greenhouseteam.mib.registry.internal.RegistrationCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class MibItems {
    public static final Item ACOUSTIC_GUITAR = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.ACOUSTIC_GUITAR, 1, SwingOffhandInstrumentAnimation.INSTANCE)));
    public static final Item COPPER_GOAT_HORN = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.COPPER_GOAT_HORN, new NoteWithOctave(MibNote.G, 3), TootInstrumentAnimation.INSTANCE)));
    public static final Item FANTASY_TRUMPET = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.FANTASY_TRUMPET, new NoteWithOctave(MibNote.G_SHARP, 3), TootInstrumentAnimation.INSTANCE)));
    public static final Item FLUTE = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.FLUTE, new NoteWithOctave(MibNote.C, 4), FluteInstrumentAnimation.INSTANCE)));
    public static final Item HARPSICHORD = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.HARPSICHORD, 1)));
    public static final Item KEYBOARD = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.KEYBOARD, 1)));
    public static final Item SAXOPHONE = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.SAXOPHONE, TootInstrumentAnimation.INSTANCE)));
    public static final Item VIOLIN = new MibInstrumentItem(new Item.Properties().stacksTo(1).component(MibDataComponents.INSTRUMENT, new ItemInstrument(MibSoundSets.VIOLIN)));

    public static void registerAll(RegistrationCallback<Item> callback) {
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("acoustic_guitar"), ACOUSTIC_GUITAR);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("copper_goat_horn"), COPPER_GOAT_HORN);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("fantasy_trumpet"), FANTASY_TRUMPET);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("flute"), FLUTE);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("harpsichord"), HARPSICHORD);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("keyboard"), KEYBOARD);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("saxophone"), SAXOPHONE);
        callback.register(BuiltInRegistries.ITEM, Mib.asResource("violin"), VIOLIN);
    }
}

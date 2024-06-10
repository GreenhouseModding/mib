package dev.greenhouseteam.mib.registry;

import dev.greenhouseteam.mib.Mib;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MibTags {
    public static class ItemTags {
        public static final TagKey<Item> INSTRUMENTS = TagKey.create(Registries.ITEM, Mib.asResource("instruments"));
    }
}

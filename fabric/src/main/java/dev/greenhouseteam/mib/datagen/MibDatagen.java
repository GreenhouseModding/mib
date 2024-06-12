package dev.greenhouseteam.mib.datagen;

import dev.greenhouseteam.mib.data.KeyWithOctave;
import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.registry.MibItems;
import dev.greenhouseteam.mib.registry.MibSoundSets;
import dev.greenhouseteam.mib.registry.MibSoundEvents;
import dev.greenhouseteam.mib.registry.MibTags;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.ConstantFloat;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MibDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(DynamicRegistryGenerator::new);
        pack.addProvider(ItemTagProvider::new);
    }

    private static class DynamicRegistryGenerator extends FabricDynamicRegistryProvider {

        public DynamicRegistryGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            entries.add(MibSoundSets.FANTASY_TRUMPET, new MibSoundSet(Map.of(KeyWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FANTASY_TRUMPET_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FANTASY_TRUMPET_LOOP).orElseThrow())))))));
            entries.add(MibSoundSets.FLUTE, new MibSoundSet(Map.of(KeyWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FLUTE_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FLUTE_LOOP).orElseThrow())), ConstantFloat.of(0.0075F))))));
            entries.add(MibSoundSets.KEYBOARD, new MibSoundSet(Map.of(KeyWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.KEYBOARD).orElseThrow())))))));
        }

        @Override
        public @NotNull String getName() {
            return "Mib Dynamic Registries";
        }
    }

    private static class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
        public ItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture, null);
        }

        @Override
        protected void addTags(HolderLookup.Provider wrapperLookup) {
            getOrCreateTagBuilder(MibTags.ItemTags.INSTRUMENTS)
                    .add(MibItems.FANTASY_TRUMPET)
                    .add(MibItems.FLUTE)
                    .add(MibItems.KEYBOARD);
        }
    }
}

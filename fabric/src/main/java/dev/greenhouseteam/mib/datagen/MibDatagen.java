package dev.greenhouseteam.mib.datagen;

import dev.greenhouseteam.mib.data.KeyWithOctave;
import dev.greenhouseteam.mib.data.MibSoundSet;
import dev.greenhouseteam.mib.data.ExtendedSound;
import dev.greenhouseteam.mib.registry.MibSoundSets;
import dev.greenhouseteam.mib.registry.MibSoundEvents;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MibDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(DynamicRegistryGenerator::new);
    }

    private static class DynamicRegistryGenerator extends FabricDynamicRegistryProvider {

        public DynamicRegistryGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            entries.add(MibSoundSets.EASTERN_TRUMPET, new MibSoundSet(Map.of(KeyWithOctave.DEFAULT, List.of(new ExtendedSound(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.EASTERN_TRUMPET_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.EASTERN_TRUMPET_LOOP).orElseThrow()), 13,  true)))));
            entries.add(MibSoundSets.MINI_PIANO, new MibSoundSet(Map.of(KeyWithOctave.DEFAULT, List.of(new ExtendedSound(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.MINI_PIANO).orElseThrow()), 0,  false)))));
        }

        @Override
        public @NotNull String getName() {
            return "Mib Dynamic Registries";
        }
    }
}

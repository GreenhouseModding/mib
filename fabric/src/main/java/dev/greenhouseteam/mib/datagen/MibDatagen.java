package dev.greenhouseteam.mib.datagen;

import dev.greenhouseteam.mib.data.NoteWithOctave;
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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MibDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(CraftingRecipeProvider::new);
        pack.addProvider(DynamicRegistryGenerator::new);
        pack.addProvider(ItemTagProvider::new);
    }

    private static class DynamicRegistryGenerator extends FabricDynamicRegistryProvider {

        public DynamicRegistryGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            entries.add(MibSoundSets.ACOUSTIC_GUITAR, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.ACOUSTIC_GUITAR).orElseThrow())))))));
            entries.add(MibSoundSets.COPPER_GOAT_HORN, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.COPPER_GOAT_HORN_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.COPPER_GOAT_HORN_LOOP).orElseThrow())), ConstantFloat.of(0.0075F))))));
            entries.add(MibSoundSets.FANTASY_TRUMPET, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FANTASY_TRUMPET_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FANTASY_TRUMPET_LOOP).orElseThrow())))))));
            entries.add(MibSoundSets.FLUTE, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FLUTE_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.FLUTE_LOOP).orElseThrow())), ConstantFloat.of(0.0075F))))));
            entries.add(MibSoundSets.HARPSICHORD, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.HARPSICHORD).orElseThrow())))))));
            entries.add(MibSoundSets.KEYBOARD, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.KEYBOARD).orElseThrow())))))));
            entries.add(MibSoundSets.SAXOPHONE, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.SAXOPHONE_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.SAXOPHONE_LOOP).orElseThrow())))))));
            entries.add(MibSoundSets.VIOLIN, new MibSoundSet(Map.of(NoteWithOctave.DEFAULT, List.of(new ExtendedSound(new ExtendedSound.Sounds(BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.VIOLIN_START).orElseThrow()), BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(BuiltInRegistries.SOUND_EVENT.getResourceKey(MibSoundEvents.VIOLIN_LOOP).orElseThrow())))))));
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
                    .add(MibItems.ACOUSTIC_GUITAR)
                    .add(MibItems.COPPER_GOAT_HORN)
                    .add(MibItems.FANTASY_TRUMPET)
                    .add(MibItems.FLUTE)
                    .add(MibItems.HARPSICHORD)
                    .add(MibItems.KEYBOARD)
                    .add(MibItems.SAXOPHONE)
                    .add(MibItems.VIOLIN);
        }
    }

    private static class CraftingRecipeProvider extends FabricRecipeProvider {
        public CraftingRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public void buildRecipes(RecipeOutput output) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.ACOUSTIC_GUITAR)
                    .pattern("  T")
                    .pattern("WS ")
                    .pattern("SW ")
                    .define('W', ItemTags.PLANKS)
                    .define('S', ConventionalItemTags.STRINGS)
                    .define('T', ConventionalItemTags.WOODEN_RODS)
                    .unlockedBy(getHasName(Items.STRING), has(ConventionalItemTags.STRINGS))
                    .showNotification(false)
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.COPPER_GOAT_HORN)
                    .pattern("CHC")
                    .define('C', ConventionalItemTags.COPPER_INGOTS)
                    .define('H', Items.GOAT_HORN)
                    .unlockedBy(getHasName(Items.GOAT_HORN), has(Items.GOAT_HORN))
                    .showNotification(false)
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.FANTASY_TRUMPET)
                    .pattern("  G")
                    .pattern("GWG")
                    .define('G', ConventionalItemTags.GOLD_INGOTS)
                    .define('W', ItemTags.WOOL)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(ConventionalItemTags.GOLD_INGOTS))
                    .showNotification(false)
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.FLUTE)
                    .pattern("BB")
                    .define('B', Items.BAMBOO)
                    .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                    .showNotification(false)
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.HARPSICHORD)
                    .pattern("WW ")
                    .pattern("BWW")
                    .define('W', ItemTags.PLANKS)
                    .define('B', Items.BONE)
                    .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                    .showNotification(false)
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.KEYBOARD)
                    .pattern("WWW")
                    .pattern("BBB")
                    .define('W', ItemTags.PLANKS)
                    .define('B', Items.BONE)
                    .unlockedBy(getHasName(Items.BONE), has(Items.BONE))
                    .showNotification(false)
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.SAXOPHONE)
                    .pattern("  G")
                    .pattern("G G")
                    .pattern(" G ")
                    .define('G', ConventionalItemTags.GOLD_INGOTS)
                    .unlockedBy(getHasName(Items.GOLD_INGOT), has(ConventionalItemTags.GOLD_INGOTS))
                    .showNotification(false)
                    .save(output);

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, MibItems.VIOLIN)
                    .pattern(" T ")
                    .pattern("WS ")
                    .pattern("SWT")
                    .define('W', ItemTags.PLANKS)
                    .define('S', ConventionalItemTags.STRINGS)
                    .define('T', ConventionalItemTags.WOODEN_RODS)
                    .unlockedBy(getHasName(Items.STRING), has(ConventionalItemTags.STRINGS))
                    .showNotification(false)
                    .save(output);
        }
    }
}

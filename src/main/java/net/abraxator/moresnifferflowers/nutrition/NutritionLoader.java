package net.abraxator.moresnifferflowers.nutrition;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class NutritionLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Codec<Map<Either<TagKey<Item>, Item>, Integer>> CODEC = Codec.unboundedMap(
            Codec.either(
                    TagKey.hashedCodec(Registries.ITEM), ForgeRegistries.ITEMS.getCodec()
            ), Codec.INT
    );
    
    public static Map<String, List<Nutrition>> modNutritions = ImmutableMap.of();
    public static Map<NutritionType, List<Nutrition>> typeNutritions = ImmutableMap.of();
    public static List<Nutrition> allNutritions = ImmutableList.of();
    
    public NutritionLoader() {
        super(GSON, "nutrition");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<String, List<Nutrition>> modNutritions = new HashMap<>();
        Map<NutritionType, List<Nutrition>> typeNutritions = new HashMap<>();
        List<Nutrition> allNutritions = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            Map<Item, List<NutritionEntry>> currentModNutritions = Maps.newHashMap();
            ResourceLocation path = entry.getKey();
            
            try {
                for (NutritionType nutritionType : NutritionType.values()) {
                    JsonObject entries = entry.getValue().getAsJsonObject().getAsJsonObject(nutritionType.name);
                    Map<NutritionType, List<NutritionEntry>> currentTypeNutritions = Maps.newHashMap();
                    
                    var values = CODEC.parse(JsonOps.INSTANCE, entries).get();
                    if(values.left().isPresent()) {
                        Map<Item, NutritionEntry> map = new HashMap<>();
                        
                        for (Map.Entry<Either<TagKey<Item>, Item>, Integer> mapEntry : values.left().get().entrySet()) {
                            List<Item> itemList = new ArrayList<>();
                            Map.Entry<Item, NutritionEntry> ret;

                            if (mapEntry.getKey().left().isPresent()) {
                                itemList = (Arrays.stream(Ingredient.of(mapEntry.getKey().left().get()).getItems())
                                        .map(ItemStack::getItem)
                                        .toList()
                                );
                            }
                            if (mapEntry.getKey().right().isPresent()) {
                                itemList = List.of(mapEntry.getKey().right().get());
                            }

                            for (Item item : itemList) {
                                map.put(item, new NutritionEntry(nutritionType, mapEntry.getValue()));
                            }
                        }
                        
                        map.forEach((item, nutritionEntry) -> {
                            currentModNutritions.merge(item,
                                    new ArrayList<>(List.of(nutritionEntry)),
                                    (existingList, newList) -> {
                                        existingList.addAll(newList);
                                        return existingList;
                                    });
                        });
                    }
                }
            } catch (IllegalStateException | JsonParseException e) {
                MoreSnifferFlowers.LOGGER.error("Error parsing nutrition {}", path, e);
            }
        
            var nutritionList = currentModNutritions.entrySet().stream()
                    .map(itemListEntry -> new Nutrition(itemListEntry.getKey(), itemListEntry.getValue()))
                    .toList();
            modNutritions.put(
                    path.getPath(), 
                    nutritionList);
            allNutritions.addAll(nutritionList);

            for (Nutrition nutrition : allNutritions) {
                for (NutritionEntry nutritionEntry : nutrition.getNutritionEntries()) {
                    NutritionType type  = nutritionEntry.nutrition();
                    typeNutritions
                            .computeIfAbsent(type, k -> new ArrayList<>())
                            .add(nutrition);
                }
            }
        }
        
        this.modNutritions = modNutritions;
        this.typeNutritions = typeNutritions;
        this.allNutritions = allNutritions;
    }
}

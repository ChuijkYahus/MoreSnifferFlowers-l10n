package net.abraxator.moresnifferflowers.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapEncoder;
import net.abraxator.moresnifferflowers.MoreSnifferFlowers;
import net.abraxator.moresnifferflowers.nutrition.Nutrition;
import net.abraxator.moresnifferflowers.nutrition.NutritionEntry;
import net.abraxator.moresnifferflowers.nutrition.NutritionType;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.rmi.MarshalledObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class NutritionLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Codec<Map<Either<TagKey<Item>, Item>, Integer>> CODEC = Codec.unboundedMap(
            Codec.either(
                    TagKey.hashedCodec(Registries.ITEM), ForgeRegistries.ITEMS.getCodec()
            ), Codec.INT
    );
    
    private Map<String, List<Nutrition>> modNutritions = ImmutableMap.of();
    private List<Nutrition> allNutritions = ImmutableList.of();
    
    public NutritionLoader() {
        super(GSON, "nutrition");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<String, List<Nutrition>> modNutritions = new HashMap<>();
        List<Nutrition> allNutritions = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            Map<Item, List<NutritionEntry>> currentModNutritions = Maps.newHashMap();
            ResourceLocation path = entry.getKey();
            
            try {
                for (NutritionType nutritionType : NutritionType.values()) {
                    JsonObject entries = entry.getValue().getAsJsonObject().getAsJsonObject(nutritionType.name);
                    var values = CODEC.parse(JsonOps.INSTANCE, entries).get();
                    Map<Item, NutritionEntry> map = Maps.newHashMap(); 
                    if(values.left().isPresent()) {
                        var left = values.left().get();
                        for (Map.Entry<Either<TagKey<Item>, Item>, Integer> mapEntry : left.entrySet()) {
                            List<Item> itemList = new ArrayList<>();

                            if(mapEntry.getKey().left().isPresent()) {
                                itemList = (Arrays.stream(Ingredient.of(mapEntry.getKey().left().get()).getItems())
                                        .map(ItemStack::getItem)
                                        .toList()
                                );
                            }
                            if(mapEntry.getKey().right().isPresent()) {
                                itemList = List.of(mapEntry.getKey().right().get());
                            }

                            for (Item item : itemList) {
                                map.put(item, new NutritionEntry(nutritionType, mapEntry.getValue()));
                            }
                        }
                        
                        map.forEach((item, nutritionEntry) -> {
                            List<NutritionEntry> nutritions = new ArrayList<>();
                            nutritions.add(nutritionEntry);
                            
                            if (currentModNutritions.containsKey(item)) {
                                nutritions.addAll(currentModNutritions.get(item));
                            }
                            
                            currentModNutritions.put(item, nutritions);
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
        }
        
        this.modNutritions = modNutritions;
        this.allNutritions = allNutritions;
    }
}

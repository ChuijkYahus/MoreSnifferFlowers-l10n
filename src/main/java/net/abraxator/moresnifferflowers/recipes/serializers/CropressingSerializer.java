package net.abraxator.moresnifferflowers.recipes.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.abraxator.moresnifferflowers.recipes.CropressingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class CropressingSerializer implements RecipeSerializer<CropressingRecipe> {
    @Override
    public CropressingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        var count = GsonHelper.getAsInt(pSerializedRecipe, "count");
        var ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient"));
        var result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));

        return new CropressingRecipe(pRecipeId, ingredient, count, result);
    }

    @Override
    public @Nullable CropressingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf buffer) {
        var ingredient = Ingredient.fromNetwork(buffer);
        var count =  buffer.readInt();
        var item = buffer.readItem();
        
        return new CropressingRecipe(pRecipeId, ingredient, count, item);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CropressingRecipe pRecipe) {
        pRecipe.ingredient().toNetwork(buffer);
        buffer.writeInt(pRecipe.count());
        buffer.writeItem(pRecipe.result());
    }
}
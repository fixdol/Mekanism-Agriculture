package com.fxd927.mekanismagriculture.common.recipe.serializer;

import com.fxd927.mekanismagriculture.api.recipes.ForcingRecipe;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mekanism.api.JsonConstants;
import mekanism.api.SerializerHelper;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.Mekanism;
import mekanism.common.recipe.serializer.PressurizedReactionRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

public class ForcingRecipeSerializer<RECIPE extends ForcingRecipe> implements RecipeSerializer<RECIPE> {
    private final ForcingRecipeSerializer.IFactory<RECIPE> factory;

    public ForcingRecipeSerializer(ForcingRecipeSerializer.IFactory<RECIPE> factory) {
        this.factory = factory;
    }

    @NotNull
    @Override
    public RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
        JsonElement firstItemInput = GsonHelper.isArrayNode(json, JsonConstants.ITEM_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.ITEM_INPUT) :
                GsonHelper.getAsJsonObject(json, JsonConstants.MAIN_INPUT);
        ItemStackIngredient firstItemIngredient = IngredientCreatorAccess.item().deserialize(firstItemInput);
        JsonElement secondItemInput = GsonHelper.isArrayNode(json, JsonConstants.ITEM_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.ITEM_INPUT) :
                GsonHelper.getAsJsonObject(json, JsonConstants.EXTRA_INPUT);
        ItemStackIngredient secondItemIngredient = IngredientCreatorAccess.item().deserialize(secondItemInput);
        JsonElement fluidInput = GsonHelper.isArrayNode(json, JsonConstants.FLUID_INPUT) ? GsonHelper.getAsJsonArray(json, JsonConstants.FLUID_INPUT) :
                GsonHelper.getAsJsonObject(json, JsonConstants.FLUID_INPUT);
        FluidStackIngredient fluidIngredient = IngredientCreatorAccess.fluid().deserialize(fluidInput);
        ItemStack mainItemOutput = ItemStack.EMPTY;
        ItemStack secondaryItemOutput = ItemStack.EMPTY;
        if (json.has(JsonConstants.SECONDARY_OUTPUT)) {
            if (json.has(JsonConstants.MAIN_OUTPUT)) {
                mainItemOutput = SerializerHelper.getItemStack(json, JsonConstants.MAIN_OUTPUT);
                if (mainItemOutput.isEmpty()) {
                    throw new JsonSyntaxException("Sawmill main recipe output must not be empty, if it is defined.");
                }
            }
            secondaryItemOutput = SerializerHelper.getItemStack(json, JsonConstants.SECONDARY_OUTPUT);
            if (secondaryItemOutput.isEmpty()) {
                throw new JsonSyntaxException("Sawmill secondary recipe output must not be empty, if there is no main output.");
            }
        } else {
            mainItemOutput = SerializerHelper.getItemStack(json, JsonConstants.MAIN_OUTPUT);
            if (mainItemOutput.isEmpty()) {
                throw new JsonSyntaxException("Sawmill main recipe output must not be empty, if there is no secondary output.");
            }
        }
        return this.factory.create(recipeId, firstItemIngredient, secondItemIngredient, fluidIngredient, mainItemOutput, secondaryItemOutput);
    }

    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient firstItemInput = IngredientCreatorAccess.item().read(buffer);
            ItemStackIngredient secondItemInput = IngredientCreatorAccess.item().read(buffer);
            FluidStackIngredient inputFluid = IngredientCreatorAccess.fluid().read(buffer);
            ItemStack mainOutputItem = buffer.readItem();
            ItemStack secondaryOutputItem = buffer.readItem();
            return this.factory.create(recipeId, firstItemInput, secondItemInput, inputFluid, mainOutputItem, secondaryOutputItem);
        } catch (Exception e) {
            Mekanism.logger.error("Error reading pressurized reaction recipe from packet.", e);
            throw e;
        }
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull RECIPE recipe) {
        try {
            recipe.write(buffer);
        } catch (Exception e) {
            Mekanism.logger.error("Error writing pressurized reaction recipe to packet.", e);
            throw e;
        }
    }

    @FunctionalInterface
    public interface IFactory<RECIPE extends ForcingRecipe> {

        RECIPE create(ResourceLocation id, ItemStackIngredient firstItemInput, ItemStackIngredient secondItemInput, FluidStackIngredient fluidInput,
                      ItemStack mainOutputItem, ItemStack secondaryItemOutput);
    }
}

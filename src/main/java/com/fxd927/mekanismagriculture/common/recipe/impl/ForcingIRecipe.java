package com.fxd927.mekanismagriculture.common.recipe.impl;

import com.fxd927.mekanismagriculture.api.recipes.ForcingRecipe;
import com.fxd927.mekanismagriculture.common.recipe.MARecipeType;
import com.fxd927.mekanismagriculture.common.registries.MABlocks;
import com.fxd927.mekanismagriculture.common.registries.MARecipeSerializers;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ForcingIRecipe extends ForcingRecipe {
    public ForcingIRecipe(ResourceLocation id, ItemStackIngredient firstItemInput, ItemStackIngredient secondItemInput, FluidStackIngredient fluidInput, ItemStack mainItemOutput, ItemStack secondaryItemOutput) {
        super(id, firstItemInput, secondItemInput, fluidInput, mainItemOutput, secondaryItemOutput);
    }

    @Override
    public RecipeType<ForcingRecipe> getType() {
        return MARecipeType.FORCING.get();
    }

    @Override
    public RecipeSerializer<ForcingRecipe> getSerializer() {
        return MARecipeSerializers.FORCING.get();
    }

    @Override
    public String getGroup() {
        return MABlocks.HIGH_SPEED_FORCING_MACHINE.getName();
    }

    @Override
    public ItemStack getToastSymbol() {
        return MABlocks.HIGH_SPEED_FORCING_MACHINE.getItemStack();
    }
}

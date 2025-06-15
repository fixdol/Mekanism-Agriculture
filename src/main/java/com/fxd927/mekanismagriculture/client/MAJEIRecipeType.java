package com.fxd927.mekanismagriculture.client;

import com.fxd927.mekanismagriculture.api.recipes.ForcingRecipe;
import com.fxd927.mekanismagriculture.common.registries.MABlocks;
import mekanism.client.jei.MekanismJEIRecipeType;

public class MAJEIRecipeType {
    public static final MekanismJEIRecipeType<ForcingRecipe> FORCING = new MekanismJEIRecipeType<>(MABlocks.HIGH_SPEED_FORCING_MACHINE, ForcingRecipe.class);
}

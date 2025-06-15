package com.fxd927.mekanismagriculture.common.recipe.lookup.monitor;

import com.fxd927.mekanismagriculture.common.recipe.lookup.IMARecipeLookupHandler;
import mekanism.api.recipes.MekanismRecipe;
import org.jetbrains.annotations.NotNull;

public class MAFactoryRecipeCacheLookupMonitor<RECIPE extends MekanismRecipe> extends MARecipeCacheLookupMonitor<RECIPE> {
    private final Runnable setSortingNeeded;

    public MAFactoryRecipeCacheLookupMonitor(IMARecipeLookupHandler<RECIPE> handler, int cacheIndex, Runnable setSortingNeeded) {
        super(handler, cacheIndex);
        this.setSortingNeeded = setSortingNeeded;
    }

    @Override
    public void onChange() {
        super.onChange();
        setSortingNeeded.run();
    }

    public void updateCachedRecipe(@NotNull RECIPE recipe) {
        cachedRecipe = createNewCachedRecipe(recipe, cacheIndex);
        hasNoRecipe = false;
    }
}

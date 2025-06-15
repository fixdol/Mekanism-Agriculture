package com.fxd927.mekanismagriculture.common.registries;

import com.fxd927.mekanismagriculture.api.recipes.ForcingRecipe;
import com.fxd927.mekanismagriculture.common.MekanismAgriculture;
import com.fxd927.mekanismagriculture.common.recipe.impl.ForcingIRecipe;
import com.fxd927.mekanismagriculture.common.recipe.serializer.ForcingRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class MARecipeSerializers {
    private MARecipeSerializers(){
    }

    public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = new RecipeSerializerDeferredRegister(MekanismAgriculture.MODID);

    public static final RecipeSerializerRegistryObject<ForcingRecipe> FORCING;

    static {
        FORCING = RECIPE_SERIALIZERS.register("forcing", () -> new ForcingRecipeSerializer<>(ForcingIRecipe::new));
    }
}

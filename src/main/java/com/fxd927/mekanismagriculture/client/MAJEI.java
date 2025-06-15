package com.fxd927.mekanismagriculture.client;

import com.fxd927.mekanismagriculture.client.jei.MARecipeRegistryHelper;
import com.fxd927.mekanismagriculture.client.jei.machine.ForcingRecipeCategory;
import com.fxd927.mekanismagriculture.common.MekanismAgriculture;
import com.fxd927.mekanismagriculture.common.recipe.MARecipeType;
import com.fxd927.mekanismagriculture.common.registries.MABlocks;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.fxd927.mekanismagriculture.client.MAJEIRecipeType.FORCING;

@JeiPlugin
public class MAJEI implements IModPlugin {
    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return MekanismAgriculture.rl("jei_plugin");
    }

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistration registry) {
        MekanismJEI.registerItemSubtypes(registry, MABlocks.BLOCKS.getAllBlocks());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(new ForcingRecipeCategory(guiHelper, FORCING));
       }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registry) {
        CatalystRegistryHelper.register(registry, MABlocks.HIGH_SPEED_FORCING_MACHINE);
        }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registry) {
        MARecipeRegistryHelper.register(registry, FORCING, MARecipeType.FORCING);
    }
}

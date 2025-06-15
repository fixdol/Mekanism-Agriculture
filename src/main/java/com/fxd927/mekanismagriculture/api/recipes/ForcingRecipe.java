package com.fxd927.mekanismagriculture.api.recipes;

import mekanism.api.recipes.ElectrolysisRecipe;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class ForcingRecipe extends MekanismRecipe implements TriPredicate<@NotNull ItemStack, @NotNull ItemStack, @NotNull FluidStack> {
    private final ItemStackIngredient firstInputItem;
    private final ItemStackIngredient secondInputItem;
    private final FluidStackIngredient inputFluid;
    private final ItemStack mainOutputItem;
    private final ItemStack secondaryOutputItem;

    /**
     * @param id             Recipe name.
     * @param firstInputItem     First Item input.
     * @param secondInputItem     Second Item input.
     * @param inputFluid       Fluid input.
     * @param mainOutputItem     Item output.
     * @param secondaryOutputItem     Item output.
     *
     * @apiNote At least one output must not be empty.
     */
    public ForcingRecipe(ResourceLocation id, ItemStackIngredient firstInputItem, ItemStackIngredient secondInputItem, FluidStackIngredient inputFluid, ItemStack mainOutputItem, ItemStack secondaryOutputItem) {
        super(id);
        this.firstInputItem = Objects.requireNonNull(firstInputItem, "First Item input cannot be null.");
        this.secondInputItem = Objects.requireNonNull(secondInputItem, "Second Item input cannot be null.");
        this.inputFluid = Objects.requireNonNull(inputFluid, "Fluid input cannot be null.");
        Objects.requireNonNull(mainOutputItem, "Main Item output cannot be null.");
        Objects.requireNonNull(secondaryOutputItem, "Secondary Item output cannot be null.");
        if (mainOutputItem.isEmpty() && secondaryOutputItem.isEmpty()) {
            throw new IllegalArgumentException("At least one output must not be empty.");
        }
        if (mainOutputItem.isEmpty()) {
            throw new IllegalArgumentException("At least one output must not be empty.");
        }
        this.mainOutputItem = mainOutputItem.copy();
        this.secondaryOutputItem = secondaryOutputItem.copy();
    }

    /**
     * Gets the item input ingredient.
     */
    public ItemStackIngredient getFirstInputItem() {
        return firstInputItem;
    }

    /**
     * Gets the item input ingredient.
     */
    public ItemStackIngredient getSecondInputItem() {
        return secondInputItem;
    }

    /**
     * Gets the fluid input ingredient.
     */
    public FluidStackIngredient getInputFluid() {
        return inputFluid;
    }

    @Override
    public boolean test(ItemStack first, ItemStack second, FluidStack liquid) {
        return this.firstInputItem.test(first) && this.secondInputItem.test(second) && this.inputFluid.test(liquid);
    }

    public List<ItemStack> getFirstOutputDefinition() {
        return Collections.singletonList(mainOutputItem);
    }

    public List<ItemStack> getSecondOutputDefinition() {
        return Collections.singletonList(secondaryOutputItem);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public ItemStack getMainOutput(ItemStack firstInputItem, ItemStack secondInputItem, FluidStack fluidStack) {
        return mainOutputItem.copy();
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public ItemStack getSecondaryOutput(ItemStack firstInputItem, ItemStack secondInputItem, FluidStack fluidStack) {
        return secondaryOutputItem.copy();
    }

    @Override
    public boolean isIncomplete() {
        return firstInputItem.hasNoMatchingInstances() || secondInputItem.hasNoMatchingInstances() || inputFluid.hasNoMatchingInstances();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        firstInputItem.write(buffer);
        secondInputItem.write(buffer);
        inputFluid.write(buffer);
        buffer.writeItem(mainOutputItem);
        buffer.writeItem(secondaryOutputItem);
    }

    public record ForcingRecipeOutput(@NotNull ItemStack mainItem, @NotNull ItemStack secondaryItem, @NotNull FluidStack fluidStack) {

        public ForcingRecipeOutput {
            Objects.requireNonNull(mainItem, "Left output cannot be null.");
            Objects.requireNonNull(secondaryItem, "Right output cannot be null.");
            if (mainItem.isEmpty()) {
                throw new IllegalArgumentException("Left output cannot be empty.");
            } else if (secondaryItem.isEmpty()) {
                throw new IllegalArgumentException("Right output cannot be empty.");
            }
        }
    }
}

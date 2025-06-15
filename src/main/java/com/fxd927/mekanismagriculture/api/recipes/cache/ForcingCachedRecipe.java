package com.fxd927.mekanismagriculture.api.recipes.cache;

import com.fxd927.mekanismagriculture.api.recipes.ForcingRecipe;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BooleanSupplier;

@NothingNullByDefault
public class ForcingCachedRecipe extends CachedRecipe<ForcingRecipe> {
    private final IOutputHandler<@NotNull ItemStack> mainOutputHandler;
    private final IOutputHandler<@NotNull ItemStack> secondaryOutputHandler;
    private final IInputHandler<@NotNull ItemStack> firstItemInputHandler;
    private final IInputHandler<@NotNull ItemStack> secondItemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;

    private ItemStack recipeFirstItem = ItemStack.EMPTY;
    private ItemStack recipeSecondItem = ItemStack.EMPTY;
    private FluidStack recipeFluid = FluidStack.EMPTY;

    private ItemStack mainOutput = ItemStack.EMPTY;
    private ItemStack secondaryOutput = ItemStack.EMPTY;

    /**
     * @param recipe            Recipe.
     * @param recheckAllErrors  Returns {@code true} if processing should be continued even if an error is hit in order to gather all the errors. It is recommended to not
     *                          do this every tick or if there is no one viewing recipes.
     * @param firstItemInputHandler  Item input handler.
     * @param secondItemInputHandler  Item input handler.
     * @param fluidInputHandler Chemical input handler.
     * @param mainOutputHandler     Output handler.
     * @param secondaryOutputHandler     Output handler.
     */
    public ForcingCachedRecipe(ForcingRecipe recipe, BooleanSupplier recheckAllErrors, IInputHandler<@NotNull ItemStack> firstItemInputHandler, IInputHandler<@NotNull ItemStack> secondItemInputHandler,
                               IInputHandler<@NotNull FluidStack> fluidInputHandler, IOutputHandler<@NotNull ItemStack> mainOutputHandler, IOutputHandler<@NotNull ItemStack> secondaryOutputHandler) {
        super(recipe, recheckAllErrors);
        this.firstItemInputHandler = Objects.requireNonNull(firstItemInputHandler, "First Item input handler cannot be null.");
        this.secondItemInputHandler = Objects.requireNonNull(secondItemInputHandler, "Second Item input handler cannot be null.");
        this.fluidInputHandler = Objects.requireNonNull(fluidInputHandler, "Gas input handler cannot be null.");
        this.mainOutputHandler = Objects.requireNonNull(mainOutputHandler, "Input handler cannot be null.");
        this.secondaryOutputHandler = Objects.requireNonNull(secondaryOutputHandler, "Input handler cannot be null.");
    }

    @Override
    protected void calculateOperationsThisTick(OperationTracker tracker) {
        super.calculateOperationsThisTick(tracker);
        if (tracker.shouldContinueChecking()) {
            recipeFirstItem = firstItemInputHandler.getRecipeInput(recipe.getFirstInputItem());
            if (recipeFirstItem.isEmpty()) {
                tracker.mismatchedRecipe();
            } else {
                recipeFluid = fluidInputHandler.getRecipeInput(recipe.getInputFluid());
                if (recipeFluid.isEmpty()) {
                    tracker.mismatchedRecipe();
                } else {
                    recipeSecondItem = secondItemInputHandler.getRecipeInput(recipe.getSecondInputItem());
                    if (recipeSecondItem.isEmpty()) {
                        tracker.mismatchedRecipe();
                    } else {
                        firstItemInputHandler.calculateOperationsCanSupport(tracker, recipeFirstItem);
                        if (tracker.shouldContinueChecking()) {
                            fluidInputHandler.calculateOperationsCanSupport(tracker, recipeFluid);
                            if (tracker.shouldContinueChecking()) {
                                secondItemInputHandler.calculateOperationsCanSupport(tracker, recipeSecondItem);
                                if (tracker.shouldContinueChecking()) {
                                    mainOutput = recipe.getMainOutput(recipeFirstItem, recipeSecondItem, recipeFluid);
                                    mainOutputHandler.calculateOperationsCanSupport(tracker, mainOutput);
                                    secondaryOutput = recipe.getSecondaryOutput(recipeFirstItem, recipeSecondItem, recipeFluid);
                                    secondaryOutputHandler.calculateOperationsCanSupport(tracker, secondaryOutput);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInputValid() {
        ItemStack firstItem = firstItemInputHandler.getInput();
        if (firstItem.isEmpty()) {
            return false;
        }
        ItemStack secondItem = secondItemInputHandler.getInput();
        if (secondItem.isEmpty()) {
            return false;
        }
        FluidStack fluid = fluidInputHandler.getInput();
        return !fluid.isEmpty() && recipe.test(firstItem, secondItem, fluid);
    }

    @Override
    protected void finishProcessing(int operations) {
        if (mainOutput != null && secondaryOutput != null && !recipeFirstItem.isEmpty() && !recipeFluid.isEmpty() && !recipeSecondItem.isEmpty()) {
            fluidInputHandler.use(recipeFluid, operations);
            mainOutputHandler.handleOutput(mainOutput, operations);
            secondaryOutputHandler.handleOutput(secondaryOutput, operations);
        }
    }
}

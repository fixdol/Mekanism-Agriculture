package com.fxd927.mekanismagriculture.client.jei.machine;

import com.fxd927.mekanismagriculture.api.recipes.ForcingRecipe;
import com.fxd927.mekanismagriculture.common.registries.MABlocks;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityHighSpeedForcingMachine;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.util.MekanismUtils;
import mekanism.generators.client.gui.element.GuiStateTexture;
import mekanism.generators.common.MekanismGenerators;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BooleanSupplier;

public class ForcingRecipeCategory extends BaseRecipeCategory<ForcingRecipe> {
    private final GuiGauge<?> inputFluid;
    private final GuiSlot outputSlot;
    private final GuiSlot firstInputSlot;
    private final GuiSlot secondInputSlot;

    BooleanSupplier alwaysTrue = () -> true;

    public ForcingRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<ForcingRecipe> recipeType) {
        super(helper, recipeType, MABlocks.HIGH_SPEED_FORCING_MACHINE, 3, 3, 170, 79);
        inputFluid = addElement(GuiFluidGauge.getDummy(GaugeType.SMALL_MED.with(DataType.INPUT), this, 12, 20));
        outputSlot = addSlot(SlotType.OUTPUT_WIDE, 113, 30);
        firstInputSlot = addSlot(SlotType.INPUT, 35, 23);
        secondInputSlot = addSlot(SlotType.EXTRA, 35, 49);
        addSlot(SlotType.POWER, 154, 62).with(SlotOverlay.POWER);
        addSimpleProgress(ProgressType.LARGE_RIGHT, 60, 40);
        //addElement(new GuiStateTexture(this, 75, 23, alwaysTrue, MekanismGenerators.rl(MekanismUtils.ResourceType.GUI.getPrefix() + "sees_sun.png"),
                //MekanismGenerators.rl(MekanismUtils.ResourceType.GUI.getPrefix() + "no_sun.png")));
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 5));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, ForcingRecipe recipe, @NotNull IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, firstInputSlot, recipe.getFirstInputItem().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, secondInputSlot, recipe.getSecondInputItem().getRepresentations());
        initFluid(builder, RecipeIngredientRole.INPUT, inputFluid, recipe.getInputFluid().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputSlot.getRelativeX() + 4, outputSlot.getRelativeY() + 4, recipe.getFirstOutputDefinition());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputSlot.getRelativeX() + 20, outputSlot.getRelativeY() + 4, recipe.getSecondOutputDefinition());
    }
}

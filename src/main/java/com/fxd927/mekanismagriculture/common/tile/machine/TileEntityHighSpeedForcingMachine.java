package com.fxd927.mekanismagriculture.common.tile.machine;

import com.fxd927.mekanismagriculture.api.recipes.ForcingRecipe;
import com.fxd927.mekanismagriculture.api.recipes.cache.ForcingCachedRecipe;
import com.fxd927.mekanismagriculture.common.inventory.slot.MAInputInventorySlot;
import com.fxd927.mekanismagriculture.common.recipe.IMARecipeTypeProvider;
import com.fxd927.mekanismagriculture.common.recipe.MARecipeType;
import com.fxd927.mekanismagriculture.common.recipe.lookup.IMATripleRecipeLookupHandler;
import com.fxd927.mekanismagriculture.common.recipe.lookup.cache.MAInputRecipeCache;
import com.fxd927.mekanismagriculture.common.registries.MABlocks;
import com.fxd927.mekanismagriculture.common.tile.prefab.MATileEntityProgressMachine;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static mekanism.common.tile.machine.TileEntityPressurizedReactionChamber.NOT_ENOUGH_FLUID_INPUT_ERROR;

public class TileEntityHighSpeedForcingMachine extends MATileEntityProgressMachine<ForcingRecipe> implements
        IMATripleRecipeLookupHandler.DoubleItemSingleFluidRecipeLookupHandler<ForcingRecipe> {

    public static final CachedRecipe.OperationTracker.RecipeError NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR = CachedRecipe.OperationTracker.RecipeError.create();
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT,
            CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR,
            CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );
    public static final int MAX_FLUID = 10_000;
    public static final int BASE_TICKS_REQUIRED = 100;

    public BasicFluidTank inputTank;

    private final IOutputHandler<@NotNull ItemStack> mainOutputHandler;
    private final IOutputHandler<@NotNull ItemStack> secondaryOutputHandler;
    private final IInputHandler<@NotNull ItemStack> firstItemInputHandler;
    private final IInputHandler<@NotNull ItemStack> secondItemInputHandler;
    private final IInputHandler<@NotNull FluidStack> fluidInputHandler;

    private MachineEnergyContainer<TileEntityHighSpeedForcingMachine> energyContainer;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    MAInputInventorySlot firstInputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    MAInputInventorySlot secondInputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    OutputInventorySlot mainOutputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    OutputInventorySlot secondaryOutputSlot;
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class, methodNames = "getEnergyItem", docPlaceholder = "energy slot")
    EnergyInventorySlot energySlot;

    public TileEntityHighSpeedForcingMachine(BlockPos pos, BlockState state) {
        super(MABlocks.HIGH_SPEED_FORCING_MACHINE, pos, state, TRACKED_ERROR_TYPES, BASE_TICKS_REQUIRED);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID, TransmissionType.ENERGY);
        configComponent.setupItemIOExtraConfig(firstInputSlot, mainOutputSlot, secondInputSlot, energySlot);

        ConfigInfo itemConfig = configComponent.getConfig(TransmissionType.ITEM);
        if (itemConfig != null) {
            itemConfig.addSlotInfo(DataType.OUTPUT_1, new InventorySlotInfo(true, true, mainOutputSlot));
            itemConfig.addSlotInfo(DataType.OUTPUT_2, new InventorySlotInfo(true, true, secondaryOutputSlot));
            itemConfig.addSlotInfo(DataType.ENERGY, new InventorySlotInfo(true, true, energySlot));
            itemConfig.setDataType(DataType.INPUT);
            itemConfig.setDataType(DataType.OUTPUT_1, RelativeSide.RIGHT);
            itemConfig.setDataType(DataType.OUTPUT_2, RelativeSide.FRONT);
            itemConfig.setDataType(DataType.ENERGY);
        }

        configComponent.setupInputConfig(TransmissionType.FLUID, inputTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID)
                .setCanTankEject(tank -> tank != inputTank);

        firstItemInputHandler = InputHelper.getInputHandler(firstInputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        secondItemInputHandler = InputHelper.getInputHandler(secondInputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        fluidInputHandler = InputHelper.getInputHandler(inputTank, NOT_ENOUGH_FLUID_INPUT_ERROR);
        mainOutputHandler = OutputHelper.getOutputHandler(mainOutputSlot, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        secondaryOutputHandler = OutputHelper.getOutputHandler(secondaryOutputSlot, NOT_ENOUGH_SPACE_SECONDARY_OUTPUT_ERROR);
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTank = BasicFluidTank.create(MAX_FLUID, fluid -> containsRecipeC(inputTank.getFluid()),
                this::containsRecipeC, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(firstInputSlot = MAInputInventorySlot.at(item -> containsRecipeABC(item, secondInputSlot.getStack(), inputTank.getFluid()), this::containsRecipeA, recipeCacheListener,
                35, 23)
        ).tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT)));
        builder.addSlot(secondInputSlot = MAInputInventorySlot.at(item -> containsRecipeBAC(firstInputSlot.getStack(), item, inputTank.getFluid()), this::containsRecipeB, recipeCacheListener,
                35, 49)
        ).tracksWarnings(slot -> slot.warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_SECONDARY_INPUT)));builder.addSlot(mainOutputSlot = OutputInventorySlot.at(listener, 118, 35));
        builder.addSlot(secondaryOutputSlot = OutputInventorySlot.at(listener, 134, 35));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 154, 62));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public IMARecipeTypeProvider<ForcingRecipe, MAInputRecipeCache.DoubleItemSingleFluid<ForcingRecipe>> getRecipeType() {
        return MARecipeType.FORCING;
    }

    @Nullable
    @Override
    public ForcingRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(firstItemInputHandler, secondItemInputHandler, fluidInputHandler);
    }

    @NotNull
    @Override
    public CachedRecipe<ForcingRecipe> createNewCachedRecipe(@NotNull ForcingRecipe recipe, int cacheIndex) {
        return new ForcingCachedRecipe(recipe, recheckAllRecipeErrors, firstItemInputHandler,  secondItemInputHandler, fluidInputHandler, mainOutputHandler, secondaryOutputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    public MachineEnergyContainer<TileEntityHighSpeedForcingMachine> getEnergyContainer() {
        return energyContainer;
    }
}

package com.fxd927.mekanismagriculture.common.tile.prefab;

import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TileEntityDependsOnSunshineMachine<RECIPE extends MekanismRecipe> extends MATileEntityRecipeMachine<RECIPE> {
    public int ticksRequired;
    protected int baseTicksRequired;
    private int operatingTicks;

    protected TileEntityDependsOnSunshineMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes, int baseTicksRequired) {
        super(blockProvider, pos, state, errorTypes);
        this.baseTicksRequired = baseTicksRequired;
        this.ticksRequired = MekanismUtils.getTicks(this, baseTicksRequired);
    }

    public double getScaledProgress() {
        return getOperatingTicks() / (double) ticksRequired;
    }

    @ComputerMethod(nameOverride = "getRecipeProgress")
    public int getOperatingTicks() {
        return operatingTicks;
    }

    protected void setOperatingTicks(int ticks) {
        this.operatingTicks = ticks;
    }

    @ComputerMethod
    public int getTicksRequired() {
        return ticksRequired;
    }

    @Override
    public int getSavedOperatingTicks(int cacheIndex) {
        return getOperatingTicks();
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        operatingTicks = nbt.getInt(NBTConstants.PROGRESS);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putInt(NBTConstants.PROGRESS, getOperatingTicks());
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            updateTicksRequired();
        }
    }

    @NotNull
    @Override
    public List<Component> getInfo(@NotNull Upgrade upgrade) {
        return UpgradeUtils.getMultScaledInfo(this, upgrade);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getOperatingTicks, this::setOperatingTicks));
        container.track(SyncableInt.create(this::getTicksRequired, value -> ticksRequired = value));
    }

    @ComputerMethod
    public boolean checkCanSeeSun() {
        return WorldUtils.canSeeSun(level, worldPosition.above());
    }

    @ComputerMethod
    public boolean checkBrightness(){
        int brightness = level.getBrightness(LightLayer.BLOCK, getBlockPos());
        return brightness > 0;
    }

    protected void updateTicksRequired() {
        int base = MekanismUtils.getTicks(this, baseTicksRequired);
        ticksRequired = !checkCanSeeSun() && !checkBrightness() ? base * 4 : base;
    }

    public boolean isTicksRequiredQuadrupled() {
        int base = MekanismUtils.getTicks(this, baseTicksRequired);
        return ticksRequired < base * 4;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        checkCanSeeSun();
        updateTicksRequired();
        isTicksRequiredQuadrupled();
    }
}

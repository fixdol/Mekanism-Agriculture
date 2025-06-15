package com.fxd927.mekanismagriculture.common.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class MAStorageConfig extends BaseMekanismConfig {
    private final ForgeConfigSpec configSpec;

    public final CachedFloatingLongValue highSpeedForcingMachine;
    public final CachedFloatingLongValue electricFisher;


    MAStorageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Agriculture Energy Storage Config. This config is synced from server to client.").push("storage");

        electricFisher = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).", "airCompressor",
                FloatingLong.createConst(40_000));
        highSpeedForcingMachine = CachedFloatingLongValue.define(this, builder, "Base energy storage (Joules).", "airCompressor",
                FloatingLong.createConst(40_000));


        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "agriculture-storage";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public ModConfig.Type getConfigType() {
        return ModConfig.Type.SERVER;
    }

    @Override
    public boolean addToContainer() {
        return false;
    }
}

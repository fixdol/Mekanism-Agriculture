package com.fxd927.mekanismagriculture.common.config;

import mekanism.api.math.FloatingLong;
import mekanism.common.config.BaseMekanismConfig;
import mekanism.common.config.value.CachedFloatingLongValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class MAUsageConfig extends BaseMekanismConfig {
    public final CachedFloatingLongValue electricFisher;
    public final CachedFloatingLongValue highSpeedForcingMachine;

    private final ForgeConfigSpec configSpec;

    MAUsageConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("MA Energy Usage Config. This config is synced from server to client.").push("storage");

        electricFisher = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).", "airCompressor", FloatingLong.createConst(1_000));
        highSpeedForcingMachine = CachedFloatingLongValue.define(this, builder, "Energy per operation tick (Joules).", "airCompressor", FloatingLong.createConst(1_000));

        builder.pop();
        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "agriculture-usage";
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

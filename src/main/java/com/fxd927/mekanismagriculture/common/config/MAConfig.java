package com.fxd927.mekanismagriculture.common.config;

import mekanism.common.config.MekanismConfigHelper;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

public class MAConfig {
    private MAConfig() {
    }

    public static final MAStorageConfig storageConfig = new MAStorageConfig();
    public static final MAUsageConfig usageConfig = new MAUsageConfig();

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        MekanismConfigHelper.registerConfig(modContainer, storageConfig);
        MekanismConfigHelper.registerConfig(modContainer, usageConfig);
    }
}

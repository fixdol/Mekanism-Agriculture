package com.fxd927.mekanismagriculture.common.registries;

import com.fxd927.mekanismagriculture.common.MALang;
import com.fxd927.mekanismagriculture.common.config.MAConfig;
import com.fxd927.mekanismagriculture.common.content.blocktype.MABlockShapes;
import com.fxd927.mekanismagriculture.common.content.blocktype.MAMachine;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityElectricFisher;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityHighSpeedForcingMachine;
import mekanism.api.Upgrade;
import mekanism.common.block.attribute.Attributes;

import java.util.EnumSet;

public class MABlockTypes {
    private MABlockTypes(){
    }

    public static final MAMachine<TileEntityElectricFisher> ELECTRIC_FISHER = MAMachine.MAMachineBuilder
            .createMAMachine(() -> MATileEntityTypes.ELECTRIC_FISHER, MALang.DESCRIPTION_ELECTRIC_FISHER)
            .withGui(() -> MAContainerTypes.ELECTRIC_FISHER)
            .withEnergyConfig(MAConfig.usageConfig.electricFisher, MAConfig.storageConfig.electricFisher)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
            .withComputerSupport("electricFisher")
            .replace(Attributes.ACTIVE)
            .build();

    public static final MAMachine<TileEntityHighSpeedForcingMachine> HIGH_SPEED_FORCING_MACHINE = MAMachine.MAMachineBuilder
            .createMAMachine(() -> MATileEntityTypes.HIGH_SPEED_FORCING_MACHINE, MALang.DESCRIPTION_HIGH_SPEED_FORCING_MACHINE)
            .withGui(() -> MAContainerTypes.HIGH_SPEED_FORCING_MACHINE)
            .withEnergyConfig(MAConfig.usageConfig.highSpeedForcingMachine, MAConfig.storageConfig.highSpeedForcingMachine)
            .withCustomShape(MABlockShapes.HIGH_SPEED_FORCING_MACHINE)
            .withSupportedUpgrades(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING))
            .withComputerSupport("highSpeedForcingMachine")
            .replace(Attributes.ACTIVE)
            .build();
}

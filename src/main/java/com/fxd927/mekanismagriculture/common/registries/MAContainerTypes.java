package com.fxd927.mekanismagriculture.common.registries;

import com.fxd927.mekanismagriculture.common.MekanismAgriculture;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityElectricFisher;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityHighSpeedForcingMachine;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class MAContainerTypes {
    private MAContainerTypes(){
    }

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(MekanismAgriculture.MODID);

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityElectricFisher>> ELECTRIC_FISHER;
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityHighSpeedForcingMachine>> HIGH_SPEED_FORCING_MACHINE;

    static {
        ELECTRIC_FISHER = CONTAINER_TYPES.custom(MABlocks.ELECTRIC_FISHER, TileEntityElectricFisher.class).offset(0, 76).build();;
        HIGH_SPEED_FORCING_MACHINE = CONTAINER_TYPES.register(MABlocks.HIGH_SPEED_FORCING_MACHINE, TileEntityHighSpeedForcingMachine.class);
    }
}

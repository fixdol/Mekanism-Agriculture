package com.fxd927.mekanismagriculture.common.registries;

import com.fxd927.mekanismagriculture.common.MekanismAgriculture;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityElectricFisher;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityHighSpeedForcingMachine;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class MATileEntityTypes {
    private MATileEntityTypes(){
    }

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(MekanismAgriculture.MODID);

    public static final TileEntityTypeRegistryObject<TileEntityElectricFisher> ELECTRIC_FISHER;
    public static final TileEntityTypeRegistryObject<TileEntityHighSpeedForcingMachine> HIGH_SPEED_FORCING_MACHINE;

    static {
        ELECTRIC_FISHER = TILE_ENTITY_TYPES.register(MABlocks.ELECTRIC_FISHER, TileEntityElectricFisher::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
        HIGH_SPEED_FORCING_MACHINE = TILE_ENTITY_TYPES.register(MABlocks.HIGH_SPEED_FORCING_MACHINE, TileEntityHighSpeedForcingMachine::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    }
}

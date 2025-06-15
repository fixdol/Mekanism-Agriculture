package com.fxd927.mekanismagriculture.common.registries;

import com.fxd927.mekanismagriculture.common.MekanismAgriculture;
import com.fxd927.mekanismagriculture.common.content.blocktype.MAMachine;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityElectricFisher;
import com.fxd927.mekanismagriculture.common.tile.machine.TileEntityHighSpeedForcingMachine;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;

public class MABlocks {
    private MABlocks(){
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(MekanismAgriculture.MODID);

    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityElectricFisher, MAMachine<TileEntityElectricFisher>>, ItemBlockMachine> ELECTRIC_FISHER;
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityHighSpeedForcingMachine, MAMachine<TileEntityHighSpeedForcingMachine>>, ItemBlockMachine> HIGH_SPEED_FORCING_MACHINE;

    static {
        ELECTRIC_FISHER = BLOCKS.register("electric_fisher", () -> new BlockTile.BlockTileModel<>(MABlockTypes.ELECTRIC_FISHER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
        HIGH_SPEED_FORCING_MACHINE = BLOCKS.register("high_speed_forcing_machine", () -> new BlockTile.BlockTileModel<>(MABlockTypes.HIGH_SPEED_FORCING_MACHINE, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    }
}
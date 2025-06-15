package com.fxd927.mekanismagriculture.common;

import com.fxd927.mekanismagriculture.common.config.MAConfig;
import com.fxd927.mekanismagriculture.common.recipe.MARecipeType;
import com.fxd927.mekanismagriculture.common.registries.MABlocks;
import com.fxd927.mekanismagriculture.common.registries.MAContainerTypes;
import com.fxd927.mekanismagriculture.common.registries.MARecipeSerializers;
import com.fxd927.mekanismagriculture.common.registries.MATileEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MekanismAgriculture.MODID)
public class MekanismAgriculture {
    public static final String MODID = "mekanismagriculture";

    public MekanismAgriculture(){
        this(FMLJavaModLoadingContext.get());
    }

    public MekanismAgriculture(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        MABlocks.BLOCKS.register(modEventBus);
        MAConfig.registerConfigs(ModLoadingContext.get());
        MAContainerTypes.CONTAINER_TYPES.register(modEventBus);
        MARecipeType.RECIPE_TYPES.register(modEventBus);
        MARecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        MATileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation rl(String path){
        return new ResourceLocation(MekanismAgriculture.MODID, path);
    }
}

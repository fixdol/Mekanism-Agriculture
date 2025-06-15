package com.fxd927.mekanismagriculture.client;

import com.fxd927.mekanismagriculture.client.gui.machine.GuiElectricFisher;
import com.fxd927.mekanismagriculture.client.gui.machine.GuiHighSpeedForcingMachine;
import com.fxd927.mekanismagriculture.common.MekanismAgriculture;
import com.fxd927.mekanismagriculture.common.registries.MAContainerTypes;
import mekanism.client.ClientRegistrationUtil;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = MekanismAgriculture.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MAClientRegistration {
    private MAClientRegistration() {
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContainers(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {
            ClientRegistrationUtil.registerScreen(MAContainerTypes.ELECTRIC_FISHER, GuiElectricFisher::new);
            ClientRegistrationUtil.registerScreen(MAContainerTypes.HIGH_SPEED_FORCING_MACHINE, GuiHighSpeedForcingMachine::new);
        });
    }
}

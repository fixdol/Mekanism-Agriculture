package com.fxd927.mekanismagriculture.common;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum MALang implements ILangEntry {
    DESCRIPTION_ELECTRIC_FISHER("description","electric_fisher"),
    DESCRIPTION_HIGH_SPEED_FORCING_MACHINE("description","high_speed_forcing_machine"),
    MEKANISM_AGRICULTURE("constants","mod_name");

    private final String key;

    MALang(String type,String path){
        this(Util.makeDescriptionId(type, MekanismAgriculture.rl(path)));
    }

    MALang(String key){
        this.key = key;
    }

    @Override
    public String getTranslationKey(){
        return key;
    }
}

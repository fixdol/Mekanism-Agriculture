package com.fxd927.mekanismagriculture.common.inventory.slot;

import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.common.inventory.slot.BasicInventorySlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

@NothingNullByDefault
public class MABasicInventorySlot extends BasicInventorySlot {
    protected MABasicInventorySlot(BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canExtract, BiPredicate<@NotNull ItemStack, @NotNull AutomationType> canInsert, Predicate<@NotNull ItemStack> validator, @Nullable IContentsListener listener, int x, int y) {
        super(1, canExtract, canInsert, validator, listener, x, y);
    }
}

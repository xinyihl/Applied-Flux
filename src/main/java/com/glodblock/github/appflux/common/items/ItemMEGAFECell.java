package com.glodblock.github.appflux.common.items;

import com.glodblock.github.appflux.common.AFItemAndBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMEGAFECell extends ItemFECell {

    public ItemMEGAFECell(Item coreItem, int kilobytes, double idleDrain) {
        super(coreItem, kilobytes, idleDrain);
    }

    @Override
    public ItemStack getHousing() {
        return new ItemStack(AFItemAndBlock.MEGA_FE_HOUSING);
    }
}

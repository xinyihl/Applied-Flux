package com.glodblock.github.appflux.common.items;

import appeng.api.stacks.AEKeyType;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.items.AEBaseItem;
import com.glodblock.github.appflux.api.IFluxCell;
import com.glodblock.github.appflux.common.me.key.type.EnergyType;
import com.glodblock.github.appflux.common.me.key.type.FluxKeyType;
import net.minecraft.item.ItemStack;

public class ItemCreativeFECell extends AEBaseItem implements IFluxCell {

    public ItemCreativeFECell() {
        setMaxStackSize(1);
    }

    @Override
    public AEKeyType getKeyType() {
        return FluxKeyType.TYPE;
    }

    @Override
    public EnergyType getEnergyType() {
        return EnergyType.FE;
    }

    @Override
    public long getBytes(ItemStack cellItem) {
        return Long.MAX_VALUE / FluxKeyType.TYPE.getAmountPerByte();
    }

    @Override
    public double getIdleDrain() {
        return 0;
    }

    @Override
    public IUpgradeInventory getUpgrades(ItemStack stack) {
        return UpgradeInventories.empty();
    }
}

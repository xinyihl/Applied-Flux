package com.glodblock.github.appflux.mixins;

import appeng.api.config.Actionable;
import appeng.tile.networking.TileCreativeEnergyCell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileCreativeEnergyCell.class, remap = false)
public abstract class MixinCreativeEnergyCell {

    @Overwrite
    public double injectAEPower(double amt, Actionable mode) {
        return amt;
    }
}

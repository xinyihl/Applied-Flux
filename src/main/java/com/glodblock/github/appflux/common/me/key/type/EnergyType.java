package com.glodblock.github.appflux.common.me.key.type;

import com.glodblock.github.appflux.AppFlux;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public enum EnergyType {
    FE("forge");

    private final String from;

    EnergyType(String from) {
        this.from = from;
    }

    public ResourceLocation id() {
        return AppFlux.id(name().toLowerCase());
    }

    public String from() {
        return from;
    }

    public ITextComponent translate() {
        return new TextComponentTranslation("appflux.type." + name().toLowerCase() + ".name");
    }
}

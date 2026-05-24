package com.glodblock.github.appflux.mixins;

import appeng.api.upgrades.IUpgradeableObject;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.implementations.GuiPatternProvider;
import appeng.client.gui.style.GuiStyle;
import appeng.client.gui.widgets.UpgradesPanel;
import appeng.container.SlotSemantics;
import appeng.container.implementations.ContainerPatternProvider;
import appeng.helpers.patternprovider.PatternProviderLogic;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiPatternProvider.class, remap = false)
public abstract class MixinGuiPatternProvider extends AEBaseGui<ContainerPatternProvider> {

    protected MixinGuiPatternProvider(ContainerPatternProvider container, InventoryPlayer playerInventory, GuiStyle style) {
        super(container, playerInventory, style);
    }

    @Inject(method = "<init>(Lappeng/container/implementations/ContainerPatternProvider;Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/util/text/ITextComponent;Lappeng/client/gui/style/GuiStyle;)V", at = @At("TAIL"))
    private void appflux$addUpgradesPanel(ContainerPatternProvider container, InventoryPlayer playerInventory, ITextComponent title, GuiStyle style, CallbackInfo ci) {
        PatternProviderLogic logic = container.getLogic();
        if (logic instanceof IUpgradeableObject) {
            widgets.add("upgrades", new UpgradesPanel(container.getSlots(SlotSemantics.UPGRADE), (IUpgradeableObject) logic));
        }
    }
}

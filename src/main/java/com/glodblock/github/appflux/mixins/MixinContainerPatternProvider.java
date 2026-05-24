package com.glodblock.github.appflux.mixins;

import appeng.api.upgrades.IUpgradeableObject;
import appeng.container.AEBaseContainer;
import appeng.container.implementations.ContainerPatternProvider;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ContainerPatternProvider.class, remap = false)
public abstract class MixinContainerPatternProvider extends AEBaseContainer {

    @Final
    @Shadow
    private PatternProviderLogic logic;

    protected MixinContainerPatternProvider(InventoryPlayer playerInventory, Object target) {
        super(playerInventory, target);
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/player/InventoryPlayer;Lappeng/helpers/patternprovider/PatternProviderLogicHost;)V", at = @At("TAIL"))
    private void appflux$setupUpgrades(InventoryPlayer playerInventory, PatternProviderLogicHost host, CallbackInfo ci) {
        if (logic instanceof IUpgradeableObject) {
            setupUpgrades(((IUpgradeableObject) logic).getUpgrades());
        }
    }
}

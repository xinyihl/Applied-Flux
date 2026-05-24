package com.glodblock.github.appflux.mixins;

import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageService;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.api.upgrades.UpgradeInventories;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import com.glodblock.github.appflux.common.AFItemAndBlock;
import com.glodblock.github.appflux.common.me.energy.EnergyHandler;
import com.glodblock.github.appflux.common.me.service.EnergyDistributeService;
import com.glodblock.github.appflux.common.me.service.IEnergyDistributor;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Mixin(value = PatternProviderLogic.class, remap = false)
public abstract class MixinPatternProviderLogic implements IUpgradeableObject, IEnergyDistributor, INeighborListener {

    @Unique
    private IUpgradeInventory af$upgrades = UpgradeInventories.empty();
    @Unique
    private Set<EnumFacing> af$sides = java.util.EnumSet.noneOf(EnumFacing.class);
    @Unique
    private EnergyDistributeService af$service;
    @Unique
    private final EnergyHandler.SendAction[] af$actions = new EnergyHandler.SendAction[6];
    @Final
    @Shadow
    private PatternProviderLogicHost host;
    @Final
    @Shadow
    private IManagedGridNode mainNode;
    @Final
    @Shadow
    private IActionSource actionSource;

    @Inject(method = "<init>(Lappeng/api/networking/IManagedGridNode;Lappeng/helpers/patternprovider/PatternProviderLogicHost;I)V", at = @At("TAIL"))
    private void appflux$init(IManagedGridNode mainNode, PatternProviderLogicHost host, int patternInventorySize, CallbackInfo ci) {
        af$upgrades = UpgradeInventories.forMachine(host.getTerminalIcon().getItem(), 1, this::af$onUpgradesChanged);
        this.mainNode.addService(IEnergyDistributor.class, this);
    }

    @Unique
    private void af$onUpgradesChanged() {
        host.saveChanges();
        af$updateSleep();
    }

    @Override
    public IUpgradeInventory getUpgrades() {
        return af$upgrades;
    }

    @Inject(method = "writeToNBT", at = @At("TAIL"))
    private void appflux$save(NBTTagCompound tag, CallbackInfo ci) {
        af$upgrades.writeToNBT(tag, "appflux_upgrades");
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"))
    private void appflux$load(NBTTagCompound tag, CallbackInfo ci) {
        af$upgrades.readFromNBT(tag, "appflux_upgrades");
    }

    @Inject(method = "addDrops", at = @At("TAIL"))
    private void appflux$drops(List<ItemStack> drops, CallbackInfo ci) {
        for (ItemStack stack : af$upgrades) {
            if (!stack.isEmpty()) {
                drops.add(stack.copy());
            }
        }
    }

    @Inject(method = "clearContent", at = @At("TAIL"))
    private void appflux$clear(CallbackInfo ci) {
        af$upgrades.clear();
    }

    @Override
    public void distribute() {
        IStorageService storage = af$getStorage();
        TileEntity self = host.getTileEntity();
        if (storage == null || self == null || self.getWorld() == null) {
            return;
        }
        for (EnumFacing side : af$sides) {
            if (af$actions[side.ordinal()] == null) {
                TileEntity te = AFUtil.neighbor(self, side);
                EnumFacing targetSide = side.getOpposite();
                af$actions[side.ordinal()] = te != null
                        && AFUtil.getGrid(te, targetSide) != mainNode.getGrid()
                        && AFUtil.isWhiteListTE(te, targetSide)
                        && AFUtil.shouldTryCast(te, targetSide)
                        ? EnergyHandler.getHandler(te, side.getOpposite())
                        : EnergyHandler.SendAction.NOOP;
            }
            af$actions[side.ordinal()].send(storage, actionSource);
        }
    }

    @Unique
    private IStorageService af$getStorage() {
        return mainNode.getGrid() == null ? null : mainNode.getGrid().getStorageService();
    }

    @Override
    public boolean isActive() {
        return mainNode.isActive();
    }

    @Override
    public void setServiceHost(@Nullable EnergyDistributeService service) {
        af$service = service;
        af$sides = AFUtil.getSides(host);
        af$updateSleep();
    }

    @Override
    public void onChange(EnumFacing side) {
        af$actions[side.ordinal()] = null;
    }

    @Unique
    private void af$updateSleep() {
        if (af$service != null) {
            if (af$upgrades.isInstalled(AFItemAndBlock.INDUCTION_CARD)) {
                af$service.wake(this);
            } else {
                af$service.sleep(this);
            }
        }
    }
}

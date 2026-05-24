package com.glodblock.github.appflux.mixins;

import appeng.block.AEBaseBlock;
import appeng.block.misc.InterfaceBlock;
import appeng.helpers.InterfaceLogicHost;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import com.glodblock.github.appflux.util.AFUtil;
import com.glodblock.github.appflux.util.helpers.INeighborListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InterfaceBlock.class)
public abstract class MixinInterfaceBlock extends AEBaseBlock {

    protected MixinInterfaceBlock(Material material) {
        super(material);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn == null || worldIn.isRemote) {
            return;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof InterfaceLogicHost) {
            AFUtil.notifyNeighbor((INeighborListener) ((InterfaceLogicHost) te).getInterfaceLogic(), pos, fromPos);
        }
    }
}

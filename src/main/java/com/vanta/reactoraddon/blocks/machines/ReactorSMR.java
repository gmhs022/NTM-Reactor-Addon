package com.vanta.reactoraddon.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hbm.blocks.BlockDummyable;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.vanta.reactoraddon.NTMReactorAddon;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

public class ReactorSMR extends BlockDummyable {

    public ReactorSMR(Material mat) {
        super(mat);
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 7, 0, 1, 1, 1, 1 }; // tf?
    }

    @Override
    public int getOffset() {
        return 1;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (meta >= 12) return new TileEntityReactorSMR();
        if (meta >= 6) return new TileEntityProxyCombo(true, false, true);
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else if (!player.isSneaking()) {
            int[] core = this.findCore(world, x, y, z);
            if (core != null) {
                // this better work, I don't want to have to copy over NTM's entire GUI handler just for a damn addon
                FMLNetworkHandler.openGui(player, NTMReactorAddon.instance, 0, world, core[0], core[1], core[2]);
                return true;
            } else return false;
        } else return true;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        this.makeExtra(world, x + 1, y, z + 1);
        this.makeExtra(world, x - 1, y, z + 1);
        this.makeExtra(world, x + 1, y, z - 1);
        this.makeExtra(world, x - 1, y, z - 1);
    }
}

package com.vanta.reactoraddon.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.hbm.blocks.BlockDummyable;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

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
}

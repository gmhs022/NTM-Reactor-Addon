package com.vanta.reactoraddon.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hbm.blocks.BlockDummyable;
import com.hbm.tileentity.TileEntityProxyCombo;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorDMRCore;

public class ReactorDMRCore extends BlockDummyable {

    public ReactorDMRCore(Material mat) {
        super(mat);
    }

    @Override
    public int[] getDimensions() {
        return new int[] { 6, 0, 3, 3, 3, 3 };
    }

    @Override
    public int getOffset() {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        if (meta >= 12) return new TileEntityReactorDMRCore();
        if (meta >= 6) return new TileEntityProxyCombo(true, true, true);
        return null;
    }

    @Override
    public void fillSpace(World world, int x, int y, int z, ForgeDirection dir, int o) {
        super.fillSpace(world, x, y, z, dir, o);

        x += dir.offsetX * o;
        z += dir.offsetZ * o;

        this.makeExtra(world, x + 3, y, z + 2);
        this.makeExtra(world, x + 3, y, z);
        this.makeExtra(world, x + 3, y, z - 2);
        this.makeExtra(world, x - 3, y, z + 2);
        this.makeExtra(world, x - 3, y, z);
        this.makeExtra(world, x - 3, y, z - 2);

        this.makeExtra(world, x + 2, y, z + 3);
        this.makeExtra(world, x, y, z + 3);
        this.makeExtra(world, x - 2, y, z + 3);
        this.makeExtra(world, x + 2, y, z - 3);
        this.makeExtra(world, x, y, z - 3);
        this.makeExtra(world, x - 2, y, z - 3);
    }
}

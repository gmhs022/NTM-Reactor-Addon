package com.vanta.reactoraddon.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorDMRCore;

public class ContainerReactorDMRCore extends Container {

    private TileEntityReactorDMRCore dmr;
    protected int slotCount;

    public ContainerReactorDMRCore(InventoryPlayer invPlayer, TileEntityReactorDMRCore tile) {
        dmr = tile;

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return dmr.isUseableByPlayer(player);
    }
}

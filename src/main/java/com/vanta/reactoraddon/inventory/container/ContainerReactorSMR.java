package com.vanta.reactoraddon.inventory.container;

import com.hbm.inventory.SlotTakeOnly;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerReactorSMR extends Container {
    private TileEntityReactorSMR smr;

    public ContainerReactorSMR(InventoryPlayer invPlayer, TileEntityReactorSMR te) {
        smr = te;
        { // main reactor slots
            int n = 0;
            for (int y = -3; y < 5; y++) {
                int size = 7-Math.max(Math.abs(y)-1,0);
                for (int x = 0; x < size; x++) {
                    int startx = 7+(size-3)*18;
                    this.addSlotToContainer(new Slot(te,n,startx+x*18,7+18*(y+3)));
                    n++;
                }
            }
            // fluid
            this.addSlotToContainer(new Slot(te,n,178,115));
            this.addSlotToContainer(new SlotTakeOnly(te,n+1,178,133));
            // fluid id
            this.addSlotToContainer(new Slot(te,n+2,123,133));

            // player
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 9; j++) {
                    this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 90));
                }
            }

            for(int i = 0; i < 9; i++) {
                this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 232));
            }
        }


    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return smr.isUseableByPlayer(player);
    }
}

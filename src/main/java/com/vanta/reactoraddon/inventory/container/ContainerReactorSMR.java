package com.vanta.reactoraddon.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.InventoryUtil;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

public class ContainerReactorSMR extends Container {

    private TileEntityReactorSMR smr;
    protected int slotCount;

    public ContainerReactorSMR(InventoryPlayer invPlayer, TileEntityReactorSMR te) {
        smr = te;
        { // main reactor slots
            int n = 0;
            for (int y = -3; y < 4; y++) {
                int size = 7 - Math.max(Math.abs(y) * 2 - 2, 0);
                int startx = 44 - (size / 2 - 1) * 18;
                for (int x = 0; x < size; x++) {
                    this.addSlotToContainer(new Slot(te, n, startx + x * 18, 8 + 18 * (y + 3)));
                    n++;
                }
            }

            // fluid
            this.addSlotToContainer(new Slot(te, n, 179, 116));
            this.addSlotToContainer(new SlotTakeOnly(te, n + 1, 179, 134));
            // fluid id
            this.addSlotToContainer(new Slot(te, n + 2, 124, 134));

            slotCount = n + 3;

            // player
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 + 90));
                }
            }

            for (int i = 0; i < 9; i++) {
                this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 232));
            }
        }

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack stackFinal = null;
        Slot slot = (Slot) this.inventorySlots.get(slotId);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            stackFinal = slotStack.copy();
            if (slotId < slotCount) {
                if (!InventoryUtil
                    .mergeItemStack(this.inventorySlots, slotStack, slotCount, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!InventoryUtil.mergeItemStack(this.inventorySlots, slotStack, 0, slotCount, false)) {
                return null;
            }

            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, slotStack);
        }
        return stackFinal;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return smr.isUseableByPlayer(player);
    }
}

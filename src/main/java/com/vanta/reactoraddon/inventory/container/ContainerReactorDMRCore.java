package com.vanta.reactoraddon.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.InventoryUtil;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorDMRCore;

public class ContainerReactorDMRCore extends Container {

    private TileEntityReactorDMRCore dmr;
    protected int slotCount = 7;

    public ContainerReactorDMRCore(InventoryPlayer invPlayer, TileEntityReactorDMRCore tile) {
        dmr = tile;

        // Particle in/out
        this.addSlotToContainer(new Slot(tile, 0, 104, 6));
        this.addSlotToContainer(new SlotTakeOnly(tile, 1, 140, 6));

        // fuel id
        this.addSlotToContainer(new Slot(tile, 2, 55, 104));

        // coolant in/out/id
        this.addSlotToContainer(new Slot(tile, 3, 172, 6));
        this.addSlotToContainer(new SlotTakeOnly(tile, 4, 190, 6));
        this.addSlotToContainer(new Slot(tile, 5, 208, 6));

        // battery
        this.addSlotToContainer(new Slot(tile, 6, 8, 92));

        // player
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18 + 26, 84 + i * 18 + 42));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18 + 26, 201));
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
        return dmr.isUseableByPlayer(player);
    }
}

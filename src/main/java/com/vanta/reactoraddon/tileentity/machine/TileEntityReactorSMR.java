package com.vanta.reactoraddon.tileentity.machine;

import api.hbm.fluid.IFluidStandardTransceiver;
import api.hbm.tile.IInfoProviderEC;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TileEntityReactorSMR extends TileEntityMachineBase implements IControlReceiver, IFluidStandardTransceiver, IGUIProvider, IInfoProviderEC {

    public FluidTank[] tanks;
    public TileEntityReactorSMR() {
        super(28);
        this.tanks = new FluidTank[2];
    }

    @Override
    public String getName() {
        return "container.smr";
    }

    @Override
    public void updateEntity() {

    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return this.isUseableByPlayer(player);
    }

    @Override
    public void receiveControl(NBTTagCompound data) {

    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] {tanks[1]};
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] {tanks[0]};
    }

    @Override
    public FluidTank[] getAllTanks() {
        return tanks;
    }

    @Override
    public void provideExtraInfo(NBTTagCompound data) {

    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}

package com.vanta.reactoraddon.tileentity.machine;

import api.hbm.fluid.IFluidStandardTransceiver;
import api.hbm.tile.IInfoProviderEC;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
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

    public int temp;    // degrees c
    public int pressure;// psi
    public double nFlux;// abstract
    public int control; // %

    public float cRodCoef; // wooo coefficients!
    public float voidCoef; // mostly affects boilables, generally negative but graphite increases
    public float tempCoef;
    public float presCoef;
    public float deplCoef;

    public int crCount;
    public int fuelCount;
    public int totalFuelReactivity;

    public FluidTank[] tanks;
    public TileEntityReactorSMR() {
        super(28);
        this.tanks = new FluidTank[2];
        this.tanks[0] = new FluidTank(Fluids.COOLANT, 32_000);
        this.tanks[1] = new FluidTank(Fluids.COOLANT_HOT, 32_000);

    }

    private double getReactivity() {
        return totalFuelReactivity-cRodCoef*((double) control /100);
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

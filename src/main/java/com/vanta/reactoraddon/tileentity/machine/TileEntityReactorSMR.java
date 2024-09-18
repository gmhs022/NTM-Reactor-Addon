package com.vanta.reactoraddon.tileentity.machine;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.vanta.reactoraddon.inventory.container.ContainerReactorSMR;
import com.vanta.reactoraddon.inventory.gui.GUIReactorSMR;

import api.hbm.fluid.IFluidStandardTransceiver;
import api.hbm.tile.IInfoProviderEC;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityReactorSMR extends TileEntityMachineBase
    implements IControlReceiver, IFluidStandardTransceiver, IGUIProvider, IInfoProviderEC {

    public float temp; // degrees c
    public double pressure;// bar
    public double nFlux;// abstract
    public float control; // %

    public double reactivity; // rho/p
    public int thermalOutput; // TU/s

    public double cRodCoef; // wooo coefficients!
    public double voidCoef; // mostly affects boilables, generally negative but graphite increases
    public double tempCoef;
    public double presCoef;

    public int crCount;
    public int fuelCount;
    public int totalFuelReactivity;

    public static final int meltTemp = 1400;

    public static final int burstPres = 200;

    public FluidTank[] tanks;

    public TileEntityReactorSMR() {
        super(40);
        this.tanks = new FluidTank[2];
        this.tanks[0] = new FluidTank(Fluids.COOLANT, 32_000);
        this.tanks[1] = new FluidTank(Fluids.COOLANT_HOT, 32_000);

    }

    private double getEMF() { // effective multiplication factor, each step n = n * k
        return this.totalFuelReactivity + this.cRodCoef * (this.control / 100) + this.tempCoef * (this.temp);
    }

    private void meltdown() {

    }

    private void rupture() {

    }

    private void checkFail() {
        if (this.pressure > burstPres) {
            meltdown();
        } else if (this.temp > meltTemp) {
            rupture();
        }
    }

    @Override
    public String getName() {
        return "container.smr";
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            double k = getEMF();
            if (k > 0) {
                reactivity = (k - 1) / k;
            }

            checkFail();

            this.markDirty();
            this.networkPackNT(150);
        }
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerReactorSMR(player.inventory, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIReactorSMR(player.inventory, this);
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
        return new FluidTank[] { tanks[1] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { tanks[0] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return tanks;
    }

    @Override
    public void provideExtraInfo(NBTTagCompound data) {

    }
}

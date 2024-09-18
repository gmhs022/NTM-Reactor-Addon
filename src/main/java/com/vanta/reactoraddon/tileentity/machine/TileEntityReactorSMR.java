package com.vanta.reactoraddon.tileentity.machine;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Heatable;
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
    public double totalFuelReactivity;

    public static final int meltTemp = 1400;

    public static final int burstPres = 200;

    public static final int maxThermalPower = 10000; // used for the gui and crap, not an actual limit

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
            this.tanks[0].setType(this.getSizeInventory() - 1, slots);
            handleTanks();

            double k = getEMF();
            if (k > 0) {
                reactivity = (k - 1) / k;
            }

            checkFail();

            this.markDirty();
            this.networkPackNT(150);
        }
    }

    protected void handleTanks() {
        FT_Heatable trait = tanks[0].getTankType()
            .getTrait(FT_Heatable.class);
        if (trait != null) {
            double efficiency = Math.max(
                trait.getEfficiency(FT_Heatable.HeatingType.BOILER),
                trait.getEfficiency(FT_Heatable.HeatingType.HEATEXCHANGER));
            if (efficiency > 0) {
                tanks[1].setTankType(trait.getFirstStep().typeProduced);
                return;
            }
        }
        tanks[0].setTankType(Fluids.NONE);
        tanks[1].setTankType(Fluids.NONE);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        tanks[0].readFromNBT(nbt, "t0");
        tanks[1].readFromNBT(nbt, "t1");

        this.temp = nbt.getFloat("temp");
        this.pressure = nbt.getDouble("pressure");
        this.nFlux = nbt.getDouble("nFlux");
        this.control = nbt.getFloat("control");

        this.thermalOutput = nbt.getInteger("thermalOutput");
        this.cRodCoef = nbt.getDouble("cRodCoef");
        this.voidCoef = nbt.getDouble("voidCoef");
        this.tempCoef = nbt.getDouble("tempCoef");
        this.presCoef = nbt.getDouble("presCoef");
        this.crCount = nbt.getInteger("crCount");
        this.fuelCount = nbt.getInteger("fuelCount");
        this.totalFuelReactivity = nbt.getDouble("totalFuelReactivity");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        tanks[0].writeToNBT(nbt, "t0");
        tanks[1].writeToNBT(nbt, "t1");

        nbt.setFloat("temp", temp); // degrees c
        nbt.setDouble("pressure", pressure);// public double pressure;// bar
        nbt.setDouble("nFlux", nFlux);// public double nFlux;// abstract
        nbt.setFloat("control", control);// public float control; // %

        nbt.setInteger("thermalOutput", thermalOutput);// public int thermalOutput; // TU/s
        nbt.setDouble("cRodCoef", cRodCoef);// public double cRodCoef; // wooo coefficients!
        nbt.setDouble("voidCoef", voidCoef);// public double voidCoef; // mostly affects boilables, generally negative
                                            // but graphite increases
        nbt.setDouble("tempCoef", tempCoef);// public double tempCoef;
        nbt.setDouble("presCoef", presCoef);// public double presCoef;
        nbt.setInteger("crCount", crCount);// public int crCount;
        nbt.setInteger("fuelCount", fuelCount);// public int fuelCount;
        nbt.setDouble("totalFuelReactivity", totalFuelReactivity);// public int totalFuelReactivity;
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
        if (data.hasKey("rods")) {
            this.control = data.getFloat("rods");
            this.markChanged();
        }
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

package com.vanta.reactoraddon.tileentity.machine;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.vanta.reactoraddon.NTMReactorAddon;
import com.vanta.reactoraddon.inventory.container.ContainerReactorSMR;
import com.vanta.reactoraddon.inventory.gui.GUIReactorSMR;

import api.hbm.fluid.IFluidStandardTransceiver;
import api.hbm.tile.IInfoProviderEC;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class TileEntityReactorSMR extends TileEntityMachineBase
    implements IControlReceiver, IFluidStandardTransceiver, IGUIProvider, IInfoProviderEC {

    public float temp; // degrees c
    public double pressure;// bar
    public double nFlux;// abstract
    public float control; // %

    public double reactivity; // rho/p
    public int thermalOutput; // TU/s

    public double cRodCoef; // wooo coefficients! all PCM per unit unless otherwise specified
    public double voidCoef; // mostly affects boilables, generally negative but graphite increases | measured as effect
                            // at 100% voiding
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

    private double getReactivity() { // in pcm
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

            for (DirPos pos : getConPos()) {
                this.trySubscribe(tanks[0].getTankType(), worldObj, pos.getX(), pos.getY(), pos.getZ(), pos.getDir());
            }

            boolean transferred = this.tanks[0].setType(this.getSizeInventory() - 1, slots);
            if (transferred) {
                handleTanks();
                NTMReactorAddon.LOG.info("Transferred PWR fluid type.");
            }
            tanks[0].loadTank(this.getSizeInventory() - 3, this.getSizeInventory() - 2, slots);

            double r = getReactivity();
            double rawK = 1 / (1 - r / 1e5);

            if (rawK > 0) {

            }

            checkFail();

            for (DirPos pos : getConPos()) {
                this.sendFluid(tanks[1], worldObj, pos.getX(), pos.getY(), pos.getZ(), pos.getDir());
            }

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
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeFloat(control);
        buf.writeFloat(temp);
        buf.writeDouble(pressure);
        buf.writeDouble(nFlux);
        buf.writeInt(thermalOutput);
        buf.writeDouble(reactivity);
        for (int i = 0; i < 2; i++) tanks[i].serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        this.control = buf.readFloat();
        this.temp = buf.readFloat();
        this.pressure = buf.readDouble();
        this.nFlux = buf.readDouble();
        this.thermalOutput = buf.readInt();
        this.reactivity = buf.readDouble();
        for (int i = 0; i < 2; i++) tanks[i].deserialize(buf);
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

    AxisAlignedBB bb = null;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (bb == null) {
            bb = AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 1, yCoord + 7, zCoord + 1);
        }
        return bb;
    }

    private DirPos[] getConPos() {
        return new DirPos[] { new DirPos(xCoord - 2, yCoord, zCoord - 1, ForgeDirection.WEST),
            new DirPos(xCoord - 2, yCoord, zCoord + 1, ForgeDirection.WEST),
            new DirPos(xCoord - 1, yCoord, zCoord - 2, ForgeDirection.NORTH),
            new DirPos(xCoord - 1, yCoord, zCoord + 2, ForgeDirection.NORTH),
            new DirPos(xCoord + 2, yCoord, zCoord - 1, ForgeDirection.EAST),
            new DirPos(xCoord + 2, yCoord, zCoord + 1, ForgeDirection.EAST),
            new DirPos(xCoord - 1, yCoord, zCoord + 2, ForgeDirection.SOUTH),
            new DirPos(xCoord - 1, yCoord, zCoord + 2, ForgeDirection.SOUTH), };
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

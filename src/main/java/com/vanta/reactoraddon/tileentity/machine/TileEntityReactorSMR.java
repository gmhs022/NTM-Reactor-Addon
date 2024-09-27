package com.vanta.reactoraddon.tileentity.machine;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.hbm.explosion.ExplosionLarge;
import com.hbm.explosion.ExplosionNukeGeneric;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Heatable;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.vanta.reactoraddon.NTMReactorAddon;
import com.vanta.reactoraddon.inventory.container.ContainerReactorSMR;
import com.vanta.reactoraddon.inventory.fluid.trait.FT_SMRCoolant;
import com.vanta.reactoraddon.inventory.gui.GUIReactorSMR;
import com.vanta.reactoraddon.items.ModItems;
import com.vanta.reactoraddon.items.machine.ItemSMRFuelRod;

import api.hbm.fluid.IFluidStandardTransceiver;
import api.hbm.tile.IInfoProviderEC;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class TileEntityReactorSMR extends TileEntityMachineBase
    implements IControlReceiver, IFluidStandardTransceiver, IGUIProvider, IInfoProviderEC {

    public int heat; // TU
    public double pressure;// bar
    public double nFlux;// abstract
    public float control; // %
    public float controlTgt; // %

    public double reactivity; // pcm
    public int thermalOutput; // TU/s

    public double cRodCoef; // wooo coefficients! all PCM per unit unless otherwise specified
    public double voidCoef; // mostly affects boilables, generally negative but graphite increases | measured as effect
                            // at 100% voiding
    public double tempCoef;
    public double presCoef;

    public int crCount;
    public int graphiteRods;
    public int graphiteCount;
    public int fuelCount;
    public int emptySlots;
    public double totalFuelReactivity;
    public double emission;

    public static final int meltHeat = 10_000_000;

    public static final int burstPres = 200;

    public static final int maxThermalPower = 2_000_000; // used for the gui and crap, not an actual limit

    public FluidTank[] tanks;

    public TileEntityReactorSMR() {
        super(40);
        this.controlTgt = 100f;
        this.control = 100f;
        this.tanks = new FluidTank[2];
        this.tanks[0] = new FluidTank(Fluids.COOLANT, 32_000);
        this.tanks[1] = new FluidTank(Fluids.COOLANT_HOT, 32_000);
    }

    private double getReactivity() { // in pcm
        double mod = 0;
        if (this.graphiteRods > 0) {
            double rodMod = 1 - Math.pow(1.25 * this.control / 100 - 1, 2);
            mod += rodMod * this.graphiteRods;
        }
        mod += this.graphiteCount;
        this.cRodCoef = this.crCount * -250;

        FT_SMRCoolant coolantTrait = this.tanks[0].getTankType()
            .getTrait(FT_SMRCoolant.class);

        double r = -600;
        if (coolantTrait != null) {
            mod += coolantTrait.getModFactor();
            r = coolantTrait.getReactivity();
        }
        double modFactor = Math.max(1.1D - Math.pow(mod / 16 - 1, 2), 0.1);

        return r + this.totalFuelReactivity * modFactor
            + this.cRodCoef * (this.control / 100)
            + this.tempCoef * (this.heat);
    }

    private void meltdown() {
        worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        worldObj.playSoundEffect(xCoord, yCoord + 3, zCoord, "hbm:block.rbmk_explosion", 10.0F, 1.0F);
        worldObj.createExplosion(null, this.xCoord, this.yCoord + 3, this.zCoord, 12F, true);
        ExplosionNukeGeneric.waste(worldObj, this.xCoord, this.yCoord, this.zCoord, 35);
    }

    private void rupture() {
        worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        worldObj.playSoundEffect(xCoord, yCoord + 3, zCoord, "hbm:block.rbmk_explosion", 25.0F, 0.8F);
        ExplosionLarge.explode(worldObj, this.xCoord, this.yCoord, this.zCoord, 50F, true, false, true);
        ExplosionNukeGeneric.waste(worldObj, this.xCoord, this.yCoord, this.zCoord, 50);
    }

    private boolean checkFail() {
        if (this.pressure > burstPres) {
            rupture();
            return true;
        } else if (this.heat > meltHeat) {
            meltdown();
            return true;
        } else return false;
    }

    @Override
    public String getName() {
        return "container.smr";
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {

            if (checkFail()) return;

            for (DirPos pos : getConPos()) {
                this.trySubscribe(tanks[0].getTankType(), worldObj, pos.getX(), pos.getY(), pos.getZ(), pos.getDir());
            }

            boolean transferred = this.tanks[0].setType(this.getSizeInventory() - 1, slots);
            if (transferred) {
                handleTanks();
                NTMReactorAddon.LOG.info("Transferred PWR fluid type.");
            }

            if (this.control != this.controlTgt) {
                if (Math.abs(this.control - this.controlTgt) < 0.25f) {
                    this.control = this.controlTgt;
                } else {
                    this.control += Math.signum(this.controlTgt - this.control) * 0.25f;
                }
            }

            double prevFlux = this.nFlux;

            tanks[0].loadTank(this.getSizeInventory() - 3, this.getSizeInventory() - 2, slots);

            recalcSlots();

            double r = getReactivity();
            this.reactivity = r;
            double rawK = 1 / (1 - r / 1e5);

            if (rawK > 0) {
                this.nFlux = this.nFlux * rawK;
            }
            this.nFlux += emission;

            this.thermalOutput = (int) nFlux / 10;

            if (this.heat > 0) this.heat++;
            this.heat += this.thermalOutput;

            runCoolant();

            double realK = nFlux / prevFlux;

            updateSlots();

            for (DirPos pos : getConPos()) {
                this.sendFluid(tanks[1], worldObj, pos.getX(), pos.getY(), pos.getZ(), pos.getDir());
            }

            this.markDirty();
            this.networkPackNT(150);
        }
    }

    private void runCoolant() {
        FT_Heatable trait = this.tanks[0].getTankType()
            .getTrait(FT_Heatable.class);
        if (trait != null) {
            double efficiency = Math.max(
                trait.getEfficiency(FT_Heatable.HeatingType.BOILER),
                Math.max(
                    trait.getEfficiency(FT_Heatable.HeatingType.HEATEXCHANGER),
                    trait.getEfficiency(FT_Heatable.HeatingType.PWR)));
            if (efficiency > 0) {
                FT_Heatable.HeatingStep step = trait.getFirstStep();
                int usableHeat = (int) (this.heat * (0.15 + (double) this.emptySlots / 400) * efficiency);
                if (usableHeat > step.heatReq && this.tanks[0].getFill() >= step.amountReq) {
                    int ops = Math.min(
                        Math.min((usableHeat / step.heatReq), this.tanks[0].getFill() / step.amountReq),
                        (this.tanks[1].getMaxFill() - this.tanks[1].getFill()) / step.amountProduced);
                    if (ops > 0) {
                        this.heat -= ops * step.heatReq;
                        this.tanks[0].setFill(this.tanks[0].getFill() - step.amountReq * ops);
                        this.tanks[1].setFill(this.tanks[1].getFill() + step.amountProduced * ops);
                    }
                }
            }
        }
    }

    private void recalcSlots() {
        this.tempCoef = 0;
        this.emission = 0;
        this.totalFuelReactivity = 0;
        this.crCount = 0;
        this.graphiteRods = 0;
        this.graphiteCount = 0;
        this.fuelCount = 0;
        this.emptySlots = 0;
        for (int i = 0; i < this.getSizeInventory() - 3; i++) {
            if (slots[i] != null) {
                Item item = slots[i].getItem();
                if (item != null) {
                    if (item instanceof ItemSMRFuelRod) {
                        ItemSMRFuelRod rod = ((ItemSMRFuelRod) item);
                        this.fuelCount++;
                        this.totalFuelReactivity += rod.rodReactivity
                            - rod.rodReactivity * rod.depletionFactor * ItemSMRFuelRod.getDepletion(slots[i])
                            - ItemSMRFuelRod.getXenon(slots[i]);
                        this.tempCoef -= rod.tempCoef;
                        this.emission += rod.emissionRate * (1 - ItemSMRFuelRod.getDepletion(slots[i]));
                    } else if (item == ModItems.smr_control_boron) {
                        this.crCount++;
                    } else if (item == ModItems.smr_control_graphite) {
                        this.crCount++;
                        this.graphiteRods++;
                    } else if (item == ModItems.smr_graphite_insert) {
                        this.graphiteCount++;
                    }
                } else {
                    this.emptySlots++;
                }
            } else {
                this.emptySlots++;
            }
        }
        if (this.fuelCount > 0) this.tempCoef /= this.fuelCount;
    }

    public static double depletionRate = 1e-12;

    private void updateSlots() {
        for (int i = 0; i < this.getSizeInventory() - 3; i++) {
            if (slots[i] != null) {
                Item item = slots[i].getItem();
                if (item != null) {
                    if (item instanceof ItemSMRFuelRod) {
                        ItemSMRFuelRod rod = ((ItemSMRFuelRod) item);
                        double depl = ItemSMRFuelRod.getDepletion(slots[i]);
                        if (depl < 1) {
                            ItemSMRFuelRod.setDepletion(
                                slots[i],
                                Math.min(depl + (this.nFlux * depletionRate) / (this.fuelCount * rod.yield), 1));
                        }
                    }
                }
            }
        }
    }

    protected void handleTanks() {
        FT_Heatable trait = tanks[0].getTankType()
            .getTrait(FT_Heatable.class);
        FT_SMRCoolant valid = tanks[0].getTankType()
            .getTrait(FT_SMRCoolant.class);
        if (trait != null && valid != null) {
            double efficiency = Math.max(
                trait.getEfficiency(FT_Heatable.HeatingType.BOILER),
                Math.max(
                    trait.getEfficiency(FT_Heatable.HeatingType.HEATEXCHANGER),
                    trait.getEfficiency(FT_Heatable.HeatingType.PWR)));
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

        this.heat = nbt.getInteger("heat");
        this.pressure = nbt.getDouble("pressure");
        this.nFlux = nbt.getDouble("nFlux");
        this.control = nbt.getFloat("control");
        this.controlTgt = nbt.getFloat("controlTgt");

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

        nbt.setInteger("heat", heat); // degrees c
        nbt.setDouble("pressure", pressure);// public double pressure;// bar
        nbt.setDouble("nFlux", nFlux);// public double nFlux;// abstract
        nbt.setFloat("control", control);// public float control; // %
        nbt.setFloat("controlTgt", controlTgt);

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
        buf.writeFloat(controlTgt);
        buf.writeInt(heat);
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
        this.controlTgt = buf.readFloat();
        this.heat = buf.readInt();
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
            this.controlTgt = data.getFloat("rods");
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
            new DirPos(xCoord + 1, yCoord, zCoord - 2, ForgeDirection.SOUTH),
            new DirPos(xCoord + 1, yCoord, zCoord + 2, ForgeDirection.SOUTH), };
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

package com.vanta.reactoraddon.tileentity.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.main.MainRegistry;
import com.hbm.sound.AudioWrapper;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;
import com.vanta.reactoraddon.inventory.container.ContainerReactorDMRCore;
import com.vanta.reactoraddon.inventory.gui.GUIReactorDMRCore;

import api.hbm.energymk2.IEnergyReceiverMK2;
import api.hbm.fluidmk2.IFluidStandardTransceiverMK2;
import api.hbm.tile.IInfoProviderEC;
import cpw.mods.fml.common.Optional;
import io.netty.buffer.ByteBuf;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "OpenComputers")
public class TileEntityReactorDMRCore extends TileEntityMachineBase implements IControlReceiver,
    IFluidStandardTransceiverMK2, IGUIProvider, IEnergyReceiverMK2, IInfoProviderEC, SimpleComponent {

    public static int maxParticles = 12;
    public static int maxHeat;

    public float injRate;
    public double particleLevel;
    public int heat; // HU
    public int integrity; // %*1000

    public float reactionRate; // might just be for sound idk
    public int melting;

    public FluidTank[] tanks;
    // 0 = coolant in, 1 = coolant out, 2 = fuel

    private AudioWrapper audio;

    public TileEntityReactorDMRCore() {
        super(7);
        this.injRate = 1.0f;
        this.particleLevel = 0.0d;
        this.reactionRate = 0;
        this.melting = 0;
        this.integrity = 100_000;
        this.tanks = new FluidTank[3];
        this.tanks[0] = new FluidTank(Fluids.COOLANT, 32_000);
        this.tanks[1] = new FluidTank(Fluids.COOLANT_HOT, 32_000);
        this.tanks[2] = new FluidTank(Fluids.DEUTERIUM, 32_000);
    }

    @Override
    public String getName() {
        return "container.dmr";
    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {

        } else {

            if (this.reactionRate > 0 && MainRegistry.proxy.me()
                .getDistanceSq(xCoord + 0.5, yCoord + 2.5, zCoord + 0.5) < (50 ^ 2)) {
                if (audio == null) {
                    audio = MainRegistry.proxy.getLoopedSound(
                        "reactoraddon:block.dmrRun",
                        xCoord + 0.5F,
                        yCoord + 2.5F,
                        zCoord + 0.5F,
                        getVolume(Math.min(this.reactionRate, 1)),
                        50F,
                        this.reactionRate,
                        20);
                    audio.startSound();
                } else {
                    audio.updateVolume(getVolume(Math.min(this.reactionRate, 1)));
                    audio.updatePitch(this.reactionRate);
                    audio.keepAlive();
                }
            } else {
                if (audio != null) {
                    if (audio.isPlaying()) {
                        audio.stopSound();
                    }
                    audio = null;
                }
            }

        }
    }

    @Override
    public void receiveControl(NBTTagCompound data) {
        if (data.hasKey("injRate")) {
            this.injRate = data.getFloat("injRate");
            this.markChanged();
        }
    }

    AxisAlignedBB bb = null;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (bb == null) {
            bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 2, yCoord + 8, zCoord + 2);
        }
        return bb;
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { tanks[1] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { tanks[0], tanks[2] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return tanks;
    }

    @Override
    public boolean hasPermission(EntityPlayer player) {
        return this.isUseableByPlayer(player);
    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        buf.writeFloat(injRate);
        for (int i = 0; i < 3; i++) tanks[i].serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        this.injRate = buf.readFloat();
        for (int i = 0; i < 3; i++) tanks[i].deserialize(buf);
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerReactorDMRCore(player.inventory, this);
    }

    @Override
    public Object provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GUIReactorDMRCore(player.inventory, this);
    }

    @Override
    public long getPower() {
        return 0;
    }

    @Override
    public void setPower(long power) {

    }

    @Override
    public long getMaxPower() {
        return 0;
    }

    @Override
    public void provideExtraInfo(NBTTagCompound data) {

    }

    // OPENCOMPUTERS !!
    @Override
    @Optional.Method(modid = "OpenComputers")
    public String getComponentName() {
        return "ntm_ra_dmr";
    }

    @Callback(direct = true, doc = "function():number; Returns the reactor's current control rod target")
    @Optional.Method(modid = "OpenComputers")
    public Object[] getInjectionRate(Context context, Arguments args) {
        return new Object[] { injRate };
    }

    @Callback(direct = true)
    @Optional.Method(modid = "OpenComputers")
    public Object[] setInjectionRate(Context context, Arguments args) {
        injRate = (float) Math.min(Math.max(args.checkDouble(0), 0F), 100F);
        this.markChanged();
        return new Object[] { true };
    }
}

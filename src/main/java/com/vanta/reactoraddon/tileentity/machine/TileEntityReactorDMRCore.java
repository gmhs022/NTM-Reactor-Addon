package com.vanta.reactoraddon.tileentity.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.tileentity.IGUIProvider;
import com.hbm.tileentity.TileEntityMachineBase;

import api.hbm.fluid.IFluidStandardTransceiver;
import io.netty.buffer.ByteBuf;

public class TileEntityReactorDMRCore extends TileEntityMachineBase
    implements IControlReceiver, IFluidStandardTransceiver, IGUIProvider {

    public FluidTank[] tanks;
    // 0 = fuel, 1 = spent, 2 = coolant in, 3 = coolant out

    public TileEntityReactorDMRCore() {
        super(10); // TODO: Actually make the GUI and figure this value out!
        this.tanks = new FluidTank[4];
        this.tanks[0] = new FluidTank(Fluids.DEUTERIUM, 32_000);
        this.tanks[1] = new FluidTank(Fluids.HELIUM4, 32_000);
        this.tanks[2] = new FluidTank(Fluids.COOLANT, 32_000);
        this.tanks[3] = new FluidTank(Fluids.COOLANT_HOT, 32_000);
    }

    @Override
    public String getName() {
        return "container.dmr";
    }

    @Override
    public void updateEntity() {

    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { tanks[1], tanks[3] };
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
    public void receiveControl(NBTTagCompound data) {

    }

    @Override
    public void serialize(ByteBuf buf) {
        super.serialize(buf);
        for (int i = 0; i < 4; i++) tanks[i].serialize(buf);
    }

    @Override
    public void deserialize(ByteBuf buf) {
        super.deserialize(buf);
        for (int i = 0; i < 4; i++) tanks[i].deserialize(buf);
    }

    @Override
    public Container provideContainer(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object provideGUI(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}

package com.vanta.reactoraddon.tileentity;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;

import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

public class TileMappings {

    public static HashMap<Class<? extends TileEntity>, String[]> map = new HashMap<Class<? extends TileEntity>, String[]>();

    public static void writeMappings() {
        map.put(TileEntityReactorSMR.class, new String[] { "tileentity_smr" });
    }
}

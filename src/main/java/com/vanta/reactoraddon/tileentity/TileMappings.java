package com.vanta.reactoraddon.tileentity;

import java.util.HashMap;

import net.minecraft.tileentity.TileEntity;

import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorDMRCore;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

public class TileMappings {

    public static HashMap<Class<? extends TileEntity>, String[]> map = new HashMap<>();

    public static void writeMappings() {
        map.put(TileEntityReactorSMR.class, new String[] { "tileentity_smr" });
        map.put(TileEntityReactorDMRCore.class, new String[] { "tileentity_dmr_core" });
    }
}

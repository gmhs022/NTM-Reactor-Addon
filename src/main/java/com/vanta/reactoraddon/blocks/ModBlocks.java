package com.vanta.reactoraddon.blocks;

import com.hbm.main.MainRegistry;
import com.vanta.reactoraddon.blocks.machines.ReactorSMR;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {

    public static void mainRegistry() {
        init();
        register();
    }

    public static Block reactor_smr;

    private static void init() {
        reactor_smr = new ReactorSMR(Material.iron).setBlockName("machine_smr").setHardness(5.0F).setResistance(100.0F).setCreativeTab(MainRegistry.machineTab).setBlockTextureName("hbm:block_steel");
    }
    private static void register() {
        GameRegistry.registerBlock(reactor_smr,reactor_smr.getUnlocalizedName());
    }

}

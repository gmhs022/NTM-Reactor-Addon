package com.vanta.reactoraddon.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.hbm.main.MainRegistry;
import com.vanta.reactoraddon.blocks.machines.ReactorSMR;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static Block reactor_smr;

    public static void init() {
        reactor_smr = new ReactorSMR(Material.iron).setBlockName("machine_smr")
            .setHardness(5.0F)
            .setResistance(100.0F)
            .setCreativeTab(MainRegistry.machineTab)
            .setBlockTextureName("hbm:block_steel");
    }

    public static void register() {
        GameRegistry.registerBlock(reactor_smr, reactor_smr.getUnlocalizedName());
    }

}

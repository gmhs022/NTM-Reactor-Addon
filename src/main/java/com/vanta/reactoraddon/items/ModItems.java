package com.vanta.reactoraddon.items;

import net.minecraft.item.Item;

import com.hbm.items.ItemCustomLore;
import com.hbm.main.MainRegistry;
import com.vanta.reactoraddon.NTMReactorAddon;
import com.vanta.reactoraddon.items.machine.ItemSMRFuelRod;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

    public static Item smr_fuel_empty;
    public static Item smr_control_boron;
    public static Item smr_control_graphite;
    public static Item smr_graphite_insert;
    public static Item smr_chicken_soup;

    public static void init() {
        smr_fuel_empty = new Item().setUnlocalizedName("smr_fuel_empty")
            .setCreativeTab(MainRegistry.controlTab)
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_empty");
        smr_control_boron = new ItemCustomLore().setUnlocalizedName("smr_control_boron")
            .setCreativeTab(MainRegistry.controlTab)
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_control_boron");
        smr_control_graphite = new ItemCustomLore().setUnlocalizedName("smr_control_graphite")
            .setCreativeTab(MainRegistry.controlTab)
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_control_graphite");
        smr_graphite_insert = new ItemCustomLore().setUnlocalizedName("smr_graphite_insert")
            .setCreativeTab(MainRegistry.controlTab)
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_graphite_insert");
        smr_chicken_soup = new ItemLoreFood(6, 0.6f, false).setUnlocalizedName("smr_chicken_soup")
            .setCreativeTab(MainRegistry.controlTab)
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_chicken_soup_done");
        ItemSMRFuelRod.init();
    }

    public static void register() {
        GameRegistry.registerItem(smr_fuel_empty, smr_fuel_empty.getUnlocalizedName());
        GameRegistry.registerItem(smr_control_boron, smr_control_boron.getUnlocalizedName());
        GameRegistry.registerItem(smr_control_graphite, smr_control_graphite.getUnlocalizedName());
        GameRegistry.registerItem(smr_graphite_insert, smr_graphite_insert.getUnlocalizedName());
        GameRegistry.registerItem(smr_chicken_soup, smr_chicken_soup.getUnlocalizedName());
        ItemSMRFuelRod.register();
    }

}

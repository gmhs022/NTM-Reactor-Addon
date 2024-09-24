package com.vanta.reactoraddon.items.machine;

import java.util.List;
import java.util.Locale;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.hbm.main.MainRegistry;
import com.vanta.reactoraddon.NTMReactorAddon;
import com.vanta.reactoraddon.items.ModItems;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemSMRFuelRod extends Item {

    public String fullName;
    public double rodReactivity; // pcm
    public double depletionFactor = 2.0D; // *rodReactivity
    public double iodineRate = 1e-5; // per neutron flux
    public double emissionRate = 0.0D;
    public double tempCoef = 0.75D; // pcm per *C
    public double yield = 1.0D;
    public double depletion = 0.0D;
    public double iodine = 0.0D;
    public double xenon = 0.0D;
    public double trueReactivity;

    public ItemSMRFuelRod(String fullName, double rodReactivity) {
        this.fullName = fullName;
        this.rodReactivity = rodReactivity;
        this.trueReactivity = rodReactivity;
        this.setContainerItem(ModItems.smr_fuel_empty);
        this.setMaxStackSize(1);
        this.setCreativeTab(MainRegistry.controlTab);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {
        list.add(EnumChatFormatting.ITALIC + this.fullName);
        list.add(EnumChatFormatting.GREEN + "Depletion: " + Math.floor(depletion * 100) / 100 + "%");
        list.add(EnumChatFormatting.YELLOW + "Reactivity: " + Math.floor(this.trueReactivity * 100) / 100 + " PCM");
        list.add(EnumChatFormatting.YELLOW + "Temp Coefficient: " + Math.floor(this.tempCoef * -100) / 100 + " PCM/°C");
        list.add(
            EnumChatFormatting.LIGHT_PURPLE
                + String.format(Locale.US, "Iodine Rate: %.2e/s/Flux", this.iodineRate * 20));
        if (this.emissionRate > 0) {
            list.add(EnumChatFormatting.GOLD + String.format(Locale.US, "Source Flux: %.2e", this.emissionRate));
        }
    }

    public ItemSMRFuelRod setDepletionFactor(double depletion) {
        this.depletionFactor = depletion;
        return this;
    }

    public ItemSMRFuelRod setIodineRate(double iodine) {
        this.iodineRate = iodine;
        return this;
    }

    public ItemSMRFuelRod setEmissionRate(double emission) {
        this.emissionRate = emission;
        return this;
    }

    public ItemSMRFuelRod setTempCoef(double tempCoef) {
        this.tempCoef = tempCoef;
        return this;
    }

    public ItemSMRFuelRod setYield(double yield) {
        this.yield = yield;
        return this;
    }

    public ItemSMRFuelRod setDepletion(double depletion) {
        this.depletion = depletion;
        return this;
    }

    public static ItemSMRFuelRod ueu235;
    public static ItemSMRFuelRod meu235;
    public static ItemSMRFuelRod heu235;

    public static void init() {
        ueu235 = (ItemSMRFuelRod) new ItemSMRFuelRod("Unenriched Uranium", 50).setDepletionFactor(1.0)
            .setEmissionRate(1e-5)
            .setYield(2.0D)
            .setUnlocalizedName("smr_fuel_ueu235")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_ueu235");
        meu235 = (ItemSMRFuelRod) new ItemSMRFuelRod("Reactor-Grade Uranium", 100).setDepletionFactor(2.0)
            .setEmissionRate(5e-5)
            .setYield(1.0D)
            .setUnlocalizedName("smr_fuel_meu235")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_ueu235");
        heu235 = (ItemSMRFuelRod) new ItemSMRFuelRod("Weapons-Grade Uranium", 250).setDepletionFactor(4.0)
            .setEmissionRate(5e-4)
            .setYield(0.5D)
            .setUnlocalizedName("smr_fuel_heu235")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_ueu235");
    }

    public static void register() {
        GameRegistry.registerItem(ueu235, ueu235.getUnlocalizedName());
        GameRegistry.registerItem(meu235, meu235.getUnlocalizedName());
        GameRegistry.registerItem(heu235, heu235.getUnlocalizedName());
    }
}

package com.vanta.reactoraddon.items.machine;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.hbm.main.MainRegistry;
import com.vanta.reactoraddon.NTMReactorAddon;
import com.vanta.reactoraddon.items.ModItems;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemSMRFuelRod extends Item {

    public String fullName;
    public double rodReactivity; // pcm
    public double depletionFactor = 1.5D; // *rodReactivity
    public double iodineRate = 1e-5; // per neutron flux
    public double emissionRate = 0.0D;
    public double tempCoef = 0.00075; // pcm per HU
    public double yield = 1.0D;

    public ItemSMRFuelRod(String fullName, double rodReactivity) {
        this.fullName = fullName;
        this.rodReactivity = rodReactivity;
        this.setContainerItem(ModItems.smr_fuel_empty);
        this.setMaxStackSize(1);
        this.setCreativeTab(MainRegistry.controlTab);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean bool) {
        if (stack.getItem() == undefined) { // COVER YOURSELF IN OIL
            list.add(
                EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.RED
                    + (player.worldObj.rand.nextInt(5) < 2 ? "java.lang.ArrayIndexOutOfBoundsException"
                        : EnumChatFormatting.OBFUSCATED + "java.lang.ArrayIndexOutOfBoundsException"));
        } else {
            list.add(EnumChatFormatting.ITALIC + this.fullName);
        }
        list.add(
            EnumChatFormatting.GREEN + String.format(Locale.US, "Depletion: %.04f", getDepletion(stack) * 100) + "%");
        list.add(
            EnumChatFormatting.GREEN + String.format(Locale.US, "Yield: %d", (int) Math.floor(this.yield * 100)) + "%");
        list.add(EnumChatFormatting.YELLOW + "Reactivity: " + Math.floor(getReactivity(stack) * 100) / 100 + " PCM");
        list.add(
            EnumChatFormatting.YELLOW + "Temp Coefficient: " + Math.floor(this.tempCoef * -1e5) / 100 + " PCM/kHU");
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

    public static void setReactivity(ItemStack stack, double depletion) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);
        stack.stackTagCompound.setDouble("reactivity", depletion);
    }

    public static double getReactivity(ItemStack stack) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);

        return stack.stackTagCompound.getDouble("reactivity");
    }

    public static void setDepletion(ItemStack stack, double depletion) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);
        stack.stackTagCompound.setDouble("depletion", depletion);
    }

    public static double getDepletion(ItemStack stack) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);

        return stack.stackTagCompound.getDouble("depletion");
    }

    public static void setIodine(ItemStack stack, double depletion) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);
        stack.stackTagCompound.setDouble("iodine", depletion);
    }

    public static double getIodine(ItemStack stack) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);

        return stack.stackTagCompound.getDouble("iodine");
    }

    public static void setXenon(ItemStack stack, double depletion) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);
        stack.stackTagCompound.setDouble("xenon", depletion);
    }

    public static double getXenon(ItemStack stack) {
        if (!stack.hasTagCompound()) setNBTDefaults(stack);

        return stack.stackTagCompound.getDouble("xenon");
    }

    private static void setNBTDefaults(ItemStack stack) {
        stack.stackTagCompound = new NBTTagCompound();
        setDepletion(stack, 0.0D);
        setReactivity(stack, ((ItemSMRFuelRod) Objects.requireNonNull(stack.getItem())).rodReactivity);
        setIodine(stack, 0.0D);
        setXenon(stack, 0.0D);
    }

    public static ItemSMRFuelRod ueu235;
    public static ItemSMRFuelRod meu235;
    public static ItemSMRFuelRod heu235;

    public static ItemSMRFuelRod undefined;

    public static ItemSMRFuelRod soup;

    public static void init() {
        ueu235 = (ItemSMRFuelRod) new ItemSMRFuelRod("Unenriched Uranium", 25).setDepletionFactor(1.0)
            .setEmissionRate(1e-5)
            .setYield(2.0D)
            .setUnlocalizedName("smr_fuel_ueu235")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_ueu235");
        meu235 = (ItemSMRFuelRod) new ItemSMRFuelRod("Reactor-Grade Uranium", 50).setDepletionFactor(2.0)
            .setEmissionRate(5e-5)
            .setYield(1.0D)
            .setUnlocalizedName("smr_fuel_meu235")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_ueu235");
        heu235 = (ItemSMRFuelRod) new ItemSMRFuelRod("Weapons-Grade Uranium", 125).setDepletionFactor(4.0)
            .setEmissionRate(5e-4)
            .setYield(0.5D)
            .setUnlocalizedName("smr_fuel_heu235")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_ueu235");
        undefined = (ItemSMRFuelRod) new ItemSMRFuelRod("Undefined", 500).setDepletionFactor(-3.0)
            .setTempCoef(-0.02)
            .setEmissionRate(1.337D)
            .setYield(400.0D)
            .setIodineRate(1e-2)
            .setUnlocalizedName("smr_fuel_undefined")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_fuel_error");
        soup = (ItemSMRFuelRod) new ItemSMRFuelRod("Now with extra zirconium cladding!", -125).setDepletionFactor(0)
            .setTempCoef(0)
            .setYield(0.25D)
            .setIodineRate(0)
            .setUnlocalizedName("smr_fuel_soup")
            .setTextureName(NTMReactorAddon.MODID + ":machine/smr_chicken_soup");
    }

    public static void register() {
        GameRegistry.registerItem(ueu235, ueu235.getUnlocalizedName());
        GameRegistry.registerItem(meu235, meu235.getUnlocalizedName());
        GameRegistry.registerItem(heu235, heu235.getUnlocalizedName());
        GameRegistry.registerItem(undefined, undefined.getUnlocalizedName());
        GameRegistry.registerItem(soup, soup.getUnlocalizedName());
    }
}

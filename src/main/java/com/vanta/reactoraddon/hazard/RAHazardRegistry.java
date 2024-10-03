package com.vanta.reactoraddon.hazard;

import net.minecraft.item.Item;

import com.hbm.hazard.HazardData;
import com.hbm.hazard.HazardEntry;
import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.HazardSystem;
import com.hbm.hazard.type.HazardTypeBase;
import com.vanta.reactoraddon.hazard.modifier.HazardModifierSMRFuel;
import com.vanta.reactoraddon.items.ModItems;
import com.vanta.reactoraddon.items.machine.ItemSMRFuelRod;

public class RAHazardRegistry {

    public static void register() {
        HazardSystem.register(ModItems.smr_chicken_soup, newData(HazardRegistry.RADIATION, 1F));
        registerSMRRod(ItemSMRFuelRod.ueu235, HazardRegistry.u * 4, HazardRegistry.wst * 4 * 20F);
        registerSMRRod(ItemSMRFuelRod.meu235, HazardRegistry.uf * 4, HazardRegistry.wst * 4 * 21.5F);
        registerSMRRod(ItemSMRFuelRod.heu235, HazardRegistry.u235 * 4, HazardRegistry.wst * 4 * 30F);

        registerSMRRod(ItemSMRFuelRod.undefined, 1000F, 10000F, 0.5F, 5F);
        registerSMRRod(ItemSMRFuelRod.soup, 0F, 1F);
    }

    private static void registerSMRRod(Item rod, float base, float target) {
        HazardData data = new HazardData();
        data.addEntry(new HazardEntry(HazardRegistry.RADIATION, base).addMod(new HazardModifierSMRFuel(target)));
        HazardSystem.register(rod, data);
    }

    private static void registerSMRRod(Item rod, float base, float target, float digamma, float digammaTgt) {
        HazardData data = new HazardData();
        data.addEntry(new HazardEntry(HazardRegistry.RADIATION, base).addMod(new HazardModifierSMRFuel(target)));
        data.addEntry(new HazardEntry(HazardRegistry.DIGAMMA, digamma).addMod(new HazardModifierSMRFuel(digammaTgt)));
        HazardSystem.register(rod, data);
    }

    private static HazardData newData(HazardTypeBase hazard) {
        return new HazardData().addEntry(hazard);
    }

    private static HazardData newData(HazardTypeBase hazard, float level) {
        return new HazardData().addEntry(hazard, level);
    }
}

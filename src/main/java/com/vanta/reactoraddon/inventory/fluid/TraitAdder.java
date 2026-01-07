package com.vanta.reactoraddon.inventory.fluid;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FluidTrait;
import com.vanta.reactoraddon.inventory.fluid.trait.FT_DMRFuel;
import com.vanta.reactoraddon.inventory.fluid.trait.FT_SMRCoolant;

public class TraitAdder {

    static {
        registerTrait("smrcoolant", FT_SMRCoolant.class);
        registerTrait("dmrfuel", FT_DMRFuel.class);
    }

    public static void init() {
        // SMR Coolant
        SMRCoolant(Fluids.WATER, -300, 1);
        SMRCoolant(Fluids.COOLANT, -100, 2);
        SMRCoolant(Fluids.MUG, -200, 6);
        SMRCoolant(Fluids.HEAVYWATER, -300, 14);
        SMRCoolant(Fluids.BLOOD, -300, 8);
        SMRCoolant(Fluids.SODIUM, 0, 0);
        SMRCoolant(Fluids.PERFLUOROMETHYL, -100, 2);

        // DMR Fuel
        DMRFuel(Fluids.HYDROGEN, 1_000_000, 150, 0.2);
        DMRFuel(Fluids.DEUTERIUM, 500_000, 200, 0.5);
        DMRFuel(Fluids.TRITIUM, 300_000, 300, 2);
        DMRFuel(Fluids.HELIUM3, 400_000, 500, 0);

        DMRFuel(Fluids.BALEFIRE, 5_000_000, 800, 5, 0.5f);
        DMRFuel(Fluids.ASCHRAB, 1_000_000, 100, 0, 3);
        DMRFuel(Fluids.STELLAR_FLUX, 10_000_000, 2_000, 10, 2);

    }

    private static void SMRCoolant(FluidType fluid, double reactivity, double ModerationFactor) {
        fluid.addTraits(new FT_SMRCoolant(reactivity, ModerationFactor));
    }

    private static void DMRFuel(FluidType fluid, float fusionHeat, double fusionEnergy, double neutronGeneration,
        float exoticFactor) {
        fluid.addTraits(new FT_DMRFuel(fusionHeat, fusionEnergy, neutronGeneration, exoticFactor));
    }

    private static void DMRFuel(FluidType fluid, float fusionHeat, double fusionEnergy, double neutronGeneration) {
        fluid.addTraits(new FT_DMRFuel(fusionHeat, fusionEnergy, neutronGeneration));
    }

    private static void registerTrait(String name, Class<? extends FluidTrait> clazz) {
        FluidTrait.traitNameMap.put(name, clazz);
        FluidTrait.traitList.add(clazz);
    }

}

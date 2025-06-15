package com.vanta.reactoraddon.inventory.fluid;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.trait.FluidTrait;
import com.vanta.reactoraddon.inventory.fluid.trait.FT_SMRCoolant;

public class TraitAdder {

    static {
        registerTrait("smrcoolant", FT_SMRCoolant.class);
    }

    public static void init() {
        SMRCoolant(Fluids.WATER, -300, 1);
        SMRCoolant(Fluids.COOLANT, -100, 2);
        SMRCoolant(Fluids.MUG, -200, 6);
        SMRCoolant(Fluids.HEAVYWATER, -300, 14);
        SMRCoolant(Fluids.BLOOD, -300, 8);
        SMRCoolant(Fluids.SODIUM, 0, 0);
        SMRCoolant(Fluids.PERFLUOROMETHYL, -100, 2);
    }

    private static void SMRCoolant(FluidType fluid, double reactivity, double ModerationFactor) {
        fluid.addTraits(new FT_SMRCoolant(reactivity, ModerationFactor));
    }

    private static void registerTrait(String name, Class<? extends FluidTrait> clazz) {
        FluidTrait.traitNameMap.put(name, clazz);
        FluidTrait.traitList.add(clazz);
    }

}

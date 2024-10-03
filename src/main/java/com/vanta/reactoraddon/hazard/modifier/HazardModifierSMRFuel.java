package com.vanta.reactoraddon.hazard.modifier;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import com.hbm.hazard.HazardRegistry;
import com.hbm.hazard.modifier.HazardModifier;
import com.vanta.reactoraddon.items.machine.ItemSMRFuelRod;

public class HazardModifierSMRFuel extends HazardModifier {

    float target;

    public HazardModifierSMRFuel(float target) {
        this.target = target;
    }

    @Override
    public float modify(ItemStack stack, EntityLivingBase holder, float level) {
        if (stack.getItem() instanceof ItemSMRFuelRod) {
            double depl = ItemSMRFuelRod.getDepletion(stack);
            double xe = ItemSMRFuelRod.getXenon(stack);

            level += (float) (depl * (this.target - level)) + (float) (HazardRegistry.xe135 * xe);
        }
        return level;
    }
}

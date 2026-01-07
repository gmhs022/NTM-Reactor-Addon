package com.vanta.reactoraddon.inventory.fluid.trait;

import java.io.IOException;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.fluid.trait.FluidTrait;

public class FT_DMRFuel extends FluidTrait {

    private float fusionHeat;
    private double fusionEnergy;
    private double neutronGeneration;
    private float exoticFactor;

    public FT_DMRFuel(float fusionHeat, double fusionEnergy, double neutronGeneration, float exoticFactor) {
        this.fusionHeat = fusionHeat;
        this.fusionEnergy = fusionEnergy;
        this.neutronGeneration = neutronGeneration;
        this.exoticFactor = exoticFactor;
    }

    public FT_DMRFuel(float fusionHeat, double fusionEnergy, double neutronGeneration) {
        this(fusionHeat, fusionEnergy, neutronGeneration, 0);
    }

    public FT_DMRFuel(float fusionHeat, double fusionEnergy) {
        this(fusionHeat, fusionEnergy, 0);
    }

    public float getFusionHeat() {
        return this.fusionHeat;
    }

    public double getFusionEnergy() {
        return this.fusionEnergy;
    }

    public double getNeutronGeneration() {
        return this.neutronGeneration;
    }

    public float getExoticFactor() {
        return this.exoticFactor;
    }

    @Override
    public void addInfo(List<String> info) {
        info.add(EnumChatFormatting.LIGHT_PURPLE + "[DMR Fuel]");
    }

    @Override
    public void addInfoHidden(List<String> info) {
        info.add(EnumChatFormatting.LIGHT_PURPLE.toString() + Math.floor(this.fusionHeat) / 1000 + "kTU required");
        info.add(EnumChatFormatting.LIGHT_PURPLE.toString() + Math.floor(this.fusionEnergy * 100) / 100 + "TU/mb");
        if (this.neutronGeneration > 0) {
            info.add(
                EnumChatFormatting.LIGHT_PURPLE.toString() + Math.floor(this.neutronGeneration * 100) / 100 + "NF/mb");
        }
        if (this.exoticFactor > 0) {
            info.add(EnumChatFormatting.LIGHT_PURPLE + "Potentially self-sustaining!");
        }
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("fusionHeat")
            .value(this.fusionHeat);
        writer.name("fusionEnergy")
            .value(this.fusionEnergy);
        writer.name("neutronGeneration")
            .value(this.neutronGeneration);
        writer.name("exoticFactor")
            .value(this.exoticFactor);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.fusionHeat = obj.get("fusionHeat")
            .getAsFloat();
        this.fusionEnergy = obj.get("fusionEnergy")
            .getAsDouble();
        this.neutronGeneration = obj.get("neutronGeneration")
            .getAsDouble();
        this.exoticFactor = obj.get("exoticFactor")
            .getAsFloat();
    }
}

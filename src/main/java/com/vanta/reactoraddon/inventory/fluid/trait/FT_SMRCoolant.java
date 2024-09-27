package com.vanta.reactoraddon.inventory.fluid.trait;

import java.io.IOException;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.inventory.fluid.trait.FluidTrait;

public class FT_SMRCoolant extends FluidTrait {

    private double reactivity;
    private double modFactor;
    private double voidCoefficient;

    public FT_SMRCoolant(double reactivity, double moderationFactor, double voidCoefficient) {
        this.reactivity = reactivity;
        this.modFactor = moderationFactor;
        this.voidCoefficient = voidCoefficient;
    }

    public FT_SMRCoolant(double reactivity, double moderationFactor) {
        this(reactivity, moderationFactor, 0);
    }

    public double getReactivity() {
        return this.reactivity;
    }

    public double getModFactor() {
        return this.modFactor;
    }

    public double getVoidCoefficient() {
        return voidCoefficient;
    }

    @Override
    public void addInfo(List<String> info) {
        info.add(EnumChatFormatting.YELLOW + "[SMR Coolant]");
    }

    @Override
    public void addInfoHidden(List<String> info) {
        info.add(EnumChatFormatting.YELLOW.toString() + Math.floor(this.reactivity * 100) / 100 + " PCM");
        info.add(EnumChatFormatting.YELLOW.toString() + Math.floor(this.modFactor * 100) / 100 + " Insert Equivalent");
    }

    @Override
    public void serializeJSON(JsonWriter writer) throws IOException {
        writer.name("reactivity")
            .value(this.reactivity);
        writer.name("modFactor")
            .value(this.modFactor);
        writer.name("voidCoefficient")
            .value(this.voidCoefficient);
    }

    @Override
    public void deserializeJSON(JsonObject obj) {
        this.reactivity = obj.get("reactivity")
            .getAsDouble();
        this.modFactor = obj.get("modFactor")
            .getAsDouble();
        this.voidCoefficient = obj.get("voidCoefficient")
            .getAsDouble();
    }
}

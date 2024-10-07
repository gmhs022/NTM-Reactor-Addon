package com.vanta.reactoraddon.main;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import com.hbm.render.loader.HFRWavefrontObject;
import com.vanta.reactoraddon.NTMReactorAddon;

public class ResourceManager {

    // models
    public static final IModelCustom smr = new HFRWavefrontObject(
        new ResourceLocation(NTMReactorAddon.MODID, "models/SMR.obj"));
    public static final IModelCustom dmr = new HFRWavefrontObject(
        new ResourceLocation(NTMReactorAddon.MODID, "models/DMRCore.obj"));

    // textures
    public static final ResourceLocation smr_tex = new ResourceLocation(
        NTMReactorAddon.MODID,
        "textures/models/ModelSMR.png");
    public static final ResourceLocation dmr_tex = new ResourceLocation(
        NTMReactorAddon.MODID,
        "textures/models/ModelDMRCore.png");

}

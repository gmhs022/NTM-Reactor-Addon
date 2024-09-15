package com.vanta.reactoraddon.main;

import com.hbm.render.loader.HFRWavefrontObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

public class ResourceManager {

    // models
    public static final IModelCustom smr = new HFRWavefrontObject(new ResourceLocation("reactoraddon","models/SMR.obj"));


    //textures
    public static final ResourceLocation smr_tex = new ResourceLocation("reactoraddon","textures/models/ModelSMR.png");

}

package com.vanta.reactoraddon.main;

import com.vanta.reactoraddon.render.tileentity.RenderSMR;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReactorSMR.class, new RenderSMR());
    }
}

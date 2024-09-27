package com.vanta.reactoraddon.main;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.hbm.render.tileentity.IItemRendererProvider;
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

        registerItemRenderer();
    }

    public void registerItemRenderer() {
        for (Object renderer : TileEntityRendererDispatcher.instance.mapSpecialRenderers.values()) {
            if (renderer instanceof IItemRendererProvider) {
                IItemRendererProvider prov = (IItemRendererProvider) renderer;
                for (Item item : prov.getItemsForRenderer()) {
                    MinecraftForgeClient.registerItemRenderer(item, prov.getRenderer());
                }
            }
        }
    }
}

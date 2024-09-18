package com.vanta.reactoraddon.main;

import com.hbm.handler.GUIHandler;
import com.vanta.reactoraddon.NTMReactorAddon;
import com.vanta.reactoraddon.Tags;

import com.vanta.reactoraddon.blocks.ModBlocks;
import com.vanta.reactoraddon.tileentity.TileMappings;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.tileentity.TileEntity;

import java.util.Map;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        ModBlocks.preLoad();

        // will this work?
        NetworkRegistry.INSTANCE.registerGuiHandler(NTMReactorAddon.instance, new GUIHandler());

        TileMappings.writeMappings();

        for(Map.Entry<Class<? extends TileEntity>, String[]> te : TileMappings.map.entrySet() ) {
            if (te.getValue().length == 1)
                GameRegistry.registerTileEntity(te.getKey(),te.getValue()[0]);
            else
                GameRegistry.registerTileEntityWithAlternatives(te.getKey(),te.getValue()[0],te.getValue());
        }

        NTMReactorAddon.LOG.info(Config.greeting);
        NTMReactorAddon.LOG.info("NTM Reactor Addon loaded. Version " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}

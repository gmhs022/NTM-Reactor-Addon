package com.vanta.reactoraddon.inventory.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.vanta.reactoraddon.inventory.container.ContainerReactorSMR;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

public class GUIReactorSMR extends GuiInfoContainer {

    private static final ResourceLocation tex = new ResourceLocation(
        "reactoraddon",
        "textures/gui/reactors/gui_smr.png");
    private TileEntityReactorSMR smr;

    public GUIReactorSMR(InventoryPlayer invPlayer, TileEntityReactorSMR tile) {
        super(new ContainerReactorSMR(invPlayer, tile));
        smr = tile;
        this.xSize = 203;
        this.ySize = 256;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(tex);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}

package com.vanta.reactoraddon.inventory.gui;

import java.util.Objects;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.vanta.reactoraddon.inventory.container.ContainerReactorDMRCore;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorDMRCore;

public class GUIReactorDMRCore extends GuiInfoContainer {

    private TileEntityReactorDMRCore dmr;

    public GUIReactorDMRCore(InventoryPlayer invPlayer, TileEntityReactorDMRCore tile) {
        super(new ContainerReactorDMRCore(invPlayer, tile));
        dmr = tile;
        this.xSize = 256;
        this.ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void drawItemStack(ItemStack stack, int x, int y, String label) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (stack != null) font = Objects.requireNonNull(stack.getItem())
            .getFontRenderer(stack);
        if (font == null) font = fontRendererObj;
        itemRender.renderItemAndEffectIntoGUI(font, this.mc.getTextureManager(), stack, x, y);
        GL11.glScaled(0.5, 0.5, 0.5);
        itemRender.renderItemOverlayIntoGUI(
            font,
            this.mc.getTextureManager(),
            stack,
            (x + font.getStringWidth(label) / 4) * 2,
            (y + 15) * 2,
            label);
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glPopMatrix();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}

package com.vanta.reactoraddon.inventory.gui;

import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.render.util.GaugeUtil;
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
    protected void drawItemStack(ItemStack stack, int x, int y, String label) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        itemRender.zLevel = 200.0F;
        FontRenderer font = null;
        if (stack != null) font = stack.getItem()
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
    public void drawScreen(int x, int y, float interp) {
        super.drawScreen(x, y, interp);

        this.drawCustomInfoStat(
            x,
            y,
            guiLeft + 160,
            guiTop + 25,
            18,
            17,
            x,
            y,
            "Temperature:",
            String.format(Locale.US, "%.2f °C", smr.temp));
        this.drawCustomInfoStat(
            x,
            y,
            guiLeft + 178,
            guiTop + 25,
            18,
            17,
            x,
            y,
            "Pressure:",
            String.format(Locale.US, "%.2f Bar", smr.pressure));
        this.drawCustomInfoStat(
            x,
            y,
            guiLeft + 160,
            guiTop + 58,
            18,
            17,
            x,
            y,
            "Thermal power:",
            String.format(Locale.US, "%,d TU/t", smr.thermalOutput));
        this.drawCustomInfoStat(
            x,
            y,
            guiLeft + 178,
            guiTop + 58,
            18,
            17,
            x,
            y,
            "Reactivity:",
            (int) (smr.reactivity * 1e5) + " PCM");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(tex);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (smr.control > 0) {

        }

        GaugeUtil.drawSmoothGauge(guiLeft + 169, guiTop + 34, this.zLevel, smr.temp / smr.meltTemp, 5, 2, 1, 0x202020);
    }
}

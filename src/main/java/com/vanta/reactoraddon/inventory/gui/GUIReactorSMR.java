package com.vanta.reactoraddon.inventory.gui;

import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.hbm.packet.PacketDispatcher;
import com.hbm.packet.toserver.NBTControlPacket;
import com.hbm.render.util.GaugeUtil;
import com.vanta.reactoraddon.inventory.container.ContainerReactorSMR;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorSMR;

public class GUIReactorSMR extends GuiInfoContainer {

    private static final ResourceLocation tex = new ResourceLocation(
        "reactoraddon",
        "textures/gui/reactors/gui_smr.png");
    private TileEntityReactorSMR smr;

    private GuiTextField rodField;

    public GUIReactorSMR(InventoryPlayer invPlayer, TileEntityReactorSMR tile) {
        super(new ContainerReactorSMR(invPlayer, tile));
        smr = tile;
        this.xSize = 203;
        this.ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.rodField = new GuiTextField(this.fontRendererObj, guiLeft + 146, guiTop + 82, 44, 8);
        this.rodField.setTextColor(0x00ff00);
        this.rodField.setDisabledTextColour(0x008000);
        this.rodField.setEnableBackgroundDrawing(false);
        this.rodField.setMaxStringLength(6);

        this.rodField.setText(String.format(Locale.US, "%.2f", 100 - smr.control));
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
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_RETURN && this.rodField.isFocused()) {
            if (NumberUtils.isNumber(this.rodField.getText())) {
                float newLevel = (float) MathHelper.clamp_double(Double.parseDouble(this.rodField.getText()), 0, 100);
                this.rodField.setText(String.format(Locale.US, "%.2f", newLevel));
                NBTTagCompound packet = new NBTTagCompound();
                packet.setFloat("rods", newLevel);
                PacketDispatcher.wrapper.sendToServer(new NBTControlPacket(packet, smr.xCoord, smr.yCoord, smr.zCoord));
                mc.getSoundHandler()
                    .playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1F));
            }
        } else if (!this.rodField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int i) {
        super.mouseClicked(mouseX, mouseY, i);
        this.rodField.mouseClicked(mouseX, mouseY, i);

        if (mouseX >= guiLeft + 117 && mouseX < guiLeft + 135 && mouseY >= guiTop + 99 && mouseY < guiTop + 117) {
            // add locking crap here fuck
            mc.getSoundHandler()
                .playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1F));
        }
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

        smr.tanks[0].renderTankInfo(this, x, y, guiLeft + 143, guiTop + 98, 16, 52);
        smr.tanks[1].renderTankInfo(this, x, y, guiLeft + 161, guiTop + 98, 16, 52);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(tex);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        if (smr.control > 0) {
            int dist = (int) (smr.control / 100 * 64);
            if (dist > 0) {
                drawTexturedModalRect(guiLeft + 142, guiTop + 11, 208, 64 - dist, 4, dist);
            }
        }

        if (smr.reactivity > 0) {
            drawTexturedModalRect(guiLeft + 115, guiTop + 7, 220, 0, 18, 18);
        }

        GaugeUtil.drawSmoothGauge(
            guiLeft + 169,
            guiTop + 34,
            this.zLevel,
            smr.temp / TileEntityReactorSMR.meltTemp,
            5,
            2,
            1,
            0x202020);
        GaugeUtil.drawSmoothGauge(
            guiLeft + 187,
            guiTop + 34,
            this.zLevel,
            smr.pressure / TileEntityReactorSMR.burstPres,
            5,
            2,
            1,
            0x202020);
        GaugeUtil.drawSmoothGauge(
            guiLeft + 169,
            guiTop + 67,
            this.zLevel,
            (float) (smr.thermalOutput) / TileEntityReactorSMR.maxThermalPower,
            5,
            2,
            1,
            0x202020);
        GaugeUtil.drawSmoothGauge(
            guiLeft + 187,
            guiTop + 67,
            this.zLevel,
            (smr.reactivity + 0.01) / 0.02,
            5,
            2,
            1,
            0x202020);

        GL11.glDisable(GL11.GL_LIGHTING);

        smr.tanks[0].renderTank(guiLeft + 143, guiTop + 150, this.zLevel, 16, 52);
        smr.tanks[1].renderTank(guiLeft + 161, guiTop + 150, this.zLevel, 16, 52);

        this.rodField.drawTextBox();
    }
}

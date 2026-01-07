package com.vanta.reactoraddon.inventory.gui;

import java.util.Locale;
import java.util.Objects;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.hbm.inventory.gui.GuiInfoContainer;
import com.vanta.reactoraddon.inventory.container.ContainerReactorDMRCore;
import com.vanta.reactoraddon.tileentity.machine.TileEntityReactorDMRCore;

public class GUIReactorDMRCore extends GuiInfoContainer {

    private static final ResourceLocation tex = new ResourceLocation(
        "reactoraddon",
        "textures/gui/reactors/gui_dmr.png");
    private TileEntityReactorDMRCore dmr;

    private GuiTextField injField;

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
        this.injField = new GuiTextField(this.fontRendererObj, guiLeft + 45, guiTop + 16, 44, 8);
        this.injField.setTextColor(0x00ff00);
        this.injField.setDisabledTextColour(0x008000);
        this.injField.setEnableBackgroundDrawing(false);
        this.injField.setMaxStringLength(6);

        this.injField.setText(String.format(Locale.US, "%.2f", dmr.injRate));
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
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_RETURN && this.injField.isFocused()) {
            float newLevel = (float) MathHelper.clamp_double(Double.parseDouble(this.injField.getText()), 0, 100);
            this.injField.setText(String.format(Locale.US, "%.2f", newLevel));
            // TODO: packet, need to do tileent shit first
            mc.getSoundHandler()
                .playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1F));
        } else if (!this.injField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int i) {
        super.mouseClicked(mouseX, mouseY, i);
        this.injField.mouseClicked(mouseX, mouseY, i);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}

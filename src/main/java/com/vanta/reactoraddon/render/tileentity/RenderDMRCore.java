package com.vanta.reactoraddon.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.hbm.blocks.BlockDummyable;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.tileentity.IItemRendererProvider;
import com.vanta.reactoraddon.blocks.ModBlocks;
import com.vanta.reactoraddon.main.ResourceManager;

public class RenderDMRCore extends TileEntitySpecialRenderer implements IItemRendererProvider {

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        switch (tileEntity.getBlockMetadata() - BlockDummyable.offset) {
            case 2:
                GL11.glRotatef(270, 0F, 1F, 0F);
                break;
            case 4:
                GL11.glRotatef(0, 0F, 1F, 0F);
                break;
            case 3:
                GL11.glRotatef(90, 0F, 1F, 0F);
                break;
            case 5:
                GL11.glRotatef(180, 0F, 1F, 0F);
                break;
        }

        GL11.glShadeModel(GL11.GL_SMOOTH);

        bindTexture(ResourceManager.dmr_tex);

        ResourceManager.dmr.renderAll();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_FLAT);

        GL11.glPopMatrix();
    }

    @Override
    public Item getItemForRenderer() {
        return Item.getItemFromBlock(ModBlocks.reactor_dmr_core);
    }

    @Override
    public IItemRenderer getRenderer() {
        return new ItemRenderBase() {

            @Override
            public void renderInventory() {
                GL11.glTranslated(0, -3.75, 0);
                GL11.glScaled(3.5, 3.5, 3.5);
            }

            @Override
            public void renderCommon() {
                GL11.glScaled(0.5, 0.5, 0.5);
                GL11.glShadeModel(GL11.GL_SMOOTH);
                bindTexture(ResourceManager.dmr_tex);
                ResourceManager.dmr.renderAll();
                GL11.glShadeModel(GL11.GL_FLAT);
            }
        };
    }
}

package com.vanta.reactoraddon.items;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import com.hbm.util.I18nUtil;

public class ItemLoreFood extends ItemFood {

    public ItemLoreFood(int healAmount, float saturationModifier, boolean isWolfsFavoriteMeat) {
        super(healAmount, saturationModifier, isWolfsFavoriteMeat);
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List<String> list, boolean bool) {
        String unloc = this.getUnlocalizedName() + ".desc";
        String loc = I18nUtil.resolveKey(unloc);

        if (!unloc.equals(loc)) {
            String[] locs = loc.split("\\$");

            list.addAll(Arrays.asList(locs));
        }
    }
}

package com.vanta.reactoraddon.event.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TooltipEventHandler {

    public static final HashMap<Item, List<String>> customToolTipMap = new HashMap<>();

    @SubscribeEvent
    public void eventTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;
        List<String> tip = event.toolTip;

    }

    // ARY
    public static void registerToolTip(Item item, List<String> tooltip) {
        if (customToolTipMap.containsKey(item)) {
            customToolTipMap.get(item)
                .addAll(tooltip);
        } else {
            customToolTipMap.put(item, tooltip);
        }
    }

    public static void registerToolTip(Item item, String tooltip) {
        List<String> newList;
        if (customToolTipMap.containsKey(item)) {
            newList = customToolTipMap.get(item);
        } else {
            newList = new ArrayList<>(1);
        }
        newList.add(tooltip);
        customToolTipMap.put(item, newList);
    }

    public static void registerToolTip(Item item, String tooltip, int initialSize) {
        List<String> newList;
        if (customToolTipMap.containsKey(item)) {
            newList = customToolTipMap.get(item);
        } else {
            newList = new ArrayList<>(initialSize);
        }
        newList.add(tooltip);
        customToolTipMap.put(item, newList);
    }

    public static List<String> getToolTip(Item item) {
        if (customToolTipMap.containsKey(item)) {
            return customToolTipMap.get(item);
        }
        return null;
    }
}

package com.bokmcdok.cat.objects.items;

import net.minecraft.world.item.HoneyBottleItem;

/**
 *  Peacemaker Honey Bottle
 *  - Gained by milking Peacemaker Cows
 *  - Can be crafted into sugar
 *  - Can be used to breed Peacemaker Butterflies
 */
public class PeacemakerHoneyBottleItem extends HoneyBottleItem {

    //  The name used for registering this item
    public static final String NAME = "peacemaker_honey_bottle";

    /**
     * Create a Peacemaker Honey Bottle Item
     * @param properties The item properties defined when registering
     */
    public PeacemakerHoneyBottleItem(Properties properties) {
        super(properties);
    }
}

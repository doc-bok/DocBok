package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entities.Butterfly;
import com.bokmcdok.cat.objects.entities.NyanCat;
import com.bokmcdok.cat.objects.entities.PeacemakerButterfly;
import com.bokmcdok.cat.objects.items.ButterflyNetItem;
import com.bokmcdok.cat.objects.items.PeacemakerHoneyBottleItem;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Holds a list of items for use elsewhere in the mod.
 */
public class ItemList {
    //  The item registry
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CatMod.MOD_ID);

    //  Some helpers for setting properties
    public static final Item.Properties MISC_ITEM_PROPERTIES = new Item.Properties().tab(CreativeModeTab.TAB_MISC);

    //  Peacemaker Honey Bottle
    //  - Gained by milking Peacemaker Cows
    //  - Can be crafted into sugar
    //  - Can be used to breed Peacemaker Butterflies
    public static final RegistryObject<Item> PEACEMAKER_HONEY_BOTTLE = ITEMS.register(PeacemakerHoneyBottleItem.NAME,
            () -> new PeacemakerHoneyBottleItem(new Item.Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .food(Foods.HONEY_BOTTLE)
                    .tab(CreativeModeTab.TAB_FOOD)
                    .stacksTo(16)));

    //  Butterfly net
    //  - Used to catch butterflies
    public static final RegistryObject<Item> BUTTERFLY_NET = ITEMS.register(ButterflyNetItem.NAME,
            () -> new ButterflyNetItem(new Item.Properties()
                    .stacksTo(1)
                    .tab(CreativeModeTab.TAB_TOOLS)));

    //  Spawn eggs
    public static final RegistryObject<Item> PEACEMAKER_BUTTERFLY_EGG = ITEMS.register(PeacemakerButterfly.NAME,
            () -> new ForgeSpawnEggItem(EntityList.PEACEMAKER_BUTTERFLY, 0x0088ff, 0x00ffff, MISC_ITEM_PROPERTIES));
    public static final RegistryObject<Item> BUTTERFLY_EGG = ITEMS.register(Butterfly.NAME,
            () -> new ForgeSpawnEggItem(EntityList.BUTTERFLY, 0xff0000, 0x000000, MISC_ITEM_PROPERTIES));
    public static final RegistryObject<Item> NYAN_CAT_EGG = ITEMS.register(NyanCat.NAME,
            () -> new ForgeSpawnEggItem(EntityList.NYAN_CAT, 0xff35c8, 0xffff00, MISC_ITEM_PROPERTIES));
}

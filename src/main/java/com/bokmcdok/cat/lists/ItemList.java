package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entities.Butterfly;
import com.bokmcdok.cat.objects.entities.NyanCat;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
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

    //  Spawn eggs
    public static final RegistryObject<Item> BUTTERFLY_EGG = ITEMS.register(Butterfly.NAME,
            () -> new ForgeSpawnEggItem(EntityList.BUTTERFLY, 0xff0000, 0x000000, MISC_ITEM_PROPERTIES));
    public static final RegistryObject<Item> NYAN_CAT_EGG = ITEMS.register(NyanCat.NAME,
            () -> new ForgeSpawnEggItem(EntityList.NYAN_CAT, 0xffff00, 0x00ffff, MISC_ITEM_PROPERTIES));
}
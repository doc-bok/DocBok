package com.bokmcdok.cat.objects.models;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.lists.ItemList;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

/**
 * Create item models.
 */
public class ItemModels extends ItemModelProvider {

    /**
     * Create an item model generator
     * @param generator The global data generator
     * @param existingFileHelper A helper class (?)
     */
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, CatMod.MOD_ID, existingFileHelper);
    }

    /**
     * Register the item models.
     */
    @Override
    protected void registerModels() {

        //  Items
        withExistingParent(Objects.requireNonNull(ItemList.BOTTLED_BUTTERFLY.get().getRegistryName()).getPath(), mcLoc("item/bottled_butterfly"));
        withExistingParent(Objects.requireNonNull(ItemList.BUTTERFLY_NET.get().getRegistryName()).getPath(), mcLoc("item/butterfly_net"));
        withExistingParent(Objects.requireNonNull(ItemList.PEACEMAKER_HONEY_BOTTLE.get().getRegistryName()).getPath(), mcLoc("item/peacemaker_honey_bottle"));

        //  Spawn Eggs
        withExistingParent(Objects.requireNonNull(ItemList.PEACEMAKER_BUTTERFLY_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.PEACEMAKER_EVOKER_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.PEACEMAKER_ILLUSIONER_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.PEACEMAKER_PILLAGER_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.PEACEMAKER_VILLAGER_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.PEACEMAKER_VINDICATOR_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.BUTTERFLY_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.NYAN_CAT_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
    }
}
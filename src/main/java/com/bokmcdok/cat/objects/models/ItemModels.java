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
        withExistingParent(Objects.requireNonNull(ItemList.BUTTERFLY_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ItemList.NYAN_CAT_EGG.get().getRegistryName()).getPath(), mcLoc("item/template_spawn_egg"));
    }
}
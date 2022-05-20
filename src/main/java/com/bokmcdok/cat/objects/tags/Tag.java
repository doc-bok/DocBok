package com.bokmcdok.cat.objects.tags;

import com.bokmcdok.cat.supplier.RegistrySetSupplier;
import com.bokmcdok.cat.tools.LazyValue;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class Tag {

    private final Set<ResourceLocation> entries;
    private final LazyValue<Set<Item>> mItems;

    private Tag() {
        this.entries = Sets.newHashSet();
        mItems = new LazyValue<>(new RegistrySetSupplier<>(ForgeRegistries.ITEMS, entries));
    }

    public static Tag fromJson(JsonObject data) {
        Tag result = new Tag();
        JsonArray values = GsonHelper.getAsJsonArray(data, "values");
        for (JsonElement i : values) {

            //  Take care of tags that are actually objects.
            JsonElement temp = i;
            if (temp.isJsonObject()) {
                temp = i.getAsJsonObject().get("id");
            }

            String location = temp.getAsString();

            //  Remove the hashtags sometimes added by forge.
            location = location.replace("#", "");

            result.entries.add(new ResourceLocation(location));
        }

        return result;
    }

    /**
     * Get the set of items that match the tag entries.
     * @return The set of items.
     */
    public Set<Item> getItems() {
        return mItems.getValue();
    }
}

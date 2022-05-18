package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.objects.tag.Tag;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Loads the tags into an accessible location, and holds some convenient lists
 * for use in the rest of the code base.
 */
public class TagList extends SimpleJsonResourceReloadListener {

    //  This tag contains all cat food items.
    public static Tag CAT_FOOD_ITEMS;

    //  Google's JSON parser for Java.
    private static final Gson GSON =
            (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    //  The Mojang log class.
    private static final Logger LOGGER = LogUtils.getLogger();

    //  The list of tags loaded from data.
    private final Map<ResourceLocation, Tag> tags = Maps.newHashMap();

    /**
     * Creates a JSON listener ready for any data reloads.
     */
    public TagList() {
        super(GSON, "tags");
        MinecraftForge.EVENT_BUS.addListener(this::onResourceReload);
    }

    /**
     * Called with a list of files in the "tags" data folder. Parses them one
     * by one and creates a new Tag for each.
     * @param files The list of files under the "tags" directory
     * @param resourceManager Unused
     * @param profilerFiller Unused
     */
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files,
                         @NotNull ResourceManager resourceManager,
                         @NotNull ProfilerFiller profilerFiller) {
        files.forEach((key, value) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(value, "tag");
                Tag tag = Tag.fromJson(jsonObject);
                tags.put(key, tag);
            }
            catch (RuntimeException e) {
                LOGGER.error("Couldn't read tag list for key {}", key.toString(), e);
            }
        });

        CAT_FOOD_ITEMS = tags.get(new ResourceLocation("cat:items/cat_food_items"));
    }

    /**
     * Adds this class as a listener when resources are reloaded.
     * @param event The reload event instance.
     */
    private void onResourceReload(AddReloadListenerEvent event)
    {
        event.addListener(this);
    }
}

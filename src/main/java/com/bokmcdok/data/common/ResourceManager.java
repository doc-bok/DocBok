package com.bokmcdok.data.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.FallbackResourceManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;

import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ResourceManager {
    private final Map<String, FallbackResourceManager> mNamespaceResourceManagers = Maps.newHashMap();
    private final ResourcePackType mType;

    /**
     * Create a new Resource Manager
     * @param side Either client-side or server-side.
     * @param modId The ID of the mod to relate this resource manager to.
     */
    public ResourceManager(ResourcePackType side, String modId) {
        mType = side;

        ModFileInfo modFileInfo = ModList.get().getModFileById(modId);
        ModFileResourcePack resourcePack = new ModFileResourcePack(modFileInfo.getFile());
        addResourcePack(resourcePack);
    }

    /**
     * Get a resource from a location.
     * @param resourceLocation The location of the resource.
     * @return The resource at the location.
     * @throws IOException Thrown if file not found.
     */
    public IResource getResource(ResourceLocation resourceLocation) throws IOException {
        IResourceManager resourceManager = mNamespaceResourceManagers.get(resourceLocation.getNamespace());
        if (resourceManager != null) {
            return resourceManager.getResource(resourceLocation);
        } else {
            throw new FileNotFoundException((resourceLocation.toString()));
        }
    }

    /**
     * Get all the resource locations at a specific path.
     * @param path The path to get the locations from.
     * @param filter A filter to only obtain specific resources (e.g. JSON files).s
     * @return A list of resource locations at the path.
     */
    public Collection<ResourceLocation> getAllResourceLocations(String path, Predicate<String> filter) {
        Set<ResourceLocation> set = Sets.newHashSet();

        for(FallbackResourceManager fallbackresourcemanager : mNamespaceResourceManagers.values()) {
            set.addAll(fallbackresourcemanager.getAllResourceLocations(path, filter));
        }

        List<ResourceLocation> list = Lists.newArrayList(set);
        Collections.sort(list);
        return list;
    }

    /**
     * Add a resource pack to the resource manager.
     * @param resourcePack The resource pack to add.
     */
    public void addResourcePack(IResourcePack resourcePack) {
        for(String s : resourcePack.getResourceNamespaces(mType)) {
            FallbackResourceManager fallbackResourceManager = mNamespaceResourceManagers.get(s);
            if (fallbackResourceManager == null) {
                fallbackResourceManager = new FallbackResourceManager(mType, s);
                mNamespaceResourceManagers.put(s, fallbackResourceManager);
            }

            fallbackResourceManager.addResourcePack(resourcePack);
        }
    }
}

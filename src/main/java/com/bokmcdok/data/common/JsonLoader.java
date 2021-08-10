package com.bokmcdok.data.common;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.IResource;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
                                                      .disableHtmlEscaping()
                                                      .create();
    private static final String JSON_EXTENSION = ".json";
    private static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Loads all JSON resources within a folder.
     * @param resourceManager The resource manager to use for loading files.
     * @param folder The folder to load JSON files from.
     * @return A list of JSON data loaded from the folder.
     */
    public Map<ResourceLocation, JsonObject> loadJsonResources(
            ResourceManager resourceManager,
            String folder) {
        Map<ResourceLocation, JsonObject> result = Maps.newHashMap();
        int folderNameLength = folder.length() + 1;

        for (ResourceLocation resourceLocation :
                resourceManager.getAllResourceLocations(folder, (x) -> x.endsWith(JSON_EXTENSION))) {
            String path = resourceLocation.getPath();
            String namespace = resourceLocation.getNamespace();
            String resourceName = path.substring(folderNameLength,
                    path.length() - JSON_EXTENSION_LENGTH);
            ResourceLocation registryName = new ResourceLocation(namespace, resourceName);

            try (
                    IResource resource =
                            resourceManager.getResource(resourceLocation);
                    InputStream inputStream = resource.getInputStream();
                    Reader reader = new BufferedReader(new InputStreamReader(
                            inputStream, StandardCharsets.UTF_8))
            ) {
                JsonObject jsonObject =
                        JSONUtils.fromJson(GSON, reader, JsonObject.class);
                if (jsonObject != null) {
                    JsonObject entry = result.put(registryName, jsonObject);
                    if (entry != null) {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + registryName);
                    }
                } else {
                    LOGGER.error("Couldn't load file {} from {} as it's null or empty.",
                            registryName, resourceLocation);
                }
            } catch (IllegalArgumentException | IOException | JsonParseException exception) {
                LOGGER.error("Couldn't parse data file {} from {}",
                        registryName, resourceLocation, exception);
            }
        }

        return  result;
    }
}

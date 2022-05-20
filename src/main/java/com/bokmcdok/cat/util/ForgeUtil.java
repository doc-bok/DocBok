package com.bokmcdok.cat.util;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.lists.EntityList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = CatMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeUtil {
    private static Biome.BiomeCategory BUTTERFLY_BIOMES[] = {
            Biome.BiomeCategory.TAIGA,
            Biome.BiomeCategory.JUNGLE,
            Biome.BiomeCategory.MESA,
            Biome.BiomeCategory.PLAINS,
            Biome.BiomeCategory.SAVANNA,
            Biome.BiomeCategory.FOREST,
            Biome.BiomeCategory.RIVER,
            Biome.BiomeCategory.SWAMP//,
            //Biome.BiomeCategory.MUSHROOM
    };

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent event) {
        if (Arrays.asList(BUTTERFLY_BIOMES).contains(event.getCategory())) {
            event.getSpawns().addSpawn(MobCategory.AMBIENT,
                    new MobSpawnSettings.SpawnerData(EntityList.BUTTERFLY.get(), 10, 3, 5));
        }
    }
}

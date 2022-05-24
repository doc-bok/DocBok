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

/**
 * Handle events on the Forge event bus
 */
@Mod.EventBusSubscriber(modid = CatMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeUtil {

    //  Biomes where butterflies can spawn
    private static final Biome.BiomeCategory[] BUTTERFLY_BIOMES = {
            Biome.BiomeCategory.TAIGA,
            Biome.BiomeCategory.JUNGLE,
            Biome.BiomeCategory.MESA,
            Biome.BiomeCategory.PLAINS,
            Biome.BiomeCategory.SAVANNA,
            Biome.BiomeCategory.FOREST,
            Biome.BiomeCategory.RIVER,
            Biome.BiomeCategory.SWAMP
    };

    //  Non-overworld biomes
    private static final Biome.BiomeCategory[] NON_OVERWORLD_BIOMES = {
            Biome.BiomeCategory.THEEND,
            Biome.BiomeCategory.NETHER
    };

    @SubscribeEvent
    public static void biomeLoading(BiomeLoadingEvent event) {

        //  Butterflies
        if (Arrays.asList(BUTTERFLY_BIOMES).contains(event.getCategory())) {
            event.getSpawns().addSpawn(MobCategory.AMBIENT,
                    new MobSpawnSettings.SpawnerData(EntityList.BUTTERFLY.get(), 10, 3, 5));
        }

        //  Peacemaker butterflies
        if (!Arrays.asList(NON_OVERWORLD_BIOMES).contains(event.getCategory())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER,
                    new MobSpawnSettings.SpawnerData(EntityList.PEACEMAKER_BUTTERFLY.get(), 10, 1, 4));
        }
    }
}

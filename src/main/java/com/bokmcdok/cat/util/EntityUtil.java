package com.bokmcdok.cat.util;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entity.NyanCatEntity;
import com.bokmcdok.cat.lists.EntityList;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CatMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityUtil {

    /**
     * Register the renderers for our entities.
     * @param event The event information
     */
    @SubscribeEvent
    public static void registerEntityRenders(final EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(EntityList.NYAN_CAT.get(), CatRenderer::new);
    }

    /**
     * Register the attributes for living entities.
     */
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityList.NYAN_CAT.get(), NyanCatEntity.setCustomAttributes().build());
    }

    /**
     * Register entity spawn placements here.
     * @param event The event information.
     */
    @SubscribeEvent
    public static void registerEntitySpawnPlacement(final RegistryEvent.Register<EntityType<?>> event) {
        SpawnPlacements.register(EntityList.NYAN_CAT.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.WORLD_SURFACE,
                Cat::checkAnimalSpawnRules);
    }
}

package com.bokmcdok.fauna.util;

import com.bokmcdok.fauna.FaunaMod;
import com.bokmcdok.fauna.objects.entity.NyanCatEntity;
import com.bokmcdok.fauna.lists.EntityList;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = FaunaMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityUtil {

    /**
     * Register the renderers for our entities.
     * @param event The event information
     */
    @SubscribeEvent
    public static void registerEntityRenders(final FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityList.NYAN_CAT.get(),
                CatRenderer::new);
    }

    /**
     * Register the attributes for living entities.
     */
    public static void registerEntityAttributes() {
        GlobalEntityTypeAttributes.put(EntityList.NYAN_CAT.get(),
                NyanCatEntity.setCustomAttributes().func_233813_a_());
    }

    /**
     * Register entity spawn placements here.
     * @param event The event information.
     */
    @SubscribeEvent
    public static void registerEntitySpawnPlacement(final RegistryEvent.Register<EntityType<?>> event) {
        EntitySpawnPlacementRegistry.register(EntityList.NYAN_CAT.get(),
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.WORLD_SURFACE,
                CatEntity::canAnimalSpawn);
    }
}

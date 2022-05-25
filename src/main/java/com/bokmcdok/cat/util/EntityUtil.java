package com.bokmcdok.cat.util;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entities.Butterfly;
import com.bokmcdok.cat.objects.entities.NyanCat;
import com.bokmcdok.cat.lists.EntityList;
import com.bokmcdok.cat.objects.entities.PeacemakerButterfly;
import com.bokmcdok.cat.objects.models.ButterflyModel;
import com.bokmcdok.cat.objects.models.PeacemakerButterflyModel;
import com.bokmcdok.cat.objects.renderers.ButterflyRenderer;
import com.bokmcdok.cat.objects.renderers.PeacemakerButterflyRenderer;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Holds event handlers for entities
 */
@Mod.EventBusSubscriber(modid = CatMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityUtil {

    /**
     * Register the renderers for our entities
     * @param event The event information
     */
    @SubscribeEvent
    public static void registerEntityRenders(final EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(EntityList.PEACEMAKER_BUTTERFLY.get(), PeacemakerButterflyRenderer::new);
        event.registerEntityRenderer(EntityList.BUTTERFLY.get(), ButterflyRenderer::new);
        event.registerEntityRenderer(EntityList.NYAN_CAT.get(), CatRenderer::new);
        event.registerEntityRenderer(EntityList.PEACEMAKER_VILLAGER.get(), VillagerRenderer::new);
    }

    /**
     * Register the attributes for living entities
     */
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityList.NYAN_CAT.get(), NyanCat.createAttributes().build());
        event.put(EntityList.BUTTERFLY.get(), Butterfly.createAttributes().build());
        event.put(EntityList.PEACEMAKER_BUTTERFLY.get(), PeacemakerButterfly.createAttributes().build());
        event.put(EntityList.PEACEMAKER_VILLAGER.get(), Villager.createAttributes().build());
    }

    /**
     * Register entity spawn placements here
     * @param event The event information
     */
    @SubscribeEvent
    public static void registerEntitySpawnPlacement(final RegistryEvent.Register<EntityType<?>> event) {
        SpawnPlacements.register(EntityList.NYAN_CAT.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Cat::checkAnimalSpawnRules);

        SpawnPlacements.register(EntityList.BUTTERFLY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Butterfly::checkSpawnRules);

        SpawnPlacements.register(EntityList.PEACEMAKER_BUTTERFLY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                PeacemakerButterfly::checkSpawnRules);

        SpawnPlacements.register(EntityList.PEACEMAKER_VILLAGER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Villager::checkMobSpawnRules);
    }

    /**
     * Registers models to be used for rendering
     * @param event The event information
     */
    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ButterflyModel.LAYER_LOCATION, ButterflyModel::createBodyLayer);
        event.registerLayerDefinition(PeacemakerButterflyModel.LAYER_LOCATION, PeacemakerButterflyModel::createBodyLayer);
    }
}

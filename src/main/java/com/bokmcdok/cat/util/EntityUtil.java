package com.bokmcdok.cat.util;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entities.living.Butterfly;
import com.bokmcdok.cat.objects.entities.living.NyanCat;
import com.bokmcdok.cat.lists.EntityList;
import com.bokmcdok.cat.objects.entities.PeacemakerPillager;
import com.bokmcdok.cat.objects.entities.living.PeacemakerButterfly;
import com.bokmcdok.cat.objects.entities.living.PeacemakerEvoker;
import com.bokmcdok.cat.objects.entities.living.PeacemakerIllusioner;
import com.bokmcdok.cat.objects.entities.living.PeacemakerVillager;
import com.bokmcdok.cat.objects.models.ButterflyModel;
import com.bokmcdok.cat.objects.models.PeacemakerButterflyModel;
import com.bokmcdok.cat.objects.renderers.ButterflyRenderer;
import com.bokmcdok.cat.objects.renderers.PeacemakerButterflyRenderer;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EvokerRenderer;
import net.minecraft.client.renderer.entity.IllusionerRenderer;
import net.minecraft.client.renderer.entity.PillagerRenderer;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Pillager;
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
     * Helper method to check if an entity is hosted by a butterfly
     * @param entity The entity to check
     * @return True if the entity is a butterfly host
     */
    public static boolean isNotPeacemakerTarget(LivingEntity entity) {
        return !(entity instanceof PeacemakerButterfly ||
                entity instanceof PeacemakerEvoker ||
                entity instanceof PeacemakerIllusioner ||
                entity instanceof PeacemakerPillager ||
                entity instanceof PeacemakerVillager);
    }

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
        event.registerEntityRenderer(EntityList.PEACEMAKER_EVOKER.get(), EvokerRenderer::new);
        event.registerEntityRenderer(EntityList.PEACEMAKER_ILLUSIONER.get(), IllusionerRenderer::new);
        event.registerEntityRenderer(EntityList.PEACEMAKER_PILLAGER.get(), PillagerRenderer::new);
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
        event.put(EntityList.PEACEMAKER_EVOKER.get(), PeacemakerEvoker.createAttributes().build());
        event.put(EntityList.PEACEMAKER_ILLUSIONER.get(), PeacemakerIllusioner.createAttributes().build());
        event.put(EntityList.PEACEMAKER_PILLAGER.get(), PeacemakerPillager.createAttributes().build());
        event.put(EntityList.PEACEMAKER_VILLAGER.get(), PeacemakerVillager.createAttributes().build());
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

        SpawnPlacements.register(EntityList.PEACEMAKER_EVOKER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Evoker::checkMobSpawnRules);

        SpawnPlacements.register(EntityList.PEACEMAKER_ILLUSIONER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Illusioner::checkMobSpawnRules);

        SpawnPlacements.register(EntityList.PEACEMAKER_PILLAGER.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Pillager::checkMobSpawnRules);

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

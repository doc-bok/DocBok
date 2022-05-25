package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entities.Butterfly;
import com.bokmcdok.cat.objects.entities.NyanCat;
import com.bokmcdok.cat.objects.entities.PeacemakerButterfly;
import com.bokmcdok.cat.objects.entities.PeacemakerEvoker;
import com.bokmcdok.cat.objects.entities.PeacemakerIllusioner;
import com.bokmcdok.cat.objects.entities.PeacemakerPillager;
import com.bokmcdok.cat.objects.entities.PeacemakerVillager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityList {
    //  Our entity registry
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, CatMod.MOD_ID);

    //  The Nyan Cat Entity
    public static final RegistryObject<EntityType<NyanCat>> NYAN_CAT =
            ENTITIES.register(NyanCat.NAME, () ->
                    EntityType.Builder.of(NyanCat::new, MobCategory.CREATURE)
                    .sized(0.3f, 0.4f)
                    .build(NyanCat.NAME));

    //  The butterfly entity
    public static final RegistryObject<EntityType<Butterfly>> BUTTERFLY =
            ENTITIES.register(Butterfly.NAME, () ->
                    EntityType.Builder.of(Butterfly::new, MobCategory.AMBIENT)
                            .sized(0.3f, 0.4f)
                            .build(Butterfly.NAME));

    //  Peacemaker Butterfly
    public static final RegistryObject<EntityType<PeacemakerButterfly>> PEACEMAKER_BUTTERFLY =
            ENTITIES.register(PeacemakerButterfly.NAME, () ->
                    EntityType.Builder.of(PeacemakerButterfly::new, MobCategory.MONSTER)
                            .sized(0.3f, 0.4f)
                            .build(PeacemakerButterfly.NAME));

    //  Peacemaker Evoker
    public static final RegistryObject<EntityType<PeacemakerEvoker>> PEACEMAKER_EVOKER =
            ENTITIES.register(PeacemakerEvoker.NAME, () ->
                    EntityType.Builder.of(PeacemakerEvoker::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build(PeacemakerEvoker.NAME));

    //  Peacemaker Illusioner
    public static final RegistryObject<EntityType<PeacemakerIllusioner>> PEACEMAKER_ILLUSIONER =
            ENTITIES.register(PeacemakerIllusioner.NAME, () ->
                    EntityType.Builder.of(PeacemakerIllusioner::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build(PeacemakerIllusioner.NAME));

    //  Peacemaker Pillager
    public static final RegistryObject<EntityType<PeacemakerPillager>> PEACEMAKER_PILLAGER =
            ENTITIES.register(PeacemakerPillager.NAME, () ->
                    EntityType.Builder.of(PeacemakerPillager::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build(PeacemakerPillager.NAME));

    //  Peacemaker Villager
    public static final RegistryObject<EntityType<PeacemakerVillager>> PEACEMAKER_VILLAGER =
            ENTITIES.register(PeacemakerVillager.NAME, () ->
                    EntityType.Builder.of(PeacemakerVillager::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build(PeacemakerVillager.NAME));
}

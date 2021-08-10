package com.bokmcdok.fauna.lists;

import com.bokmcdok.fauna.FaunaMod;
import com.bokmcdok.fauna.objects.entity.NyanCatEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityList {

    //  Our entity registry
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, FaunaMod.MOD_ID);

    //  The Nyan Cat Entity
    public static final RegistryObject<EntityType<NyanCatEntity>> NYAN_CAT =
            ENTITIES.register(NyanCatEntity.NAME, () ->
                    EntityType.Builder.create(NyanCatEntity::new, EntityClassification.CREATURE)
                    .size(0.3f, 0.4f)
                    .build(NyanCatEntity.NAME));
}

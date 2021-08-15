package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entity.NyanCatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityList {

    //  Our entity registry
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, CatMod.MOD_ID);

    //  The Nyan Cat Entity
    public static final RegistryObject<EntityType<NyanCatEntity>> NYAN_CAT =
            ENTITIES.register(NyanCatEntity.NAME, () ->
                    EntityType.Builder.of(NyanCatEntity::new, MobCategory.CREATURE)
                    .sized(0.3f, 0.4f)
                    .build(NyanCatEntity.NAME));
}

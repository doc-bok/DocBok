package com.bokmcdok.cat.objects.entities;

import com.bokmcdok.cat.objects.models.PeacemakerButterflyModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.Random;

public class PeacemakerButterfly extends Monster {

    public static final String NAME = "peacemaker_butterfly";


    /**
     * Defines the spawn rules for butterflies: they can spawn anywhere the
     * light level is above 8.
     * @param entityType The entity type to spawn.
     * @param level The current level.
     * @param spawnType The type of spawn.
     * @param position The position to spawn in.
     * @param rng The random number generator.
     * @return True if the butterfly can spawn.
     */
    public static boolean checkSpawnRules(@SuppressWarnings("unused") EntityType<PeacemakerButterfly> entityType,
                                          LevelAccessor level,
                                          @SuppressWarnings("unused") MobSpawnType spawnType,
                                          BlockPos position,
                                          @SuppressWarnings("unused") Random rng) {
        return level.getRawBrightness(position, 0) > 8;
    }

    public PeacemakerButterfly(EntityType<? extends PeacemakerButterfly> type, Level level) {
        super(type, level);
    }

    /**
     * Create attributes for a butterfly.
     * @return Butterflies have only 3 health (1.5 hearts).
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3d);
    }
}

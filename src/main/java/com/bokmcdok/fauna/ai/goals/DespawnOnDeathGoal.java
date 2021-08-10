package com.bokmcdok.fauna.ai.goals;

import net.minecraft.entity.MobEntity;

/**
 * Despawns an entity after death. Useful for preventing summoned
 * monsters from spawning loot.
 */
public class DespawnOnDeathGoal extends DespawnGoal {

    /**
     * Construction
     * @param ownerIn The entity that owns the goal.
     */
    public DespawnOnDeathGoal(MobEntity ownerIn) {
        super(ownerIn);
    }

    /**
     * Executes after the entity has existed for too long.
     * @return TRUE if the entity has existed for longer than allowed.
     */
    @Override
    public boolean shouldExecute() {
        return !owner.isAlive();
    }
}

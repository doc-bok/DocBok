package com.bokmcdok.fauna.ai.goals;

import net.minecraft.entity.MobEntity;

/**
 * Despawns an entity after a certain amount of time. Useful for summoned
 * monsters.
 */
public class DelayedDespawnGoal extends DespawnGoal {
    private final int ticksUntilDespawn;

    /**
     * Construction
     * @param ownerIn The entity that owns the goal.
     * @param ticksUntilDespawnIn The number of ticks until the entity despawns.
     */
    public DelayedDespawnGoal(MobEntity ownerIn, int ticksUntilDespawnIn) {
        super(ownerIn);
        ticksUntilDespawn = ticksUntilDespawnIn;
    }

    /**
     * Executes after the entity has existed for too long.
     * @return TRUE if the entity has existed for longer than allowed.
     */
    @Override
    public boolean shouldExecute() {
        return owner.ticksExisted > ticksUntilDespawn;
    }
}

package com.bokmcdok.fauna.ai.goals;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Simple goal that despawns a living entity.
 */
public abstract class DespawnGoal extends Goal {
    protected final MobEntity owner;

    /**
     * Construction
     * @param ownerIn The owner of this goal.
     */
    public DespawnGoal(MobEntity ownerIn) {
        owner = ownerIn;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Simple - just remove the entity from the world.
     */
    @Override
    public void tick() {
        owner.remove();
    }
}

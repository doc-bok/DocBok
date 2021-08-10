package com.bokmcdok.fauna.ai.goals;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;

/**
 * Similar to the vanilla breed goal, except it doesn't spawn XP if a player
 * didn't breed the animal.
 */
public class NaturalBreedGoal extends BreedGoal {

    /**
     * Construction
     * @param animalIn The entity this goal belongs to.
     * @param speedIn The speed at which to perform the goal.
     */
    public NaturalBreedGoal(AnimalEntity animalIn, double speedIn) {
        this(animalIn, speedIn, animalIn.getClass());
    }

    /**
     * Construction
     * @param animalIn The entity this goal belongs to.
     * @param speedIn The speed at which to perform the goal.
     * @param classIn The class of the entity to breed.
     */
    public NaturalBreedGoal(AnimalEntity animalIn,
                            double speedIn,
                            Class<? extends AnimalEntity> classIn) {
        super(animalIn, speedIn, classIn);
    }

    /**
     * This override allows for animals breeding without player interference
     * meaning they won't spawn experience orbs. It also fixes a corner case
     * where two different players may feed one of each animal and updates
     * stats correctly.
     */
    @Override
    protected void spawnBaby() {
        AgeableEntity child = animal.createChild(targetMate);

        final BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(animal, targetMate, child);
        final boolean cancelled = MinecraftForge.EVENT_BUS.post(event);

        child = event.getChild();
        if (!cancelled && child != null) {
            ServerPlayerEntity player1 = animal.getLoveCause();
            ServerPlayerEntity player2 = targetMate.getLoveCause();

            Vector3d position = animal.getPositionVec();
            if (player1 != null || player2 != null) {
                if (player1 != null) {
                    player1.addStat(Stats.ANIMALS_BRED);
                    CriteriaTriggers.BRED_ANIMALS.trigger(player1, animal, targetMate, child);
                }

                if (player2 != null) {
                    player2.addStat(Stats.ANIMALS_BRED);
                    CriteriaTriggers.BRED_ANIMALS.trigger(player2, targetMate, animal, child);
                }

                if (world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                    world.addEntity(new ExperienceOrbEntity(
                            world,
                            position.x,
                            position.y,
                            position.z,
                            animal.getRNG().nextInt(7) + 1));
                }
            }

            child.setGrowingAge(-24000);
            child.setLocationAndAngles(position.x, position.y, position.z, 0, 0);
            world.addEntity(child);
            world.setEntityState(animal, (byte)18);
        }

        animal.setGrowingAge(6000);
        targetMate.setGrowingAge(6000);
        animal.resetInLove();
        targetMate.resetInLove();
    }
}

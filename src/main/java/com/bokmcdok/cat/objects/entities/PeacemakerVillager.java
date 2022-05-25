package com.bokmcdok.cat.objects.entities;

import com.bokmcdok.cat.lists.EntityList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PeacemakerVillager extends Villager {

    //  The entity name, used for registration
    public final static String NAME = "peacemaker_villager";

    /**
     * Create a peacemaker villager
     * @param entityType The type of entity
     * @param level The current level
     */
    public PeacemakerVillager(EntityType<? extends PeacemakerVillager> entityType,
                              Level level) {
        super(entityType, level);
    }

    /**
     * Spawn a peacemaker butterfly after death
     * @param damageSource The source of the damage that killed the villager
     */
    @Override
    public void die(@NotNull DamageSource damageSource) {
        super.die(damageSource);

        if (!this.level.isClientSide()) {
            PeacemakerButterfly butterfly = EntityList.PEACEMAKER_BUTTERFLY.get().create(this.level);
            if (butterfly != null) {
                butterfly.copyPosition(this);
                butterfly.finalizeSpawn((ServerLevel) this.level,
                        butterfly.level.getCurrentDifficultyAt(butterfly.getOnPos()),
                        MobSpawnType.CONVERSION,
                        null,
                        null);
                this.level.addFreshEntity(butterfly);
            }
        }
    }

    /**
     * Refresh the villager's brain
     * @param level The current level
     */
    @Override
    public void refreshBrain(@NotNull ServerLevel level) {
        Brain<Villager> brain = this.getBrain();
        brain.stopAll(level, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    /**
     * Make a brain for the villager
     * @param saveData The save data to load from
     * @return A new brain
     */
    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> saveData) {
        Brain<Villager> brain = this.brainProvider().makeBrain(saveData);
        this.registerBrainGoals(brain);
        return brain;
    }

    /**
     * Register a brain's goals. Overrides sleeping behaviour in normal villagers
     * @param brain The brain to create a new one from
     */
    private void registerBrainGoals(Brain<Villager> brain) {
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();
        if (this.isBaby()) {
            brain.setSchedule(Schedule.VILLAGER_BABY);
            brain.addActivity(Activity.PLAY, VillagerGoalPackages.getPlayPackage(0.5F));
        } else {
            brain.setSchedule(Schedule.VILLAGER_DEFAULT);
            brain.addActivityWithConditions(Activity.WORK,
                    VillagerGoalPackages.getWorkPackage(villagerprofession, 0.5F),
                    ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
        }

        brain.addActivity(Activity.CORE, VillagerGoalPackages.getCorePackage(villagerprofession, 0.5F));
        brain.addActivityWithConditions(Activity.MEET,
                VillagerGoalPackages.getMeetPackage(villagerprofession, 0.5F),
                ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        brain.addActivity(Activity.REST, VillagerGoalPackages.getHidePackage(villagerprofession, 0.5F));
        brain.addActivity(Activity.IDLE, VillagerGoalPackages.getIdlePackage(villagerprofession, 0.5F));
        brain.addActivity(Activity.PANIC, VillagerGoalPackages.getPanicPackage(villagerprofession, 0.5F));
        brain.addActivity(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage(villagerprofession, 0.5F));
        brain.addActivity(Activity.RAID, VillagerGoalPackages.getRaidPackage(villagerprofession, 0.5F));
        brain.addActivity(Activity.HIDE, VillagerGoalPackages.getHidePackage(villagerprofession, 0.5F));
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }
}

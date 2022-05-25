package com.bokmcdok.cat.objects.entities;

import com.bokmcdok.cat.util.EntityUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PeacemakerIllusioner extends Illusioner {

    //  The name used for registration
    public static final String NAME = "peacemaker_illusioner";

    /**
     * Create a peacemaker illusioner
     * @param type The entity type
     * @param level The current level
     */
    public PeacemakerIllusioner(EntityType<? extends  PeacemakerIllusioner> type,
                                Level level) {
        super(type, level);
    }

    /**
     * Spawn a peacemaker butterfly after death
     * @param damageSource The source of the damage that killed the villager
     */
    @Override
    public void die(@NotNull DamageSource damageSource) {
        super.die(damageSource);

        PeacemakerButterfly.respawn(this);
    }

    /**
     * Override the target goals to ignore peacemaker mobs
     */
    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.targetSelector.removeAllGoals();
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true))
                .setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false,
                EntityUtil::isNotPeacemakerTarget)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false)
                .setUnseenMemoryTicks(300));
    }
}

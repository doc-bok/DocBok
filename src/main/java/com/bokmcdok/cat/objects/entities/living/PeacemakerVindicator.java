package com.bokmcdok.cat.objects.entities.living;

import com.bokmcdok.cat.util.EntityUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PeacemakerVindicator extends Vindicator {

    //  The name used for registration
    public static final String NAME = "peacemaker_vindicator";

    /**
     * Butterflies make their hosts faster, stronger, and tougher
     * @return Attributes for the entity
     */
    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.45F)
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 12.0D);
    }

    /**
     * Create a peacemaker vindicator
     * @param type The entity type
     * @param level The current level
     */
    public PeacemakerVindicator(EntityType<? extends PeacemakerVindicator> type,
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
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true,
                EntityUtil::isNotPeacemakerTarget)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false));
    }
}

package com.bokmcdok.fauna.ai.goals;

import com.bokmcdok.fauna.ai.behaviors.ISpellcasterBehavior;
import com.bokmcdok.fauna.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundCategory;

import java.util.EnumSet;
import java.util.function.BiFunction;

/**
 * Allows a mob to cast a spell.
 */
public abstract class CastSpellGoal extends Goal {
    protected final MobEntity caster;
    private final Spell spell;
    private final double speed;
    private final BiFunction<MobEntity, LivingEntity, Boolean> predicate;
    private int castingTime = 0;
    private int cooldown = 0;

    /**
     * Execute if we have a target.
     * @return TRUE if the goal should execute.
     */
    @Override
    public boolean shouldExecute() {
        if (caster.ticksExisted > cooldown) {
            LivingEntity target = getTarget();
            if (target != null) {
                return predicate == null || predicate.apply(caster, target);
            }
        }

        return false;
    }

    /**
     * Reset the task if it is cancelled for any reason.
     */
    @Override
    public void resetTask() {
        super.resetTask();

        if (caster instanceof ISpellcasterBehavior) {
            ((ISpellcasterBehavior) caster).setCastingSpell(false);
        }

        castingTime = 0;
    }

    /**
     * Move in range of the target and cast the spell.
     */
    @Override
    public void tick() {
        LivingEntity target = getTarget();
        caster.getLookController().setLookPositionWithEntity(target, 10, caster.getVerticalFaceSpeed());

        if (caster != target && caster.getDistanceSq(target) > spell.getRangeSquared()) {
            caster.getNavigator().tryMoveToEntityLiving(target, speed);

            if (castingTime != 0) {
                caster.world.playSound(null, caster.func_233580_cy_(), spell.getFailSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                resetTask();
            }
        } else {
            if (castingTime == 0) {
                if (caster instanceof ISpellcasterBehavior) {
                    ((ISpellcasterBehavior)caster).setCastingSpell(true);
                }

                caster.world.playSound(null, caster.func_233580_cy_(), spell.getPrepareSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
            }

            ++castingTime;
            if (castingTime >= spell.getCastingTime()) {
                if (spell.cast(caster, target)) {
                    caster.world.playSound(null, caster.func_233580_cy_(), spell.getCastSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                } else {
                    caster.world.playSound(null, caster.func_233580_cy_(), spell.getFailSound(), SoundCategory.HOSTILE, 5.0f, 1.0F);
                }

                resetTask();

                cooldown = caster.ticksExisted + spell.getCooldown();
            }
        }
    }

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     */
    protected CastSpellGoal(MobEntity caster, Spell spell, double speed) {
        this(caster, spell, speed, null);
    }

    /**
     * Construction
     * @param casterIn The entity that will cast the spell.
     * @param spellIn The spell to cast.
     * @param predicateIn The conditions under which to cast the spell.
     */
    protected CastSpellGoal(MobEntity casterIn,
                            Spell spellIn,
                            double speedIn,
                            BiFunction<MobEntity, LivingEntity, Boolean> predicateIn) {
        caster = casterIn;
        spell = spellIn;
        speed = speedIn;
        predicate = predicateIn;
        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    /**
     * Get a living entity target.
     * @return The entity being targeted, if any.
     */
    protected abstract LivingEntity getTarget();
}

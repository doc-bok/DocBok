package com.bokmcdok.fauna.ai.goals;

import com.bokmcdok.fauna.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

import java.util.function.BiFunction;

/**
 * Cast a spell on the attack target.
 */
public class CastAttackSpellGoal extends CastSpellGoal {

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     */
    public CastAttackSpellGoal(MobEntity caster,
                               Spell spell,
                               double speed) {
        this(caster, spell, speed, null);
    }

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     * @param predicate The conditions under which to cast the spell.
     */
    public CastAttackSpellGoal(MobEntity caster,
                               Spell spell,
                               double speed,
                               BiFunction<MobEntity, LivingEntity, Boolean> predicate) {
        super(caster, spell, speed, predicate);
    }

    /**
     * Get a living entity target.
     * @return The entity being targeted, if any.
     */
    @Override
    protected LivingEntity getTarget() {
        return caster.getAttackTarget();
    }

    /**
     * Reset the target so the entity doesn't just keep trying the same spell on the same target.
     */
    @Override
    public void resetTask() {
        super.resetTask();
        caster.setAttackTarget(null);
    }
}
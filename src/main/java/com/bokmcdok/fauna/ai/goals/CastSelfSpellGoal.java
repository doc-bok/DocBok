package com.bokmcdok.fauna.ai.goals;

import com.bokmcdok.fauna.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;

import java.util.function.BiFunction;

/**
 * Cast a buff spell on themselves.
 */
public class CastSelfSpellGoal extends CastSpellGoal {

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     */
    public CastSelfSpellGoal(MobEntity caster,
                             Spell spell) {
        this(caster, spell, null);
    }

    /**
     * Construction
     * @param caster The entity that will cast the spell.
     * @param spell The spell to cast.
     * @param predicate The conditions under which to cast the spell.
     */
    public CastSelfSpellGoal(MobEntity caster,
                             Spell spell,
                             BiFunction<MobEntity, LivingEntity, Boolean> predicate) {
        super(caster, spell, 1, predicate);
    }

    /**
     * Get a living entity target.
     * @return The entity being targeted, if any.
     */
    @Override
    protected LivingEntity getTarget() {
        return caster;
    }
}

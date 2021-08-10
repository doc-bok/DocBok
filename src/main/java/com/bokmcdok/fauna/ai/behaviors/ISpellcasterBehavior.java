package com.bokmcdok.fauna.ai.behaviors;

/**
 * This provides an interface to mobs that have spellcasting behavior.
 */
public interface ISpellcasterBehavior {
    boolean isCastingSpell();

    void setCastingSpell(boolean castingSpell);
}
package com.bokmcdok.cat.objects.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class PeacemakerButterfly extends Monster {

    public static final String NAME = "peacemaker_butterfly";


    /**
     * Defines the spawn rules for butterflies: they can spawn anywhere the
     * light level is above 8.
     * @param entityType The entity type to spawn.
     * @param level The current level.
     * @param spawnType The type of spawn.
     * @param position The position to spawn in.
     * @param rng The random number generator.
     * @return True if the butterfly can spawn.
     */
    public static boolean checkSpawnRules(@SuppressWarnings("unused") EntityType<PeacemakerButterfly> entityType,
                                          LevelAccessor level,
                                          @SuppressWarnings("unused") MobSpawnType spawnType,
                                          BlockPos position,
                                          @SuppressWarnings("unused") Random rng) {
        return level.getRawBrightness(position, 0) > 8;
    }

    /**
     * Create attributes for a butterfly.
     * @return Butterflies have only 3 health (1.5 hearts).
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FLYING_SPEED, 0.9D);
    }

    /**
     * Create a peacemaker butterfly entity
     * @param type The entity type
     * @param level The current level
     */
    public PeacemakerButterfly(EntityType<? extends PeacemakerButterfly> type, Level level) {
        super(type, level);

        //  Add a flying move controller
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    /**
     * Butterflies ignore all fall damage.
     * @param fallDistance The distance fell.
     * @param blockModifier The modifier based on the softness of the block landed on.
     * @param damageSource The source of the damage.
     * @return Always false, to indicate no damage was applied.
     */
    @Override
    public boolean causeFallDamage(float fallDistance, float blockModifier, @NotNull DamageSource damageSource) {
        return false;
    }

    /**
     * Butterflies ignore fall damage.
     * @param yPos The current y-position of the butterfly.
     * @param onGround TRUE if the butterfly is on the ground.
     * @param block The block landed on.
     * @param position The position of the block being landed on.
     */
    @Override
    protected void checkFallDamage(double yPos, boolean onGround, @NotNull BlockState block, @NotNull BlockPos position) {
        //  No-op
    }

    /**
     * Create a pathfinder for flying
     * @param level The current level
     * @return A flying navigator
     */
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    /**
     * Register the goals that define the entity's behaviour
     */
    @Override
    protected void registerGoals() {

        //  Movement goals
        this.goalSelector.addGoal(5, new FloatGoal(this));
        //this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, () -> false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomFlyingGoal(this, 1.0D));

        //  Look at goals
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

}

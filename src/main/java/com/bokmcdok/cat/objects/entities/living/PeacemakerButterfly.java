package com.bokmcdok.cat.objects.entities.living;

import com.bokmcdok.cat.lists.EntityList;
import com.bokmcdok.cat.lists.ItemList;
import com.bokmcdok.cat.objects.goals.FlyThroughVillageGoal;
import com.bokmcdok.cat.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

/**
 * A peacemaker butterfly
 */
public class PeacemakerButterfly extends Monster {

    //  The name of the entity, used to register the entity
    public static final String NAME = "peacemaker_butterfly";

    //  "Cave" biomes where the height limit is ignored
    private static final Biome.BiomeCategory[] CAVE_BIOMES = {
            Biome.BiomeCategory.MOUNTAIN,
            Biome.BiomeCategory.EXTREME_HILLS,
            Biome.BiomeCategory.UNDERGROUND
    };

    /**
     * Defines the spawn rules for peacemaker butterflies: they generally spawn
     * underground or in mountain caves
     *
     * @param entityType The entity type to spawn.
     * @param level      The current level.
     * @param spawnType  The type of spawn.
     * @param position   The position to spawn in.
     * @param rng        The random number generator.
     * @return True if the butterfly can spawn.
     */
    @SuppressWarnings("deprecation")
    public static boolean checkSpawnRules(EntityType<PeacemakerButterfly> entityType,
                                          LevelAccessor level,
                                          MobSpawnType spawnType,
                                          BlockPos position,
                                          Random rng) {
        Biome.BiomeCategory biomeCategory = Biome.getBiomeCategory(level.getBiome(position));
        if (!Arrays.asList(CAVE_BIOMES).contains(biomeCategory)) {
            if (position.getY() > level.getSeaLevel()) {
                return false;
            }
        }

        int i = level.getMaxLocalRawBrightness(position);
        int j = 4;

        if (rng.nextBoolean()) {
            return false;
        }

        return i <= rng.nextInt(j) && checkMobSpawnRules(entityType, level, spawnType, position, rng);
    }

    /**
     * Create attributes for a butterfly.
     *
     * @return Butterflies have only 3 health (1.5 hearts).
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FLYING_SPEED, 0.9D);
    }

    /**
     * Convert an illager to one with a butterfly host
     * @param level   The current level
     * @param illager The illager to convert
     */
    public static void possess(ServerLevel level,
                               AbstractIllager illager) {
        Difficulty difficulty = level.getDifficulty();
        if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
            if (difficulty != Difficulty.HARD && illager.getRandom().nextBoolean()) {
                return;
            }

            if (illager instanceof Evoker) {
                possess(level, illager, EntityList.PEACEMAKER_EVOKER.get());
            } else if (illager instanceof Illusioner) {
                possess(level, illager, EntityList.PEACEMAKER_ILLUSIONER.get());
            } else if (illager instanceof Pillager) {
                possess(level, illager, EntityList.PEACEMAKER_PILLAGER.get());
            } else if (illager instanceof Vindicator) {
                possess(level, illager, EntityList.PEACEMAKER_VINDICATOR.get());
            }
        }
    }

    /**
     * Convert a villager to one with a butterfly host
     * @param level The current level
     * @param villager The villager to convert
     */
    public static void possess(ServerLevel level,
                               Villager villager) {
        Difficulty difficulty = level.getDifficulty();
        if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
            if (difficulty != Difficulty.HARD && villager.getRandom().nextBoolean()) {
                return;
            }

            if (ForgeEventFactory.canLivingConvert(villager, EntityList.PEACEMAKER_VILLAGER.get(), (x) -> {
            })) {
                PeacemakerVillager peacemakerVillager = villager.convertTo(EntityList.PEACEMAKER_VILLAGER.get(), false);
                if (peacemakerVillager != null) {
                    peacemakerVillager.setVillagerData(villager.getVillagerData());
                    peacemakerVillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE).getValue());
                    peacemakerVillager.setOffers(villager.getOffers());
                    peacemakerVillager.setVillagerXp(villager.getVillagerXp());
                    peacemakerVillager.finalizeSpawn(level,
                            level.getCurrentDifficultyAt(peacemakerVillager.blockPosition()),
                            MobSpawnType.CONVERSION,
                            null,
                            null);

                    peacemakerVillager.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));

                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert(villager, peacemakerVillager);

                    if (!peacemakerVillager.isSilent()) {
                        level.levelEvent(null, 1026, peacemakerVillager.blockPosition(), 0);
                    }
                }
            }
        }
    }

    /**
     * Respawns a peacemaker butterfly after its host has died
     * @param entity The host entity
     */
    public static void respawn(LivingEntity entity) {
        if (!entity.level.isClientSide()) {
            PeacemakerButterfly butterfly = EntityList.PEACEMAKER_BUTTERFLY.get().create(entity.level);
            if (butterfly != null) {
                butterfly.copyPosition(entity);
                butterfly.finalizeSpawn((ServerLevel) entity.level,
                        butterfly.level.getCurrentDifficultyAt(butterfly.getOnPos()),
                        MobSpawnType.CONVERSION,
                        null,
                        null);
                entity.level.addFreshEntity(butterfly);
            }
        }
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
     * Convert villagers and pillagers to Peacemaker mobs
     * @param level The current level
     * @param victim The entity just "killed"
     */
    @Override
    public void killed(@NotNull ServerLevel level,
                       @NotNull LivingEntity victim) {
        super.killed(level, victim);

        if (victim instanceof Villager villager) {
            possess(level, villager);
            if (!this.isSilent()) {
                level.levelEvent(null, 1027, this.blockPosition(), 0);
            }

            this.remove(RemovalReason.DISCARDED);
        }

        if (victim instanceof AbstractIllager illager) {
            possess(level, illager);

            this.remove(RemovalReason.DISCARDED);
        }
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
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(6, new FlyThroughVillageGoal(this, 1.0D, false, 4, () -> false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomFlyingGoal(this, 1.0D));

        //  Look at goals
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        //  Attack goals
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));

        //  Tempt goals
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.25D, Ingredient.of(ItemList.PEACEMAKER_HONEY_BOTTLE.get()), false));

        //  Targets
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this))
                .setAlertOthers(PeacemakerButterfly.class)
                .setAlertOthers(PeacemakerEvoker.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, false,
                EntityUtil::isNotPeacemakerTarget));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false,
                EntityUtil::isNotPeacemakerTarget));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    /**
     * Convert an illager to one with a butterfly host
     * @param level   The current level
     * @param illager The illager to convert
     * @param type    The entity type
     * @param <T>     The entity class
     */
    private static <T extends Mob> void possess(ServerLevel level,
                                                AbstractIllager illager,
                                                EntityType<T> type) {
        if (ForgeEventFactory.canLivingConvert(illager, type, (x) -> {
        })) {
            T newMob = illager.convertTo(type, false);
            if (newMob != null) {
                newMob.finalizeSpawn(level,
                        level.getCurrentDifficultyAt(newMob.blockPosition()),
                        MobSpawnType.CONVERSION,
                        null,
                        null);
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(illager, newMob);

                if (!newMob.isSilent()) {
                    level.levelEvent(null, 1026, newMob.blockPosition(), 0);
                }
            }
        }
    }
}

package com.bokmcdok.cat.objects.entities;

import com.bokmcdok.cat.objects.renderers.ButterflyRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Butterflies that fly around the world to add some ambience.
 */
public class Butterfly extends AmbientCreature {

    //  This is the name we will use to reference the Butterfly
    public static final String NAME = "butterfly";

    //  Holds the variant of the butterfly, which defines the texture used to
    //  render it.
    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.INT);

    //  The number of ticks per flap. Used for event emissions.
    private static final int TICKS_PER_FLAP = Mth.ceil(2.4166098f);

    //  The name of the variant attribute in the save data.
    private static final String VARIANT = "butterflyVariant";

    //  The current position the butterfly is flying toward.
    @Nullable
    private BlockPos targetPosition;

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
    public static boolean checkButterflySpawnRules(@SuppressWarnings("unused") EntityType<Butterfly> entityType,
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3d);
    }

    /**
     * Create an instance of a butterfly.
     * @param entityType The type of entity.
     * @param level The current level.
     */
    public Butterfly(EntityType<? extends Butterfly> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Save the variant of the butterfly.
     * @param tag The tag where the save data is stored.
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(VARIANT, this.entityData.get(DATA_VARIANT));
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
     * Set the variant of this butterfly before spawning
     * @param level The current level
     * @param difficulty The difficulty setting
     * @param spawnType The spawn type
     * @param groupData The group being spawned with
     * @param tag The tag data
     * @return Updated group data
     */
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level,
                                        @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType spawnType,
                                        @Nullable SpawnGroupData groupData,
                                        @Nullable CompoundTag tag) {
        setVariant(this.random.nextInt(ButterflyRenderer.TEXTURE.length));
        return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    }

    /**
     * Get the variant of butterfly.
     * @return The index of the texture to use for the butterfly.
     */
    public int getVariant() {
        return this.entityData.get(DATA_VARIANT);
    }

    /**
     * Should a flapping event be emitted?
     * @return TRUE if the butterfly has flapped its wings.
     */
    @Override
    public boolean isFlapping() {
        return this.tickCount % TICKS_PER_FLAP == 0;
    }

    /**
     * Butterflies won't trigger pressure plates or tripwires.
     * @return Always false.
     */
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    /**
     * Butterflies are too small to push.
     * @return Always false.
     */
    @Override
    public boolean isPushable() {
        return false;
    }

    /**
     * Read the variant of the butterfly.
     * @param tag The tag containing the save data.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_VARIANT, tag.getInt(VARIANT));
    }

    /**
     * Update the butterfly's movement delta.
     */
    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1d, 0.6d, 1d));
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
     * AI update. Handles the butterfly's erratic movement. Based on the bat's
     * movement code.
     */
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.targetPosition != null &&
                (!this.level.isEmptyBlock(this.targetPosition) ||
                        this.targetPosition.getY() <= this.level.getMinBuildHeight())) {
            this.targetPosition = null;
        }

        if (this.targetPosition == null ||
                this.random.nextInt(30) == 0 ||
                this.targetPosition.closerToCenterThan(this.position(), 2.0D)) {
            this.targetPosition = new BlockPos(this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7), this.getY() + (double)this.random.nextInt(6) - 2.0D, this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
        }

        double d2 = (double)this.targetPosition.getX() + 0.5D - this.getX();
        double d0 = (double)this.targetPosition.getY() + 0.1D - this.getY();
        double d1 = (double)this.targetPosition.getZ() + 0.5D - this.getZ();
        Vec3 vec3 = this.getDeltaMovement();
        Vec3 vec31 = vec3.add((Math.signum(d2) * 0.5D - vec3.x) * (double)0.1F,
                (Math.signum(d0) * (double)0.7F - vec3.y) * (double)0.1F,
                (Math.signum(d1) * 0.5D - vec3.z) * (double)0.1F);
        this.setDeltaMovement(vec31);
        float f = (float)(Mth.atan2(vec31.z, vec31.x) * (double)(180F / (float)Math.PI)) - 90.0F;
        float f1 = Mth.wrapDegrees(f - this.getYRot());
        this.zza = 0.5F;
        this.setYRot(this.getYRot() + f1);
    }

    /**
     * Sync the variant of the butterfly, so it looks the same in networked
     * games.
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
    }

    /**
     * Butterflies don't collide with other entities.
     * @param collidingEntity The entity the butterfly would have collided with.
     */
    @Override
    protected void doPush(@NotNull Entity collidingEntity) {
        //  No-op
    }

    /**
     * Butterfly movement will send events but no sounds.
     * @return Event emissions only.
     */
    @Override
    protected @NotNull MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    /**
     * Butterflies are silent.
     * @return Always zero.
     */
    @Override
    protected float getSoundVolume() {
        return 0f;
    }

    /**
     * Get the height of the butterfly's eyes.
     * @param pose The current pose of the butterfly.
     * @param dimensions The dimensions of the butterfly.
     * @return the height of the butterfly's eyes.
     */
    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions dimensions) {
        return dimensions.height / 2f;
    }

    /**
     * Butterflies cannot push other entities.
     */
    @Override
    protected void pushEntities() {
        //  No-op
    }

    /**
     * Set the variant of this butterfly.
     * @param variant The variant of this butterfly.
     */
    private void setVariant(int variant) {
        entityData.set(DATA_VARIANT, variant);
    }
}

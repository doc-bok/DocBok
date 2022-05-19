package com.bokmcdok.cat.objects.entities;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.lists.ParticleList;
import com.bokmcdok.cat.lists.SoundEventList;
import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Nyan Cat in Minecraft!
 */
public class NyanCat extends Cat {

    //  This is the name we will use to reference the Nyan Cat
    public static final String NAME = "nyan_cat";

    //  This is the Nyan Cat's texture, based on the default cat textures.
    private static final ResourceLocation NYAN_TEXTURE =
            new ResourceLocation(CatMod.MOD_ID, "textures/entity/cat/nyan.png");

    //  The target position for Nyan Cat's movement.
    private BlockPos targetPosition;

    //  The direction that Nyan Cat is looking.
    private final Vector3d lookVector = new Vector3d(0, 0, 0);

    /**
     * Construction
     * @param type The Entity Type for the cat.
     * @param world The current world.
     */
    public NyanCat(EntityType<? extends NyanCat> type, Level world) {
        super(type, world);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    /**
     * Play the Nyan Cat music when one is spawned in.
     * @param world The current world.
     * @param difficulty The current difficulty.
     * @param reason The reason the Nyan Cat was spawned in.
     * @param spawnData The data for this spawn.
     * @param dataTag Any NBT tags to apply to this instance.
     * @return The entity's data.
     */
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world,
                                        @Nonnull DifficultyInstance difficulty,
                                        @Nonnull MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {

        if (reason == MobSpawnType.CONVERSION) {
            playSound(SoundEventList.NYAN_CAT_MUSIC.get(), 0.5f, 1.0f);
        }

        return super.finalizeSpawn(world, difficulty, reason, spawnData , dataTag);
    }

    /**
     * Spawn rainbow particles for the Nyan Cat!
     */
    @Override
    public void aiStep() {
        if (level.isClientSide()) {
            updateLookVector();

            addRainbowParticle(ParticleList.RED_RAINBOW_PARTICLE, 0.7f);
            addRainbowParticle(ParticleList.ORANGE_RAINBOW_PARTICLE, 0.6f);
            addRainbowParticle(ParticleList.YELLOW_RAINBOW_PARTICLE, 0.5f);
            addRainbowParticle(ParticleList.GREEN_RAINBOW_PARTICLE, 0.4f);
            addRainbowParticle(ParticleList.BLUE_RAINBOW_PARTICLE, 0.3f);
            addRainbowParticle(ParticleList.INDIGO_RAINBOW_PARTICLE, 0.2f);
            addRainbowParticle(ParticleList.VIOLET_RAINBOW_PARTICLE, 0.1f);
        }

        super.aiStep();
    }

    /**
     * Handles the movement of the Nyan Cat - making it dance around like the
     * actual Nyan Cat.
     */
    @Override
    public void customServerAiStep() {
        if (targetPosition != null &&
                (!level.isEmptyBlock(targetPosition) || targetPosition.getY() <= level.getMinBuildHeight())) {
            targetPosition = null;
        }

        if (targetPosition == null || random.nextInt(30) == 0 || targetPosition.closerThan(blockPosition(), 2.0D)) {
            targetPosition = new BlockPos(
                    getX() + (double)random.nextInt(7) - (double)random.nextInt(7),
                    getY() + (double)random.nextInt(6) - 2.0D,
                    getZ() + (double)random.nextInt(7) - (double)random.nextInt(7));
        }

        double d2 = (double)targetPosition.getX() + 0.5D - getX();
        double d0 = (double)targetPosition.getY() + 0.1D - getY();
        double d1 = (double)targetPosition.getZ() + 0.5D - getZ();
        Vec3 vec3 = getDeltaMovement();
        Vec3 vec31 = vec3.add(
                (Math.signum(d2) * 0.5D - vec3.x) * (double)0.1F,
                (Math.signum(d0) * (double)0.7F - vec3.y) * (double)0.1F,
                (Math.signum(d1) * 0.5D - vec3.z) * (double)0.1F);
        setDeltaMovement(vec31);
        float f = (float)(Mth.atan2(vec31.z, vec31.x) * (double)(180F / (float)Math.PI)) - 90.0F;
        float f1 = Mth.wrapDegrees(f - getYRot());
        zza = 0.5F;
        setYRot(getYRot() + f1);
    }

    /**
     * Add a rainbow particle.
     * @param particle The type of particle to add.
     * @param yOffset The y-offset of the particle.
     */
    private void addRainbowParticle(RegistryObject<SimpleParticleType> particle, float yOffset) {
        level.addParticle(particle.get(),
                xOld - (lookVector.x * 0.5),
                yOld + yOffset,
                zOld - (lookVector.z * 0.5),
                (getX() - xOld - lookVector.x) * 50f,
                0f,
                (getZ() - zOld - lookVector.z) * 50f);
    }

    /**
     * Used by the renderer class.
     * @return The Resource Location of Nyan Cat's texture.
     */
    @Override
    @Nonnull
    public ResourceLocation getResourceLocation() {
        return NYAN_TEXTURE;
    }

    /**
     * Nyan Cat is faster than a normal cat!
     * @return The attributes for Nyan Cat.
     */
    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.75f)
                .add(Attributes.MAX_HEALTH, 20.0d)
                .add(Attributes.ATTACK_DAMAGE, 5.0d)
                .add(Attributes.FLYING_SPEED, 0.6d);
    }

    /**
     * Helper function to convert degrees to radians
     * @param angle The angle in degrees
     * @return The angle in radians
     */
    private double rad(float angle) {
        return (double) angle * Math.PI / 180;
    }

    /**
     * Helper to get the look vector of the entity.
     */
    private void updateLookVector() {
        double vx = -Math.sin(rad(getYRot())) * Math.cos(rad(getXRot()));
        double vz = Math.cos(rad(getYRot())) * Math.cos(rad(getXRot()));
        double vy = -Math.sin(rad(getYRot()));
        lookVector.set(vx, vy, vz);
    }
}

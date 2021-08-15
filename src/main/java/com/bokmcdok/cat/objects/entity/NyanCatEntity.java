package com.bokmcdok.cat.objects.entity;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.lists.ParticleList;
import com.bokmcdok.cat.lists.SoundEventList;
import com.mojang.math.Vector3d;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Nyan Cat in Minecraft!
 */
public class NyanCatEntity extends Cat {

    //  This is the name we will use to reference the Nyan Cat
    public static final String NAME = "nyan_cat";

    //  This is the Nyan Cat's texture, based on the default cat textures.
    private static final ResourceLocation NYAN_TEXTURE =
            new ResourceLocation(CatMod.MOD_ID, "textures/entity/cat/nyan.png");

    /**
     * Construction
     * @param type The Entity Type for the cat.
     * @param world The current world.
     */
    public NyanCatEntity(EntityType<? extends NyanCatEntity> type, Level world) {
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
            Vector3d lookVector = lookVector();
            level.addParticle(ParticleList.RED_RAINBOW_PARTICLE.get(),
                    xOld - (lookVector.x * 0.5),
                    yOld + 0.7f,
                    zOld - (lookVector.z * 0.5),
                    (getX() - xOld - lookVector.x) * 50f,
                    0f,
                    (getZ() - zOld - lookVector.z) * 50f);

            level.addParticle(ParticleList.ORANGE_RAINBOW_PARTICLE.get(),
                    xOld - (lookVector.x * 0.5),
                    yOld + 0.6f,
                    zOld - (lookVector.z * 0.5),
                    (getX() - xOld - lookVector.x) * 50f,
                    0f,
                    (getZ() - zOld - lookVector.z) * 50f);

            level.addParticle(ParticleList.YELLOW_RAINBOW_PARTICLE.get(),
                    xOld - (lookVector.x * 0.5),
                    yOld + 0.5f,
                    zOld - (lookVector.z * 0.5),
                    (getX() - xOld - lookVector.x) * 50f,
                    0f,
                    (getZ() - zOld - lookVector.z) * 50f);

            level.addParticle(ParticleList.GREEN_RAINBOW_PARTICLE.get(),
                    xOld - (lookVector.x * 0.5),
                    yOld + 0.4f,
                    zOld - (lookVector.z * 0.5),
                    (getX() - xOld - lookVector.x) * 50f,
                    0f,
                    (getZ() - zOld - lookVector.z) * 50f);

            level.addParticle(ParticleList.BLUE_RAINBOW_PARTICLE.get(),
                    xOld - (lookVector.x * 0.5),
                    yOld + 0.3f,
                    zOld - (lookVector.z * 0.5),
                    (getX() - xOld - lookVector.x) * 50f,
                    0f,
                    (getZ() - zOld - lookVector.z) * 50f);

            level.addParticle(ParticleList.INDIGO_RAINBOW_PARTICLE.get(),
                    xOld - (lookVector.x * 0.5),
                    yOld + 0.2f,
                    zOld - (lookVector.z * 0.5),
                    (getX() - xOld - lookVector.x) * 50f,
                    0f,
                    (getZ() - zOld - lookVector.z) * 50f);

            level.addParticle(ParticleList.VIOLET_RAINBOW_PARTICLE.get(),
                    xOld - (lookVector.x * 0.5),
                    yOld + 0.1f,
                    zOld - (lookVector.z * 0.5),
                    (getX() - xOld - lookVector.x) * 50f,
                    0f,
                    (getZ() - zOld - lookVector.z) * 50f);
        }

        super.aiStep();
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
    public static AttributeSupplier.Builder setCustomAttributes() {
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
     * Helper to get the look vector of the entity
     * @return The look vector of the entity
     */
    private Vector3d lookVector() {
        float vx = (float) (-Math.sin(rad(getYRot())) * Math.cos(rad(getXRot())));
        float vz = (float) (Math.cos(rad(getYRot())) * Math.cos(rad(getXRot())));
        float vy = (float) -Math.sin(rad(getYRot()));
        return new Vector3d(vx, vy, vz);
    }
}

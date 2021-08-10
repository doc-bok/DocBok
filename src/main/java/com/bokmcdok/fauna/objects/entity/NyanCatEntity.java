package com.bokmcdok.fauna.objects.entity;

import com.bokmcdok.fauna.FaunaMod;
import com.bokmcdok.fauna.lists.AttributeList;
import com.bokmcdok.fauna.lists.ParticleList;
import com.bokmcdok.fauna.lists.SoundEventList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Nyan Cat in Minecraft!
 */
public class NyanCatEntity extends CatEntity {

    //  This is the name we will use to reference the Nyan Cat
    public static final String NAME = "nyan_cat";

    //  This is the Nyan Cat's texture, based on the default cat textures.
    private static final ResourceLocation NYAN_TEXTURE =
            new ResourceLocation(FaunaMod.MOD_ID, "textures/entity/cat/nyan.png");

    /**
     * Construction
     * @param type The Entity Type for the cat.
     * @param world The current world.
     */
    public NyanCatEntity(EntityType<? extends NyanCatEntity> type, World world) {
        super(type, world);
        moveController = new FlyingMovementController(this, 20, true);
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
    public ILivingEntityData onInitialSpawn(@Nonnull IWorld world,
                                            @Nonnull DifficultyInstance difficulty,
                                            @Nonnull SpawnReason reason,
                                            @Nullable ILivingEntityData spawnData,
                                            @Nullable CompoundNBT dataTag) {

        if (reason == SpawnReason.CONVERSION) {
            playSound(SoundEventList.NYAN_CAT_MUSIC.get(), 0.5f, 1.0f);
        }

        return super.onInitialSpawn(world, difficulty, reason, spawnData , dataTag);
    }

    /**
     * Spawn rainbow particles for the Nyan Cat!
     */
    @Override
    public void livingTick() {
        if (world.isRemote) {
            Vector3d lookVector = lookVector();
            world.addParticle(ParticleList.RED_RAINBOW_PARTICLE.get(),
                    lastTickPosX - (lookVector.x * 0.5),
                    lastTickPosY + 0.7f,
                    lastTickPosZ - (lookVector.z * 0.5),
                    (getPosX() - lastTickPosX - lookVector.x) * 50f,
                    0f,
                    (getPosZ() - lastTickPosZ - lookVector.z) * 50f);

            world.addParticle(ParticleList.ORANGE_RAINBOW_PARTICLE.get(),
                    lastTickPosX - (lookVector.x * 0.5),
                    lastTickPosY + 0.6f,
                    lastTickPosZ - (lookVector.z * 0.5),
                    (getPosX() - lastTickPosX - lookVector.x) * 50f,
                    0f,
                    (getPosZ() - lastTickPosZ - lookVector.z) * 50f);

            world.addParticle(ParticleList.YELLOW_RAINBOW_PARTICLE.get(),
                    lastTickPosX - (lookVector.x * 0.5),
                    lastTickPosY + 0.5f,
                    lastTickPosZ - (lookVector.z * 0.5),
                    (getPosX() - lastTickPosX - lookVector.x) * 50f,
                    0f,
                    (getPosZ() - lastTickPosZ - lookVector.z) * 50f);

            world.addParticle(ParticleList.GREEN_RAINBOW_PARTICLE.get(),
                    lastTickPosX - (lookVector.x * 0.5),
                    lastTickPosY + 0.4f,
                    lastTickPosZ - (lookVector.z * 0.5),
                    (getPosX() - lastTickPosX - lookVector.x) * 50f,
                    0f,
                    (getPosZ() - lastTickPosZ - lookVector.z) * 50f);

            world.addParticle(ParticleList.BLUE_RAINBOW_PARTICLE.get(),
                    lastTickPosX - (lookVector.x * 0.5),
                    lastTickPosY + 0.3f,
                    lastTickPosZ - (lookVector.z * 0.5),
                    (getPosX() - lastTickPosX - lookVector.x) * 50f,
                    0f,
                    (getPosZ() - lastTickPosZ - lookVector.z) * 50f);

            world.addParticle(ParticleList.INDIGO_RAINBOW_PARTICLE.get(),
                    lastTickPosX - (lookVector.x * 0.5),
                    lastTickPosY + 0.2f,
                    lastTickPosZ - (lookVector.z * 0.5),
                    (getPosX() - lastTickPosX - lookVector.x) * 50f,
                    0f,
                    (getPosZ() - lastTickPosZ - lookVector.z) * 50f);

            world.addParticle(ParticleList.VIOLET_RAINBOW_PARTICLE.get(),
                    lastTickPosX - (lookVector.x * 0.5),
                    lastTickPosY + 0.1f,
                    lastTickPosZ - (lookVector.z * 0.5),
                    (getPosX() - lastTickPosX - lookVector.x) * 50f,
                    0f,
                    (getPosZ() - lastTickPosZ - lookVector.z) * 50f);
        }

        super.livingTick();
    }

    /**
     * Used by the renderer class.
     * @return The Resource Location of Nyan Cat's texture.
     */
    @Override
    @Nonnull
    public ResourceLocation getCatTypeName() {
        return NYAN_TEXTURE;
    }

    /**
     * Nyan Cat is faster than a normal cat!
     * @return The attributes for Nyan Cat.
     */
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .func_233815_a_(AttributeList.MOVEMENT_SPEED, 0.75f)
                .func_233815_a_(AttributeList.MAX_HEALTH, 20.0d)
                .func_233815_a_(AttributeList.ATTACK_DAMAGE, 5.0d)
                .func_233815_a_(AttributeList.FLYING_SPEED, 0.6d);
    }

    /**
     * Helper function to convert degrees to radians
     * @param angle The angle in degrees
     * @return The angle in radians
     */
    private float rad(float angle) {
        return angle * (float) Math.PI / 180;
    }

    /**
     * Helper to get the look vector of the entity
     * @return The look vector of the entity
     */
    private Vector3d lookVector() {
        float vx = -MathHelper.sin(rad(rotationYaw)) * MathHelper.cos(rad(rotationPitch));
        float vz = MathHelper.cos(rad(rotationYaw)) * MathHelper.cos(rad(rotationPitch));
        float vy = -MathHelper.sin(rad(rotationPitch));
        return new Vector3d(vx, vy, vz);
    }
}

package com.bokmcdok.fauna.objects.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RainbowParticle extends SpriteTexturedParticle {

    /**
     * Construction
     * @param world The current world
     * @param y The position to spawn the particle
     * @param z The position to spawn the particle
     * @param x The position to spawn the particle
     * @param vX The velocity of the particle
     * @param vY The velocity of the particle
     * @param vZ The velocity of the particle
     */
    protected RainbowParticle(ClientWorld world,
                              double x, double y, double z,
                              double vX, double vY, double vZ) {
        super(world, x, y, z, vX, vY, vZ);

        setSize(0.02f, 0.02f);
        particleScale *= this.rand.nextFloat() * 1.1F;
        motionX *= 0.02f;
        motionY *= 0.02f;
        motionZ *= 0.02f;
        maxAge = 80;
    }

    /**
     * Get the render type
     * @return How we will render our particle
     */
    @Override
    @Nonnull
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    /**
     * Animate/expire the particles
     */
    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (age++ >= maxAge) {
            setExpired();
        } else {
            move(motionX, motionY, motionZ);
        }
    }

    /**
     * Factory to create a rainbow particle.
     */
    @OnlyIn(Dist.CLIENT)
    public static abstract class Factory implements IParticleFactory<BasicParticleType> {

        //  The sprite set to use for the particle.
        private final IAnimatedSprite spriteSet;

        protected float r;
        protected float g;
        protected float b;

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public Factory(IAnimatedSprite sprite) {
            spriteSet = sprite;
            r = 1;
            g = 1;
            b = 1;
        }

        /**
         * Create a rainbow particle
         * @param type The type of particle
         * @param world The current world
         * @param x The position of the particle
         * @param y The position of the particle
         * @param z The position of the particle
         * @param xSpeed The speed of the particle
         * @param ySpeed The speed of the particle
         * @param zSpeed The speed of the particle
         * @return The new particle instance
         */
        @Nullable
        @Override
        public Particle makeParticle(@Nonnull BasicParticleType type,
                                     @Nonnull ClientWorld world,
                                     double x, double y, double z,
                                     double xSpeed, double ySpeed, double zSpeed) {
            RainbowParticle particle= new RainbowParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.selectSpriteRandomly(spriteSet);
            particle.setColor(r, g, b);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class RedFactory extends Factory {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public RedFactory(IAnimatedSprite sprite) {
            super(sprite);
            r = 1;
            g = 0;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class OrangeFactory extends Factory {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public OrangeFactory(IAnimatedSprite sprite) {
            super(sprite);
            r = 1;
            g = 0.5f;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class YellowFactory extends Factory {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public YellowFactory(IAnimatedSprite sprite) {
            super(sprite);
            r = 1;
            g = 1;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class GreenFactory extends Factory {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public GreenFactory(IAnimatedSprite sprite) {
            super(sprite);
            r = 0;
            g = 1;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class BlueFactory extends Factory {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public BlueFactory(IAnimatedSprite sprite) {
            super(sprite);
            r = 0;
            g = 0;
            b = 1;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class IndigoFactory extends Factory {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public IndigoFactory(IAnimatedSprite sprite) {
            super(sprite);
            r = 0.25f;
            g = 0;
            b = 0.5f;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class VioletFactory extends Factory {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public VioletFactory(IAnimatedSprite sprite) {
            super(sprite);
            r = 1;
            g = 0.5f;
            b = 1;
        }
    }
}

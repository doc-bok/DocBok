package com.bokmcdok.cat.objects.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class RainbowParticle extends TextureSheetParticle {

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
    protected RainbowParticle(ClientLevel world,
                              double x, double y, double z,
                              double vX, double vY, double vZ) {
        super(world, x, y, z, vX, vY, vZ);

        setSize(0.02f, 0.02f);
        quadSize *= random.nextFloat() * 1.1F;
        xd *= 0.02f;
        yd *= 0.02f;
        zd *= 0.02f;
        lifetime = 80;
    }

    /**
     * Get the render type
     * @return How we will render our particle
     */
    @Override
    @Nonnull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    /**
     * Animate/expire the particles
     */
    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime) {
            remove();
        } else {
            move(xd, yd, zd);
        }
    }

    /**
     * Factory to create a rainbow particle.
     */
    @OnlyIn(Dist.CLIENT)
    public static abstract class Provider implements ParticleProvider<SimpleParticleType> {

        //  The sprite set to use for the particle.
        private final SpriteSet spriteSet;

        protected float r;
        protected float g;
        protected float b;

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public Provider(SpriteSet sprite) {
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
        public Particle createParticle(@Nonnull SimpleParticleType type,
                                     @Nonnull ClientLevel world,
                                     double x, double y, double z,
                                     double xSpeed, double ySpeed, double zSpeed) {
            RainbowParticle particle= new RainbowParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(spriteSet);
            particle.setColor(r, g, b);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class RedFactory extends Provider {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public RedFactory(SpriteSet sprite) {
            super(sprite);
            r = 1;
            g = 0;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class OrangeFactory extends Provider {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public OrangeFactory(SpriteSet sprite) {
            super(sprite);
            r = 1;
            g = 0.5f;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class YellowFactory extends Provider {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public YellowFactory(SpriteSet sprite) {
            super(sprite);
            r = 1;
            g = 1;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class GreenFactory extends Provider {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public GreenFactory(SpriteSet sprite) {
            super(sprite);
            r = 0;
            g = 1;
            b = 0;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class BlueFactory extends Provider {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public BlueFactory(SpriteSet sprite) {
            super(sprite);
            r = 0;
            g = 0;
            b = 1;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class IndigoFactory extends Provider {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public IndigoFactory(SpriteSet sprite) {
            super(sprite);
            r = 0.25f;
            g = 0;
            b = 0.5f;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class VioletFactory extends Provider {

        /**
         * Construction
         * @param sprite The sprite set to use
         */
        public VioletFactory(SpriteSet sprite) {
            super(sprite);
            r = 1;
            g = 0.5f;
            b = 1;
        }
    }
}

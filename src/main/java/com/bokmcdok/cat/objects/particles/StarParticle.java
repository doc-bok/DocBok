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
import org.jetbrains.annotations.NotNull;

/**
 * A star particle for the Nyan Cat
 */
@OnlyIn(Dist.CLIENT)
public class StarParticle extends TextureSheetParticle {

    //  The list of star sprites
    private final SpriteSet sprites;

    //  Flag to help us skip every frame (slower star animation)
    private boolean skip = true;

    /**
     * Create a star particle
     * @param level The current level
     * @param x The position to spawn the particle
     * @param y The position to spawn the particle
     * @param z The position to spawn the particle
     * @param xd The velocity of the particle
     * @param yd The velocity of the particle
     * @param zd The velocity of the particle
     * @param sprites The sprites to use for the particle
     */
    StarParticle(ClientLevel level,
                 double x, double y, double z,
                 double xd, double yd, double zd,
                 SpriteSet sprites) {
        super(level, x, y, z, xd, yd, zd);
        this.sprites = sprites;
        this.lifetime = 6;
        this.gravity = 0.008f;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.setSpriteFromAge(sprites);
    }

    /**
     * Animate the particle
     */
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (!this.skip) {
            this.age++;
        }

        this.skip = !this.skip;

        if (this.age >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.setSpriteFromAge(this.sprites);
        }
    }

    /**
     * Render the particle as a sprite sheet
     * @return The render type
     */
    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    /**
     * Provider for the registry
     * @param sprites
     */
    @OnlyIn(Dist.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        /**
         * Create a new star particle
         * @param type The type of particle
         * @param level The current level
         * @param x The position to create the particle
         * @param y The position to create the particle
         * @param z The position to create the particle
         * @param xd The initial velocity of the particle
         * @param yd The initial velocity of the particle
         * @param zd The initial velocity of the particle
         * @return The newly created particle
         */
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType type,
                                                @NotNull ClientLevel level,
                                                double x, double y, double z,
                                                double xd, double yd, double zd) {
            return new StarParticle(level, x, y, z, xd, yd, zd, this.sprites);
        }
    }
}

package com.bokmcdok.fauna.lists;

import com.bokmcdok.fauna.FaunaMod;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A list of particles used by the mod.
 */
public class ParticleList {

    //  Our registry for particle types.
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, FaunaMod.MOD_ID);

    //  A rainbow particle, used by Nyan Cat!
    public static final RegistryObject<BasicParticleType> RED_RAINBOW_PARTICLE =
            registerBasicParticle("red_rainbow_particle", true);
    public static final RegistryObject<BasicParticleType> ORANGE_RAINBOW_PARTICLE =
            registerBasicParticle("orange_rainbow_particle", true);
    public static final RegistryObject<BasicParticleType> YELLOW_RAINBOW_PARTICLE =
            registerBasicParticle("yellow_rainbow_particle", true);
    public static final RegistryObject<BasicParticleType> GREEN_RAINBOW_PARTICLE =
            registerBasicParticle("green_rainbow_particle", true);
    public static final RegistryObject<BasicParticleType> BLUE_RAINBOW_PARTICLE =
            registerBasicParticle("blue_rainbow_particle", true);
    public static final RegistryObject<BasicParticleType> INDIGO_RAINBOW_PARTICLE =
            registerBasicParticle("indigo_rainbow_particle", true);
    public static final RegistryObject<BasicParticleType> VIOLET_RAINBOW_PARTICLE =
            registerBasicParticle("violet_rainbow_particle", true);

    //  Helper method for registering basic particles
    private static RegistryObject<BasicParticleType> registerBasicParticle(String location, boolean alwaysRender) {
        return PARTICLES.register(location, () -> new BasicParticleType(alwaysRender));
    }
}

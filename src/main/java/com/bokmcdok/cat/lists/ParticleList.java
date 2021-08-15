package com.bokmcdok.cat.lists;

import com.bokmcdok.cat.CatMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A list of particles used by the mod.
 */
public class ParticleList {

    //  Our registry for particle types.
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CatMod.MOD_ID);

    //  A rainbow particle, used by Nyan Cat!
    public static final RegistryObject<SimpleParticleType> RED_RAINBOW_PARTICLE =
            registerBasicParticle("red_rainbow_particle", true);
    public static final RegistryObject<SimpleParticleType> ORANGE_RAINBOW_PARTICLE =
            registerBasicParticle("orange_rainbow_particle", true);
    public static final RegistryObject<SimpleParticleType> YELLOW_RAINBOW_PARTICLE =
            registerBasicParticle("yellow_rainbow_particle", true);
    public static final RegistryObject<SimpleParticleType> GREEN_RAINBOW_PARTICLE =
            registerBasicParticle("green_rainbow_particle", true);
    public static final RegistryObject<SimpleParticleType> BLUE_RAINBOW_PARTICLE =
            registerBasicParticle("blue_rainbow_particle", true);
    public static final RegistryObject<SimpleParticleType> INDIGO_RAINBOW_PARTICLE =
            registerBasicParticle("indigo_rainbow_particle", true);
    public static final RegistryObject<SimpleParticleType> VIOLET_RAINBOW_PARTICLE =
            registerBasicParticle("violet_rainbow_particle", true);

    //  Helper method for registering basic particles
    private static RegistryObject<SimpleParticleType> registerBasicParticle(String location, boolean alwaysRender) {
        return PARTICLES.register(location, () -> new SimpleParticleType(alwaysRender));
    }
}

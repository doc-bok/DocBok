package com.bokmcdok.cat.util;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.lists.ParticleList;
import com.bokmcdok.cat.objects.particles.RainbowParticle;
import com.bokmcdok.cat.objects.particles.StarParticle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registers the particle factories.
 */
@Mod.EventBusSubscriber(modid = CatMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleUtil {

    /**
     * Register particle factories here.
     * @param event The event information.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {

        //  Rainbow particles
        Minecraft.getInstance().particleEngine.register(ParticleList.RED_RAINBOW_PARTICLE.get(),
                RainbowParticle.RedFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleList.ORANGE_RAINBOW_PARTICLE.get(),
                RainbowParticle.OrangeFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleList.YELLOW_RAINBOW_PARTICLE.get(),
                RainbowParticle.YellowFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleList.GREEN_RAINBOW_PARTICLE.get(),
                RainbowParticle.GreenFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleList.BLUE_RAINBOW_PARTICLE.get(),
                RainbowParticle.BlueFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleList.INDIGO_RAINBOW_PARTICLE.get(),
                RainbowParticle.IndigoFactory::new);
        Minecraft.getInstance().particleEngine.register(ParticleList.VIOLET_RAINBOW_PARTICLE.get(),
                RainbowParticle.VioletFactory::new);

        //  Star Particle
        Minecraft.getInstance().particleEngine.register(ParticleList.STAR_PARTICLE.get(),
                StarParticle.Provider::new);
    }
}

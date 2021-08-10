package com.bokmcdok.fauna.util;

import com.bokmcdok.fauna.FaunaMod;
import com.bokmcdok.fauna.lists.ParticleList;
import com.bokmcdok.fauna.objects.particles.RainbowParticle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Registers the particle factories.
 */
@Mod.EventBusSubscriber(modid = FaunaMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleUtil {

    /**
     * Register particle factories here.
     * @param event The event information.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particles.registerFactory(ParticleList.RED_RAINBOW_PARTICLE.get(),
                RainbowParticle.RedFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleList.ORANGE_RAINBOW_PARTICLE.get(),
                RainbowParticle.OrangeFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleList.YELLOW_RAINBOW_PARTICLE.get(),
                RainbowParticle.YellowFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleList.GREEN_RAINBOW_PARTICLE.get(),
                RainbowParticle.GreenFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleList.BLUE_RAINBOW_PARTICLE.get(),
                RainbowParticle.BlueFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleList.INDIGO_RAINBOW_PARTICLE.get(),
                RainbowParticle.IndigoFactory::new);
        Minecraft.getInstance().particles.registerFactory(ParticleList.VIOLET_RAINBOW_PARTICLE.get(),
                RainbowParticle.VioletFactory::new);
    }
}

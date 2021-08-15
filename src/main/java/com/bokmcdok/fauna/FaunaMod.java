package com.bokmcdok.fauna;

import com.bokmcdok.fauna.lists.ParticleList;
import com.bokmcdok.fauna.event_listener.CatEventListener;
import com.bokmcdok.fauna.lists.EntityList;
import com.bokmcdok.fauna.lists.SoundEventList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The base class for the Bamboo Mod.
 */
@Mod(FaunaMod.MOD_ID)
public class FaunaMod
{
    //  The Mod ID.
    public static final String MOD_ID = "fauna";

    /**
     * Register the event listeners for the mod.
     */
    public FaunaMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //  Event listeners
        new CatEventListener();

        // Deferred registries.
        EntityList.ENTITIES.register(modEventBus);
        ParticleList.PARTICLES.register(modEventBus);
        SoundEventList.SOUND_EVENTS.register(modEventBus);
    }
}

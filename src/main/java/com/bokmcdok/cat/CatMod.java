package com.bokmcdok.cat;

import com.bokmcdok.cat.event_listeners.EntityEventListener;
import com.bokmcdok.cat.lists.BlockEntityList;
import com.bokmcdok.cat.lists.BlockList;
import com.bokmcdok.cat.lists.ItemList;
import com.bokmcdok.cat.lists.ParticleList;
import com.bokmcdok.cat.event_listeners.CatEventListener;
import com.bokmcdok.cat.lists.EntityList;
import com.bokmcdok.cat.lists.SoundEventList;
import com.bokmcdok.cat.lists.TagList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The base class for the Bamboo Mod.
 */
@Mod(CatMod.MOD_ID)
public class CatMod
{
    //  The Mod ID.
    public static final String MOD_ID = "cat";

    /**
     * Register the event listeners for the mod.
     */
    public CatMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //  Event listeners
        new EntityEventListener();
        new CatEventListener();

        // Lists
        new TagList();

        // Deferred registries.
        BlockList.BLOCKS.register(modEventBus);
        BlockEntityList.BLOCK_ENTITIES.register(modEventBus);
        EntityList.ENTITIES.register(modEventBus);
        ItemList.ITEMS.register(modEventBus);
        ParticleList.PARTICLES.register(modEventBus);
        SoundEventList.SOUND_EVENTS.register(modEventBus);
    }
}

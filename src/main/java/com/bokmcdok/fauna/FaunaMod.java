package com.bokmcdok.fauna;

import com.bokmcdok.fauna.lists.ParticleList;
import com.bokmcdok.fauna.event_listener.CatEventListener;
import com.bokmcdok.fauna.lists.EntityList;
import com.bokmcdok.fauna.lists.SoundEventList;
import com.bokmcdok.fauna.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

/**
 * The base class for the Bamboo Mod.
 */
@Mod(FaunaMod.MOD_ID)
public class FaunaMod
{
    //  The Mod ID.
    public static final String MOD_ID = "fauna";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Register the event listeners for the mod.
     */
    public FaunaMod() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        modEventBus.addListener(this::setup);

        // Register the enqueueIMC method for modloading
        modEventBus.addListener(this::enqueueIMC);

        // Register the processIMC method for modloading
        modEventBus.addListener(this::processIMC);

        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //  Event listeners
        new CatEventListener();

        // Deferred registries.
        EntityList.ENTITIES.register(modEventBus);
        ParticleList.PARTICLES.register(modEventBus);
        SoundEventList.SOUND_EVENTS.register(modEventBus);
    }

    /**
     * Mod Setup event
     * @param event The event information.
     */
    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        //  Register attributes
        DeferredWorkQueue.runLater(EntityUtil::registerEntityAttributes);
    }

    /**
     * Setup the client side code.
     * @param event The event information.
     */
    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    /**
     * Send messages to other mods.
     * @param event The event information.
     */
    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("data", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    /**
     * Receive messages from other mods.
     * @param event The event information.
     */
    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    /**
     * You can use SubscribeEvent and let the Event Bus discover methods to call
     * @param event The event information
     */
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    /**
     * You can use EventBusSubscriber to automatically subscribe events on the
     * contained class (this is subscribing to the MOD Event bus for receiving
     * Registry Events)
     */
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid=MOD_ID)
    public static class RegistryEvents {

        /**
         * Register Blocks here.
         * @param blockRegistryEvent The event information.
         */
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}

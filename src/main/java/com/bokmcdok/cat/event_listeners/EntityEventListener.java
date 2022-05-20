package com.bokmcdok.cat.event_listeners;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Class to help with overriding the default behaviour of vanilla entities.
 */
public class EntityEventListener {

    //  The type of entity we want to override behaviour for.
    private final EntityType<?> mEntityType;

    /**
     * Construction.
     * @param entityType The entity type we will be overriding behaviour for.
     */
    protected EntityEventListener(EntityType<?> entityType) {
        mEntityType = entityType;

        //  Register the listeners.
        MinecraftForge.EVENT_BUS.addListener(this::onJoinWorldEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerInteractEvent);
    }

    /**
     * Override this method to alter behaviour of an entity when it joins the
     * world.
     * @param event Information for the event.
     */
    protected void onJoinWorld(EntityJoinWorldEvent event) {
        //  No-op
    }

    /**
     * Override this event to alter behaviour of an entity when a player
     * interacts with it.
     * @param event Information for the event.
     */
    protected void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        //  No-op
    }

    /**
     * Called when an entity joins the world.
     * @param event Information for the event.
     */
    private void onJoinWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity().getType() == mEntityType) {
            onJoinWorld(event);
        }
    }

    /**
     * Called when a player interacts with an entity.
     * @param event Information for the event.
     */
    private void onPlayerInteractEvent(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget().getType() == mEntityType) {
            onPlayerInteract(event);
        }
    }
}

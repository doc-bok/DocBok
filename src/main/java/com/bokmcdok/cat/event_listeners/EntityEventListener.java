package com.bokmcdok.cat.event_listeners;

import com.bokmcdok.cat.objects.entities.PeacemakerButterfly;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Class to help with overriding the default behaviour of vanilla entities.
 */
public class EntityEventListener {

    //  The type of entity we want to override behaviour for.
    private final EntityType<?> mEntityType;

    /**
     * Construction.
     */
    public EntityEventListener() {
        this(null);
    }

    protected EntityEventListener(EntityType<?> entityType) {
        mEntityType = entityType;

        //  Register the listeners.
        MinecraftForge.EVENT_BUS.addListener(this::onJoinWorldEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerInteractEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingDropsEvent);
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

    private void onLivingDropsEvent(LivingDropsEvent event) {
        //  If a villager or illager is killed by a peacemaker butterfly they
        //  shouldn't drop loot.
        if (event.getSource().getEntity() instanceof PeacemakerButterfly) {
            if (event.getEntity() instanceof Villager ||
                event.getEntity() instanceof AbstractIllager) {
                event.setCanceled(true);
            }
        }
    }
}

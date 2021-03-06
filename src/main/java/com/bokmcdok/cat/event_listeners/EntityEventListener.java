package com.bokmcdok.cat.event_listeners;

import com.bokmcdok.cat.lists.ItemList;
import com.bokmcdok.cat.objects.entities.living.PeacemakerButterfly;
import com.bokmcdok.cat.objects.items.BottledButterflyItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

    /**
     * Register our event listeners
     * @param entityType The entity type to register for
     */
    protected EntityEventListener(EntityType<?> entityType) {
        mEntityType = entityType;

        //  Register the listeners.
        MinecraftForge.EVENT_BUS.addListener(this::onItemCraftedEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onJoinWorldEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingDropsEvent);
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
        Entity entity = event.getEntity();
        if (entity.getType() == mEntityType) {
            onJoinWorld(event);
        }

        if (event.loadedFromDisk()) {
            return;
        }

        Level level = event.getWorld();
        if (!level.isClientSide()) {
            EntityType<?> type = entity.getType();
            if (type == EntityType.PILLAGER ||
                type == EntityType.EVOKER ||
                type == EntityType.ILLUSIONER ||
                type == EntityType.VINDICATOR) {
                AbstractIllager illager = (AbstractIllager) entity;
                if (illager.getRandom().nextInt(100) < 17) {
                    PeacemakerButterfly.possess((ServerLevel) level, illager);
                    event.setCanceled(true);
                }
            }

            if (type == EntityType.VILLAGER) {
                Villager villager = (Villager) entity;
                if (villager.getRandom().nextInt(1000) < 17) {
                    PeacemakerButterfly.possess((ServerLevel) level, villager);
                    event.setCanceled(true);
                }
            }
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

    /**
     * If a villager or illager is killed by a peacemaker butterfly they
     * shouldn't drop loot.
     * @param event The drop event to cancel
     */
    private void onLivingDropsEvent(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof PeacemakerButterfly) {
            if (event.getEntity() instanceof Villager ||
                event.getEntity() instanceof AbstractIllager) {
                event.setCanceled(true);
            }
        }
    }

    /**
     * Transfer butterfly data when moving from a butterfly net to a bottle
     * @param event The event data
     */
    private void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent event) {
        ItemStack craftingItem = event.getCrafting();
        if (craftingItem.getItem() == ItemList.BOTTLED_BUTTERFLY.get()) {
            Container craftingMatrix = event.getInventory();
            for (int i = 0; i < craftingMatrix.getContainerSize(); ++i) {
                ItemStack recipeItem = craftingMatrix.getItem(i);
                if (recipeItem.getItem() == ItemList.BUTTERFLY_NET.get()) {
                    CompoundTag tag = recipeItem.getOrCreateTag();
                    int state = tag.getInt("CustomModelData");

                    BottledButterflyItem.setVariant(craftingItem, state);

                    break;
                }
            }
        }
    }
}

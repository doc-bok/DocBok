package com.bokmcdok.cat.event_listener;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.objects.entity.NyanCatEntity;
import com.bokmcdok.cat.lists.EntityList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Class to override the default behaviour of a cat. This class will allow the
 * cat to eat more types of food and attack other small animals.
 */
public class CatEventListener extends EntityEventListener {

    //  The logger class
    private static final Logger LOGGER = LogManager.getLogger();

    //  The tag that contains a list of items cats can eat.
    private static final ResourceLocation CAT_FOOD_ITEMS =
            new ResourceLocation(CatMod.MOD_ID, "cat_food_items");

    /**
     * Create a listener that listens for cat events.
     */
    public CatEventListener() {
        super(EntityType.CAT);
    }

    /**
     * On joining the world modify the cat's goals so it eats more food and
     * attacks more animals.
     * @param event Information for the event.
     */
    @Override
    protected void onJoinWorld(EntityJoinWorldEvent event) {
        try {
            //  Get the Cat's TemptGoal class and set it's constructor to be
            //  accessible.
            Class<?> innerClass = Class.forName(
                    "net.minecraft.entity.passive.CatEntity$TemptGoal");

            Constructor<?> constructor = innerClass.getDeclaredConstructor(
                    Cat.class,
                    double.class,
                    Ingredient.class,
                    boolean.class);

            constructor.setAccessible(true);

            Cat cat = (Cat) event.getEntity();

            //  Remove the old tempt goal.
            Goal goal = ObfuscationReflectionHelper.getPrivateValue(
                    Cat.class, cat, "temptGoal");
            if (goal != null) {
                cat.goalSelector.removeGoal(goal);
            }

            //  Add the new tempt goal that has the new items.
            Tag<Item> catfood = ItemTags.getAllTags().getTagOrEmpty(CAT_FOOD_ITEMS);
            Ingredient items = Ingredient.of(catfood);
            Goal newGoal = (Goal) constructor.newInstance(
                    cat, 0.6D, items, true);
            cat.goalSelector.addGoal(3, newGoal);

            //  Add new attack goals for other small creatures.
            cat.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(
                    cat, Chicken.class, false, null));

            constructor.setAccessible(false);

            // cat.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(cat, ModMouseEntity.class, 10,false, false, null));
            // cat.targetSelector.addGoal(1, new NonTamedTargetGoal<>(cat, ModButterflyEntity.class, false, null));
            // cat.targetSelector.addGoal(1, new NonTamedTargetGoal<>(cat, ModWidowbirdEntity.class, false, null));
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Handles taming and feeding of cats using custom food items.
     * @param event Information for the event.
     */
    @Override
    protected void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        Cat cat = (Cat) event.getTarget();
        Tag<Item> catfood = ItemTags.getAllTags().getTagOrEmpty(CAT_FOOD_ITEMS);
        if (catfood.contains(item)) {
            if (cat.isTame() && cat.isOwnedBy(player)) {
                FoodProperties food = item.getFoodProperties();
                if (food != null && heal(cat, food.getNutrition())) {
                    consumeEvent(event, player, hand, stack);
                }
            } else {
                consumeEvent(event, player, hand, stack);
                if (!event.getWorld().isClientSide()) {
                    if (cat.getRandom().nextInt(3) == 0) {
                        cat.tame(player);
                        cat.setOrderedToSit(true);
                        cat.level.broadcastEntityEvent(cat, (byte)7);
                        //playTameEffect(cat, true);
                    } else {
                        //playTameEffect(cat, false);
                        cat.level.broadcastEntityEvent(cat, (byte)8);
                    }
                }
            }
        }

        //  Add Snow to the game!
        if (item instanceof NameTagItem) {
            String name = stack.getHoverName().getString();
            if ("Snow".equals(name)) {
                cat.setCatType(8);
            } else if ("Marty".equals(name)) {
                if (event.getWorld() instanceof ServerLevel) {
                    NyanCatEntity nyanCat = EntityList.NYAN_CAT.get().create(player.level);
                    if (nyanCat != null) {
                        nyanCat.copyPosition(cat);
                        nyanCat.finalizeSpawn((ServerLevel) event.getWorld(),
                                player.level.getCurrentDifficultyAt(cat.getOnPos()),
                                MobSpawnType.CONVERSION,
                                null,
                                null);
                        nyanCat.setCustomName(stack.getHoverName());
                        nyanCat.setCustomNameVisible(true);
                        nyanCat.setOwnerUUID(cat.getOwnerUUID());
                        cat.remove(Entity.RemovalReason.DISCARDED);

                        player.level.addFreshEntity(nyanCat);

                        consumeEvent(event, player, hand, stack);
                    }
                }
            }
        }
    }

    /**
     * Heal the animal by the specified amount.
     * @param cat The animal to heal.
     * @param heal The amount to heal.
     * @return True if the animal was healed.
     */
    private boolean heal(Cat cat, int heal) {
        if (cat.getHealth() < cat.getMaxHealth() && heal > 0.0f) {
            cat.heal(heal);
            return true;
        }

        return false;
    }

    /**
     * Consumes the used item and consumes the event
     * @param event The event to consume
     * @param player The player
     * @param stack The stack of items used on the mob
     */
    private void consumeEvent(PlayerInteractEvent.EntityInteract event, Player player, InteractionHand hand, ItemStack stack) {
        if (!player.isCreative()) {
            ItemStack leftovers = stack.finishUsingItem(player.level, player);
            player.setItemInHand(hand, leftovers);
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}

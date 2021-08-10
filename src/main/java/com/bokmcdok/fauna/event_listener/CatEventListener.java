package com.bokmcdok.fauna.event_listener;

import com.bokmcdok.fauna.FaunaMod;
import com.bokmcdok.fauna.objects.entity.NyanCatEntity;
import com.bokmcdok.fauna.lists.EntityList;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NonTamedTargetGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

/**
 * Class to override the default behaviour of a cat. This class will allow the
 * cat to eat more types of food and attack other small animals.
 */
public class CatEventListener extends EntityEventListener {

    //  The logger class
    private static final Logger LOGGER = LogManager.getLogger();

    //  The tag that contains a list of items cats can eat.
    private static final ResourceLocation CAT_FOOD_ITEMS =
            new ResourceLocation(FaunaMod.MOD_ID, "cat_food_items");

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
                    CatEntity.class,
                    double.class,
                    Ingredient.class,
                    boolean.class);

            constructor.setAccessible(true);

            CatEntity cat = (CatEntity) event.getEntity();

            //  Remove the old tempt goal.
            Goal goal = ObfuscationReflectionHelper.getPrivateValue(
                    CatEntity.class, cat, "temptGoal");
            if (goal != null) {
                cat.goalSelector.removeGoal(goal);
            }

            //  Add the new tempt goal that has the new items.
            ITag<Item> catfood = ItemTags.getCollection().getOrCreate(CAT_FOOD_ITEMS);
            Ingredient items = Ingredient.fromTag(catfood);
            Goal newGoal = (Goal) constructor.newInstance(
                    cat, 0.6D, items, true);
            cat.goalSelector.addGoal(3, newGoal);

            //  Add new attack goals for other small creatures.
            cat.targetSelector.addGoal(1, new NonTamedTargetGoal<>(
                    cat, ChickenEntity.class, false, null));

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
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack stack = player.getHeldItem(hand);
        Item item = stack.getItem();
        CatEntity cat = (CatEntity) event.getTarget();
        ITag<Item> catfood = ItemTags.getCollection().getOrCreate(CAT_FOOD_ITEMS);
        if (item.isIn(catfood)) {
            if (cat.isTamed() && cat.isOwner(player)) {
                Food food = item.getFood();
                if (food != null && heal(cat, item.getFood().getHealing())) {
                    consumeEvent(event, player, hand, stack);
                }
            } else {
                consumeEvent(event, player, hand, stack);
                if (!cat.world.isRemote) {
                    if (cat.getRNG().nextInt(3) == 0) {
                        cat.setTamedBy(player);
                        playTameEffect(cat, true);
                    } else {
                        playTameEffect(cat, false);
                    }

                    cat.world.setEntityState(cat, (byte) 7);
                }
            }
        }

        //  Add Snow to the game!
        if (item instanceof NameTagItem) {
            String name = TextFormatting.getTextWithoutFormattingCodes(stack.getDisplayName().getString());
            if ("Snow".equals(name)) {
                cat.setCatType(8);
            } else if ("Marty".equals(name)) {
                NyanCatEntity nyanCat = EntityList.NYAN_CAT.get().create(player.world);
                if (nyanCat != null) {
                    nyanCat.copyLocationAndAnglesFrom(cat);
                    nyanCat.onInitialSpawn(player.world,
                            player.world.getDifficultyForLocation(cat.func_233580_cy_()),
                            SpawnReason.CONVERSION,
                            null,
                            null);
                    nyanCat.setCustomName(stack.getDisplayName());
                    nyanCat.setCustomNameVisible(true);
                    nyanCat.setOwnerId(cat.getOwnerId());
                    cat.remove();

                    player.world.addEntity(nyanCat);

                    consumeEvent(event, player, hand, stack);
                }
            }
        }
    }

    /**
     * Heal the animal by the specified amount.
     * @param animal The animal to heal.
     * @param heal The amount to heal.
     * @return True if the animal was healed.
     */
    private boolean heal(AnimalEntity animal, int heal) {
        if (animal.getHealth() < animal.getMaxHealth() && heal > 0.0f) {
            animal.heal(heal);
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
    private void consumeEvent(PlayerInteractEvent.EntityInteract event, PlayerEntity player, Hand hand, ItemStack stack) {
        if (!player.abilities.isCreativeMode) {
            ItemStack leftovers = stack.onItemUseFinish(player.getEntityWorld(), player);
            player.setHeldItem(hand, leftovers);
        }

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);
    }

    /**
     * Play the taming effect, will either be hearts or smoke depending on status
     * @param entity The entity being tamed.
     * @param play Whether to play the success or fail effect.
     */
    public static void playTameEffect(TameableEntity entity, boolean play) {
        IParticleData iparticledata = ParticleTypes.HEART;
        if (!play) {
            iparticledata = ParticleTypes.SMOKE;
        }

        Vector3d position = entity.getPositionVec();
        Random rand = entity.getRNG();
        for(int i = 0; i < 7; ++i) {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            entity.world.addParticle(iparticledata,
                    position.x + (double)(rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(),
                    position.y + 0.5D + (double)(rand.nextFloat() * entity.getHeight()),
                    position.z + (double)(rand.nextFloat() * entity.getWidth() * 2.0F) - (double)entity.getWidth(), d0, d1, d2);
        }
    }
}

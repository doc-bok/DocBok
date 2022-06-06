package com.bokmcdok.cat.objects.items;

import com.bokmcdok.cat.lists.EntityList;
import com.bokmcdok.cat.lists.ItemList;
import com.bokmcdok.cat.objects.entities.living.Butterfly;
import com.bokmcdok.cat.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Butterfly net
 * - Used to catch butterflies
 */
public class ButterflyNetItem extends Item {

    //  The name to register with
    public static final String NAME = "butterfly_net";

    /**
     * Create a butterfly net
     * @param properties The item properties
     */
    public ButterflyNetItem(Properties properties) {
        super(properties);
    }

    /**
     * Return an empty net when used in a crafting recipe
     * @param itemStack The current ItemStack
     * @return A new item stack for an empty butterfly net
     */
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(ItemList.BUTTERFLY_NET.get());
    }

    /**
     * Tell the recipe system that we have an item left over after crafting
     * We do it this way because it returns the same item type
     * @return Always true
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    /**
     * Capture a butterfly in the net (it's really hard!)
     * @param stack  The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return True if a butterfly is captured
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity.getType() == EntityList.BUTTERFLY.get()) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getInt("CustomModelData") == 0) {
                Butterfly butterfly = (Butterfly) entity;
                tag.putInt("CustomModelData", butterfly.getVariant() + 1);
                entity.discard();

                player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1F, 1F);

                return true;
            }
        }
        if (entity.getType() == EntityList.PEACEMAKER_BUTTERFLY.get()) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getInt("CustomModelData") == 0) {
                tag.putInt("CustomModelData", 10);
                entity.discard();

                player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1F, 1F);

                return true;
            }
        }

        return super.onLeftClickEntity(stack, player, entity);
    }

    /**
     * Release a butterfly from the net
     * @param level The current level
     * @param player The player using the net
     * @param hand The hand holding the net
     * @return The result of the interaction
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = ItemUtil.releaseButterfly(level, player, hand, player.blockPosition());
        if (result == null) {
            result = super.use(level, player, hand);
        }

        return result;
    }
}

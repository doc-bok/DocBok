package com.bokmcdok.cat.objects.items;

import com.bokmcdok.cat.lists.EntityList;
import com.bokmcdok.cat.objects.entities.Butterfly;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
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
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag tag = stack.getOrCreateTag();
            int state = tag.getInt("CustomModelData");
            if (state != 0) {
                if (level instanceof ServerLevel) {
                    Butterfly butterfly = EntityList.BUTTERFLY.get().create(player.level);
                    if (butterfly != null) {
                        butterfly.copyPosition(player);
                        butterfly.finalizeSpawn((ServerLevel) level,
                                level.getCurrentDifficultyAt(player.getOnPos()),
                                MobSpawnType.NATURAL,
                                null,
                                null);
                        butterfly.setVariant(state - 1);

                        player.level.addFreshEntity(butterfly);

                        tag.putInt("CustomModelData", 0);
                    }
                } else {
                    player.playSound(SoundEvents.PLAYER_ATTACK_WEAK, 1F, 1F);
                }

                return InteractionResultHolder.success(stack);
            }

        return super.use(level, player, hand);
    }
}

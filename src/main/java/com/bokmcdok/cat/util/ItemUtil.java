package com.bokmcdok.cat.util;

import com.bokmcdok.cat.CatMod;
import com.bokmcdok.cat.lists.EntityList;
import com.bokmcdok.cat.objects.entities.Butterfly;
import com.bokmcdok.cat.objects.entities.PeacemakerButterfly;
import com.bokmcdok.cat.objects.models.ItemModels;
import net.minecraft.data.DataGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handles events for items
 */
@Mod.EventBusSubscriber(modid = CatMod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemUtil {

    /**
     * Register our data generators
     * @param event The event information
     */
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(new ItemModels(generator, event.getExistingFileHelper()));
        }
    }

    /**
     * Release a butterfly from an item that holds one
     * @param level The current level
     * @param player The player holding the item
     * @param hand The hand doing the action
     * @return The result of the interaction
     */
    public static InteractionResultHolder<ItemStack> releaseButterfly(@NotNull Level level,
                                                                      @NotNull Player player,
                                                                      @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();
        int state = tag.getInt("CustomModelData");
        if (state != 0) {
            if (level instanceof ServerLevel) {
                if (state == 9999) {
                    PeacemakerButterfly peacemakerButterfly =
                            EntityList.PEACEMAKER_BUTTERFLY.get().create(player.level);
                    if (peacemakerButterfly != null) {
                        peacemakerButterfly.copyPosition(player);
                        peacemakerButterfly.finalizeSpawn((ServerLevel) level,
                                level.getCurrentDifficultyAt(player.getOnPos()),
                                MobSpawnType.NATURAL,
                                null,
                                null);

                        player.level.addFreshEntity(peacemakerButterfly);

                        tag.putInt("CustomModelData", 0);
                    }
                } else {
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
                }
            } else {
                player.playSound(SoundEvents.PLAYER_ATTACK_WEAK, 1F, 1F);
            }

            return InteractionResultHolder.success(stack);
        }

        return null;
    }
}

package com.bokmcdok.cat.objects.items;

import com.bokmcdok.cat.util.ItemUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Bottled Butterfly
 * - A butterfly trapped in a bottle.
 */
public class BottledButterflyItem extends BlockItem {

    //  The name to register with
    public static String NAME = "bottled_butterfly";

    /**
     * Create a bottled butterfly
     * @param properties The item properties
     */
    public BottledButterflyItem(Block block, Properties properties) {
        super(block, properties);
    }

    /**
     * Release a butterfly from the bottle
     * @param level The current level
     * @param player The player using the net
     * @param hand The hand holding the net
     * @return The result of the interaction
     */
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = ItemUtil.releaseButterfly(level, player, hand);
        if (result == null) {
            result = super.use(level, player, hand);
        }

        player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));

        return result;
    }

    /**
     * Set the variant on the bottled butterfly item
     * @param stack The item stack to modify
     * @param variant The variant of the butterfly
     */
    public static void setVariant(ItemStack stack, int variant) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("CustomModelData", variant);

        CompoundTag blockStateTag = stack.getOrCreateTagElement("BlockStateTag");
        blockStateTag.putInt("variant", variant);
    }
}

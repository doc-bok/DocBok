package com.bokmcdok.cat.objects.blocks;

import com.bokmcdok.cat.lists.ItemList;
import com.bokmcdok.cat.objects.items.BottledButterflyItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BottledButterflyBlock extends Block {

    //  Name used for registration
    public static String NAME = "bottled_butterfly";

    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 10);

    //  The AABB(s) for the bottle
    private static final VoxelShape AABB = Shapes.or(
            Block.box(5.0, 0.0, 5.0, 10.0, 1.0, 10.0),
            Block.box(4.0, 1.0, 4.D, 11.0, 2.0, 11.0),
            Block.box(3.0, 2.0, 3.0, 12.0, 6.0, 12.0),
            Block.box(4.0, 6.0, 4.D, 11.0, 7.0, 11.0),
            Block.box(5.0, 7.0, 5.0, 10.0, 8.0, 10.0),
            Block.box(6.0, 8.0, 6.0, 9.0, 10.0, 9.0),
            Block.box(5.0, 10.0, 5.0, 10.0, 12.0, 10.0),
            Block.box(6.0, 12.0, 6.0, 9.0, 13.0, 9.0));

    /**
     * Create a butterfly block
     * @param properties The properties of this block
     */
    public BottledButterflyBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, 0));
    }

    /**
     * Get the AABB for the bottle
     * @param state The current block state
     * @param getter Gets details about blocks in certain positions
     * @param position The position of the block
     * @param context The requested collision context
     * @return An AABB(s) for the bottle
     */
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState state,
                                        @NotNull BlockGetter getter,
                                        @NotNull BlockPos position,
                                        @NotNull CollisionContext context) {
        return AABB;
    }

    /**
     * Create the drops for the bottled butterfly that preserves the variant
     * @param state The block state being destroyed
     * @param builder The loot builder
     * @return A bottle with the correct bottle
     */
    @Override
    @SuppressWarnings("deprecation")
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state,
                                             LootContext.@NotNull Builder builder) {

        ItemStack stack = new ItemStack(ItemList.BOTTLED_BUTTERFLY.get());
        BottledButterflyItem.setVariant(stack, state.getValue(VARIANT));

        List<ItemStack> result = Lists.newArrayList();
        result.add(stack);
        return result;
    }

    /**
     * Add the variant property to the block state definition
     * @param builder The builder class
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
        super.createBlockStateDefinition(builder);
    }
}

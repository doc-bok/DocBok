package com.bokmcdok.cat.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Helper methods for blocks
 */
public class BlockUtil {

    /**
     * Used to set never on certain block properties
     * @param state The block's state
     * @param getter Get other blocks if needed
     * @param position The position of the block
     * @return Always false
     */
    @SuppressWarnings("unused")
    public static boolean never(BlockState state,
                                BlockGetter getter,
                                BlockPos position) {
        return false;
    }
    /**
     * Used to set never on certain block properties
     * @param state The block's state
     * @param getter Get other blocks if needed
     * @param position The position of the block
     * @param type The entity type
     * @return Always false
     */
    @SuppressWarnings("unused")
    public static Boolean never(BlockState state,
                                BlockGetter getter,
                                BlockPos position,
                                EntityType<?> type) {
        return false;
    }
}

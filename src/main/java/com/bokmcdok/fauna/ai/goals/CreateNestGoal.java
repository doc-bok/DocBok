package com.bokmcdok.fauna.ai.goals;

import com.bokmcdok.fauna.objects.entity.NestingEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * This goal allows two animals to create a nest that will eventually spawn
 * children.
 */
public class CreateNestGoal extends MoveToBlockGoal {
    private final NestingEntity owner;
    private final Block nestBlock;


    /**
     * Construction
     * @param ownerIn The owner of this goal
     * @param nestBlockIn The type of nest block to create
     * @param moveSpeedIn The speed to move when creating a nest
     * @param radiusIn The radius to search when looking for a good nest spot
     * @param heightIn The height to search when looking for a good nest spot
     */
    public CreateNestGoal(NestingEntity ownerIn,
                          Block nestBlockIn,
                          double moveSpeedIn,
                          int radiusIn,
                          int heightIn) {
        super(ownerIn, moveSpeedIn, radiusIn, heightIn);
        owner = ownerIn;
        nestBlock = nestBlockIn;
    }

    /**
     * Should the goal be executed
     * @return Usually TRUE if the nesting animal is fertilized.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay < 0 && !ForgeEventFactory.getMobGriefingEvent(owner.world, owner)) {
            return false;
        }

        return owner.getIsFertilized() && searchForDestination();
    }

    /**
     * Should the goal continue to be executed
     * @return Usually TRUE if the nesting animal is fertilized.
     */
    @Override
    public boolean shouldContinueExecuting() {
        return owner.getIsFertilized() && searchForDestination();
    }

    /**
     * Update the goal and create the nest if it can.
     */
    @Override
    public void tick() {
        super.tick();
        owner.getLookController().setLookPosition(
                (double)destinationBlock.getX() + 0.5D,
                destinationBlock.getY() + 1,
                (double)destinationBlock.getZ() + 0.5D,
                10.0F, (float)owner.getVerticalFaceSpeed());
        if (getIsAboveDestination()) {
            World world = owner.world;
            BlockState blockstate = world.getBlockState(destinationBlock.up());
            Block block = blockstate.getBlock();
            if (owner.getIsFertilized() && block instanceof TallGrassBlock) {
                world.setBlockState(destinationBlock.up(), nestBlock.getDefaultState(), 2);
            }

            owner.setIsFertilized(false);
            owner.setNestPosition(destinationBlock.up());
            runDelay = 10;
        }
    }

    /**
     * Check if moving to a block will help achieve the goal.
     * @param world The current world.
     * @param position The position of the block.
     * @return TRUE if the block can be pollinated.
     */
    protected boolean shouldMoveTo(IWorldReader world, BlockPos position) {
        Block block = world.getBlockState(position.up()).getBlock();
        return block instanceof TallGrassBlock && owner.getIsFertilized();
    }
}

package com.bokmcdok.fauna.ai.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;

/**
 * A goal that encourages the owner to move towards the light.
 */
public class AttractToLightGoal extends Goal {
    private final CreatureEntity creature;
    private final double speed;
    private final int minHeight;
    private final int searchRadius;
    private final int searchHeight;

    private BlockPos destination = BlockPos.ZERO;
    private int maxStayTicks;
    private int runDelay;
    private int timeoutCounter;

    /**
     * Construction
     * @param creatureIn The creature that owns the goal.
     * @param speedIn The speed the creature moves.
     * @param searchRadiusIn The maximum distance to search for a target.
     */
    public AttractToLightGoal(CreatureEntity creatureIn,
                              double speedIn,
                              int searchRadiusIn) {
        this(creatureIn, speedIn, searchRadiusIn, 1);
    }

    /**
     * Construction
     * @param creatureIn The creature that owns the goal.
     * @param speedIn The speed the creature moves.
     * @param searchRadiusIn The maximum distance to search for a target.
     * @param searchHeightIn The maximum height to search for a target.
     */
    public AttractToLightGoal(CreatureEntity creatureIn,
                              double speedIn,
                              int searchRadiusIn,
                              int searchHeightIn) {
        creature = creatureIn;
        speed = speedIn;
        searchRadius = searchRadiusIn;
        searchHeight = searchHeightIn;

        minHeight = 0;

        setMutexFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
    }

    /**
     * Should this goal execute?
     * @return TRUE if there is a valid destination.
     */
    @Override
    public boolean shouldExecute() {
        if (runDelay > 0) {
            --runDelay;
            return false;
        } else {
            runDelay = getRunDelay();
            searchForDestination();
            return true;
        }
    }

    /**
     * Should this goal continue executing?
     * @return TRUE if the timeout hasn't passed yet.
     */
    @Override
    public boolean shouldContinueExecuting() {
        return timeoutCounter >= -maxStayTicks &&
                timeoutCounter <= 1200 &&
                shouldMoveTo(destination);
    }

    /**
     * Try and move to the target block.
     */
    @Override
    public void startExecuting() {
        tryMoveToBlock();
        timeoutCounter = 0;
        maxStayTicks = creature.getRNG().nextInt(creature.getRNG().nextInt(1200) + 1200) + 1200; // WTF
    }

    /**
     * Continue trying to move until target destination is reached.
     */
    @Override
    public void tick() {
        if (!destination.up().withinDistance(creature.getPositionVec(), 1)) {
            ++timeoutCounter;
            if (shouldMove()) {
                tryMoveToBlock();
            }
        } else {
            --timeoutCounter;
        }
    }

    /**
     * Get a random run delay for this goal.
     * @return A random number between 200 and 400.
     */
    private int getRunDelay() {
        return 200 + creature.getRNG().nextInt(200);
    }

    /**
     * Try and move towards the block.
     */
    private void tryMoveToBlock() {
        creature.getNavigator().tryMoveToXYZ(
                (double) destination.getX() + 0.5d,
                (double) destination.getY() + 1,
                (double) destination.getZ() + 0.5d,
                speed);
    }

    /**
     * Should the creature move?
     * @return TRUE if the creature should move again.
     */
    private boolean shouldMove() {
        return timeoutCounter % 40 == 0;
    }

    /**
     * Search for a destination block
     */
    private void searchForDestination() {
        BlockPos creaturePosition = creature.func_233580_cy_();
        BlockPos.Mutable potentialDestination = new BlockPos.Mutable();
        int lightValue = 0;

        for(int y = minHeight; y <= searchHeight; y = y > 0 ? -y : 1 - y) {
            for (int xz = 0; xz < searchRadius; ++xz) {
                for (int x = 0; x <= xz; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < xz && x > -xz ? xz : 0; z <= xz; z = z > 0 ? -z : 1 - z) {
                        potentialDestination.func_239621_a_(creaturePosition, x, y - 1, z);
                        int newLightValue = creature.world.getLightValue(potentialDestination);
                        if (newLightValue > lightValue && shouldMoveTo(potentialDestination)
                            ) {

                            lightValue = newLightValue;
                            destination = potentialDestination;

                            //  If we get to the highest light value then save time.
                            if (lightValue >= 15) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Check we can move to the specified block.
     * @param position The position to check.
     * @return TRUE if we can move to the block.
     */
    private boolean shouldMoveTo(BlockPos position) {
        return creature.isWithinHomeDistanceFromPosition(position) &&
                creature.world.isAirBlock(position.up());
    }
}

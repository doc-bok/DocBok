package com.bokmcdok.cat.objects.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * Equivalent to vanilla MoveThroughVillageGoal, except for flying mobs
 */
public class FlyThroughVillageGoal extends Goal {

    //  The mob using this goal
    protected final PathfinderMob mob;

    //  Can the mob open/break doors
    private final BooleanSupplier canDealWithDoors;

    //  True if we should only visit villages at night
    private final boolean onlyAtNight;

    // The current path being followed
    @Nullable private Path path;

    //  The target position
    private BlockPos poiPosition;

    //  The range of this goal
    private final int range;

    //  The modifier to speed when moving through a village
    private final double speedModifier;

    //  List of visited POIs
    private final List<BlockPos> visited = Lists.newArrayList();

    /**
     * Create an instance of the goal
     * @param mob The mob that owns this goal
     * @param speedModifier The modifier to speed when moving through a village
     * @param onlyAtNight True if we should only visit villages at night
     * @param range The range of this goal
     * @param canDealWithDoors Can the mob open/break doors
     */
    public FlyThroughVillageGoal(PathfinderMob mob,
                                 double speedModifier,
                                 boolean onlyAtNight,
                                 int range,
                                 BooleanSupplier canDealWithDoors) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.onlyAtNight = onlyAtNight;
        this.range = range;
        this.canDealWithDoors = canDealWithDoors;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));

        //  Don't use this goal with ground navigation.
        if (GoalUtils.hasGroundPathNavigation(mob)) {
            throw new IllegalArgumentException(
                    "Unsupported mob for FlyThroughVillageGoal. " +
                    "Use MoveThroughVillageGoal instead.");
        }
    }

    /**
     * Check mob can carry on using the goal
     * @return True if the mob can still reach the POI
     */
    @Override
    public boolean canContinueToUse() {
        if (this.mob.getNavigation().isDone()) {
            return false;
        } else {
            return !this.poiPosition.closerToCenterThan(this.mob.position(), this.mob.getBbWidth() + (float)this.range);
        }
    }

    /**
     * Can the mob use this goal
     * @return True if the goal can be used
     */
    @Override
    public boolean canUse() {

        //  Cannot be used with ground navigation
        if (GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        }

        this.updateVisited();

        //  Check if the mob can only do this at night
        if (this.onlyAtNight && this.mob.level.isDay()) {
            return false;
        }

        //  Check if a village is nearby
        ServerLevel level = (ServerLevel) this.mob.level;
        BlockPos mobPosition = this.mob.blockPosition();
        if (!level.isCloseToVillage(mobPosition, 6)) {
            return false;
        }

        //  Pick a random position
        Vec3 randomPosition = LandRandomPos.getPos(this.mob, 15, 7, (x) -> {
            if (!level.isVillage(x)) {
                return Double.NEGATIVE_INFINITY;
            } else {
                Optional<BlockPos> result = level.getPoiManager()
                        .find(PoiType.ALL, this::hasNotVisited, x, 10, PoiManager.Occupancy.IS_OCCUPIED);
                return result.map(blockPos -> -blockPos.distSqr(mobPosition))
                        .orElse(Double.NEGATIVE_INFINITY);
            }
        });

        //  Can't run if we didn't get a random position
        if (randomPosition == null) {
            return false;
        }

        //  Try to find a POI position
        Optional<BlockPos> poiPosition = level.getPoiManager()
                .find(PoiType.ALL, this::hasNotVisited, new BlockPos(randomPosition),
                      10, PoiManager.Occupancy.IS_OCCUPIED);
        if (poiPosition.isEmpty()) {
            return false;
        }

        //  Construct the path
        this.poiPosition = poiPosition.get().immutable();
        FlyingPathNavigation navigation = (FlyingPathNavigation) this.mob.getNavigation();
        boolean flag = navigation.canOpenDoors();
        navigation.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
        this.path = navigation.createPath(this.poiPosition, 0);
        navigation.setCanOpenDoors(flag);
        if (this.path == null) {
            Vec3 direction = DefaultRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(this.poiPosition),
                    (float) Math.PI / 2F);
            if (direction == null) {
                return false;
            }

            navigation.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
            this.path = this.mob.getNavigation().createPath(direction.x, direction.y + 1, direction.z, 0);
            navigation.setCanOpenDoors(flag);
            if (this.path == null) {
                return false;
            }
        }

        for (int i = 0; i < this.path.getNodeCount(); ++i) {
            Node node = this.path.getNode(i);
            BlockPos target = new BlockPos(node.x, node.y + 1, node.z);
            if (DoorBlock.isWoodenDoor(this.mob.level, target)) {
                this.path = this.mob.getNavigation().createPath(node.x, node.y, node.z, 0);
                break;
            }
        }

        return this.path != null;
    }

    /**
     * Start moving toward the POI
     */
    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
    }

    /**
     * Stop moving toward the POI
     */
    @Override
    public void stop() {
        if (this.mob.getNavigation().isDone() || this.poiPosition.closerToCenterThan(this.mob.position(), this.range)) {
            this.visited.add(this.poiPosition);
        }

    }

    /**
     * Check the mob hasn't recently visited this POI
     * @param position The position to check
     * @return True if the mob hasn't visited this POI recently
     */
    private boolean hasNotVisited(BlockPos position) {
        for(BlockPos blockpos : this.visited) {
            if (Objects.equals(position, blockpos)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Only keep the last 15 visited POIs
     */
    private void updateVisited() {
        if (this.visited.size() > 15) {
            this.visited.remove(0);
        }
    }
}

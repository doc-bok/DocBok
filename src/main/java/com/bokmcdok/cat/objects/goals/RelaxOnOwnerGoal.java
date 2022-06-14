package com.bokmcdok.cat.objects.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Random;

/**
 * Goal for pets that sleep with their owner
 */
public class RelaxOnOwnerGoal extends Goal {

    //  The pet entity
    private final Cat entity;

    //  The owning player
    @Nullable private Player owner;

    //  The target position for this goal
    @Nullable private BlockPos goalPosition;

    //  Time spent on the bed
    private int onBedTicks;

    /**
     * Create a new goal instance
     * @param entity The pet entity
     */
    public RelaxOnOwnerGoal(Cat entity) {
        this.entity = entity;
    }

    /**
     * Check the entity can keep using the goal
     * @return True if the entity can use the goal
     */
    public boolean canContinueToUse() {
        return this.entity.isTame() &&
                !this.entity.isOrderedToSit() &&
                this.owner != null &&
                this.owner.isSleeping() &&
                this.goalPosition != null &&
                this.spaceIsNotOccupied();
    }

    /**
     * Check if the entity can use the goal
     * @return True if the owner is sleeping
     */
    public boolean canUse() {
        if (!this.entity.isTame()) {
            return false;
        } else if (this.entity.isOrderedToSit()) {
            return false;
        } else {
            LivingEntity owner = this.entity.getOwner();
            if (owner instanceof Player) {
                this.owner = (Player)owner;
                if (!owner.isSleeping()) {
                    return false;
                }

                if (this.entity.distanceToSqr(this.owner) > 100.0) {
                    return false;
                }

                BlockPos position = this.owner.blockPosition();
                BlockState state = this.entity.level.getBlockState(position);
                if (state.is(BlockTags.BEDS)) {
                    this.goalPosition = state.getOptionalValue(BedBlock.FACING).map((p_28209_) ->
                            position.relative(p_28209_.getOpposite())).orElseGet(() ->
                            new BlockPos(position));
                    return this.spaceIsNotOccupied();
                }
            }

            return false;
        }
    }

    /**
     * Start using the goal
     */
    public void start() {
        if (this.goalPosition != null) {
            this.entity.setInSittingPose(false);
            this.entity.getNavigation().moveTo(this.goalPosition.getX(), this.goalPosition.getY(), this.goalPosition.getZ(), 1.100000023841858);
        }
    }

    /**
     * Stop using the goal
     */
    public void stop() {
        this.entity.setLying(false);
        float f = this.entity.level.getTimeOfDay(1.0F);
        if (this.owner != null) {
            if (this.owner.getSleepTimer() >= 100 && (double) f > 0.77 && (double) f < 0.8 && (double) this.entity.level.getRandom().nextFloat() < 0.7) {
                this.giveMorningGift();
            }
        }

        this.onBedTicks = 0;
        this.entity.setRelaxStateOne(false);
        this.entity.getNavigation().stop();
    }

    /**
     * Update the goal
     */
    public void tick() {
        if (this.owner != null && this.goalPosition != null) {
            this.entity.setInSittingPose(false);
            this.entity.getNavigation().moveTo(this.goalPosition.getX(), this.goalPosition.getY(), this.goalPosition.getZ(), 1.100000023841858);
            if (this.entity.distanceToSqr(this.owner) < 2.5) {
                ++this.onBedTicks;
                if (this.onBedTicks > this.adjustedTickDelay(16)) {
                    this.entity.setLying(true);
                    this.entity.setRelaxStateOne(false);
                } else {
                    this.entity.lookAt(this.owner, 45.0F, 45.0F);
                    this.entity.setRelaxStateOne(true);
                }
            } else {
                this.entity.setLying(false);
            }
        }

    }

    /**
     * Give a gift to the owner after sleeping
     */
    private void giveMorningGift() {
        Random random = this.entity.getRandom();
        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos();
        position.set(this.entity.blockPosition());
        this.entity.randomTeleport(position.getX() + random.nextInt(11) - 5, position.getY() + random.nextInt(5) - 2, position.getZ() + random.nextInt(11) - 5, false);
        position.set(this.entity.blockPosition());
        MinecraftServer server = this.entity.level.getServer();
        if (server != null) {
            LootTable loottable = server.getLootTables().get(BuiltInLootTables.CAT_MORNING_GIFT);
            LootContext.Builder lootBuilder = (new LootContext.Builder((ServerLevel) this.entity.level)).withParameter(LootContextParams.ORIGIN, this.entity.position()).withParameter(LootContextParams.THIS_ENTITY, this.entity).withRandom(random);

            for (ItemStack itemstack : loottable.getRandomItems(lootBuilder.create(LootContextParamSets.GIFT))) {
                this.entity.level.addFreshEntity(new ItemEntity(this.entity.level, (double) position.getX() - (double) Mth.sin(this.entity.yBodyRot * 0.017453292F), position.getY(), (double) position.getZ() + (double) Mth.cos(this.entity.yBodyRot * 0.017453292F), itemstack));
            }
        }
    }

    /**
     * Check there is no one in the bed already
     * @return True if there are no pets yet
     */
    private boolean spaceIsNotOccupied() {
        if (this.goalPosition != null) {
            Iterator<Cat> cats = this.entity.level.getEntitiesOfClass(Cat.class, (new AABB(this.goalPosition))
                    .inflate(2.0)).iterator();

            Cat cat;
            do {
                do {
                    if (!cats.hasNext()) {
                        return true;
                    }

                    cat = cats.next();
                } while (cat == this.entity);
            } while (!cat.isLying() && !cat.isRelaxStateOne());
        }

        return false;
    }
}
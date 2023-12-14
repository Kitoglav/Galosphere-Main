package net.orcinus.galosphere.entities.ai.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.player.Player;
import net.orcinus.galosphere.entities.Berserker;
import net.orcinus.galosphere.entities.Preserved;
import net.orcinus.galosphere.init.GEntityTypes;
import net.orcinus.galosphere.init.GMemoryModuleTypes;
import net.orcinus.galosphere.init.GSoundEvents;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Summon extends Behavior<Berserker> {
    private static final int MAX_DURATION = 70;

    public Summon() {
        super(ImmutableMap.of(
                GMemoryModuleTypes.RAMPAGE_TICKS.get(), MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED,
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,
                GMemoryModuleTypes.IS_SMASHING.get(), MemoryStatus.VALUE_ABSENT,
                GMemoryModuleTypes.IS_IMPALING.get(), MemoryStatus.VALUE_ABSENT,
                GMemoryModuleTypes.IS_SUMMONING.get(), MemoryStatus.REGISTERED,
                GMemoryModuleTypes.SUMMONING_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT,
                GMemoryModuleTypes.SUMMON_COUNT.get(), MemoryStatus.REGISTERED
        ), MAX_DURATION);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, Berserker livingEntity) {
        return livingEntity.getHealth() / livingEntity.getMaxHealth() <= 0.66F && livingEntity.hasPose(Pose.STANDING) && livingEntity.shouldAttack();
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, Berserker livingEntity, long l) {
        return true;
    }

    @Override
    protected void start(ServerLevel serverLevel, Berserker livingEntity, long l) {
        Brain<Berserker> brain = livingEntity.getBrain();
        brain.setMemoryWithExpiry(GMemoryModuleTypes.IS_SUMMONING.get(), Unit.INSTANCE, MAX_DURATION);
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        livingEntity.setPhase(Berserker.Phase.SUMMONING);
        livingEntity.playSound(GSoundEvents.BERSERKER_SUMMONING.get(), 10.0f, 1.0F);
    }

    @Override
    protected void tick(ServerLevel serverLevel, Berserker livingEntity, long l) {
        Brain<Berserker> brain = livingEntity.getBrain();
        brain.setMemory(GMemoryModuleTypes.SUMMON_COUNT.get(), brain.getMemory(GMemoryModuleTypes.SUMMON_COUNT.get()).orElse(0) + 1);
        int max = UniformInt.of(3, 5).sample(serverLevel.getRandom());
        if (serverLevel.getDifficulty() == Difficulty.HARD) {
            max = UniformInt.of(4, 8).sample(serverLevel.getRandom());
        }
        if (brain.hasMemoryValue(GMemoryModuleTypes.SUMMON_COUNT.get()) && brain.getMemory(GMemoryModuleTypes.SUMMON_COUNT.get()).get() > max) {
            return;
        }
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        List<BlockPos> positions = IntStream.range(0, 5).mapToObj(i -> LandRandomPos.getPos(livingEntity, i * 2, 1)).filter(Objects::nonNull).map(BlockPos::containing).filter(blockPos -> livingEntity.level().getWorldBorder().isWithinBounds(blockPos)).map(BlockPos::below).filter(blockPos -> serverLevel.getBlockState(blockPos).isCollisionShapeFullBlock(serverLevel, blockPos)).toList();
        BlockPos randomPos = positions.get(serverLevel.getRandom().nextInt(positions.size()));
        Preserved preserved = GEntityTypes.PRESERVED.get().create(serverLevel, null, null, livingEntity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
        preserved.moveTo(randomPos.getX(), randomPos.getY() + 1, randomPos.getZ(), 0.0f, 0.0f);
        preserved.setHealth(preserved.getMaxHealth() * ((float) livingEntity.getRandom().nextInt(4, 7) / 10));
        serverLevel.addFreshEntityWithPassengers(preserved);
    }

    @Override
    protected void stop(ServerLevel serverLevel, Berserker livingEntity, long l) {
        livingEntity.setPhase(Berserker.Phase.IDLING);
        livingEntity.getBrain().setMemoryWithExpiry(GMemoryModuleTypes.SUMMONING_COOLDOWN.get(), Unit.INSTANCE, 600);
        livingEntity.getBrain().setMemory(GMemoryModuleTypes.SUMMON_COUNT.get(), 0);
    }
}
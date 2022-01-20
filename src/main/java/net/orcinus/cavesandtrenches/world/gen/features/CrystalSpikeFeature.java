package net.orcinus.cavesandtrenches.world.gen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;
import net.orcinus.cavesandtrenches.blocks.PollinatedClusterBlock;
import net.orcinus.cavesandtrenches.world.gen.features.config.LargeCrystalConfig;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Random;

public class CrystalSpikeFeature extends Feature<LargeCrystalConfig> {

    public CrystalSpikeFeature(Codec<LargeCrystalConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeCrystalConfig> context) {
        WorldGenLevel world = context.level();
        BlockPos blockPos = context.origin();
        Random random = context.random();
        LargeCrystalConfig config = context.config();
        BlockState oppositeState = world.getBlockState(blockPos.relative(config.crystal_direction.getOpposite()));
        List<BlockPos> trigList = Lists.newArrayList();
        List<BlockPos> clusterPos = Lists.newArrayList();
        //TODO: MAKE A RADIUS CHECK AND RELATIVE DIRECTION  CHECK
        //-346814623854640707
        //-4103 -5 165
        //-1330 35 18572
        int radiusCheck = UniformInt.of(4, 6).sample(random) + 1;
        boolean flag = false;
        final int randomChance = random.nextInt(4);
        final int randomValue = radiusCheck;
        int stepHeight = randomValue + 14 + Mth.nextInt(random, 10, 14);
        if (oppositeState.is(BlockTags.BASE_STONE_OVERWORLD) && world.isStateAtPosition(blockPos, DripstoneUtils::isEmptyOrWaterOrLava) && isSurroundedWithStones(world, blockPos, radiusCheck, config)){
            if (isSurroundedWithStones(world, blockPos, radiusCheck, config)) {
                for (int y = 0; y < stepHeight; y++) {
                    int radius = randomValue - y / 2;
                    for (int x = -radius; x <= radius; x++) {
                        for (int z = -radius; z <= radius; z++) {
                            BlockPos pos = new BlockPos(blockPos.getX() + x, blockPos.getY(), blockPos.getZ() + z);
                            if (x * x + z * z <= radius * radius) {
//                            if (world.isStateAtPosition(pos.relative(config.crystal_direction.getOpposite()), DripstoneUtils::isEmptyOrWater)) {
//                                pos = pos.relative(config.crystal_direction.getOpposite());
//                            }
//                            if (world.isStateAtPosition(pos.relative(config.crystal_direction.getOpposite(), 2), DripstoneUtils::isEmptyOrWater)) {
//                                pos = pos.relative(config.crystal_direction.getOpposite(), 2);
//                            }
//                            for (int i = 0; i < 10; i++) {
//                                if (world.isStateAtPosition(pos.relative(config.crystal_direction.getOpposite(), i), DripstoneUtils::isEmptyOrWaterOrLava)) {
//                                    pos = pos.relative(config.crystal_direction.getOpposite(), i);
//                                } else {
//                                    break;
//                                }
//                            }
                                float floatvalue = switch (randomChance) {
                                    case 1 -> 11 * Mth.PI / 6;
                                    case 2 -> Mth.PI / 6;
                                    case 3 -> 7 * Mth.PI / 6;
                                    case 0 -> 5 * Mth.PI / 6;
                                    default -> throw new IllegalStateException("Unexpected value: " + randomChance);
                                };
                                float q = Mth.cos(floatvalue) * y;
                                float k = Mth.sin(Mth.PI / 2) * y;
                                float l = Mth.sin(floatvalue) * y;
                                if (config.crystal_direction == Direction.DOWN) {
                                    q = -q;
                                    k = -k;
                                    l = -l;
                                }
                                BlockPos trigPos = pos.offset(q, k, l);
                                if (world.isStateAtPosition(trigPos, DripstoneUtils::isEmptyOrWaterOrLava)) {
                                    trigList.add(trigPos);
                                }
                            }
                        }
                    }
                }
            } else {
                return false;
            }
            for (BlockPos pos : trigList) {
                if (world.getBlockState(pos).is(BlockTags.BASE_STONE_OVERWORLD) || world.isStateAtPosition(pos, DripstoneUtils::isEmptyOrWaterOrLava)) {
                    this.setBlock(world, pos, config.crystal_state);
                    clusterPos.add(pos);
                    flag = true;
                }
            }
            for (BlockPos pos : clusterPos) {
                if (random.nextInt(15) == 0) {
                    for (Direction direction : Direction.values()) {
                        BlockPos relative = pos.relative(direction);
                        if (random.nextBoolean() && world.isStateAtPosition(relative, DripstoneUtils::isEmptyOrWater)) {
                            this.setBlock(world, relative, config.cluster_state.setValue(PollinatedClusterBlock.FACING, direction).setValue(PollinatedClusterBlock.WATERLOGGED, world.getFluidState(relative).getType() == Fluids.WATER));
                        }
                    }
                }
            }
        }
        return flag;
    }

    public boolean isSurroundedWithStones(WorldGenLevel world, BlockPos blockPos, int radiusCheck, LargeCrystalConfig config) {
        List<BlockPos> list = Lists.newArrayList();
        boolean flag = false;
        for (int x = -radiusCheck; x <= radiusCheck; x++) {
            for (int z = -radiusCheck; z <= radiusCheck; z++) {
                for (int y = 0; y <= 12; y++) {
                    BlockPos pos = new BlockPos(blockPos.getX() + x, blockPos.getY(), blockPos.getZ() + z);
                    if (world.isStateAtPosition(pos.relative(config.crystal_direction.getOpposite(), y), DripstoneUtils::isEmptyOrWaterOrLava)) {
                        list.add(blockPos.relative(config.crystal_direction.getOpposite(), y));
                    }
                }
            }
        }

        if (list.isEmpty()) return false;

        for (BlockPos pos : list) {
            flag = world.getBlockState(pos.relative(config.crystal_direction.getOpposite())).is(BlockTags.BASE_STONE_OVERWORLD);
        }
        return flag;
    }

}

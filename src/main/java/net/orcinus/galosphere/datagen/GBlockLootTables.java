package net.orcinus.galosphere.datagen;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import net.orcinus.galosphere.blocks.PollinatedClusterBlock;
import net.orcinus.galosphere.init.GBlocks;
import net.orcinus.galosphere.init.GItems;

import java.util.Set;
import java.util.stream.Collectors;

public class GBlockLootTables extends BlockLootSubProvider {
    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

    public GBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(GBlocks.SILVER_ORE.get(), (block) -> createOreDrop(block, GItems.RAW_SILVER.get()));
        this.add(GBlocks.DEEPSLATE_SILVER_ORE.get(), (block) -> createOreDrop(block, GItems.RAW_SILVER.get()));
        this.dropSelf(GBlocks.STRANDED_MEMBRANE_BLOCK.get());
        this.dropSelf(GBlocks.CHARGED_LUMIERE_BLOCK.get());
        this.dropSelf(GBlocks.LUMIERE_BLOCK.get());
        this.dropSelf(GBlocks.ALLURITE_BLOCK.get());
        this.dropSelf(GBlocks.RAW_SILVER_BLOCK.get());
        this.dropSelf(GBlocks.SILVER_BLOCK.get());
        this.dropSelf(GBlocks.AMETHYST_STAIRS.get());
        this.dropSlab(GBlocks.AMETHYST_SLAB);
        this.dropSelf(GBlocks.ALLURITE_STAIRS.get());
        this.dropSlab(GBlocks.ALLURITE_SLAB);
        this.dropSelf(GBlocks.LUMIERE_STAIRS.get());
        this.dropSlab(GBlocks.LUMIERE_SLAB);
        this.dropSelf(GBlocks.SMOOTH_AMETHYST.get());
        this.dropSelf(GBlocks.SMOOTH_AMETHYST_STAIRS.get());
        this.dropSlab(GBlocks.SMOOTH_AMETHYST_SLAB);
        this.dropSelf(GBlocks.AMETHYST_BRICKS.get());
        this.dropSelf(GBlocks.AMETHYST_BRICK_STAIRS.get());
        this.dropSlab(GBlocks.AMETHYST_BRICK_SLAB);
        this.dropSelf(GBlocks.CHISELED_AMETHYST.get());
        this.add(GBlocks.ALLURITE_CLUSTER.get(), (block) -> dropAlternativeWithSilkTouch(block, GBlocks.GLINTED_ALLURITE_CLUSTER.get(), LootItem.lootTableItem(GItems.ALLURITE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(GItems.ALLURITE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.add(GBlocks.LUMIERE_CLUSTER.get(), (block) -> dropAlternativeWithSilkTouch(block, GBlocks.GLINTED_LUMIERE_CLUSTER.get(), LootItem.lootTableItem(GItems.LUMIERE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(GItems.LUMIERE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.add(GBlocks.GLINTED_ALLURITE_CLUSTER.get(), (block) -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(GItems.ALLURITE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(GItems.ALLURITE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.add(GBlocks.GLINTED_LUMIERE_CLUSTER.get(), (block) -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(GItems.LUMIERE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(GItems.LUMIERE_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.add(GBlocks.GLINTED_AMETHYST_CLUSTER.get(), (block) -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        this.dropSelf(GBlocks.MONSTROMETER.get());
        this.dropSelf(GBlocks.LUMIERE_LAMP.get());
        this.dropSelf(GBlocks.ALLURITE_LAMP.get());
        this.dropSelf(GBlocks.AMETHYST_LAMP.get());
        this.dropSelf(GBlocks.WARPED_ANCHOR.get());
        this.dropOther(GBlocks.LUMIERE_COMPOSTER.get(), Blocks.COMPOSTER);
        this.dropOther(GBlocks.SALINE_COMPOSTER.get(), Blocks.COMPOSTER);
        this.dropSelf(GBlocks.COMBUSTION_TABLE.get());
        this.dropSelf(GBlocks.SMOOTH_ALLURITE.get());
        this.dropSlab(GBlocks.SMOOTH_ALLURITE_SLAB);
        this.dropSelf(GBlocks.SMOOTH_ALLURITE_STAIRS.get());
        this.dropSelf(GBlocks.ALLURITE_BRICKS.get());
        this.dropSelf(GBlocks.CHISELED_ALLURITE.get());
        this.dropSelf(GBlocks.SMOOTH_LUMIERE.get());
        this.dropSlab(GBlocks.SMOOTH_LUMIERE_SLAB);
        this.dropSelf(GBlocks.SMOOTH_LUMIERE_STAIRS.get());
        this.dropSelf(GBlocks.LUMIERE_BRICKS.get());
        this.dropSelf(GBlocks.CHISELED_LUMIERE.get());
        this.dropSelf(GBlocks.ALLURITE_BRICK_STAIRS.get());
        this.dropSlab(GBlocks.ALLURITE_BRICK_SLAB);
        this.dropSelf(GBlocks.LUMIERE_BRICK_STAIRS.get());
        this.dropSlab(GBlocks.LUMIERE_BRICK_SLAB);
        this.dropSelf(GBlocks.LICHEN_MOSS.get());
        this.dropSelf(GBlocks.LICHEN_ROOTS.get());
        this.dropSelf(GBlocks.LICHEN_SHELF.get());
        this.dropSelf(GBlocks.BOWL_LICHEN.get());
        this.dropSelf(GBlocks.CHANDELIER.get());
        this.add(GBlocks.CHANDELIER.get(), (block) -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        this.addVinesDroptable(GBlocks.LICHEN_CORDYCEPS.get(), GBlocks.LICHEN_CORDYCEPS_PLANT.get());
        this.add(GBlocks.GLOW_INK_CLUMPS.get(), this::createMultifaceBlockDrops);
        this.dropPottedContents(GBlocks.POTTED_BOWL_LICHEN.get());
        this.dropPottedContents(GBlocks.POTTED_LICHEN_ROOTS.get());
        this.dropSelf(GBlocks.SILVER_TILES.get());
        this.dropSelf(GBlocks.SILVER_TILES_STAIRS.get());
        this.dropSlab(GBlocks.SILVER_TILES_SLAB);
        this.dropSelf(GBlocks.SILVER_PANEL.get());
        this.dropSelf(GBlocks.SILVER_PANEL_STAIRS.get());
        this.dropSlab(GBlocks.SILVER_PANEL_SLAB);
        this.dropSelf(GBlocks.SILVER_LATTICE.get());
        this.dropOther(GBlocks.GLOW_BERRIES_SILVER_LATTICE.get(), GBlocks.SILVER_LATTICE.get());
        this.dropSelf(GBlocks.PINK_SALT.get());
        this.dropSelf(GBlocks.ROSE_PINK_SALT.get());
        this.dropSelf(GBlocks.PASTEL_PINK_SALT.get());
        this.dropSelf(GBlocks.POLISHED_PINK_SALT.get());
        this.dropSelf(GBlocks.POLISHED_ROSE_PINK_SALT.get());
        this.dropSelf(GBlocks.POLISHED_PASTEL_PINK_SALT.get());
        this.dropSelf(GBlocks.PINK_SALT_BRICKS.get());
        this.dropSelf(GBlocks.ROSE_PINK_SALT_BRICKS.get());
        this.dropSelf(GBlocks.PASTEL_PINK_SALT_BRICKS.get());
        this.dropSlab(GBlocks.PINK_SALT_SLAB);
        this.dropSlab(GBlocks.ROSE_PINK_SALT_SLAB);
        this.dropSlab(GBlocks.PASTEL_PINK_SALT_SLAB);
        this.dropSlab(GBlocks.POLISHED_PINK_SALT_SLAB);
        this.dropSlab(GBlocks.POLISHED_ROSE_PINK_SALT_SLAB);
        this.dropSlab(GBlocks.POLISHED_PASTEL_PINK_SALT_SLAB);
        this.dropSlab(GBlocks.PINK_SALT_BRICK_SLAB);
        this.dropSlab(GBlocks.ROSE_PINK_SALT_BRICK_SLAB);
        this.dropSlab(GBlocks.PASTEL_PINK_SALT_BRICK_SLAB);
        this.dropSelf(GBlocks.PINK_SALT_STAIRS.get());
        this.dropSelf(GBlocks.ROSE_PINK_SALT_STAIRS.get());
        this.dropSelf(GBlocks.PASTEL_PINK_SALT_STAIRS.get());
        this.dropSelf(GBlocks.POLISHED_PINK_SALT_STAIRS.get());
        this.dropSelf(GBlocks.POLISHED_ROSE_PINK_SALT_STAIRS.get());
        this.dropSelf(GBlocks.POLISHED_PASTEL_PINK_SALT_STAIRS.get());
        this.dropSelf(GBlocks.PINK_SALT_BRICK_STAIRS.get());
        this.dropSelf(GBlocks.ROSE_PINK_SALT_BRICK_STAIRS.get());
        this.dropSelf(GBlocks.PASTEL_PINK_SALT_BRICK_STAIRS.get());
        this.dropSelf(GBlocks.PINK_SALT_WALL.get());
        this.dropSelf(GBlocks.ROSE_PINK_SALT_WALL.get());
        this.dropSelf(GBlocks.PASTEL_PINK_SALT_WALL.get());
        this.dropSelf(GBlocks.POLISHED_PINK_SALT_WALL.get());
        this.dropSelf(GBlocks.POLISHED_ROSE_PINK_SALT_WALL.get());
        this.dropSelf(GBlocks.POLISHED_PASTEL_PINK_SALT_WALL.get());
        this.dropSelf(GBlocks.PINK_SALT_BRICK_WALL.get());
        this.dropSelf(GBlocks.ROSE_PINK_SALT_BRICK_WALL.get());
        this.dropSelf(GBlocks.PASTEL_PINK_SALT_BRICK_WALL.get());
        this.dropSelf(GBlocks.CHISELED_PINK_SALT.get());
        this.dropSelf(GBlocks.CHISELED_ROSE_PINK_SALT.get());
        this.dropSelf(GBlocks.CHISELED_PASTEL_PINK_SALT.get());
        this.dropSelf(GBlocks.SHADOW_FRAME.get());
        this.dropSelf(GBlocks.PINK_SALT_LAMP.get());
        this.dropSelf(GBlocks.SUCCULENT.get());
        this.dropSelf(GBlocks.PINK_SALT_STRAW.get());
        this.dropSelf(GBlocks.CURED_MEMBRANE_BLOCK.get());
        this.add(GBlocks.PINK_SALT_CLUSTER.get(), (block) -> {
            return createSilkTouchDispatchTable(block, LootItem.lootTableItem(GItems.PINK_SALT_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(this.applyExplosionDecay(block, LootItem.lootTableItem(GItems.PINK_SALT_SHARD.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))));
        });
        this.dropSelf(GBlocks.GILDED_BEADS.get());
        this.dropSelf(GBlocks.SILVER_BALANCE.get());
        this.dropSelf(GBlocks.POTPOURRI.get());
        this.dropSelf(GBlocks.PINK_SALT_CHAMBER.get());
        this.add(GBlocks.SUCCULENT_CROP.get(), block -> {
            return LootTable.lootTable();
        });
    }

    protected static LootTable.Builder dropAlternativeWithSilkTouch(Block block, Block alternative, LootPoolEntryContainer.Builder<?> builder) {
        LootPool.Builder lootPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(
                        LootItem.lootTableItem(alternative)
                        .when(HAS_SILK_TOUCH)
                        .when(LootItemBlockStatePropertyCondition.
                                hasBlockStateProperties(block).
                                setProperties(
                                        StatePropertiesPredicate.Builder.
                                                properties().
                                                hasProperty(PollinatedClusterBlock.POLLINATED, true)
                                ))
                        .otherwise(LootItem.lootTableItem(block).when(HAS_SILK_TOUCH))
                                .otherwise(builder)
                );
        return LootTable.lootTable().withPool(lootPool);
    }

    public LootTable.Builder createMultifaceBlockDrops(Block block) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(applyExplosionDecay(block, LootItem.lootTableItem(block).apply(Direction.values(), direction -> SetItemCountFunction.setCount(ConstantValue.exactly(1.0f), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MultifaceBlock.getFaceProperty(direction), true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0f), true)))));
    }

    private void addVinesDroptable(Block block, Block plantBlock) {
        LootTable.Builder builder = createSilkTouchOrShearsDispatchTable(block, LootItem.lootTableItem(block).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.33F, 0.55F, 0.77F, 1.0F)));
        this.add(block, builder);
        this.add(plantBlock, builder);
    }

    private void dropSlab(RegistryObject<Block> slab) {
        this.add(slab.get(), this::createSlabItemTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return GBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
}

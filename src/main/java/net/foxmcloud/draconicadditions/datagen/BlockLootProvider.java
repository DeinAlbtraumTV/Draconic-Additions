package net.foxmcloud.draconicadditions.datagen;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockLootProvider extends BlockLootSubProvider {

    protected BlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }
    
    @Override
    protected void generate() {
		dropSelf(DAContent.chaosLiquifier);
		dropSelf(DAContent.chaosInfuser);
		dropSelf(DAContent.chaosExtractor);
		dropSelf(DAContent.chaosCrystalizer);
	}
    
    protected void dropSelf(Supplier<? extends Block> pBlock) {
        super.dropSelf(pBlock.get());
    }

    protected void noDrop(Supplier<? extends Block> pBlock) {
        add(pBlock.get(), noDrop());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream().filter(e -> e.getKey().location().getNamespace().equals(DraconicAdditions.MODID)).map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
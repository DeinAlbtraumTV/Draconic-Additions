package net.foxmcloud.draconicadditions.datagen;

import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.api.modules.ModuleRegistry;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.lib.DAModules;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by FoxMcloud5655 on 23/11/22.
 */
public class ItemModelGenerator extends ItemModelProvider {

	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), DraconicAdditions.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		blockItem(DAContent.chaosLiquifier.get());
		blockItem(DAContent.chaosInfuser.get());
		blockItem(DAContent.chaosExtractor.get());
		blockItem(DAContent.chaosCrystalizer.get());
		simpleItem(DAContent.chaosHeart.get());
		simpleItem(DAContent.inertPotatoHelm.get());
		simpleItem(DAContent.inertPotatoChest.get());
		simpleItem(DAContent.inertPotatoLegs.get());
		simpleItem(DAContent.inertPotatoBoots.get());
		simpleArmor(DAContent.infusedPotatoHelm.get());
		simpleArmor(DAContent.infusedPotatoChest.get());
		simpleArmor(DAContent.infusedPotatoLegs.get());
		simpleArmor(DAContent.infusedPotatoBoots.get());
		simpleItem(DAContent.chaosContainer.get(), "item/tools/animated");
		simpleCurios(DAContent.necklaceWyvern.get());
		simpleCurios(DAContent.necklaceDraconic.get());
		simpleCurios(DAContent.necklaceChaotic.get());
		simpleCurios(DAContent.harnessWyvern.get());
		simpleCurios(DAContent.harnessDraconic.get());
		simpleCurios(DAContent.harnessChaotic.get());
		simpleItem(DAContent.hermal.get(), new ResourceLocation("minecraft", "item/poisonous_potato"));
		simpleModule(DAModules.chaoticAutoFeed.get());
		simpleModule(DAModules.draconicTickAccel.get());
		simpleModule(DAModules.chaoticTickAccel.get());
		simpleModule(DAModules.semiStableChaos.get());
		simpleModule(DAModules.stableChaos.get());
		simpleModule(DAModules.unstableChaos.get());
		simpleModule(DAModules.chaosInjector.get());
	}

	private void simpleItem(Item item) {
		simpleItem(item, "item/crafting");
	}

	private void simpleArmor(Item item) {
		simpleItem(item, "item/armor");
	}

	private void simpleCurios(Item item) {
		simpleItem(item, "item/curios");
	}

	private void simpleItem(Item item, String textureFolder) {
		if (item == null) return;
		ResourceLocation reg = ForgeRegistries.ITEMS.getKey(item);
		simpleItem(item, new ResourceLocation(reg.getNamespace(), textureFolder + "/" + reg.getPath()));
	}

	private void simpleItem(Item item, ResourceLocation texture) {
		if (item == null) return;
		ResourceLocation reg = ForgeRegistries.ITEMS.getKey(item);
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile("item/generated"))
		.texture("layer0", texture);
	}

	private void simpleModule(Module<?> module) {
		simpleModule(module, "item/modules");
	}

	private void simpleModule(Module<?> module, String textureFolder) {
		if (module == null || module.getItem() == null) return;
		ResourceLocation reg = ModuleRegistry.getRegistry().getKey(module);
		simpleItem(module.getItem(), new ResourceLocation(reg.getNamespace(), textureFolder + "/" + reg.getPath().replace("_module", "")));
	}

	private void blockItem(Block block) {
		if (block == null) return;
		ResourceLocation reg = ForgeRegistries.BLOCKS.getKey(block);
		blockItem(block, new ResourceLocation(reg.getNamespace(), "block/" + reg.getPath()));
	}

	private void blockItem(Block block, ResourceLocation blockModel) {
		if (block == null) return;
		ResourceLocation reg = ForgeRegistries.BLOCKS.getKey(block);
		getBuilder(reg.getPath())
		.parent(new ModelFile.UncheckedModelFile(blockModel));
	}

	private void dummyModel(Block block) {
		dummyModel(block.asItem());
	}

	private void dummyModel(Item item) {
		getBuilder(ForgeRegistries.ITEMS.getKey(item).getPath())
		.parent(new ModelFile.UncheckedModelFile("builtin/generated"));
	}

	@Override
	public String getName() {
		return "Draconic Additions Item Models";
	}
}

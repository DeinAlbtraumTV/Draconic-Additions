package net.foxmcloud.draconicadditions.lib;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.blocks.ItemBlockBCore;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.TechProperties;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.blocks.machines.ChaosInfuser;
import net.foxmcloud.draconicadditions.blocks.machines.ChaosLiquifier;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosInfuser;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquifier;
import net.foxmcloud.draconicadditions.inventory.ChaosBaseMenu;
import net.foxmcloud.draconicadditions.inventory.ChaosInfuserMenu;
import net.foxmcloud.draconicadditions.inventory.ChaosLiquifierMenu;
import net.foxmcloud.draconicadditions.items.Hermal;
import net.foxmcloud.draconicadditions.items.curios.ModularHarness;
import net.foxmcloud.draconicadditions.items.curios.ModularNecklace;
import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DAContent {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DraconicAdditions.MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DraconicAdditions.MODID);
	public static final DeferredRegister<BlockEntityType<?>> TILES_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DraconicAdditions.MODID);
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DraconicAdditions.MODID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DraconicAdditions.MODID);
	
	public static final TechProperties hermalTier = (TechProperties) new TechProperties(TechLevel.CHAOTIC).rarity(Rarity.UNCOMMON).durability(-1).fireResistant()
			.food(new FoodProperties.Builder().alwaysEat().nutrition(0).saturationMod(0).build());

	public static void init() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(eventBus);
		ITEMS.register(eventBus);
		TILES_ENTITIES.register(eventBus);
		MENU_TYPES.register(eventBus);
		ENTITY_TYPES.register(eventBus);
	}

	// Tile Entities
	
	public static final RegistryObject<ChaosLiquifier> chaosLiquifier = BLOCKS.register("chaos_liquifier", () -> new ChaosLiquifier(DEContent.HARDENED_MACHINE));
	public static final RegistryObject<ChaosInfuser> chaosInfuser     = BLOCKS.register("chaos_infuser",   () -> new ChaosInfuser(DEContent.HARDENED_MACHINE));
	
    public static final RegistryObject<ItemBlockBCore> itemChaosLiquifier = ITEMS.register("chaos_liquifier", () -> new ItemBlockBCore(chaosLiquifier.get(), new Item.Properties()));
    public static final RegistryObject<ItemBlockBCore> itemChaosInfuser = ITEMS.register("chaos_infuser", () -> new ItemBlockBCore(chaosInfuser.get(), new Item.Properties()));

	public static final RegistryObject<BlockEntityType<TileChaosLiquifier>> tileChaosLiquifier = TILES_ENTITIES.register("chaos_liquifier", () -> BlockEntityType.Builder.of(TileChaosLiquifier::new, chaosLiquifier.get()).build(null));
	public static final RegistryObject<BlockEntityType<TileChaosInfuser>> tileChaosInfuser = TILES_ENTITIES.register("chaos_infuser", () -> BlockEntityType.Builder.of(TileChaosInfuser::new, chaosInfuser.get()).build(null));

	public static final RegistryObject<MenuType<ChaosBaseMenu>> menuChaosLiquifier = MENU_TYPES.register("chaos_liquifier", () -> IForgeMenuType.create(ChaosLiquifierMenu::new));
	public static final RegistryObject<MenuType<ChaosBaseMenu>> menuChaosInfuser = MENU_TYPES.register("chaos_infuser", () -> IForgeMenuType.create(ChaosInfuserMenu::new));

	// Crafting Components

	public static final RegistryObject<Item> inertPotatoHelm  = ITEMS.register("inert_potato_helm",  () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> inertPotatoChest = ITEMS.register("inert_potato_chest", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> inertPotatoLegs  = ITEMS.register("inert_potato_legs",  () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> inertPotatoBoots = ITEMS.register("inert_potato_boots", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> chaosHeart       = ITEMS.register("chaos_heart",        () -> new Item(new Item.Properties()));
	public static final RegistryObject<Hermal> hermal         = ITEMS.register("hermal",             () -> new Hermal(hermalTier));

	// Armor

	//public static final RegistryObject<InfusedPotatoArmor> infusedPotatoHelm = ITEMS.register("infused_potato_helm",   () -> new InfusedPotatoArmor(new Item.Properties(), net.minecraft.world.item.ArmorItem.Type.HELMET));
	//public static final RegistryObject<InfusedPotatoArmor> infusedPotatoChest = ITEMS.register("infused_potato_chest", () -> new InfusedPotatoArmor(new Item.Properties(), net.minecraft.world.item.ArmorItem.Type.CHESTPLATE));
	//public static final RegistryObject<InfusedPotatoArmor> infusedPotatoLegs = ITEMS.register("infused_potato_legs",   () -> new InfusedPotatoArmor(new Item.Properties(), net.minecraft.world.item.ArmorItem.Type.LEGGINGS));
	//public static final RegistryObject<InfusedPotatoArmor> infusedPotatoBoots = ITEMS.register("infused_potato_boots", () -> new InfusedPotatoArmor(new Item.Properties(), net.minecraft.world.item.ArmorItem.Type.BOOTS));

	// Tools

	public static final RegistryObject<ChaosContainer> chaosContainer = ITEMS.register("chaos_container",        () -> new ChaosContainer(DEContent.CHAOTIC_TOOLS));

	// Curios

	public static final RegistryObject<ModularNecklace> necklaceWyvern   = ITEMS.register("wyvern_necklace",   () -> new ModularNecklace(DEContent.WYVERN_TOOLS));
	public static final RegistryObject<ModularNecklace> necklaceDraconic = ITEMS.register("draconic_necklace", () -> new ModularNecklace(DEContent.DRACONIC_TOOLS));
	public static final RegistryObject<ModularNecklace> necklaceChaotic  = ITEMS.register("chaotic_necklace",  () -> new ModularNecklace(DEContent.CHAOTIC_TOOLS));
	public static final RegistryObject<ModularHarness>  harnessWyvern    = ITEMS.register("wyvern_harness",    () -> new ModularHarness(DEContent.WYVERN_TOOLS));
	public static final RegistryObject<ModularHarness>  harnessDraconic  = ITEMS.register("draconic_harness",  () -> new ModularHarness(DEContent.DRACONIC_TOOLS));
	public static final RegistryObject<ModularHarness>  harnessChaotic   = ITEMS.register("chaotic_harness",   () -> new ModularHarness(DEContent.CHAOTIC_TOOLS));

// Blocks

/*

	@ModFeature(name = "armor_generator", tileEntity = TileArmorGenerator.class, itemBlock = ItemBlockBCore.class)
	public static ArmorGenerator armorGenerator = new ArmorGenerator();

	@ModFeature(name = "chaotic_armor_generator", tileEntity = TileChaoticArmorGenerator.class, itemBlock = ItemBlockBCore.class)
	public static ChaoticArmorGenerator chaoticArmorGenerator = new ChaoticArmorGenerator();

	// Tools

	@ModFeature(name = "portable_wired_charger", variantMap = {
			"0:type=basic", "1:type=wyvern", "2:type=draconic", "3:type=chaotic",
			"4:type=basicactive", "5:type=wyvernactive", "6:type=draconicactive", "7:type=chaoticactive"})
	public static PortableWiredCharger pwc = new PortableWiredCharger();
	public static ItemStack pwcBasic = new ItemStack(pwc, 1, 0);
	public static ItemStack pwcWyvern = new ItemStack(pwc, 1, 1);
	public static ItemStack pwcDraconic = new ItemStack(pwc, 1, 2);
	public static ItemStack pwcChaotic = new ItemStack(pwc, 1, 3);

	@ModFeature(name = "hermal_helm", stateOverride = "armor#type=potatoHelm", isActive = false)
	public static HermalArmor hermalHelm = new HermalArmor(0, EntityEquipmentSlot.HEAD);

	@ModFeature(name = "hermal_chest", stateOverride = "armor#type=potatoChest", isActive = false)
	public static HermalArmor hermalChest = new HermalArmor(1, EntityEquipmentSlot.CHEST);

	@ModFeature(name = "hermal_legs", stateOverride = "armor#type=potatoLegs", isActive = false)
	public static HermalArmor hermalLegs = new HermalArmor(2, EntityEquipmentSlot.LEGS);

	@ModFeature(name = "hermal_boots", stateOverride = "armor#type=potatoBoots", isActive = false)
	public static HermalArmor hermalBoots = new HermalArmor(3, EntityEquipmentSlot.FEET);

	// Other Baubles

	@ModFeature(name = "overload_belt", stateOverride = "baubles#type=overloadBelt")
	public static OverloadBelt overloadBelt = new OverloadBelt();

	@ModFeature(name = "vampiric_shirt", stateOverride = "baubles#type=vampiricShirt")
	public static VampiricShirt vampiricShirt = new VampiricShirt();

	@ModFeature(name = "inertia_cancel_ring", stateOverride = "baubles#type=inertiacancelring")
	public static InertiaCancelRing inertiaCancelRing = new InertiaCancelRing();

	// Misc / Decor

	@ModFeature(name = "chaos_crystal_stable")
	public static ChaosCrystalStable chaosCrystalStable = new ChaosCrystalStable();
 */
}

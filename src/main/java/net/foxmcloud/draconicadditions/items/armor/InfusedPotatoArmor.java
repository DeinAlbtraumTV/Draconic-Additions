package net.foxmcloud.draconicadditions.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.capability.MultiCapabilityProvider;
import com.brandon3055.brandonscore.utils.EnergyUtils;
import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.modules.lib.LimitedModuleContext;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.init.DEModules;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import com.brandon3055.draconicevolution.items.equipment.IModularItem;

import codechicken.lib.item.SimpleArmorMaterial;
import net.foxmcloud.draconicadditions.DAConfig;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class InfusedPotatoArmor extends ArmorItem implements IModularItem, IDEEquipment {

	protected static final TechLevel techLevel = TechLevel.WYVERN;
	protected Type slotType;
	protected static final SimpleArmorMaterial armorMat = new SimpleArmorMaterial(new int[] {-1, -1, -1, -1}, new int[] {1, 1, 2, 1}, 0, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.EMPTY, DraconicAdditions.MODID_PREFIX + "infused_potato_armor", 0, 0);

	// Armor Slots - 0=Feet, 1=Legs, 2=Chest, 3=Head
	public InfusedPotatoArmor(Properties props, Type slotType) {
		super(armorMat, slotType, props);
		this.slotType = slotType;
	}

	@Override
	public TechLevel getTechLevel() {
		return techLevel;
	}

	@Override
	public ModuleHostImpl createHost(ItemStack stack) {
		ModuleHostImpl host = new ModuleHostImpl(techLevel, slotType == Type.CHESTPLATE ? 2 : 1, slotType == Type.CHESTPLATE ? 2 : 1, "infused", false);
		if (slotType == Type.CHESTPLATE) {
			host.addModule(DEModules.WYVERN_SHIELD_CONTROL.get().createEntity(), new LimitedModuleContext(ItemStack.EMPTY, null, null, null));
		}
		else {
			host.addModule(DEModules.WYVERN_SHIELD_RECOVERY.get().createEntity(), new LimitedModuleContext(ItemStack.EMPTY, null, null, null));
		}
		host.setRemoveCheck((a,b) -> false);
		return host;
	}

	@Nullable
	@Override
	public ModularOPStorage createOPStorage(ItemStack stack, ModuleHostImpl host) {
		// Redstone Dust generates 20 RF/t for 30 seconds. 36 Redstone Dust is used in the recipe, so 36 * 20 * 20 * 30 = 432,000 RF.
		long capacity = (long)(432000 * DAConfig.infusedCapacityMultiplier);
		ModularOPStorage storage = new ModularOPStorage(host, capacity, 0, capacity / 64);
		storage.setExtractOnly();
		storage.modifyEnergyStored(capacity);
		return storage;
	}

	@Override
	public void handleTick(ItemStack stack, LivingEntity entity, @Nullable EquipmentSlot slot, boolean inEquipModSlot) {
		IModularItem.super.handleTick(stack, entity, slot, inEquipModSlot);
		LazyOptional<IOPStorage> energy = stack.getCapability(DECapabilities.OP_STORAGE);
		if (energy.isPresent()) {
			IOPStorage energyHost = energy.orElseThrow(IllegalStateException::new);
			if (energyHost.getOPStored() <= 1) {
				if (entity instanceof ServerPlayer player) {
					breakArmor(player, stack);
				}
			}
			else if (!(stack.getItem() instanceof InfusedPotatoArmorChest)){
				ItemStack chest = InfusedPotatoArmorChest.getChestpiece(entity);
				if (!chest.isEmpty()) {
					LazyOptional<IOPStorage> chestEnergy = chest.getCapability(DECapabilities.OP_STORAGE);
					if (energy.isPresent()) {
						IOPStorage chestEnergyHost = chestEnergy.orElseThrow(IllegalStateException::new);
						long energyDiff = energyHost.getOPStored() - chestEnergyHost.getOPStored();
						if (energyDiff > 1) {
							chestEnergyHost.modifyEnergyStored(energyDiff / 2);
							energyHost.modifyEnergyStored(-energyDiff / 2);
							if (energyHost.getOPStored() < 1000 && entity instanceof ServerPlayer player) {
								breakArmor(player, stack);
							}
						}
					}
				}
			}
		}
	}

	protected void breakArmor(ServerPlayer player, ItemStack stack) {
		player.sendSystemMessage(Component.translatable("info.da.infusedArmor.break", stack.getHoverName().getString()));
		ItemStack draconiumDust = DEContent.DUST_DRACONIUM.get().getDefaultInstance();
		draconiumDust.setCount(4);
		player.getInventory().add(draconiumDust);
		player.getInventory().removeItem(stack);
	}

	@Override
	public void initCapabilities(ItemStack stack, ModuleHostImpl host, MultiCapabilityProvider provider) {
		EquipmentManager.addCaps(stack, provider);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		EnergyUtils.addEnergyInfo(stack, tooltip);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return damageBarVisible(stack);
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return damageBarWidth(stack);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return damageBarColour(stack);
	}
}

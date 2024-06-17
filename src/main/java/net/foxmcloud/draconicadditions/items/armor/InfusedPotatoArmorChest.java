package net.foxmcloud.draconicadditions.items.armor;

import com.brandon3055.draconicevolution.items.equipment.IModularArmor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InfusedPotatoArmorChest extends InfusedPotatoArmor implements IModularArmor {

	// Armor Slots - 0=Feet, 1=Legs, 2=Chest, 3=Head
	public InfusedPotatoArmorChest(Properties props) {
		super(props, Type.CHESTPLATE);
	}

	public static ItemStack getChestpiece(LivingEntity entity) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
		if (stack.getItem() instanceof InfusedPotatoArmorChest) {
			return stack;
		}
		return ItemStack.EMPTY;
	}
}

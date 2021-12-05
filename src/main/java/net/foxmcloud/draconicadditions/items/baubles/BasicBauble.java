package net.foxmcloud.draconicadditions.items.baubles;

import com.brandon3055.brandonscore.items.ItemBCore;
import com.brandon3055.draconicevolution.entity.EntityPersistentItem;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class BasicBauble extends ItemBCore implements IBauble {

	public BasicBauble() {
		this.setMaxStackSize(1);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.TRINKET;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, EnumHand hand) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (int i = 0; i < baubles.getSlots(); i++) if ((baubles.getStackInSlot(i) == null || baubles.getStackInSlot(i).isEmpty()) && baubles.isItemValidForSlot(i, player.getHeldItem(hand), player)) {
			ItemStack bauble = player.getHeldItem(hand).splitStack(1).copy();
			player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.75f);
			baubles.setStackInSlot(i, bauble);
			break;
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack stack) {
		return new EntityPersistentItem(world, location, stack);
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.75f);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
	}
}

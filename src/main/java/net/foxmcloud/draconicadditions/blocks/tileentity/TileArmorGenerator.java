package net.foxmcloud.draconicadditions.blocks.tileentity;

import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.datamanager.ManagedBool;
import com.brandon3055.brandonscore.lib.datamanager.ManagedDouble;
import com.brandon3055.brandonscore.lib.datamanager.ManagedInt;

import cofh.redstoneflux.api.IEnergyContainerItem;
import cofh.redstoneflux.api.IEnergyProvider;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileArmorGenerator extends TileChaosHolderBase implements IEnergyProvider, ITickable, IChangeListener {

	private int burnSpeed = 50;
	private int baseRFMult = 40;

	public final ManagedInt burnTime = register(new ManagedInt("burnTime", 1)).saveToTile().saveToItem().syncViaContainer().finish();
	public final ManagedInt burnTimeRemaining = register(new ManagedInt("burnTimeRemaining", 0)).saveToTile().saveToItem().syncViaContainer().finish();
	public final ManagedDouble burnSpeedMultiplier = register(new ManagedDouble("burnSpeedMultiplier", 1.0D)).saveToTile().saveToItem().syncViaContainer().finish();
	public final ManagedBool active = register(new ManagedBool("active", false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();
	public final ManagedBool powered = register(new ManagedBool("powered", false)).saveToTile().saveToItem().syncViaTile().trigerUpdate().finish();

	public TileArmorGenerator() {
		setInventorySize(1);
		setEnergySyncMode().syncViaContainer();
		setCapacityAndTransfer(10000000, 0, 50000);
		setShouldRefreshOnBlockChange();
		setMaxChaos(0);
	}

	@Override
	public void update() {
		super.update();
		if (world.isClientSide) {
			return;
		}

		active.value = burnTimeRemaining.value > 0 && getEnergyStored() < getMaxEnergyStored();

		if (burnTimeRemaining.value > 0 && getEnergyStored() < getMaxEnergyStored()) {
			int energyGen = (int) (burnSpeed * burnSpeedMultiplier.value);
			if (burnTimeRemaining.value < energyGen) energyGen = burnTimeRemaining.value;
			burnTimeRemaining.value -= energyGen;
			energyStorage.modifyEnergyStored(energyGen);
		}

		if (burnTimeRemaining.value <= 0 && getEnergyStored() < getMaxEnergyStored() && !powered.value) {
			refuel();
		}

		energyStorage.modifyEnergyStored(-sendEnergyToAll());
	}

	public void refuel() {
		if (burnTimeRemaining.value > 0 || getEnergyStored() >= getMaxEnergyStored()) return;
		ItemStack stack = getStackInSlot(0);
		if (!stack.isEmpty() && !(stack.getItem() instanceof IEnergyContainerItem)) {
			if (stack.getItem() instanceof ItemArmor) {
				ItemArmor item = (ItemArmor) stack.getItem();
				int itemBurnTime = item.damageReduceAmount * (item.getMaxDamage(stack) - item.getDamage(stack) + 1) * baseRFMult;
				burnSpeedMultiplier.value = Math.round(item.toughness > 0 ? 1 + item.toughness : 1);
				if (stack.isItemEnchanted()) {
					NBTTagList list = stack.getEnchantmentTagList();
					if (list != null) {
						double lvls = 1.0F;
						for (int i = 0; i < list.tagCount(); i++) {
							NBTTagCompound compound = list.getCompoundTagAt(i);
							lvls += compound.getShort("lvl") / 5.0D;
						}
						itemBurnTime *= lvls;
						burnSpeedMultiplier.value = burnSpeedMultiplier.value * lvls;
					}
				}
				if (itemBurnTime > 0) {
					if (stack.getCount() == 1) {
						stack = stack.getItem().getContainerItem(stack);
					}
					else {
						stack.shrink(1);
					}
					setInventorySlotContents(0, stack);
					burnSpeedMultiplier.value *= (1 + (chaos.value / 80D));
					burnTime.value = (int) (itemBurnTime * (1 + (chaos.value / 80D)));
					burnTimeRemaining.value = burnTime.value;
					if (chaos.value > 0) {
						chaos.value -= (int) Math.floor(Math.random() * (chaos.value / 100));
					}
				}
			}
			else throw new Error("There was a non-armor item in a Armor Generator!  This should never happen...");
		}
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return super.extractEnergy(from, maxExtract, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return super.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return super.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == 0 && stack.getItem() instanceof ItemArmor) {
			return true;
		}
		else return false;
	}

	@Override
	public void onNeighborChange(BlockPos neighbor) {
		powered.value = world.isBlockPowered(pos);
	}
}

package net.foxmcloud.draconicadditions.handlers;

import java.util.ArrayList;
import java.util.Arrays;

import com.brandon3055.draconicevolution.api.capability.DECapabilities;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.event.ModularItemInitEvent;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.client.gui.modular.ModularItemGui;
import com.brandon3055.draconicevolution.client.gui.modular.itemconfig.ConfigurableItemGui;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.items.equipment.IModularArmor;

import net.covers1624.quack.util.SneakyUtils;
import net.foxmcloud.draconicadditions.CommonMethods.BlockStorage;
import net.foxmcloud.draconicadditions.items.IChaosContainer;
import net.foxmcloud.draconicadditions.items.curios.ModularHarness;
import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.modules.ModuleTypes;
import net.foxmcloud.draconicadditions.modules.entities.ChaosInjectorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class DAEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Level world = event.getLevel();
		if (world.isClientSide) {
			return;
		}
		Player player = event.getEntity();
		boolean handsAreEmpty = player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty();
		if (!handsAreEmpty || !player.isShiftKeyDown() || !event.getPos().closerToCenterThan(player.position(), 2)) {
			return;
		}
		ISlotType backSlot = CuriosApi.getSlotHelper().getSlotType(SlotTypePreset.BACK.getIdentifier()).get();
		IDynamicStackHandler stackHandler = CuriosApi.getCuriosHelper().getCuriosHandler(player).orElse(null).getStacksHandler(backSlot.getIdentifier()).get().getStacks();
		if (stackHandler.getSlots() == 0) {
			return;
		}
		ItemStack harness = ItemStack.EMPTY;
		for (int i = 0; i < stackHandler.getSlots(); i++) {
			if (stackHandler.getStackInSlot(i).getItem() instanceof ModularHarness) {
				harness = stackHandler.getStackInSlot(i);
				break;
			}
		}
		if (harness.equals(ItemStack.EMPTY)) {
			return;
		}
		BlockPos abovePos = event.getPos().above();
		BlockState aboveState = world.getBlockState(abovePos);
		if (ModularHarness.hasAttachedBlockEntity(harness, world)) {
			if (event.getFace() != Direction.UP || aboveState.getBlock() != Blocks.AIR) {
				return;
			}
			Vec2 pRot = player.getRotationVector();
			Vec2 rotation = new Vec2(-pRot.x, pRot.y + 180);
			if (BlockStorage.restoreBlockFromTag(world, abovePos, rotation, harness.getTag(), true, true)) {
				player.displayClientMessage(Component.translatable("info.da.modular_harness.placeSuccess"), true);
				event.setCanceled(true);
			}
		}
		else if (world.getBlockState(event.getPos()).hasBlockEntity()) {
			if (ModularHarness.storeBlockEntity(world, event.getPos(), harness, player, true)) {
				player.displayClientMessage(Component.translatable("info.da.modular_harness.storeSuccess"), true);
			}
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public boolean chaosInjectionDeath(LivingDeathEvent event) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(event.getEntity());
		if (injector != null && injector.isChaosInBlood()) {
			event.setCanceled(true);
			return true;
		}
		return false;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public boolean blockHealingWhenInjecting(LivingHealEvent event) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(event.getEntity());
		if (injector != null && (injector.getRate() > 0 || injector.isChaosInBlood())) {
			event.setCanceled(true);
			return true;
		}
		return false;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public boolean blockShieldDamageWhenInjecting(LivingAttackEvent event) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(event.getEntity());
		if (injector != null && injector.isChaosInBlood()) {
			event.setCanceled(true);
			return true;
		}
		return false;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public boolean blockShieldDamageWhenInjecting(LivingDamageEvent event) {
		ChaosInjectorEntity injector = ChaosInjectorEntity.getInjectorEntity(event.getEntity());
		if (injector != null && injector.isChaosInBlood()) {
			event.setCanceled(true);
			return true;
		}
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void blockChaosItemMoving(ScreenEvent.MouseButtonPressed.Pre event) {
		if (!(event.getScreen() instanceof AbstractContainerScreen)) {
			return;
		}
		AbstractContainerScreen inventory = (AbstractContainerScreen)event.getScreen();
		Slot slot = inventory.getSlotUnderMouse();
		if (slot == null) {
			return;
		}
		ItemStack stack = slot.getItem();
		if (stack == null) {
			return;
		}
		if ((inventory instanceof ModularItemGui.Screen || inventory instanceof ConfigurableItemGui.Screen) && event.getButton() != 1) {
			return;
		}
		Minecraft mc = inventory.getMinecraft();
		LocalPlayer player = mc.player;
		if (stack.getItem() instanceof ChaosContainer && !player.isCreative() && ((ChaosContainer) stack.getItem()).getChaos(stack) > 0) {
			player.displayClientMessage(Component.translatable("info.da.chaos.cantmove", stack.getHoverName()), true);
			event.setCanceled(true);
		}
		else if (stack.getItem() instanceof IModularArmor && !player.isCreative()) {
			LazyOptional<ModuleHost> cap = stack.getCapability(DECapabilities.MODULE_HOST_CAPABILITY);
			if (!cap.isPresent()) {
				return;
			}
			ModuleHost host = cap.orElseThrow(IllegalStateException::new);
			ArrayList<ChaosInjectorEntity> entities = ChaosInjectorEntity.getSortedListFromStream(host.getEntitiesByType(ModuleTypes.CHAOS_INJECTOR));
			if (entities.isEmpty()) {
				return;
			}
			if (entities.get(0) != null && (entities.get(0).isChaosInBlood() || entities.get(0).getRate() > 0)) {
				player.displayClientMessage(Component.translatable("info.da.chaos.cantmove", stack.getHoverName()), true);
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void addCategoriesToContainers(ModularItemInitEvent e) {
		ArrayList<Item> validChaosContainers = new ArrayList<Item>(Arrays.asList(
			DEContent.AXE_CHAOTIC.get(),
			DEContent.BOW_CHAOTIC.get(),
			DEContent.CHESTPIECE_CHAOTIC.get(),
			DEContent.PICKAXE_CHAOTIC.get(),
			DEContent.SHOVEL_CHAOTIC.get(),
			DEContent.STAFF_CHAOTIC.get(),
			DEContent.SWORD_CHAOTIC.get()
		));
		ItemStack stack = e.getStack();
		if (validChaosContainers.contains(stack.getItem())) {
			ModuleHostImpl host = SneakyUtils.unsafeCast(e.getHost());
			host.addCategories(IChaosContainer.CHAOS_CONTAINER);
		}
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinLevelEvent e) {
		if (!(e.getEntity() instanceof ItemEntity)) {
			return;
		}
		ItemEntity entity = (ItemEntity)e.getEntity();
		ItemStack stack = entity.getItem();
		if (stack.getItem() != DEContent.DRAGON_HEART.get()) {
			return;
		}
		CompoundTag nbt = entity.getPersistentData();
		if (nbt != null && nbt.contains("guardian_heart") && nbt.getBoolean("guardian_heart")) {
			entity.setItem(DAContent.chaosHeart.get().getDefaultInstance());
		}
	}
}

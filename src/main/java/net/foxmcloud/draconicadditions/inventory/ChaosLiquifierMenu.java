package net.foxmcloud.draconicadditions.inventory;

import codechicken.lib.gui.modular.lib.container.SlotGroup;
import codechicken.lib.inventory.container.modular.ModularSlot;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosLiquifier;
import net.foxmcloud.draconicadditions.items.tools.ChaosContainer;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ChaosLiquifierMenu extends DATileMenu<TileChaosLiquifier> {
	
	public final SlotGroup main = createSlotGroup(0, 2, 1);
	public final SlotGroup hotBar = createSlotGroup(0, 2, 1);
	public final SlotGroup slot = createSlotGroup(1, 0);
	public final SlotGroup capacitor = createSlotGroup(2, 0);
	
	public ChaosLiquifierMenu(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(windowId, playerInv, getClientTile(playerInv, extraData));
    }

	public ChaosLiquifierMenu(int windowId, Inventory playerInv, TileChaosLiquifier tile) {
        super(DAContent.menuChaosLiquifier.get(), windowId, playerInv, tile); //TODO: Fix the instanceof below.
        main.addPlayerMain(inventory);
        hotBar.addPlayerBar(inventory);
		slot.addSlot(new ModularSlot(tile.itemHandler, 0).setValidator((stack) -> {return tile.isItemValidForSlot(0, stack);}));
		capacitor.addSlot(new ModularSlot(tile.itemHandler, 1).setValidator((stack) -> {return tile.isItemValidForSlot(1, stack);}));
    }
}

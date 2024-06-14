package net.foxmcloud.draconicadditions.inventory;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ChaosLiquifierMenu extends ChaosBaseMenu {
	
	public ChaosLiquifierMenu(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(windowId, playerInv, getClientTile(playerInv, extraData));
    }
	
	public ChaosLiquifierMenu(int windowId, Inventory playerInv, TileChaosHolderBase tile) {
		super(DAContent.menuChaosLiquifier.get(), windowId, playerInv, tile);
	}
}

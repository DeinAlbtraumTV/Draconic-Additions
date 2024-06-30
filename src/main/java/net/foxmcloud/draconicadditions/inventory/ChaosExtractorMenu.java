package net.foxmcloud.draconicadditions.inventory;

import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ChaosExtractorMenu extends ChaosBaseMenu {
	
	public ChaosExtractorMenu(int windowId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(windowId, playerInv, getClientTile(playerInv, extraData));
    }
	
	public ChaosExtractorMenu(int windowId, Inventory playerInv, TileChaosHolderBase tile) {
		super(DAContent.menuChaosExtractor.get(), windowId, playerInv, tile);
	}
}

package net.foxmcloud.draconicadditions.client.gui;

import com.brandon3055.brandonscore.client.gui.GuiToolkit;

import codechicken.lib.gui.modular.ModularGuiContainer;
import codechicken.lib.gui.modular.lib.GuiRender;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.inventory.ChaosBaseMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GUIChaosCrystalizer extends GUIChaosBase {
	@Override
	protected GuiToolkit getToolkit() {
		return new GuiToolkit("gui." + DraconicAdditions.MODID + ".chaos_crystalizer");
	}
	
	public static class Screen extends ModularGuiContainer<ChaosBaseMenu> {
		public Screen(ChaosBaseMenu menu, Inventory inv, Component title) {
			super(menu, inv, new GUIChaosCrystalizer());
			getModularGui().setGuiTitle(title);
		}

		@Override
		public void renderFloatingItem(GuiRender render, ItemStack itemStack, int x, int y, String string) {
			GUIChaosBase gui = (GUIChaosBase)modularGui.getProvider();
			if (!gui.gridPanel.gridRenderer.renderStackOverride(render, itemStack, x, y, string)) {
				super.renderFloatingItem(render, itemStack, x, y, string);
			}
		}
	}
}
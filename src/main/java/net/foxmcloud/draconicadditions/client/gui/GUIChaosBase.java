package net.foxmcloud.draconicadditions.client.gui;

import static codechicken.lib.gui.modular.lib.geometry.Constraint.*;
import static codechicken.lib.gui.modular.lib.geometry.GeoParam.*;

import com.brandon3055.brandonscore.client.BCGuiTextures;
import com.brandon3055.brandonscore.client.gui.GuiToolkit;
import com.brandon3055.brandonscore.client.gui.modulargui.ShaderEnergyBar.EnergyBar;
import com.brandon3055.brandonscore.client.gui.modulargui.templates.ButtonRow;
import com.brandon3055.draconicevolution.client.DEGuiTextures;
import com.brandon3055.draconicevolution.client.gui.ModuleGridPanel;

import codechicken.lib.gui.modular.ModularGui;
import codechicken.lib.gui.modular.ModularGuiContainer;
import codechicken.lib.gui.modular.elements.GuiElement;
import codechicken.lib.gui.modular.elements.GuiManipulable;
import codechicken.lib.gui.modular.elements.GuiSlots;
import codechicken.lib.gui.modular.elements.GuiText;
import codechicken.lib.gui.modular.elements.GuiTexture;
import codechicken.lib.gui.modular.elements.GuiSlots.Player;
import codechicken.lib.gui.modular.lib.Constraints;
import codechicken.lib.gui.modular.lib.GuiRender;
import codechicken.lib.gui.modular.lib.Constraints.LayoutPos;
import codechicken.lib.gui.modular.lib.container.ContainerGuiProvider;
import codechicken.lib.gui.modular.lib.container.ContainerScreenAccess;
import codechicken.lib.gui.modular.lib.geometry.Direction;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.inventory.ChaosBaseMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GUIChaosBase extends ContainerGuiProvider<ChaosBaseMenu> {
	public static final int GUI_WIDTH = 220;
	public static final int GUI_HEIGHT = 180;
	protected ModuleGridPanel gridPanel;

	@Override
	public GuiElement<?> createRootElement(ModularGui gui) {
		GuiManipulable root = new GuiManipulable(gui).addMoveHandle(3).enableCursors(true);
		GuiTexture bg = new GuiTexture(root.getContentElement(), DEGuiTextures.themedGetter("generator"));
		Constraints.bind(bg, root.getContentElement());
		return root;
	}

	@Override
	public void buildGui(ModularGui gui, ContainerScreenAccess<ChaosBaseMenu> screenAccess) {
		GuiToolkit toolkit = getToolkit();
		gui.initStandardGui(GUI_WIDTH, GUI_HEIGHT);
		ChaosBaseMenu menu = screenAccess.getMenu();
		TileChaosHolderBase tile = menu.tile;
		GuiElement<?> root = gui.getRoot();
		GuiText title = toolkit.createHeading(root, gui.getGuiTitle(), true);
		title.autoHeight();

		GuiText chaos = toolkit.createHeading(root, null);
		chaos.setTextSupplier(() -> Component.translatable("info.da.storedChaos", tile.chaos.get(), tile.getMaxChaos()));
		chaos.autoHeight().setWidth(GUI_WIDTH);
		Constraints.placeOutside(chaos, title, LayoutPos.BOTTOM_CENTER, 0, 4);
		
		gridPanel = new ModuleGridPanel(root, menu).setGridPos(ModuleGridPanel.GridPos.TOP_RIGHT, 2);
		gridPanel.setTooltipSingle(() -> Component.translatable("info.da.maxChaosInfo"));
		gridPanel.setEnableToolTip(() -> {return tile.getMaxChaos() == 0;});

		ButtonRow buttonRow = ButtonRow.topRightInside(root, Direction.DOWN, 3, 3).setSpacing(1);
		buttonRow.addButton(e -> toolkit.createThemeButton(e));
		buttonRow.addButton(e -> toolkit.createRSSwitch(e, screenAccess.getMenu().tile));
		buttonRow.addButton(e -> toolkit.createThemedIconButton(e, "grid_small").onPress(gridPanel::toggleExpanded));

		Player playInv = GuiSlots.player(root, screenAccess, menu.main, menu.hotBar);
		playInv.stream().forEach(e -> e.setSlotTexture(slot -> BCGuiTextures.getThemed("slot")));
		Constraints.placeInside(playInv.container(), root, Constraints.LayoutPos.BOTTOM_CENTER, 0, -7);
		toolkit.playerInvTitle(playInv.container());
		
		GuiSlots inv = new GuiSlots(root, screenAccess, menu.slot, 1)
				.setTooltip(toolkit.translate("chaosSlot.hover"))
				.setEnableToolTip(() -> tile.itemHandler.getStackInSlot(0).isEmpty())
				.setSlotTexture(slot -> BCGuiTextures.getThemed("slot"));
		double invYOffset = (playInv.container().getValue(TOP) - root.getValue(TOP)) / 2 - (inv.getValue(HEIGHT) / 2);
		Constraints.placeInside(inv, root, Constraints.LayoutPos.TOP_CENTER, 0, invYOffset);

		GuiSlots capInv = GuiSlots.singleSlot(root, screenAccess, menu.capacitor, 0)
				.setSlotTexture(slot -> BCGuiTextures.getThemed("slot"))
				.setEmptyIcon(BCGuiTextures.get("slots/energy"));

		//Energy Bar
		EnergyBar energyBar = toolkit.createEnergyBar(root, tile.opStorage);
		energyBar.container()
		.constrain(TOP, relative(root.get(TOP), 6))
		.constrain(BOTTOM, relative(playInv.container().get(TOP), -14))
		.constrain(LEFT, relative(root.get(LEFT), 6))
		.constrain(WIDTH, literal(14));
		Constraints.placeInside(capInv, energyBar.container(), Constraints.LayoutPos.BOTTOM_RIGHT, 20, 0);
		Constraints.placeOutside(toolkit.energySlotArrow(root, true, false), capInv, Constraints.LayoutPos.TOP_CENTER, -2, -2);
	}
	
	protected GuiToolkit getToolkit() {
		return null;
	}
}

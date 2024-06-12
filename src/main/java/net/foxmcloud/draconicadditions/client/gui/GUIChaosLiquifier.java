package net.foxmcloud.draconicadditions.client.gui;

import static codechicken.lib.gui.modular.lib.geometry.Constraint.*;
import static codechicken.lib.gui.modular.lib.geometry.GeoParam.*;
import com.brandon3055.brandonscore.client.BCGuiTextures;
import com.brandon3055.brandonscore.client.gui.GuiToolkit;
import com.brandon3055.brandonscore.client.gui.modulargui.templates.ButtonRow;
import com.brandon3055.draconicevolution.client.DEGuiTextures;
import codechicken.lib.gui.modular.ModularGui;
import codechicken.lib.gui.modular.ModularGuiContainer;
import codechicken.lib.gui.modular.elements.GuiElement;
import codechicken.lib.gui.modular.elements.GuiManipulable;
import codechicken.lib.gui.modular.elements.GuiSlots;
import codechicken.lib.gui.modular.elements.GuiSlots.Player;
import codechicken.lib.gui.modular.elements.GuiTexture;
import codechicken.lib.gui.modular.lib.Constraints;
import codechicken.lib.gui.modular.lib.container.ContainerGuiProvider;
import codechicken.lib.gui.modular.lib.container.ContainerScreenAccess;
import codechicken.lib.gui.modular.lib.geometry.Direction;
import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.foxmcloud.draconicadditions.blocks.tileentity.TileChaosHolderBase;
import net.foxmcloud.draconicadditions.inventory.ChaosLiquifierMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GUIChaosLiquifier extends ContainerGuiProvider<ChaosLiquifierMenu> {
    private static final GuiToolkit TOOLKIT = new GuiToolkit("gui." + DraconicAdditions.MODID + ".chaos_liquifier");
    public static final int GUI_WIDTH = 176;
    public static final int GUI_HEIGHT = 166;

    @Override
    public GuiElement<?> createRootElement(ModularGui gui) {
        GuiManipulable root = new GuiManipulable(gui).addMoveHandle(3).enableCursors(true);
        GuiTexture bg = new GuiTexture(root.getContentElement(), DEGuiTextures.themedGetter("generator"));
        Constraints.bind(bg, root.getContentElement());
        return root;
    }

    @Override
    public void buildGui(ModularGui gui, ContainerScreenAccess<ChaosLiquifierMenu> screenAccess) {
        gui.initStandardGui(GUI_WIDTH, GUI_HEIGHT);
        ChaosLiquifierMenu menu = screenAccess.getMenu();
        TileChaosHolderBase tile = menu.tile;
        GuiElement<?> root = gui.getRoot();
        TOOLKIT.createHeading(root, gui.getGuiTitle(), true);

        ButtonRow buttonRow = ButtonRow.topRightInside(root, Direction.DOWN, 3, 3).setSpacing(1);
        buttonRow.addButton(e -> TOOLKIT.createThemeButton(e));
        buttonRow.addButton(e -> TOOLKIT.createRSSwitch(e, screenAccess.getMenu().tile));

        GuiSlots inv = new GuiSlots(root, screenAccess, menu.slot, 1).setSlotTexture(slot -> BCGuiTextures.getThemed("slot"));
        Constraints.placeInside(inv, root, Constraints.LayoutPos.TOP_CENTER, 0, 32);

        Player playInv = GuiSlots.player(root, screenAccess, menu.main, menu.hotBar);
        playInv.stream().forEach(e -> e.setSlotTexture(slot -> BCGuiTextures.getThemed("slot")));
        Constraints.placeInside(playInv.container(), root, Constraints.LayoutPos.BOTTOM_CENTER, 0, -7);
        TOOLKIT.playerInvTitle(playInv.container());

        GuiSlots capInv = GuiSlots.singleSlot(root, screenAccess, menu.capacitor, 0)
                .setSlotTexture(slot -> BCGuiTextures.getThemed("slot"))
                .setEmptyIcon(BCGuiTextures.get("slots/energy"));

        //Energy Bar
        var energyBar = TOOLKIT.createEnergyBar(root, tile.opStorage);
        energyBar.container()
                .constrain(TOP, relative(root.get(TOP), 6))
                .constrain(BOTTOM, relative(playInv.container().get(TOP), -14))
                .constrain(LEFT, match(playInv.container().get(LEFT)))
                .constrain(WIDTH, literal(14));
        Constraints.placeInside(capInv, energyBar.container(), Constraints.LayoutPos.BOTTOM_RIGHT, 20, 0);
        Constraints.placeOutside(TOOLKIT.energySlotArrow(root, true, false), capInv, Constraints.LayoutPos.TOP_CENTER, -2, -2);
    }

    public static class Screen extends ModularGuiContainer<ChaosLiquifierMenu> {
        public Screen(ChaosLiquifierMenu menu, Inventory inv, Component title) {
            super(menu, inv, new GUIChaosLiquifier());
            getModularGui().setGuiTitle(title);
        }
    }
}
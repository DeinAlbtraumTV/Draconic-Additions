package net.foxmcloud.draconicadditions;

import net.foxmcloud.draconicadditions.client.gui.GUIChaosCrystalizer;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosExtractor;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosInfuser;
import net.foxmcloud.draconicadditions.client.gui.GUIChaosLiquifier;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientInit {
	public static void init() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(ClientInit::clientSetupEvent);
	}
	
	private static void clientSetupEvent(FMLClientSetupEvent event) {
		MenuScreens.register(DAContent.menuChaosLiquifier.get(), GUIChaosLiquifier.Screen::new);
		MenuScreens.register(DAContent.menuChaosInfuser.get(), GUIChaosInfuser.Screen::new);
		MenuScreens.register(DAContent.menuChaosExtractor.get(), GUIChaosExtractor.Screen::new);
		MenuScreens.register(DAContent.menuChaosCrystalizer.get(), GUIChaosCrystalizer.Screen::new);
	}
}

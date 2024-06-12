package net.foxmcloud.draconicadditions;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.utils.LogHelperBC;
import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.utils.LogHelper;

import net.foxmcloud.draconicadditions.handlers.DAEventHandler;
import net.foxmcloud.draconicadditions.integration.AE2Compat;
import net.foxmcloud.draconicadditions.lib.DAContent;
import net.foxmcloud.draconicadditions.lib.DACreativeTabs;
import net.foxmcloud.draconicadditions.lib.DAModules;
import net.foxmcloud.draconicadditions.lib.DASounds;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(DraconicAdditions.MODID)
public class DraconicAdditions {
	public static final String MODID = "draconicadditions";
	public static final String NAME = "Draconic Additions";
	public static final String VERSION = "${mod_version}";
	public static final String MODID_PREFIX = MODID + ":";

	public static Logger logger = LogManager.getLogger(DraconicAdditions.MODID);

	public DraconicAdditions() {
		runChecks();
		DAConfig.load();
		DAContent.init();
		DAModules.init();
		DASounds.init();
		DACreativeTabs.init();
		AE2Compat.init();
		MinecraftForge.EVENT_BUS.register(new DAEventHandler());
		//FusionCostMultiplier.init();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);
	}
	
	@SuppressWarnings("deprecation")
	private static void runChecks() {
		if (ModList.get().isLoaded("draconicevolution")) {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Core!  How's it going?");
			BrandonsCore.LOGGER.log(Level.INFO, "Oh hey, doin good. just looking for DE.");
			DraconicAdditions.logger.log(Level.INFO, "Yeah, just finished talking to them.  They're through the door behind me.  Speaking of locating mods, have you seen Curios?");
			if (ModList.get().isLoaded("curios")) {
				BrandonsCore.LOGGER.log(Level.INFO, "Yup, just go up and to the left a bit, cant miss it");
				DraconicAdditions.logger.log(Level.INFO, "Thanks!  Just be careful, DE seems a bit...  Unstable.");
				BrandonsCore.LOGGER.log(Level.INFO, "Sure sure....");
			}
			else {
				BrandonsCore.LOGGER.log(Level.INFO, "Why should i know that?");
				DraconicEvolution.LOGGER.log(Level.WARN, "Calculating explosion ETA");
				BrandonsCore.LOGGER.log(Level.INFO, "DE? What are you, ahh... NO... NONONO! DONT DO THAT!!! STOP THIS NOW!");
				DraconicEvolution.LOGGER.log(Level.WARN, "**Explosion Imminent!!!**");
				DraconicAdditions.logger.log(Level.ERROR, "Brandon, WHAT HAVE YOU DONE?!");
				throw new Error("Curios is not loaded.  It is required for Draconic Additions to work.");
			}
		}
		else {
			DraconicAdditions.logger.log(Level.INFO, "Hey, Brandon's Core!  How's it going?  Just looking for Draconic Evolution.  Seen 'em around?");
			BrandonsCore.LOGGER.log(Level.INFO, "No but at least we wont literally die from his explosions.");
			DraconicAdditions.logger.log(Level.WARN, "Wait, really?  He's not here?!");
			BrandonsCore.LOGGER.log(Level.INFO, "Not my problem.");
			DraconicAdditions.logger.log(Level.ERROR, "But...  But...  I can't do my job if he doesn't show up!");
			BrandonsCore.LOGGER.log(Level.INFO, "Sorry man cant help you there.");
			throw new Error("Draconic Evolution is not loaded.  It is required for Draconic Additions to work.");
		}
	}
}

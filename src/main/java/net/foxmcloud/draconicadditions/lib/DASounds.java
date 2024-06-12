package net.foxmcloud.draconicadditions.lib;

import com.brandon3055.draconicevolution.handlers.DESounds;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Created by FoxMcloud5655 on 11/27/2019.
 * This stores all of the sound events for Draconic Additions.
 */

public class DASounds extends DESounds {
	
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DraconicAdditions.MODID);

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SOUNDS.register(eventBus);
    }

	public static final RegistryObject<SoundEvent> hermal = SOUNDS.register("hermal", () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(DraconicAdditions.MODID, "hermal"), 16F));
	public static final RegistryObject<SoundEvent> unplug = SOUNDS.register("unplug", () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(DraconicAdditions.MODID, "unplug"), 16F));
	public static final RegistryObject<SoundEvent> boom   = SOUNDS.register("boom",   () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(DraconicAdditions.MODID, "boom"), 16F));
}

package net.foxmcloud.draconicadditions.modules.data;

import java.util.Map;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import net.minecraft.network.chat.Component;

public class StableChaosData implements ModuleData<StableChaosData> {
	private int maxInstability;
	private final int maxChaos;

	public StableChaosData(int maxInstability, int maxChaos) {
		this.maxInstability = maxInstability;
		this.maxChaos = maxChaos;
	}

	public int getMaxInstability() {
		return maxInstability;
	}

	public int getMaxChaos() {
		return maxChaos;
	}

	@Override
	public StableChaosData combine(StableChaosData other) {
		return new StableChaosData(maxInstability + other.maxInstability, maxChaos + other.maxChaos);
	}

	@Override
	public void addInformation(Map<Component, Component> map, ModuleContext context) {
		map.put(Component.translatable("module.draconicadditions.maxChaos.name"), Component.translatable("module.draconicadditions.maxChaos.value", maxChaos));
		map.put(Component.translatable("module.draconicadditions.maxInstability.name"), Component.translatable("module.draconicadditions.maxInstability.value", maxInstability));
	}
}

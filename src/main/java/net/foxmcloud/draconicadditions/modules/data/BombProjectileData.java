package net.foxmcloud.draconicadditions.modules.data;

import java.util.Map;

import com.brandon3055.draconicevolution.api.modules.data.ModuleData;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleContext;

import net.minecraft.network.chat.Component;

public class BombProjectileData implements ModuleData<BombProjectileData> {
	private final TYPE type;
	private final int radius;

	public BombProjectileData(TYPE type, int radius) {
		this.type = type;
		this.radius = radius;
	}

	public TYPE getDamageType() {
		return type;
	}
	
	public int getRadius() {
		return radius;
	}

	@Override
	public BombProjectileData combine(BombProjectileData other) {
		return new BombProjectileData(TYPE.valueOf(Math.max(type.ordinal(), other.type.ordinal())), radius + other.radius);
	}

	@Override
	public void addInformation(Map<Component, Component> map, ModuleContext context) {
		map.put(Component.translatable("module.draconicadditions.bomb_projectile.name"), Component.translatable("module.draconicadditions.bomb_projectile.radius", radius));
	}
	
	public enum TYPE {
		NORMAL,
		ELECTRIC,
		CHAOTIC;
		
		public static TYPE valueOf(int ordinal) {
			return ordinal == 0 ? NORMAL : ordinal == 1 ? ELECTRIC : ordinal == 2 ? CHAOTIC : null;
		}
	}
}

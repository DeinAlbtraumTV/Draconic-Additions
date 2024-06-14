package net.foxmcloud.draconicadditions.lib;

import java.util.ArrayList;
import java.util.List;

import com.brandon3055.brandonscore.lib.CustomTabHandling;
import com.brandon3055.draconicevolution.init.DECreativeTabs;

import net.foxmcloud.draconicadditions.DraconicAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class DACreativeTabs extends DECreativeTabs {

    public static void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(DACreativeTabs::registerTabs);
    }

    private static void registerTabs(RegisterEvent event) {
        event.register(Registries.CREATIVE_MODE_TAB, helper -> {
            List<ItemStack> icons = new ArrayList<>();
            helper.register(new ResourceLocation(DraconicAdditions.MODID, "items"), CreativeModeTab.builder().title(Component.translatable("itemGroup.draconicadditions.items"))
                            .displayItems((params, output) -> {
                                for (ResourceLocation key : ForgeRegistries.ITEMS.getKeys()) {
                                    if (key.getNamespace().equals(DraconicAdditions.MODID)) {
                                        Item item = ForgeRegistries.ITEMS.getValue(key);
                                        if (item instanceof CustomTabHandling || item == null) continue;
                                        output.accept(item);
                                        icons.add(new ItemStack(item));
                                    }
                                }
                            })
                            .withTabFactory(builder -> new CyclingTab(builder, icons))
                            .build()
            );
        });
    }

    private static class CyclingTab extends CreativeModeTab {
        private final List<ItemStack> stacks;

        public CyclingTab(CreativeModeTab.Builder builder, List<ItemStack> stacks) {
            super(builder);
            this.stacks = stacks;
        }

        @Override
        public ItemStack getIconItem() {
            int idx = (int) (System.currentTimeMillis() / 1200) % stacks.size();
            return stacks.get(idx);
        }
    }
}

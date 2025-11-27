
package com.king_tajin.repo_love_potion.init;

import com.king_tajin.repo_love_potion.RepoLovePotion;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class RepoLovePotionModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RepoLovePotion.MODID);

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(RepoLovePotionItems.LOVE_POTION.get());
			tabData.accept(RepoLovePotionItems.I_LOVE_DISC.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
			tabData.accept(RepoLovePotionItems.LOVE_CORE.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			tabData.accept(RepoLovePotionItems.HEART_JAR.get());
			tabData.accept(RepoLovePotionItems.UNPOLISHED_LOVE_CORE.get());
			tabData.accept(RepoLovePotionItems.LOVE_DUST.get());
		}
	}
}

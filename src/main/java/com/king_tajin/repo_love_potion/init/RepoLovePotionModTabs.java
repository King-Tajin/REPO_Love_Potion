
package com.king_tajin.repo_love_potion.init;

import com.king_tajin.repo_love_potion.RepoLovePotion;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RepoLovePotionModTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RepoLovePotion.MODID);

    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(RepoLovePotionItems.LOVE_POTION.get());
            event.accept(RepoLovePotionItems.I_LOVE_DISC.get());
        } else if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(RepoLovePotionItems.LOVE_CORE.get());
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(RepoLovePotionItems.HEART_JAR.get());
            event.accept(RepoLovePotionItems.UNPOLISHED_LOVE_CORE.get());
            event.accept(RepoLovePotionItems.LOVE_DUST.get());
        }
    }
}
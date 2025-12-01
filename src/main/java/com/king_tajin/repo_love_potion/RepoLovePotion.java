package com.king_tajin.repo_love_potion;

import com.king_tajin.repo_love_potion.events.*;
import com.king_tajin.repo_love_potion.init.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import java.util.*;


@Mod("repo_love_potion")
public class RepoLovePotion {
	public static final String MODID = "repo_love_potion";

	public RepoLovePotion(IEventBus modEventBus) {
		RepoLovePotionSounds.REGISTRY.register(modEventBus);

		RepoLovePotionItems.REGISTRY.register(modEventBus);

		RepoLovePotionModTabs.REGISTRY.register(modEventBus);

		RepoLovePotionMobEffects.REGISTRY.register(modEventBus);

		RepoLovePotionParticleTypes.REGISTRY.register(modEventBus);

		NeoForge.EVENT_BUS.register(PlayerDeathHandler.class);

		NeoForge.EVENT_BUS.register(new PlayerLoveEffectHandler());

		NeoForge.EVENT_BUS.register(LoveCropGrowthHandler.class);

		NeoForge.EVENT_BUS.register(CreeperLoveAIHandler.class);

        NeoForge.EVENT_BUS.register(ChargedCreeperDropHandler.class);

        NeoForge.EVENT_BUS.register(LovePotionBrewingRecipe.class);

        modEventBus.addListener(RepoLovePotionModTabs::buildTabContentsVanilla);
        modEventBus.addListener(RepoLovePotionParticles::onRegisterParticles);

	}

}


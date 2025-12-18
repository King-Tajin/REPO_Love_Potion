package com.king_tajin.repo_love_potion;

import com.king_tajin.repo_love_potion.config.RepoLovePotionConfig;
import com.king_tajin.repo_love_potion.events.*;
import com.king_tajin.repo_love_potion.init.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import java.util.*;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod("repo_love_potion")
public class RepoLovePotion {
	public static final String MODID = "repo_love_potion";

	public static RepoLovePotionConfig CONFIG;

	public RepoLovePotion(IEventBus modEventBus) {
		AutoConfig.register(RepoLovePotionConfig.class, GsonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(RepoLovePotionConfig.class).getConfig();

		RepoLovePotionSounds.REGISTRY.register(modEventBus);

		RepoLovePotionItems.REGISTRY.register(modEventBus);

		RepoLovePotionModTabs.REGISTRY.register(modEventBus);

		RepoLovePotionMobEffects.REGISTRY.register(modEventBus);

		RepoLovePotionParticleTypes.REGISTRY.register(modEventBus);

		NeoForge.EVENT_BUS.register(PlayerDeathHandler.class);

		NeoForge.EVENT_BUS.register(new PlayerLoveEffectHandler());

		NeoForge.EVENT_BUS.register(LoveCropGrowthHandler.class);

		NeoForge.EVENT_BUS.register(CreeperLoveAIHandler.class);

        NeoForge.EVENT_BUS.register(JukeboxFlowerSpawnHandler.class);

        NeoForge.EVENT_BUS.register(ChargedCreeperDropHandler.class);

        NeoForge.EVENT_BUS.register(LovePotionBrewingRecipe.class);

        modEventBus.addListener(RepoLovePotionModTabs::buildTabContentsVanilla);
        modEventBus.addListener(RepoLovePotionParticles::onRegisterParticles);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			ModLoadingContext.get().registerExtensionPoint(
					IConfigScreenFactory.class,
					() -> (mc, parent) ->
							AutoConfig.getConfigScreen(RepoLovePotionConfig.class, parent).get()
			);
		}

	}

}


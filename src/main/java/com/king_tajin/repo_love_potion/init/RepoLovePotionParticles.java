package com.king_tajin.repo_love_potion.init;

import com.roland.repolovepotion.client.particle.LoveParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RepoLovePotionParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(RepoLovePotionParticleTypes.LOVE_PARTICLE.get(), LoveParticle::provider);
		event.registerSpriteSet(RepoLovePotionParticleTypes.BLANK_PARTICLE.get(), LoveParticle::provider);
	}
}

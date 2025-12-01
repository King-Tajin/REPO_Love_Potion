package com.king_tajin.repo_love_potion.init;

import com.king_tajin.repo_love_potion.client.particle.LoveParticle;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

public class RepoLovePotionParticles {
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                RepoLovePotionParticleTypes.LOVE_PARTICLE.get(),
                LoveParticle::provider
        );
    }
}
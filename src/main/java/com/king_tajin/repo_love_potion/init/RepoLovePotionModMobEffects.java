
package com.king_tajin.repo_love_potion.init;

import com.king_tajin.repo_love_potion.RepoLovePotion;
import com.king_tajin.repo_love_potion.effects.LoveMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RepoLovePotionModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, RepoLovePotion.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> LOVE = REGISTRY.register("love", LoveMobEffect::new);
}

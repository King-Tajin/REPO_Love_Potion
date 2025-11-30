
package com.king_tajin.repo_love_potion.item;

import com.king_tajin.repo_love_potion.init.RepoLovePotionMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class LoveCoreItem extends Item {
	public LoveCoreItem() {
		super(new Properties().stacksTo(16).rarity(Rarity.RARE).food((new FoodProperties.Builder()).nutrition(4).saturationModifier(0.3f).alwaysEdible().build()));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(@NotNull ItemStack itemstack) {
		return true;
	}

	@Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity) {
		ItemStack retrieval = super.finishUsingItem(itemstack, world, entity);
		final int EFFECT_DURATION = 6000;
		if (!entity.level().isClientSide()) {

            MobEffectInstance loveEffect = new MobEffectInstance(RepoLovePotionMobEffects.LOVE, EFFECT_DURATION, 0, false, true, true);

			MobEffectInstance currentSelf = entity.getEffect(RepoLovePotionMobEffects.LOVE);
			if (currentSelf == null || currentSelf.getDuration() <= EFFECT_DURATION) {
				entity.removeEffect(RepoLovePotionMobEffects.LOVE);
				entity.addEffect(loveEffect);
			}
		}
		return retrieval;
	}
}

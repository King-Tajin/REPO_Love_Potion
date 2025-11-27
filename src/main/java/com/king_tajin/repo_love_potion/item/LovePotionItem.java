
package com.king_tajin.repo_love_potion.item;

import com.king_tajin.repo_love_potion.init.RepoLovePotionMobEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.king_tajin.repo_love_potion.WordLists.*;

public class LovePotionItem extends Item {
	private static final Random RANDOM = new Random();
	private static final double MAX_DISTANCE = 24.0;
	private static final int USE_DURATION = 40;
	private static final int EFFECT_DURATION = 900;
	private static final int COOLDOWN = 260;
	private static final String DEFAULT_PLAYER_NAME = "this potion";
	

	public LovePotionItem() {
	    super(new Properties()
	        .stacksTo(1)
	        .rarity(Rarity.EPIC)
	        .food(new FoodProperties.Builder()
	            .nutrition(0)
	            .saturationModifier(0.0f)
	            .alwaysEdible()
	            .build()));
	}

	@Override
	public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(@NotNull ItemStack itemstack, @NotNull LivingEntity livingEntity) {
		return USE_DURATION;
	}

    @Override
	public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entity) {

        if (!level.isClientSide && entity instanceof ServerPlayer serverPlayer) {
	        List<ServerPlayer> players = serverPlayer.serverLevel().players().stream()
	            .filter(p -> !p.equals(serverPlayer))
	            .toList();
	
	        ServerPlayer nearest = null;
	        double closestDistance = Double.MAX_VALUE;
	        
	        for (ServerPlayer other : players) {
	            double distance = other.distanceTo(serverPlayer);
	            if (distance <= MAX_DISTANCE && distance < closestDistance) {
	                closestDistance = distance;
	                nearest = other;
	            }
	        }

            serverPlayer.getCooldowns().addCooldown(stack.getItem(), COOLDOWN);

            SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("repo_love_potion:bluh_bluh"));
			long seed = serverPlayer.getRandom().nextLong();
			double radius = 24.0;
    		Vec3 sourcePos = serverPlayer.position();
            Holder<SoundEvent> soundHolder = null;
            if (soundEvent != null) {
                soundHolder = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent);
            }

            for (ServerPlayer other : serverPlayer.serverLevel().players()) {
			    if (other.position().distanceTo(sourcePos) <= radius) {
                    if (soundHolder != null) {
                        other.connection.send(new ClientboundSoundPacket(
                            soundHolder,
                            SoundSource.PLAYERS,
                            sourcePos.x,
							sourcePos.y,
							sourcePos.z,
                            1.0f,
                            1.0f,
                            seed
                        ));
                    }
                }
			}

			int thresholdTicks = EFFECT_DURATION + 40;

			MobEffectInstance loveEffect = new MobEffectInstance(RepoLovePotionMobEffects.LOVE, EFFECT_DURATION, 0, false, true, true);
			MobEffectInstance loveEffect2 = new MobEffectInstance(RepoLovePotionMobEffects.LOVE, EFFECT_DURATION - 30, 0, false, true, true);

			MobEffectInstance currentSelf = serverPlayer.getEffect(RepoLovePotionMobEffects.LOVE);
			if (currentSelf == null || currentSelf.getDuration() <= thresholdTicks) {
				serverPlayer.removeEffect(RepoLovePotionMobEffects.LOVE);
				serverPlayer.addEffect(loveEffect);
			}

			if (nearest != null) {
				MobEffectInstance currentNearest = nearest.getEffect(RepoLovePotionMobEffects.LOVE);
				if (currentNearest == null || currentNearest.getDuration() <= thresholdTicks) {
					nearest.removeEffect(RepoLovePotionMobEffects.LOVE);
					nearest.addEffect(loveEffect2);
				}
			}

            String transitiveVerb = TRANSITIVE_VERBS[RANDOM.nextInt(TRANSITIVE_VERBS.length)];
            String intransitiveVerb = INTRANSITIVE_VERBS[RANDOM.nextInt(INTRANSITIVE_VERBS.length)];
            String adverb = ADVERBS[RANDOM.nextInt(ADVERBS.length)];
            String intensifier = INTENSIFIERS[RANDOM.nextInt(INTENSIFIERS.length)];
            String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
            String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
			String arkCreature = ARK_CREATURE[RANDOM.nextInt(ARK_CREATURE.length)];

            String playerName = (nearest != null) ? nearest.getName().getString() : DEFAULT_PLAYER_NAME;

            String template = MESSAGES_WITH_PLAYER[RANDOM.nextInt(MESSAGES_WITH_PLAYER.length)];

			sendFormattedMessage(serverPlayer,
                template,
                Map.of(
                    "playerName", Component.literal(playerName).withStyle(ChatFormatting.GOLD),
                    "adverb", Component.literal(adverb).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                    "intensifier", Component.literal(intensifier).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                    "intransitiveVerb", Component.literal(intransitiveVerb).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                    "transitiveVerb", Component.literal(transitiveVerb).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                    "adjective", Component.literal(adjective).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
                    "noun", Component.literal(noun).withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD),
					"arkCreature", Component.literal(arkCreature).withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD)
                ),
                ChatFormatting.DARK_PURPLE
            );
        }

        return stack;
    }

	private String pluralizeVerb(String verb) {
		if (verb.endsWith("e")) {
			return verb + "s";
		} else {
			return verb + "es";
		}
	}
	public void sendFormattedMessage(ServerPlayer player, String template, Map<String, Component> placeholders, ChatFormatting defaultColor) {
		Pattern pattern = Pattern.compile("\\{(\\w+?)(s)?}");
		Matcher matcher = pattern.matcher(template);

		int lastEnd = 0;
		MutableComponent message = Component.empty();

		while (matcher.find()) {
			String before = template.substring(lastEnd, matcher.start());
			String key = matcher.group(1);
			boolean isPlural = matcher.group(2) != null;

			if (!before.isEmpty()) {
				message.append(Component.literal(before).withStyle(defaultColor));
			}

			Component base = placeholders.get(key);

			if (base != null) {
				String plain = base.getString();
				if (isPlural) {
					String plural = pluralizeVerb(plain);
					message.append(Component.literal(plural).withStyle(base.getStyle()));
				} else {
					message.append(base);
				}
			} else {
				message.append(Component.literal("{" + key + (isPlural ? "s" : "") + "}").withStyle(ChatFormatting.RED));
			}

			lastEnd = matcher.end();
		}

		if (lastEnd < template.length()) {
			String after = template.substring(lastEnd);
			message.append(Component.literal(after).withStyle(defaultColor));
		}

		for (ServerPlayer onlinePlayer : player.serverLevel().players()) {
			onlinePlayer.sendSystemMessage(message);
		}
	}
}

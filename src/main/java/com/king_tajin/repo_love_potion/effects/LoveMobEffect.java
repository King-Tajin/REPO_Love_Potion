package com.king_tajin.repo_love_potion.effects;


import com.king_tajin.repo_love_potion.RepoLovePotion;
import com.king_tajin.repo_love_potion.init.RepoLovePotionParticleTypes;
import com.king_tajin.repo_love_potion.init.RepoLovePotionSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import net.tslat.effectslib.api.EffectOverlayRenderer;
import net.tslat.effectslib.api.ExtendedMobEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class LoveMobEffect extends ExtendedMobEffect {
    private static final ResourceLocation OVERLAY_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(RepoLovePotion.MODID, "textures/mob_effect/love_overlay.png");
    public LoveMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffff0099);
    }

    @Override
    public @NotNull ParticleOptions createParticleOptions(@NotNull MobEffectInstance mobEffectInstance) {
        return new DustParticleOptions(new Vector3f(0, 0, 0), 0.0f);
    }

    //removed for continuity
    //@Override
    //public void fillEffectCures(Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {
    //    cures.add(EffectCures.HONEY);
    //}

    @Override
    public boolean shouldTickEffect(@Nullable MobEffectInstance effectInstance, @Nullable LivingEntity entity, int ticksRemaining, int amplifier) {
        return true;
    }

    @Override
    public @Nullable EffectOverlayRenderer getOverlayRenderer() {
        return (poseStack, deltaTracker, effectInstance) -> {
            Minecraft mc = Minecraft.getInstance();
            GuiGraphics guiGraphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.85f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        };
    }

    @Override
    public boolean tick(LivingEntity entity, @Nullable MobEffectInstance effectInstance, int amplifier) {

        Level level = entity.level();

        if (!level.isClientSide && level.random.nextFloat() < 0.01f) {
            level.playSound(
                null,
                entity.blockPosition(),
                RepoLovePotionSounds.YIPEE.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F + level.random.nextFloat() * 0.6F
            );
        }

        if (!level.isClientSide && level.random.nextFloat() < 0.1F) {
            ((ServerLevel)level).sendParticles(RepoLovePotionParticleTypes.LOVE_PARTICLE.get(),
                    entity.getX(), entity.getY() + 1.0D, entity.getZ(),
                    1, 0.1D, 0.1D, 0.1D, 0.01D);
        }
        return true;
    }

    @Override
    public void onEffectStarted(@NotNull LivingEntity entity, int amplifier) {
        if (!(entity instanceof ServerPlayer player)) return;

        ServerLevel level = player.serverLevel();
        Scoreboard scoreboard = level.getScoreboard();

        PlayerTeam team = scoreboard.getPlayerTeam("love_effect");
        if (team == null) {
            team = scoreboard.addPlayerTeam("love_effect");
            team.setColor(ChatFormatting.LIGHT_PURPLE);
            team.setSeeFriendlyInvisibles(true);
            team.setNameTagVisibility(Team.Visibility.ALWAYS);
            team.setCollisionRule(Team.CollisionRule.NEVER);
        }
        if (!entity.hasEffect(MobEffects.GLOWING)) {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 9999, 0, false, false, false));
        }
        scoreboard.addPlayerToTeam(player.getScoreboardName(), team);
    }
    @Override
    public void onExpiry(MobEffectInstance effectInstance, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            Scoreboard scoreboard = level.getScoreboard();

            level.playSound(
                    null,
                    entity.blockPosition(),
                    RepoLovePotionSounds.HOLY_MOLY.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F + level.random.nextFloat() * 0.9F - 0.45F
            );

            PlayerTeam team = scoreboard.getPlayerTeam("love_effect");
            if (team != null) {
                scoreboard.removePlayerFromTeam(player.getScoreboardName(), team);

                if (team.getPlayers().isEmpty()) {
                    scoreboard.removePlayerTeam(team);
                }

            }
            if (entity.hasEffect(MobEffects.GLOWING)) {
                entity.removeEffect(MobEffects.GLOWING);
            }
        }
    }
    @Override
    public boolean onRemove(MobEffectInstance effectInstance, LivingEntity entity) {

        if (entity instanceof ServerPlayer player) {
            ServerLevel level = player.serverLevel();
            Scoreboard scoreboard = level.getScoreboard();

            level.playSound(
                    null,
                    entity.blockPosition(),
                    RepoLovePotionSounds.HOLY_MOLY.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F + level.random.nextFloat() * 0.9F - 0.45F
            );

            PlayerTeam team = scoreboard.getPlayerTeam("love_effect");
            if (team != null) {
                scoreboard.removePlayerFromTeam(player.getScoreboardName(), team);

                if (team.getPlayers().isEmpty()) {
                    scoreboard.removePlayerTeam(team);
                }

            }
            if (entity.hasEffect(MobEffects.GLOWING)) {
                entity.removeEffect(MobEffects.GLOWING);
            }
        }

        return true;
    }
}
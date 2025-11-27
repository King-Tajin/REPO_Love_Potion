package com.king_tajin.repo_love_potion.events;

import com.king_tajin.repo_love_potion.init.RepoLovePotionModMobEffects;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

public class CreeperLoveAIHandler {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Creeper creeper)) return;
        Level level = creeper.level();
        if (level.isClientSide) return;
        creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(
                creeper,
                Player.class,
                14.0F,
                1.4D,
                2.2D,
                player -> player.hasEffect(RepoLovePotionModMobEffects.LOVE)
        ));
    }
}
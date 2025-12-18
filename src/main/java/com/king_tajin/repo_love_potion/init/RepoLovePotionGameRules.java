package com.king_tajin.repo_love_potion.init;

import net.minecraft.world.level.GameRules;

public class RepoLovePotionGameRules {
    public static GameRules.Key<GameRules.BooleanValue> RULE_LOVE_MUSIC_FLOWER_SPAWNING;

    public static void register() {
        RULE_LOVE_MUSIC_FLOWER_SPAWNING = GameRules.register(
                "SpawnFlowersNearILoveDisc",
                GameRules.Category.MISC,
                GameRules.BooleanValue.create(true)
        );
    }
}

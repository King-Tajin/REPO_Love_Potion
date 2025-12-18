package com.king_tajin.repo_love_potion.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "repo_love_potion")
public class RepoLovePotionConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enableLoveEffectOverlay = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int overlayOpacity = 95;
}
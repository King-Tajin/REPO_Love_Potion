
package com.king_tajin.repo_love_potion.item;

import com.king_tajin.repo_love_potion.RepoLovePotion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ILoveDiscItem extends Item {
	public static final int SONG_DURATION_TICKS = 1620;
	public ILoveDiscItem() {
		super(new Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(
				ResourceKey.create(Registries.JUKEBOX_SONG, ResourceLocation.fromNamespaceAndPath(RepoLovePotion.MODID, "i_love_disc"))));
	}
}

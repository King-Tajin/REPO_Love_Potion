
package com.king_tajin.repo_love_potion.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class HeartJarItem extends Item {
	public HeartJarItem() {
		super(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
	}
}

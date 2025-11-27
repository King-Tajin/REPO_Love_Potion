
package com.king_tajin.repo_love_potion.init;

import com.king_tajin.repo_love_potion.RepoLovePotion;
import com.king_tajin.repo_love_potion.item.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RepoLovePotionModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(RepoLovePotion.MODID);

	public static final DeferredItem<Item> LOVE_POTION = REGISTRY.register("love_potion", LovePotionItem::new);
	public static final DeferredItem<Item> LOVE_CORE = REGISTRY.register("love_core", LoveCoreItem::new);
	public static final DeferredItem<Item> HEART_JAR = REGISTRY.register("heart_jar", HeartJarItem::new);
	public static final DeferredItem<Item> UNPOLISHED_LOVE_CORE = REGISTRY.register("unpolished_love_core", UnpolishedLoveCoreItem::new);
	public static final DeferredItem<Item> I_LOVE_DISC = REGISTRY.register("i_love_disc", ILoveDiscItem::new);
	public static final DeferredItem<Item> LOVE_DUST = REGISTRY.register("love_dust", LoveDustItem::new);
	public static final DeferredItem<Item> INCOMPLETE_UNPOLISHED_LOVE_CORE = REGISTRY.register("incomplete_unpolished_love_core", () -> new IncompleteUnpolishedLoveCoreItem(new Item.Properties()));
}
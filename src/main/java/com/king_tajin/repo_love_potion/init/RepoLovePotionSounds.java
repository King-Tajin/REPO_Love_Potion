
package com.king_tajin.repo_love_potion.init;

import com.king_tajin.repo_love_potion.RepoLovePotion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RepoLovePotionSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, RepoLovePotion.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> GLUG_GLUG = REGISTRY.register("glug_glug", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("repo_love_potion", "glug_glug")));
	public static final DeferredHolder<SoundEvent, SoundEvent> BLUH_BLUH = REGISTRY.register("bluh_bluh", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("repo_love_potion", "bluh_bluh")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VILLAGER_RAP = REGISTRY.register("villager_rap", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("repo_love_potion", "villager_rap")));
	public static final DeferredHolder<SoundEvent, SoundEvent> I_LOVE_SONG = REGISTRY.register("i_love_song", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("repo_love_potion", "i_love_song")));
	public static final DeferredHolder<SoundEvent, SoundEvent> HOLY_MOLY = REGISTRY.register("holy_moly", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("repo_love_potion", "holy_moly")));
	public static final DeferredHolder<SoundEvent, SoundEvent> YIPEE = REGISTRY.register("yipee", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("repo_love_potion", "yipee")));
}

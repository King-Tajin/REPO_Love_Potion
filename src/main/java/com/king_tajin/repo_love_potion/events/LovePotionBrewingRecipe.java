package com.king_tajin.repo_love_potion.events;

import com.king_tajin.repo_love_potion.init.RepoLovePotionItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

public class LovePotionBrewingRecipe {

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        // Heart Jar + Love Core = Love Potion
        builder.addRecipe(
                Ingredient.of(RepoLovePotionItems.HEART_JAR.get()),
                Ingredient.of(RepoLovePotionItems.LOVE_CORE.get()),
                new ItemStack(RepoLovePotionItems.LOVE_POTION.get())
        );
    }
}
package com.king_tajin.repo_love_potion.events;

import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import com.king_tajin.repo_love_potion.item.ILoveDiscItem;

public class ChargedCreeperDropHandler {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Creeper creeper)) {
            return;
        }

        if (!creeper.isPowered()) {
            return;
        }

        if (event.getSource().getDirectEntity() instanceof Arrow arrow) {
            if (arrow.getOwner() instanceof Skeleton) {

                ItemStack musicDisc = new ItemStack(new ILoveDiscItem());
                creeper.spawnAtLocation(musicDisc);

                ItemStack rose = new ItemStack(Items.POPPY);
                creeper.spawnAtLocation(rose);
            }
        }
    }
}
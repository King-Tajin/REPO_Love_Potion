package com.king_tajin.repo_love_potion.events;

import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import com.king_tajin.repo_love_potion.init.RepoLovePotionItems;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

public class ChargedCreeperDropHandler {

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Creeper creeper)) {
            return;
        }

        if (!creeper.isPowered()) {
            return;
        }

        if (event.getSource().getDirectEntity() instanceof Arrow arrow) {
            if (arrow.getOwner() instanceof Skeleton) {
                event.getDrops().clear();

                ItemStack rose = new ItemStack(Items.POPPY, 1);
                creeper.spawnAtLocation(rose);

                ItemStack gunpowder = new ItemStack(Items.GUNPOWDER, 3);
                creeper.spawnAtLocation(gunpowder);

                if (creeper.level().random.nextFloat() < 0.4f) {
                    ItemStack musicDisc = new ItemStack(RepoLovePotionItems.I_LOVE_DISC.get());
                    creeper.spawnAtLocation(musicDisc);
                }
            }
        }
    }
}
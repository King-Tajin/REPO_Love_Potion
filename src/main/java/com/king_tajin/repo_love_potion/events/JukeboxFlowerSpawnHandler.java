package com.king_tajin.repo_love_potion.events;

import com.king_tajin.repo_love_potion.init.RepoLovePotionGameRules;
import com.king_tajin.repo_love_potion.init.RepoLovePotionItems;
import com.king_tajin.repo_love_potion.init.RepoLovePotionParticleTypes;
import com.king_tajin.repo_love_potion.init.RepoLovePotionSounds;
import com.king_tajin.repo_love_potion.item.ILoveDiscItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class JukeboxFlowerSpawnHandler {

    private static final int SPAWN_RADIUS = 12;
    private static final int RAIN_DURATION_TICKS = 180;
    private static final int WAIT_DURATION_TICKS = 120;

    private static final Map<BlockPos, FlowerSpawnData> activeSequences = new HashMap<>();

    private static class FlowerSpawnData {
        long startTime;
        int phase; // 0 = rain, 1 = wait, 2 = flowers, 3 = particles only

        FlowerSpawnData(long startTime) {
            this.startTime = startTime;
            this.phase = 0;
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (level.isClientSide) return;

        if (!level.getGameRules().getBoolean(RepoLovePotionGameRules.RULE_LOVE_MUSIC_FLOWER_SPAWNING)) {
            return;
        }

        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.JUKEBOX)) {
            Objects.requireNonNull(level.getServer()).execute(() -> {
                if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
                    ItemStack record = jukebox.getTheItem();
                    if (record.is(RepoLovePotionItems.I_LOVE_DISC.get())) {
                        BlockPos below = pos.below();
                        BlockState ground = level.getBlockState(below);

                        if (ground.is(Blocks.GRASS_BLOCK) || ground.is(Blocks.DIRT)) {
                            if (!activeSequences.containsKey(pos)) {
                                activeSequences.put(pos, new FlowerSpawnData(level.getGameTime()));
                            }
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        Level level = event.getServer().overworld();

        if (!level.getGameRules().getBoolean(RepoLovePotionGameRules.RULE_LOVE_MUSIC_FLOWER_SPAWNING)) {
            activeSequences.clear();
            return;
        }

        long currentTime = level.getGameTime();
        Iterator<Map.Entry<BlockPos, FlowerSpawnData>> iterator = activeSequences.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, FlowerSpawnData> entry = iterator.next();
            BlockPos pos = entry.getKey();
            FlowerSpawnData data = entry.getValue();

            long elapsed = currentTime - data.startTime;

            if (elapsed % 20 == 0) {
                BlockState state = level.getBlockState(pos);
                if (!state.is(Blocks.JUKEBOX)) {
                    iterator.remove();
                    continue;
                }

                if (elapsed > ILoveDiscItem.SONG_DURATION_TICKS) {
                    iterator.remove();
                    continue;
                }

                if (!state.getValue(JukeboxBlock.HAS_RECORD)) {
                    iterator.remove();
                    continue;
                }

                if (level.getBlockEntity(pos) instanceof JukeboxBlockEntity jukebox) {
                    ItemStack record = jukebox.getTheItem();
                    if (record.isEmpty() || !record.is(RepoLovePotionItems.I_LOVE_DISC.get())) {
                        iterator.remove();
                        continue;
                    }
                }
            }

            if (data.phase == 0) {
                if (elapsed < RAIN_DURATION_TICKS) {
                    if (elapsed % 5 == 0) {
                        spawnWaterPotion(level, pos);
                    }
                    if (elapsed % 10 == 0) {
                        spawnFlowerParticles(level, pos);
                    }
                } else {
                    data.phase = 1;
                }
            } else if (data.phase == 1) {
                if (elapsed >= RAIN_DURATION_TICKS + WAIT_DURATION_TICKS) {
                    data.phase = 2;
                }
                if (elapsed % 10 == 0) {
                    spawnFlowerParticles(level, pos);
                }
            } else if (data.phase == 2) {
                int flowerCount = countFlowersNearby(level, pos);
                int maxFlowers = 35;

                if (flowerCount >= maxFlowers) {
                    data.phase = 3;
                } else {
                    if ((currentTime - (data.startTime + RAIN_DURATION_TICKS + WAIT_DURATION_TICKS)) % 30 == 0) {
                        trySpawnSingleFlower(level, pos);
                    }
                }

                if (elapsed % 10 == 0) {
                    spawnFlowerParticles(level, pos);
                }
            } else if (data.phase == 3) {
                if (elapsed % 10 == 0) {
                    spawnFlowerParticles(level, pos);
                }
            }
        }
    }

    private static void spawnWaterPotion(Level level, BlockPos jukeboxPos) {
        double x = jukeboxPos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * SPAWN_RADIUS * 2;
        double y = jukeboxPos.getY() + 8 + level.random.nextDouble() * 3;
        double z = jukeboxPos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * SPAWN_RADIUS * 2;

        ThrownPotion potion = new ThrownPotion(level, x, y, z);
        ItemStack potionStack = new ItemStack(Items.SPLASH_POTION);
        potionStack.set(net.minecraft.core.component.DataComponents.POTION_CONTENTS,
                new PotionContents(Potions.WATER));
        potion.setItem(potionStack);

        potion.setDeltaMovement(0, -0.5, 0);

        level.addFreshEntity(potion);
    }

    private static void spawnFlowerParticles(Level level, BlockPos jukeboxPos) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) return;

        for (int x = -SPAWN_RADIUS; x <= SPAWN_RADIUS; x++) {
            for (int z = -SPAWN_RADIUS; z <= SPAWN_RADIUS; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos checkPos = jukeboxPos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);

                    if (isFlower(state)) {
                        serverLevel.sendParticles(
                                RepoLovePotionParticleTypes.LOVE_PARTICLE.get(),
                                checkPos.getX() + 0.5,
                                checkPos.getY() + 0.8,
                                checkPos.getZ() + 0.5,
                                1,
                                0.2, 0.2, 0.2,
                                0.01
                        );
                    }
                }
            }
        }
    }

    private static void trySpawnSingleFlower(Level level, BlockPos jukeboxPos) {
        int maxAttempts = 20;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = level.random.nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;
            int z = level.random.nextInt(SPAWN_RADIUS * 2 + 1) - SPAWN_RADIUS;
            int y = level.random.nextInt(5) - 2;

            BlockPos checkPos = jukeboxPos.offset(x, y, z);
            BlockState ground = level.getBlockState(checkPos);
            BlockState above = level.getBlockState(checkPos.above());

            if (ground.is(Blocks.GRASS_BLOCK) && above.isAir()) {
                BlockState flower = getRandomFlower(level);
                level.setBlock(checkPos.above(), flower, 3);

                if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            checkPos.getX() + 0.5, checkPos.getY() + 1.5, checkPos.getZ() + 0.5,
                            5, 0.3, 0.3, 0.3, 0.05);
                    serverLevel.playSound(null, checkPos, RepoLovePotionSounds.GLUG_GLUG.get(), SoundSource.BLOCKS,
                            2f, 1.0f + (level.random.nextFloat() * 0.5f - 0.25f));
                }

                return;
            }
        }
    }

    private static int countFlowersNearby(Level level, BlockPos center) {
        int count = 0;

        for (int x = -SPAWN_RADIUS; x <= SPAWN_RADIUS; x++) {
            for (int z = -SPAWN_RADIUS; z <= SPAWN_RADIUS; z++) {
                for (int y = -2; y <= 2; y++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);

                    if (isFlower(state)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private static boolean isFlower(BlockState state) {
        return state.is(Blocks.POPPY) ||
                state.is(Blocks.DANDELION) ||
                state.is(Blocks.PINK_TULIP) ||
                state.is(Blocks.RED_TULIP) ||
                state.is(Blocks.ORANGE_TULIP) ||
                state.is(Blocks.WHITE_TULIP) ||
                state.is(Blocks.CORNFLOWER) ||
                state.is(Blocks.LILY_OF_THE_VALLEY) ||
                state.is(Blocks.AZURE_BLUET) ||
                state.is(Blocks.BLUE_ORCHID) ||
                state.is(Blocks.ALLIUM) ||
                state.is(Blocks.OXEYE_DAISY) ||
                state.is(Blocks.SUNFLOWER) ||
                state.is(Blocks.LILAC) ||
                state.is(Blocks.ROSE_BUSH) ||
                state.is(Blocks.PEONY);
    }

    private static BlockState getRandomFlower(Level level) {
        int choice = level.random.nextInt(9);
        return switch (choice) {
            case 0 -> Blocks.POPPY.defaultBlockState();
            case 1 -> Blocks.DANDELION.defaultBlockState();
            case 2 -> Blocks.PINK_TULIP.defaultBlockState();
            case 3 -> Blocks.RED_TULIP.defaultBlockState();
            case 4 -> Blocks.ORANGE_TULIP.defaultBlockState();
            case 5 -> Blocks.WHITE_TULIP.defaultBlockState();
            case 6 -> Blocks.CORNFLOWER.defaultBlockState();
            case 7 -> Blocks.LILY_OF_THE_VALLEY.defaultBlockState();
            default -> Blocks.AZURE_BLUET.defaultBlockState();
        };
    }
}
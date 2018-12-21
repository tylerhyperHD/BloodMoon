package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Step;

import net.minecraft.server.v1_13_R2.MobSpawnerAbstract;
import net.minecraft.server.v1_13_R2.TileEntityMobSpawner;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.util.ListUtils;
import uk.co.jacekk.bukkit.baseplugin.util.ReflectionUtils;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.DeprecationManager;
import uk.co.jacekk.bukkit.bloodmoon.event.DungeonChestFillEvent;

@SuppressWarnings("deprecation")
public class DungeonGenerator extends BlockPopulator {

	private final BloodMoon plugin;

	public DungeonGenerator(BloodMoon plugin) {
		this.plugin = plugin;
	}

	@Override
	public void populate(World world, Random random, Chunk chunk) {
		int gridX = (int) (Math.floor(chunk.getX() / 10.0d) * 10);
		int gridZ = (int) (Math.floor(chunk.getZ() / 10.0d) * 10);

		PluginConfig worldConfig = plugin.getConfig(world);

		DungeonProperties properties = new DungeonProperties(world, worldConfig, gridX, gridZ);

		if (!properties.isInChunk(chunk)) {
			return;
		}

		int worldChunkX = properties.getChunkX();
		int worldChunkZ = properties.getChunkZ();

		plugin.getLogger().info("Generated BloodMoon dungeon at " + worldChunkX + "," + worldChunkZ);

		Random rand = properties.getRandom();

		int yMax = world.getHighestBlockYAt((worldChunkX * 16) + 8, worldChunkZ * 16) - 1;
		int yMin = yMax - (properties.getLayers() * 6);

		// Walls
		for (int y = yMax + 3; y > yMin; --y) {
			for (int x = 1; x < 15; ++x) {
				for (int z = 1; z < 15; ++z) {
					chunk.getBlock(x, y, z).setBlockData(Material.AIR.createBlockData());
				}
			}

			for (int i = 0; i < 16; ++i) {
				chunk.getBlock(i, y, 0).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
				chunk.getBlock(i, y, 15).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);

				chunk.getBlock(0, y, i).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
				chunk.getBlock(15, y, i).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
			}
		}

		// Gate
		chunk.getBlock(7, yMax + 1, 0).setBlockData(Material.IRON_BARS.createBlockData());
		chunk.getBlock(8, yMax + 1, 0).setBlockData(Material.IRON_BARS.createBlockData());
		chunk.getBlock(7, yMax + 2, 0).setBlockData(Material.IRON_BARS.createBlockData());
		chunk.getBlock(8, yMax + 2, 0).setBlockData(Material.IRON_BARS.createBlockData());

		// Roof
		for (int i = 1; i < 15; ++i) {
			chunk.getBlock(i, yMax + 4, 1)
					.setBlockData(DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
			chunk.getBlock(i, yMax + 4, 14)
					.setBlockData(DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);

			chunk.getBlock(1, yMax + 4, i)
					.setBlockData(DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
			chunk.getBlock(14, yMax + 4, i)
					.setBlockData(DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
		}

		for (int x = 2; x < 14; ++x) {
			for (int z = 2; z < 14; ++z) {
				chunk.getBlock(x, yMax + 5, z).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
			}
		}

		// TODO: Fix implementation

		// WoodenStep dataWood = new WoodenStep(TreeSpecies.REDWOOD, true);

		// BlockData spruce = Material.SPRUCE_STAIRS.createBlockData();

		// Step dataBrick = new Step(Material.STONE_BRICKS);
		// dataBrick.setInverted(true);

		for (int layer = 0; layer <= properties.getLayers(); ++layer) {
			int yBase = yMax - (layer * 6);

			// Floors
			for (int x = 1; x < 7; ++x) {
				for (int z = 1; z < 7; ++z) {
					chunk.getBlock(x, yBase, z).setBlockData(Material.SPRUCE_STAIRS.createBlockData(), false);
					chunk.getBlock(x + 8, yBase, z).setBlockData(Material.SPRUCE_STAIRS.createBlockData(), false);

					chunk.getBlock(x, yBase, z + 8).setBlockData(Material.SPRUCE_STAIRS.createBlockData(), false);
					chunk.getBlock(x + 8, yBase, z + 8).setBlockData(Material.SPRUCE_STAIRS.createBlockData(), false);
				}

				for (int bu = 5; bu < 11; ++x) {
					for (int z = 5; z < 11; ++z) {
						chunk.getBlock(bu, yBase, z).setBlockData(Material.AIR.createBlockData());
					}
				}

				// Paths
				for (int i = 1; i < 5; ++i) {
					chunk.getBlock(i, yBase, 7).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
					chunk.getBlock(i, yBase, 8).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
					chunk.getBlock(i + 10, yBase, 7).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
					chunk.getBlock(i + 10, yBase, 8).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);

					chunk.getBlock(7, yBase, i).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
					chunk.getBlock(8, yBase, i).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
					chunk.getBlock(7, yBase, i + 10).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
					chunk.getBlock(8, yBase, i + 10).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
				}

				// Columns
				for (int y = 0; y < 6; ++y) {
					chunk.getBlock(5, yBase + y, 5).setBlockData(
							DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
					chunk.getBlock(5, yBase + y, 10).setBlockData(
							DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
					chunk.getBlock(10, yBase + y, 5).setBlockData(
							DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
					chunk.getBlock(10, yBase + y, 10).setBlockData(
							DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
				}

				// Spawners
				Block[] spawners = new Block[] { chunk.getBlock(14, yBase + 1, 1), chunk.getBlock(1, yBase + 1, 14) };

				for (Block block : spawners) {
					EntityType type = EntityType.valueOf(
							ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_DUNGEONS_SPAWNER_TYPES)));

					if (type != null) {
						// (byte) 0
						block.setBlockData(Material.SPAWNER.createBlockData(), false);
						MobSpawnerAbstract spawner = ((TileEntityMobSpawner) ((CraftWorld) world)
								.getTileEntityAt(block.getX(), block.getY(), block.getZ())).getSpawner();

						spawner.setMobName(DeprecationManager.getEntityTypes(type.getName()));

						try {
							ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "minSpawnDelay", spawner,
									worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_DELAY));
							ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "maxSpawnDelay", spawner,
									worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_DELAY));
							ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "spawnCount", spawner,
									worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_COUNT));
							ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "maxNearbyEntities", spawner,
									worldConfig.getInt(Config.FEATURE_DUNGEONS_SPAWNER_MAX_MOBS));
							ReflectionUtils.setFieldValue(MobSpawnerAbstract.class, "spawnRange", spawner, 4);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		// Steps
		for (int layer = 0; layer < properties.getLayers(); ++layer) {
			int yBase = yMax - (layer * 6);

			Step topStep = new Step();
			topStep.setMaterial(Material.STONE_BRICKS);
			topStep.setInverted(true);

			if (layer % 2 == 0) {
				for (int x = 9; x < 15; ++x) {
					chunk.getBlock(x, yBase, 14).setBlockData(Material.AIR.createBlockData());
				}

				for (int y = 1; y < 4; ++y) {
					int l = y * 2;

					chunk.getBlock(7 + l, yBase - y + 1, 14).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(),
							false);
					// TODO: Invert top step
					chunk.getBlock(8 + l, yBase - y, 14).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(),
							false);

					chunk.getBlock(14, yBase - y - 2, 15 - l)
							.setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(), false);
					// TODO: Invert top step
					chunk.getBlock(14, yBase - y - 2, 16 - l).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(),
							false);
				}
			} else {
				for (int x = 1; x < 7; ++x) {
					chunk.getBlock(x, yBase, 1).setBlockData(Material.AIR.createBlockData());
				}

				for (int y = 1; y < 4; ++y) {
					int l = y * 2;

					chunk.getBlock(8 - l, yBase - y + 1, 1).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(),
							false);
					// TODO: Invert top step
					chunk.getBlock(7 - l, yBase - y, 1).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(),
							false);

					chunk.getBlock(1, yBase - y - 2, l).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(),
							false);
					// TODO: Invert top step
					chunk.getBlock(1, yBase - y - 2, l - 1).setBlockData(Material.STONE_BRICK_STAIRS.createBlockData(),
							false);
				}
			}
		}

		// Loot room
		for (int i = 0; i < 4; ++i) {
			chunk.getBlock(i + 6, yMin + 1, 5).setBlockData(Material.COBBLESTONE_WALL.createBlockData(), false);
			chunk.getBlock(i + 6, yMin + 1, 10).setBlockData(Material.COBBLESTONE_WALL.createBlockData(), false);

			chunk.getBlock(5, yMin + 1, i + 6).setBlockData(Material.COBBLESTONE_WALL.createBlockData(), false);
			chunk.getBlock(10, yMin + 1, i + 6).setBlockData(Material.COBBLESTONE_WALL.createBlockData(), false);

			for (int y = 3; y >= 0; --y) {
				chunk.getBlock(i + 6, yMin - y, 5).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
				chunk.getBlock(i + 6, yMin - y, 10).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);

				chunk.getBlock(5, yMin - y, i + 6).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
				chunk.getBlock(10, yMin - y, i + 6).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);

				for (int z = 0; z < 4; ++z) {
					chunk.getBlock(i + 6, yMin - y, z + 6).setBlockData(Material.AIR.createBlockData());
				}
			}
		}

		for (int x = 0; x < 6; ++x) {
			for (int z = 0; z < 6; ++z) {
				chunk.getBlock(x + 5, yMin - 4, z + 5).setBlockData(
						DeprecationManager.getRandomStoneBricks(rand.nextInt(3)).createBlockData(), false);
			}
		}

		Block chest1 = chunk.getBlock(7, yMin - 3, 6);
		chest1.setType(Material.CHEST);
		Directional dirchest1 = (Directional) chest1.getBlockData();
        dirchest1.setFacing(DeprecationManager.directionToBlockFace(2));
        chest1.setBlockData(dirchest1);

        Block chest2 = chunk.getBlock(8, yMin - 3, 6);
		chest2.setType(Material.CHEST);
		Directional dirchest2 = (Directional) chest2.getBlockData();
        dirchest2.setFacing(DeprecationManager.directionToBlockFace(2));
        chest2.setBlockData(dirchest2);
        
		//.setTypeIdAndData(Material.CHEST.getId(), (byte) 2, false);
		// chunk.getBlock(8, yMin - 3, 6).setTypeIdAndData(Material.CHEST.getId(), (byte) 2, false);

        Block chest3 = chunk.getBlock(7, yMin - 3, 9);
        
		chest3.setType(Material.CHEST);
		Directional dirchest3 = (Directional) chest3.getBlockData();
        dirchest3.setFacing(DeprecationManager.directionToBlockFace(3));
        chest3.setBlockData(dirchest3);
        
		//chunk.getBlock(7, yMin - 3, 9).setTypeIdAndData(Material.CHEST.getId(), (byte) 3, false);
		
		Block chest4 = chunk.getBlock(8, yMin - 3, 9);
		
		chest4.setType(Material.CHEST);
		Directional dirchest4 = (Directional) chest4.getBlockData();
        dirchest4.setFacing(DeprecationManager.directionToBlockFace(3));
        chest4.setBlockData(dirchest4);
		
		//chunk.getBlock(8, yMin - 3, 9).setTypeIdAndData(Material.CHEST.getId(), (byte) 3, false);

		Block chest5 = chunk.getBlock(6, yMin - 3, 7);
		
		chest5.setType(Material.CHEST);
		Directional dirchest5 = (Directional) chest5.getBlockData();
        dirchest5.setFacing(DeprecationManager.directionToBlockFace(4));
        chest5.setBlockData(dirchest5);
		
		// chunk.getBlock(6, yMin - 3, 7).setTypeIdAndData(Material.CHEST.getId(), (byte) 4, false);
		
		Block chest6 = chunk.getBlock(6, yMin - 3, 8);
		
		chest6.setType(Material.CHEST);
		Directional dirchest6 = (Directional) chest6.getBlockData();
        dirchest6.setFacing(DeprecationManager.directionToBlockFace(4));
        chest6.setBlockData(dirchest6);
		
		// chunk.getBlock(6, yMin - 3, 8).setTypeIdAndData(Material.CHEST.getId(), (byte) 4, false);
		
		Block chest7 = chunk.getBlock(9, yMin - 3, 7);
		
		chest7.setType(Material.CHEST);
		Directional dirchest7 = (Directional) chest7.getBlockData();
        dirchest7.setFacing(DeprecationManager.directionToBlockFace(5));
        chest7.setBlockData(dirchest7);
		
		// chunk.getBlock(9, yMin - 3, 7).setTypeIdAndData(Material.CHEST.getId(), (byte) 5, false);
		
		Block chest8 = chunk.getBlock(9, yMin - 3, 8);
		
		chest8.setType(Material.CHEST);
		Directional dirchest8 = (Directional) chest8.getBlockData();
        dirchest8.setFacing(DeprecationManager.directionToBlockFace(5));
        chest8.setBlockData(dirchest8);
		
		// chunk.getBlock(9, yMin - 3, 8).setTypeIdAndData(Material.CHEST.getId(), (byte) 5, false);

		// Add loot
		Chest[] chests = new Chest[] { (Chest) chunk.getBlock(7, yMin - 3, 6).getState(),
				(Chest) chunk.getBlock(7, yMin - 3, 9).getState(), (Chest) chunk.getBlock(6, yMin - 3, 7).getState(),
				(Chest) chunk.getBlock(9, yMin - 3, 7).getState() };

		for (Chest chest : chests) {
			Inventory inv = chest.getInventory();

			for (int i = 0; i < worldConfig.getInt(Config.FEATURE_DUNGEONS_ITEMS_PER_CHEST); ++i) {
				Material type = Material.getMaterial(
						ListUtils.getRandom(worldConfig.getStringList(Config.FEATURE_DUNGEONS_CHEST_ITEMS)));

				if (type != null) {
					ItemStack item = new ItemStack(type);
					// item.setAmount(Math.min(type.getMaxStackSize(),
					// rand.nextInt(worldConfig.getInt(Config.FEATURE_DUNGEONS_MAX_STACK_SIZE))));
					int low = worldConfig.getInt(Config.FEATURE_DUNGEONS_MIN_STACK_SIZE);
					int high = worldConfig.getInt(Config.FEATURE_DUNGEONS_MAX_STACK_SIZE);
					Random randomSam = new Random();
					int randomInt = randomSam.nextInt((high + 1) - low) + low;
					item.setAmount(randomInt);

					inv.setItem(rand.nextInt(inv.getSize()), item);
				}
			}

			this.plugin.getServer().getPluginManager().callEvent(new DungeonChestFillEvent(chest));
		}

		// Set markers for protection
		Block top = chunk.getBlock(8, yMax + 5, 8);
		Block bottom = chunk.getBlock(8, yMax - (properties.getLayers() * 6) - 4, 8);

		top.getState().getData().setData((byte) (top.getData() | 0x8));
		bottom.getState().getData().setData((byte) (bottom.getData() | 0x8));
	}
}

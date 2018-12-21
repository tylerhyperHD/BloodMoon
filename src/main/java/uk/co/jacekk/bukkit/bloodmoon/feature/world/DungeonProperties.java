package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class DungeonProperties {

	private final World world;
	private final PluginConfig worldConfig;
	private int gridX, gridZ;

	private int chunkX, chunkZ;
	private int layers;

	public DungeonProperties(World world, PluginConfig worldConfig, int gridX, int gridZ) {
		this.world = world;
		this.worldConfig = worldConfig;
		this.gridX = gridX;
		this.gridZ = gridZ;

		Random random = getRandom();

		this.chunkX = this.gridX + random.nextInt(10);
		this.chunkZ = this.gridZ + random.nextInt(10);

		int minLayers = worldConfig.getInt(Config.FEATURE_DUNGEONS_MIN_LAYERS);
		int maxLayers = worldConfig.getInt(Config.FEATURE_DUNGEONS_MAX_LAYERS);

		this.layers = random.nextInt(maxLayers - minLayers) + minLayers;
	}

	/**
	 * Gets a new Random for the dungeon in this grid.
	 *
	 * @return
	 */
	public final Random getRandom() {
		return new Random(this.world.getSeed() + (this.gridX ^ this.gridZ));
	}

	/**
	 * Gets the X chunk coordinate of the dungeon in this grid.
	 *
	 * @return The X coordinate.
	 */
	public int getChunkX() {
		return this.chunkX;
	}

	/**
	 * Gets the Z chunk coordinate of the dungeon in this grid.
	 *
	 * @return The Z coordinate.
	 */
	public int getChunkZ() {
		return this.chunkZ;
	}

	/**
	 * Gets the number of layers that this dungeon will have.
	 *
	 * @return The number.
	 */
	public int getLayers() {
		return this.layers;
	}

	/**
	 * Checks if this dungeon should generate in a Chunk. This will account for the
	 * world config as well as location.
	 *
	 * @param chunk The chunk to check.
	 * @return True if it should generate.
	 */
	public boolean isInChunk(Chunk chunk) {
		if (!chunk.getWorld().equals(this.world) || chunk.getX() != this.chunkX || chunk.getZ() != this.chunkZ) {
			return false;
		}

		Biome biome = this.world.getBiome((this.chunkX * 16) + 8, (this.chunkZ * 16) + 8);

		if (!this.worldConfig.getStringList(Config.FEATURE_DUNGEONS_BIOMES).contains(biome.name())) {
			return false;
		}

		if (this.getRandom().nextInt(100) > this.worldConfig.getInt(Config.FEATURE_DUNGEONS_CHANCE)) {
			return false;
		}

		return true;
	}

}

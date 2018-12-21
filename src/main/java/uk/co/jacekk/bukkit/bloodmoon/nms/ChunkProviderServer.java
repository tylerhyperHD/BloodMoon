package uk.co.jacekk.bukkit.bloodmoon.nms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_13_R2.BiomeBase;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.ChunkGenerator;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumCreatureType;
import net.minecraft.server.v1_13_R2.IAsyncTaskHandler;
import net.minecraft.server.v1_13_R2.IChunkLoader;
import net.minecraft.server.v1_13_R2.WorldServer;
import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;

public class ChunkProviderServer extends net.minecraft.server.v1_13_R2.ChunkProviderServer {

	private final BloodMoon plugin;
	private final List<BiomeBase.BiomeMeta> bloodMoonMobs = new ArrayList<>();

	@SuppressWarnings({ "unchecked", "deprecation" })
	public ChunkProviderServer(BloodMoon plugin, WorldServer worldserver, IChunkLoader ichunkloader,
			ChunkGenerator<?> chunkgenerator, IAsyncTaskHandler iasynctaskhandler) {
		super(worldserver, ichunkloader, chunkgenerator, iasynctaskhandler);

		this.plugin = plugin;

		// PluginConfig worldConfig =
		// this.plugin.getConfig(this.world.worldData.getName());
		PluginConfig worldConfig = this.plugin.getConfig(this.world.worldData.world.getWorld());

		for (String name : worldConfig.getStringList(Config.FEATURE_SPAWN_CONTROL_SPAWN)) {
			EntityTypes<? extends Entity> entityClass = EntityTypes.a(EntityType.valueOf(name).getName());
			this.bloodMoonMobs
					.add(new BiomeBase.BiomeMeta((EntityTypes<? extends EntityInsentient>) entityClass, 10, 4, 4)); // Entity
																													// class,
																													// weight,
																													// minGroupSize,
																													// maxGroupSize
		}
	}

	public List<BiomeBase.BiomeMeta> getMobsFor(EnumCreatureType creatureType, int x, int y, int z) {
		return (this.plugin.isActive(this.world.worldData.world.getWorld())) ? this.bloodMoonMobs
				: super.chunkGenerator.getMobsFor(creatureType, new BlockPosition(x, y, z));
	}

}

package uk.co.jacekk.bukkit.bloodmoon.feature.world;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonEndEvent;
import uk.co.jacekk.bukkit.bloodmoon.event.BloodMoonStartEvent;

public class ExtendedNightListener extends BaseListener<BloodMoon> {

	private final HashMap<String, Integer> killCount = new HashMap<>();
	@SuppressWarnings("serial")
	private final ArrayList<EntityType> hostileTypes = new ArrayList<EntityType>() {
		{
			add(EntityType.SKELETON);
			add(EntityType.SPIDER);
			add(EntityType.CAVE_SPIDER);
			add(EntityType.ZOMBIE);
			add(EntityType.PIG_ZOMBIE);
			add(EntityType.CREEPER);
			add(EntityType.ENDERMAN);
			add(EntityType.BLAZE);
			add(EntityType.GHAST);
			add(EntityType.MAGMA_CUBE);
			add(EntityType.WITCH);
			add(EntityType.ENDERMITE);
			add(EntityType.ENDER_DRAGON);
			add(EntityType.GUARDIAN);
			add(EntityType.SILVERFISH);
		}
	};

	public ExtendedNightListener(BloodMoon plugin) {
		super(plugin);
	}

	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStart(BloodMoonStartEvent event) {
		World world = event.getWorld();
		PluginConfig worldConfig = plugin.getConfig(world);

		if (plugin.isFeatureEnabled(world, Feature.EXTENDED_NIGHT)) {
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

			if (!worldConfig.getBoolean(Config.ALWAYS_ON)) {
				killCount.put(world.getName(), 0);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onStop(BloodMoonEndEvent event) {
		World world = event.getWorld();

		if (plugin.isFeatureEnabled(world, Feature.EXTENDED_NIGHT)) {
			killCount.remove(world.getName());
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
		if (hostileTypes.contains(event.getEntityType())) {
			World world = event.getEntity().getWorld();
			String worldName = world.getName();
			PluginConfig worldConfig = plugin.getConfig(world);

			if (plugin.isFeatureEnabled(world, Feature.EXTENDED_NIGHT) && killCount.containsKey(worldName)) {
				killCount.put(worldName, killCount.get(worldName) + 1);

				if (killCount.get(worldName) > worldConfig.getInt(Config.FEATURE_EXTENDED_NIGHT_MIN_KILLS)) {
					world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
				}
			}
		}
	}

}

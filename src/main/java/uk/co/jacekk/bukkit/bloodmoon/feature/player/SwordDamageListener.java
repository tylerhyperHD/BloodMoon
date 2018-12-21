package uk.co.jacekk.bukkit.bloodmoon.feature.player;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import uk.co.jacekk.bukkit.baseplugin.config.PluginConfig;
import uk.co.jacekk.bukkit.baseplugin.event.BaseListener;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.Config;
import uk.co.jacekk.bukkit.bloodmoon.Feature;

@SuppressWarnings("deprecation")
public class SwordDamageListener extends BaseListener<BloodMoon> {

	private final Random random = new Random();

	public SwordDamageListener(BloodMoon plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		World world = entity.getWorld();
		PluginConfig worldConfig = plugin.getConfig(world);

		if (event.getCause() == DamageCause.ENTITY_ATTACK && plugin.isActive(world)
				&& plugin.isFeatureEnabled(world, Feature.SWORD_DAMAGE)) {
			if (entity instanceof Creature) {
				Creature creature = (Creature) entity;
				String creatureName = creature.getType().name().toUpperCase();
				LivingEntity target = creature.getTarget();

				if (target instanceof Player) {
					Player player = (Player) target;
					ItemStack item = player.getItemInHand();
					String itemName = item.getType().name().toUpperCase();

					if (worldConfig.getStringList(Config.FEATURE_SWORD_DAMAGE_MOBS).contains(creatureName)
							&& itemName.endsWith("_SWORD")
							&& this.random.nextInt(100) <= worldConfig.getInt(Config.FEATURE_SWORD_DAMAGE_CHANCE)) {
						short damage = item.getDurability();
						short remove = (short) (item.getType().getMaxDurability() / 50);

						item.setDurability((short) ((damage > remove) ? damage - remove : 1));
					}
				}
			}
		}
	}

}

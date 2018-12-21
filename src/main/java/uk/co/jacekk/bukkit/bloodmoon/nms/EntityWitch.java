package uk.co.jacekk.bukkit.bloodmoon.nms;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftWitch;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_13_R2.World;
import uk.co.jacekk.bukkit.bloodmoon.BloodMoon;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityType;
import uk.co.jacekk.bukkit.bloodmoon.entity.BloodMoonEntityWitch;

public class EntityWitch extends net.minecraft.server.v1_13_R2.EntityWitch {

	private BloodMoon plugin;
	private BloodMoonEntityWitch bloodMoonEntity;

	public EntityWitch(World world) {
		super(world);

		Plugin gPlugin = Bukkit.getPluginManager().getPlugin("BloodMoon");

		if (gPlugin == null || !(gPlugin instanceof BloodMoon)) {
			this.world.removeEntity(this);
			return;
		}

		this.plugin = (BloodMoon) gPlugin;

		this.bukkitEntity = new CraftWitch((CraftServer) this.plugin.getServer(), this);
		this.bloodMoonEntity = new BloodMoonEntityWitch(this.plugin, this, BloodMoonEntityType.WITCH);
	}

	@Override
	public boolean cp() {
		try {
			this.bloodMoonEntity.onTick();
			super.co();
		} catch (Exception e) {
			plugin.getLogger().warning("Exception caught while ticking entity");
			e.printStackTrace();
		}
		return true;
	}

}

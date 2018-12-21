package uk.co.jacekk.bukkit.bloodmoon.event;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BloodMoonEndEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private boolean isCancelled = false;

	private final World world;

	public BloodMoonEndEvent(World world) {
		this.world = world;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}
}

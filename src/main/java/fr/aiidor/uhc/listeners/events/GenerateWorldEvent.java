package fr.aiidor.uhc.listeners.events;

import org.bukkit.WorldCreator;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aiidor.uhc.game.Game;

public class GenerateWorldEvent extends Event {
	
	private final static HandlerList handlers = new HandlerList();
	
	private Game game;
	private WorldCreator wc;
	
	private Boolean cancelled;
	
	public GenerateWorldEvent(WorldCreator wc, Game game) {
		this.game = game;
		this.wc = wc;
		
		this.cancelled = false;
	}
	
	public WorldCreator getWorldCreator() {
		return wc;
	}
	
	public void setWorldCreator(WorldCreator wc) {
		this.wc = wc;
	}
	
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public Boolean isCancelled() {
		return cancelled;
	}
	
	public Game getGame() {
		return game;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}

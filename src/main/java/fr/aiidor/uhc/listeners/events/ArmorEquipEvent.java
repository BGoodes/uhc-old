package fr.aiidor.uhc.listeners.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.game.Game;

public class ArmorEquipEvent extends Event {
	
	private final static HandlerList handlers = new HandlerList();
	
	private Game game;
	
	private Player player;
	private ItemStack item;

	private Boolean cancelled;
	
	public ArmorEquipEvent(Player player, ItemStack item, Game game) {
		this.game = game;
		
		this.player = player;
		this.item = item;
		this.cancelled = false;
	}
	
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public ItemStack getItem() {
		return item;
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

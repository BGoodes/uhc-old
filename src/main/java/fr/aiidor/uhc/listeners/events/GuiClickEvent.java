package fr.aiidor.uhc.listeners.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.game.Game;

public class GuiClickEvent {
	
	private InventoryClickEvent event;
	private Inventory inventory;
	private Player player;
	private ItemStack clicked;
	private Game game;
	
	public GuiClickEvent(InventoryClickEvent event, Inventory inventory, Player player, ItemStack clicked, Game game) {
		this.event = event;
		this.inventory = inventory;
		this.player = player;
		this.clicked = clicked;
		this.game = game;
	}
	
	public InventoryClickEvent getEvent() {
		return event;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public ItemStack getItemClicked() {
		return clicked;
	}
	
	public Game getGame() {
		return game;
	}
}

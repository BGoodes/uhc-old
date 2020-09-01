package fr.aiidor.uhc.listeners.events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class UHCPlayerDeathEvent extends Event {
	
	private final static HandlerList handlers = new HandlerList();
	
	private UHCPlayer p;
	private UHCPlayer k;
	private Location loc;
	
	private List<ItemStack> drops;
	private Integer dropped_exp;
	
	private String death_message;
	private Sound death_sound;
	
	private Boolean cancel_tp;
	
	private Game game;
	
	private Boolean cancelled;
	
	public UHCPlayerDeathEvent(UHCPlayer p, UHCPlayer k, Location loc, List<ItemStack> drops, Integer dropped_exp, String death_message, Game game) {
		this.cancelled = false;
		
		this.p = p;
		this.k = k;
		this.loc = loc;
		
		this.drops = drops;
		this.dropped_exp = dropped_exp;
		
		this.death_message = death_message;
		this.death_sound = game.getSettings().death_sound;
		
		this.cancel_tp = false;
		
		this.game = game;
	}
	
	public UHCPlayer getUHCPlayer() {
		return p;
	}

	public Player getPlayer() {
		return p.getPlayer();
	}
	
	public UHCPlayer getKiller() {
		return k;
	}
	
	public Boolean hasKiller() {
		return k != null;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public List<ItemStack> getDrops() {
		return drops;
	}
	
	public Boolean hasDrops() {
		return getDrops() != null && !getDrops().isEmpty();
	}
	
	public void setDroppedExp(Integer dropped_exp) {
		this.dropped_exp = dropped_exp;
	}
	
	
	public Integer getDroppedExp() {
		return dropped_exp;
	}
	
	public String getDeathMessage() {
		return death_message;
	}
	
	public void setDeathMessage(String death_message) {
		this.death_message = death_message;
	}
	
	public Sound getDeathSound() {
		return death_sound;
	}
	
	public void setDeathSound(Sound death_sound) {
		this.death_sound = death_sound;
	}
	
	public Boolean getCancelTeleport() {
		return cancel_tp;
	}
	
	public void setCancelTeleport(Boolean cancel_tp) {
		this.cancel_tp = cancel_tp;
	}
	
	public void setCancelled(Boolean state) {
		this.cancelled = state;
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

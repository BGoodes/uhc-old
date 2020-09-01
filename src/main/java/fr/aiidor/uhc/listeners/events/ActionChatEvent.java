package fr.aiidor.uhc.listeners.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aiidor.uhc.enums.ActionChat;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class ActionChatEvent extends Event {
	
	private final static HandlerList handlers = new HandlerList();
	
	private UHCPlayer p;
	private ActionChat action;
	private String message;
	
	private Game game;
	
	public ActionChatEvent(UHCPlayer p, ActionChat action, String message, Game game) {
		this.p = p;
		this.action = action;
		this.game = game;
		this.message = message;
	}
	
	public UHCPlayer getUHCPlayer() {
		return p;
	}
	
	public ActionChat getActionChat() {
		return action;
	}
	
	public String getMessage() {
		return message;
	}

	public Player getPlayer() {
		return p.getPlayer();
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

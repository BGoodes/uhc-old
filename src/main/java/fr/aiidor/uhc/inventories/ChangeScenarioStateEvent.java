package fr.aiidor.uhc.inventories;

import org.bukkit.entity.Player;

import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.scenarios.Scenario;

public class ChangeScenarioStateEvent {
	
	private Scenario scenario;
	private Boolean newState;
	private Player player;
	private Game game;
	
	private Boolean cancelled;
	
	public ChangeScenarioStateEvent(Scenario scenario, Boolean newState, Player player, Game game) {
		this.scenario = scenario;
		this.newState = newState;
		this.game = game;
		this.player = player;
		
		this.cancelled = false;
	}
	
	public Scenario getScenario() {
		return scenario;
	}
	
	public Boolean getState() {
		return newState;
	}
	
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public Boolean isCancelled() {
		return cancelled;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Game getGame() {
		return game;
	}
}

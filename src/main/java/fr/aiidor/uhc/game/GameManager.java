
package fr.aiidor.uhc.game;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class GameManager {
	
	private Game game = null;
	
	//GAME
	public Boolean hasGame() {
		return game != null;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public Game createGame(String name, Player host, World world) {
		this.game = new Game(name, host, world);
		return game;
	}
	
	//PLAYERS
	public Boolean isInGame(Player player) {
		return isInGame(player.getUniqueId());
	}
	
	public Boolean isInGame(UUID uuid) {
		
		if (hasGame()) {
			if (game.isHere(uuid)) {
				return true;
			}
		}
		
		return false;
	}
} 

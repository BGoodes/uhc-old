
package fr.aiidor.uhc.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.scenarios.Scenario;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.tools.Titles;

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
	
	public Game createGame(String name, UHCType type, GameSettings settings, Player host, World world) {
		this.game = new Game(name, type, settings, host, world);
		return game;
	}
	
	public void restartGame() {
		this.game.delete();
		this.game = createGame(game.getName(), game.getType(), game.getSettings(), game.getHost().getPlayer(), game.getMainWorld().getOverWorld());
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
	
	public void end() {
		
		if (game.getAlivePlayers().isEmpty()) {
			Bukkit.broadcastMessage(Lang.BC_DRAW.get());
			
			for (UHCPlayer p : game.getIngamePlayers()) {
				if (p.isConnected()) {
					new Titles(p.getPlayer()).sendTitle(Lang.BC_DRAW_LINE_1.get(), Lang.BC_DRAW_LINE_2.get(), 60);
				}
			}
			
			stopGame();
		}
		
		
		if (!game.hasTeam()) {
			
			if (game.getAlivePlayers().size() == 1) {
				for (UHCPlayer player : game.getAlivePlayers()) {
					
					Bukkit.broadcastMessage(Lang.BC_SOLO_VICTORY.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
					if (player.isConnected()) new Titles(player.getPlayer()).sendTitle(Lang.BC_VICTORY_TITLE_LINE_1.get(), Lang.BC_VICTORY_TITLE_LINE_2.get(), 60);
					
					for (UHCPlayer p : game.getIngamePlayers()) {
						if (p.isConnected() && p.isDead()) {
							new Titles(p.getPlayer()).sendTitle(Lang.BC_LOSE_TITLE_LINE_1.get(), Lang.BC_LOSE_TITLE_LINE_2.get(), 60);
						}
					}
					
					stopGame();
					break;
				}
			}
		}
		
		else {
			
			if (game.getAliveTeams().size() == 1) {
				
				if (ScenariosManager.ONLY_ONE_WINNER.isActivated()) {
					game.destroyTeams();
					game.playSound(Sound.GHAST_MOAN, 0.6f);
					game.broadcast(Lang.BC_ONE_WINNER.get());
					return;
				}
				
				for (UHCTeam team : game.getAliveTeams()) {
					
					Bukkit.broadcastMessage(Lang.BC_TEAM_VICTORY.get().replace(LangTag.TEAM_NAME.toString(), team.getName()));
					for (UHCPlayer p : game.getIngamePlayers()) {
						if (p.isConnected()) {
							if (p.isDead()) new Titles(p.getPlayer()).sendTitle(Lang.BC_LOSE_TITLE_LINE_1.get(), Lang.BC_LOSE_TITLE_LINE_2.get(), 60);
							else new Titles(p.getPlayer()).sendTitle(Lang.BC_VICTORY_TITLE_LINE_1.get(), Lang.BC_VICTORY_TITLE_LINE_2.get(), 60);
						}
					}
					
					stopGame();
					break;
				}
			}
			
		}
	}
	
	private void stopGame() {
		
		for (Scenario s : game.getSettings().getActivatedScenarios()) {
			s.stop();
		}
		
		game.getRunner().stop();
		game.setState(GameState.ENDING);
	}
} 

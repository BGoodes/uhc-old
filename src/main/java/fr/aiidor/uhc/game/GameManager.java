
package fr.aiidor.uhc.game;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.gamemodes.UHCMode;
import fr.aiidor.uhc.scenarios.Scenario;
import fr.aiidor.uhc.tools.Titles;
import fr.aiidor.uhc.tools.UHCItem;
import fr.aiidor.uhc.world.UHCWorld;

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
	
	public Game createGame(String name, UHCMode mode, GameSettings settings, Player host, Set<UHCPlayer> players, UHCWorld world, List<UHCWorld> worlds) {
		this.game = new Game(name, mode, settings, host, players,  world, worlds);
		return game;
	}
	
	public void restartGame() {
		
		reload();
		Set<UHCPlayer> players = game.getAllPlayers();
		
		
		createGame(game.getName(), game.getUHCMode(), game.getSettings(), null, null, game.getMainWorld(), game.getUHCWorlds());
		
		if (UHC.getInstance().getSettings().hasCage()) {
			UHC.getInstance().getSettings().cage.create();
		}
		
		for (UHCPlayer p : players) {
			
			if (p.isConnected()) {
				
				Player player = p.getPlayer();
				player.setScoreboard(game.getScoreboard());
				
				UHCPlayer np = new UHCPlayer(player, PlayerState.ALIVE, p.getRank(), game);
				game.addUHCPlayer(np);
				
				np.reset();
				
				player.teleport(UHC.getInstance().getSettings().lobby);
				
				if (np.hasPermission(Permission.CONFIG)) {
					player.getInventory().setItem(4, UHCItem.config_chest);
				}
				
				if (game.hasTeam()) {
					if (game.getSettings().team_type == TeamType.CHOOSE) {
						if (np.hasTeam()) {
							player.getInventory().setItem(8, UHCItem.getTeamSelecter(np.getTeam()));
						} else {
							player.getInventory().setItem(8, UHCItem.getTeamSelecter());
						}
					}
				}
			}
		}
	}
	
	public void reload() {
		
		if (game.isRunning()) game.getRunner().stop();
		
		for (Scenario s : game.getSettings().getActivatedScenarios()) {
			s.reload();
		}
		
		game.getUHCMode().stop();
	}
	
	
	public void stopGame() {
		
		if (!hasGame()) return;
		game.setState(GameState.ENDING);
		
		if (game.isRunning()) game.getRunner().stop();
		
		if (UHC.getInstance().getSettings().server_restart) {
			
			Bukkit.broadcastMessage(Lang.BC_SERVER_RESTART.get());
			
			Bukkit.getScheduler().runTaskLater(UHC.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.kickPlayer(Lang.CAUSE_SERVER_CLOSE.get());
					}
					
					Bukkit.shutdown();
				}
			}, 20 * 60);
		}
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
		
		if (!game.getSettings().can_win) return;
		
		if (game.getAlivePlayers().isEmpty()) {
			game.broadcast(Lang.BC_DRAW.get());
			
			for (UHCPlayer p : game.getIngamePlayers()) {
				if (p.isConnected()) {
					new Titles(p.getPlayer()).sendTitle(Lang.BC_DRAW_LINE_1.get(), Lang.BC_DRAW_LINE_2.get(), 60);
				}
			}
			
			stopGame();
		}
		
		game.getUHCMode().end();
	}
} 

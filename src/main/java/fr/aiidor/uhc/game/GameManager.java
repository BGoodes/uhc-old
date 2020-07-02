
package fr.aiidor.uhc.game;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.gamemodes.UHCMode;
import fr.aiidor.uhc.scenarios.Scenario;
import fr.aiidor.uhc.tools.Titles;
import fr.aiidor.uhc.tools.UHCItem;

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
	
	public Game createGame(String name, UHCMode mode, GameSettings settings, Player host, Set<UHCPlayer> players, World world) {
		this.game = new Game(name, mode, settings, host, players, world);
		return game;
	}
	
	public void restartGame() {
		
		if (game.isRunning()) game.getRunner().stop();
		
		for (Scenario s : game.getSettings().getActivatedScenarios()) {
			s.reload();
		}
		
		game.getUHCMode().stop();
		
		Set<UHCPlayer> players = game.getAllPlayers();
		
		
		createGame(game.getName(), game.getUHCMode(), game.getSettings(), null, null, game.getMainWorld().getOverWorld());
		
		if (UHC.getInstance().getSettings().hasCage()) {
			UHC.getInstance().getSettings().cage.create();
		}
		
		for (UHCPlayer p : players) {
			
			p.reset();
			
			if (p.isConnected()) {
				
				Player player = p.getPlayer();
				player.setScoreboard(game.getScoreboard());
				
				game.addUHCPlayer(new UHCPlayer(player, PlayerState.ALIVE, p.getRank(), game));
				
				player.teleport(UHC.getInstance().getSettings().lobby);
				
				if (p.hasPermission(Permission.CONFIG)) {
					player.getInventory().setItem(4, UHCItem.getConfigChest());
				}
				
				if (game.hasTeam()) {
					if (game.getSettings().team_type == TeamType.CHOOSE) {
						if (p.hasTeam()) {
							player.getInventory().setItem(8, UHCItem.getTeamSelecter(p.getTeam()));
						} else {
							player.getInventory().setItem(8, UHCItem.getTeamSelecter());
						}
					}
				}
			}

		}
	}
	
	
	public void stopGame() {
		
		if (game.isRunning()) game.getRunner().stop();
		
		game.setState(GameState.ENDING);
		
		if (UHCFile.CONFIG.getYamlConfig().getBoolean("ServerManager.server-restart")) {
			
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
			Bukkit.broadcastMessage(Lang.BC_DRAW.get());
			
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

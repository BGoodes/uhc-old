package fr.aiidor.uhc.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.tools.UHCItem;

public class PlayerConnexionEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoinServer(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		GameManager gm = UHC.getInstance().getGameManager();
		
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		
		e.setJoinMessage("");
		
		if (player.isOp() && UHC.getInstance().getSettings().getRemoveOp()) {
			player.setOp(false);
		}
		
		if (!game.join(player)) return;	
		
		if (game.getState() == GameState.LOBBY) {
					
			if (!game.isHere(player.getUniqueId())) {
				
				if (player.isOp() && UHC.getInstance().getSettings().OpIsStaff()) {
					game.addUHCPlayer(new UHCPlayer(player, Rank.STAFF, game));
				} else {
					game.addUHCPlayer(new UHCPlayer(player, Rank.PLAYER, game));
				}
			}
				
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			p.reset();
			
			player.teleport(UHC.getInstance().getSettings().getLobby());
			
			if (game.hasTeam()) {
				if (game.getSettings().team_type == TeamType.CHOOSE) {
					player.getInventory().setItem(8, UHCItem.getTeamSelecter());
				}
			}
			
			if (p.hasPermission(Permission.CONFIG)) {
				player.getInventory().setItem(4, UHCItem.getConfigChest());
			}
		}
		
		//MESSAGE
		if (game.isHere(player.getUniqueId())) {
			
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			p.setName(player.getName());
			p.onLogin();
			
			player.setScoreboard(game.getScoreboard());
			UHC.getInstance().getScoreboardManager().onLogin(player);
			
			if (p.getRank() == Rank.SPECTATOR || p.getState() == PlayerState.SPECTATOR || p.getState() == PlayerState.DEAD) {
				e.setJoinMessage(Lang.BC_SPECTATOR_JOIN.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
				p.reset();
			}
				
			else e.setJoinMessage(Lang.BC_PLAYER_JOIN.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
		}

	}
	
	@EventHandler
	public void onPlayerLeaveServer(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		GameManager gm = UHC.getInstance().getGameManager();
		
		e.setQuitMessage("");
		UHC.getInstance().getScoreboardManager().onLogout(player);
		
		if (!gm.hasGame()) return;
		Game game = gm.getGame();
		
		if (!game.isHere(player.getUniqueId())) return;
				
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
				
		if (!game.isStart()) {
			if (p.hasTeam()) p.getTeam().leave(p);
			if (p.getRank() == Rank.PLAYER || p.getRank() == Rank.SPECTATOR) {
				 game.removeUHCPlayer(p);
			}
			
		}
		
		//MESSAGE
		if (p.getRank() == Rank.SPECTATOR || p.getState() == PlayerState.SPECTATOR || p.getState() == PlayerState.DEAD)
			e.setQuitMessage(Lang.BC_SPECTATOR_LEAVE.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
		
		else e.setQuitMessage(Lang.BC_PLAYER_LEAVE.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
	}
}
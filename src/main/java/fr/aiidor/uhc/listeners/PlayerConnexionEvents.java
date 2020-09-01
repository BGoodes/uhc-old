package fr.aiidor.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.ActionChat;
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
		
		e.setJoinMessage(null);
		
		if (player.isOp() && UHC.getInstance().getSettings().remove_op) {
			player.setOp(false);
		}
		
		if (!game.join(player)) return;	
		
		if (game.getState() == GameState.WAITING) {
					
			if (!game.isHere(player.getUniqueId())) {
				game.addUHCPlayer(player);
			}
				
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			p.reset();
			
			player.teleport(UHC.getInstance().getSettings().lobby);
			
			if (game.hasTeam()) {
				if (game.getSettings().team_type == TeamType.CHOOSE) {
					if (p.hasTeam()) {
						player.getInventory().setItem(8, UHCItem.getTeamSelecter(p.getTeam()));
					} else {
						player.getInventory().setItem(8, UHCItem.getTeamSelecter());
					}
				}
			}
			
			if (p.hasPermission(Permission.CONFIG)) {
				player.getInventory().setItem(4, UHCItem.config_chest);
			}
		}
		
		//MESSAGE
		if (game.isHere(player.getUniqueId())) {
			
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			p.refreshName(player);
			p.onLogin();
			
			player.setScoreboard(game.getScoreboard());
			UHC.getInstance().getScoreboardManager().onLogin(player);
			UHC.getInstance().getTablistManager().onLogin(player);
			
			if (p.isSpec()) {
				e.setJoinMessage(Lang.BC_SPECTATOR_JOIN.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
			} else e.setJoinMessage(Lang.BC_PLAYER_JOIN.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
			
		} else {
			//SPEC VERIF
			UHCPlayer p = new UHCPlayer(player, PlayerState.SPECTATOR, Rank.SPECTATOR, game);
			
			if (player.isOp() && UHC.getInstance().getSettings().op_to_staff) {
				p = new UHCPlayer(player, PlayerState.SPECTATOR, Rank.STAFF, game);
			}
			
			e.setJoinMessage(Lang.BC_SPECTATOR_JOIN.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
			game.addUHCPlayer(p);
			p.onLogin();
		}

	}
	
	@EventHandler
	public void onPlayerLeaveServer(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		GameManager gm = UHC.getInstance().getGameManager();
		
		ActionChat.onLogout(player);
		
		UHC.getInstance().getScoreboardManager().onLogout(player);
		UHC.getInstance().getTablistManager().onLogout(player);
		
		if (!gm.hasGame()) return;
		Game game = gm.getGame();
		
		if (!game.isHere(player.getUniqueId())) {
			e.setQuitMessage(Lang.BC_SPECTATOR_LEAVE.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
			return;
		}
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
				
		if (!game.isStart()) {
			if (p.hasTeam()) p.getTeam().leave(p, false);
			if (p.getRank() == Rank.PLAYER || p.getRank() == Rank.SPECTATOR) {
				 game.removeUHCPlayer(p);
			}
		}
		
		//MESSAGE
		if (p.isSpec())
			e.setQuitMessage(Lang.BC_SPECTATOR_LEAVE.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
		
		else e.setQuitMessage(Lang.BC_PLAYER_LEAVE.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
	}
}

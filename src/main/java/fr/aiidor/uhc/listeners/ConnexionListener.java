package fr.aiidor.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
import fr.aiidor.uhc.task.UnhostTask;
import fr.aiidor.uhc.utils.UHCItem;

public class ConnexionListener implements Listener {
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) {
		
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
			
			//HOST =================================
			if (UHC.getInstance().getSettings().auto_host && player.hasPermission("uhc.host") && !game.hasHost()) {
				
				if (!game.isHere(player.getUniqueId())) game.addUHCPlayer(new UHCPlayer(player, PlayerState.ALIVE, Rank.HOST, game));
				else game.getUHCPlayer(player.getUniqueId()).setRank(Rank.HOST);
				
				Bukkit.getScheduler().runTaskLater(UHC.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						player.sendMessage(Lang.ST_BECOME_HOST.get());
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.7f, 1);
					}
				}, 1);
			}
			
			//ADD PLAYER =============================
			if (!game.isHere(player.getUniqueId())) {
				
				if (player.hasPermission("uhc.staff")) game.addUHCPlayer(new UHCPlayer(player, PlayerState.ALIVE, Rank.STAFF, game));
				else game.addUHCPlayer(new UHCPlayer(player, PlayerState.ALIVE, Rank.PLAYER, game));
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
			
			if (UHC.getInstance().getSettings().op_to_host && game.isHost(p))
				player.setOp(true);
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
			
			if (player.hasPermission("uhc.staff")) {
				p = new UHCPlayer(player, PlayerState.SPECTATOR, Rank.STAFF, game);
			}
			
			e.setJoinMessage(Lang.BC_SPECTATOR_JOIN.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
			game.addUHCPlayer(p);
			p.onLogin();
		}

	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		GameManager gm = UHC.getInstance().getGameManager();
		
		ActionChat.onLogout(player);
		
		UHC.getInstance().getScoreboardManager().onLogout(player);
		UHC.getInstance().getTablistManager().onLogout(player);
		
		if (!gm.hasGame()) return;
		
		e.setQuitMessage(null);
		
		Game game = gm.getGame();
		
		if (!game.isHere(player.getUniqueId())) {
			//e.setQuitMessage(Lang.BC_SPECTATOR_LEAVE.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
			return;
		}
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (game.isHost(p) && UHC.getInstance().getSettings().auto_unhost >= 0) {
			new UnhostTask(game, p);
		}
		
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

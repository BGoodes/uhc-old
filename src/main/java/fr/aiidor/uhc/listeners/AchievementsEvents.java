package fr.aiidor.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;

public class AchievementsEvents implements Listener {
	
	@EventHandler
	public void onAwardAchievement(PlayerAchievementAwardedEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (!game.isStart()) {
			e.setCancelled(true);
			return;
		}
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (!p.isAlive()) {
			e.setCancelled(true);
			return;
		}
	}
}

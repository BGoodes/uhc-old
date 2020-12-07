package fr.aiidor.uhc.task;

import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class UnhostTask extends BukkitRunnable {

	private Integer time;
	
	private Game game;
	private UHCPlayer player;
	
	public UnhostTask(Game game, UHCPlayer player) {
		time = UHC.getInstance().getSettings().auto_unhost;
		
		this.player = player;
		this.game = game;
		
		if (time == 0) {
			remove();
			return;
		}
		
		this.runTaskTimer(UHC.getInstance(), 0, 20);
	}
	
	@Override
	public void run() {
		
		time--;
		
		if (!UHC.getInstance().getGameManager().getGame().equals(game) || player.isConnected()) {
			cancel();
			return;
		}
		
		if (time <= 0) {
			cancel();
			
			remove();
			return;
		}
	}
	
	private void remove() {
		if (game.isStart()) {
			if (player.isAlive()) player.setRank(Rank.PLAYER);
			else player.setRank(Rank.SPECTATOR);
		}
		else {
			game.removeUHCPlayer(player);
		}
	}
}

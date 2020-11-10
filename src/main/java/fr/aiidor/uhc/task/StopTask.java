package fr.aiidor.uhc.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;

public class StopTask extends UHCTask {
	
	private Game game;
	private Integer timer;
	
	public StopTask(Game game) {
		timer = 31;
		
		this.game = game;
	}
	
	@Override
	public void launch() {
		
		Bukkit.broadcastMessage(Lang.BC_SERVER_RESTART.get());
		
		if (game.isRunning()) game.getRunner().stop();
		
		game.setRunner(this);
		
		this.runTaskTimer(UHC.getInstance(), 0, 20);
	}

	@Override
	public void run() {
		timer--;
		
		if (timer <= 0) {
			close();
			return;
		}
	}

	@Override
	public void stop() {
		
		cancel();
		
		Bukkit.broadcastMessage(Lang.BC_SERVER_RESTART_CANCEL.get());
		game.setRunner(null);
		
	}
	
	public void close() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer(Lang.CAUSE_SERVER_CLOSE.get());
		}
		
		Bukkit.shutdown();
	}
	
	@Override
	public Integer getTime() {
		return timer;
	}

}

package fr.aiidor.uhc.task;

import org.bukkit.Sound;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class Starting_Task extends UHC_Task {

	private Game game;
	private Integer Timer;
	
	public Starting_Task(Game game) {
		Timer = 31;
		
		this.game = game;
		this.game.setState(GameState.STARTING);
		
		this.runTaskTimer(UHC.getInstance(), 0, 20);
	}
	
	private String[] color = {"§a", "§e", "§6", "§c", "§4"};
	
	@Override
	public void run() {
		
		Timer --;
		
		if (Timer <= 0) {
			start();
			return;
		}
		
		setXp(Timer);
		
		if (Timer == 10 || Timer == 20 || Timer == 30) {
			game.playSound(Sound.CLICK, 0.6f);
			game.broadcast(Lang.BC_GAME_STARTING.get());
		}
		
		if (Timer <= 5) {
			game.playSound(Sound.NOTE_PLING, 0.6f);
			game.title(color[Timer - 1] + Timer, 5, 20, 0);
		}
	}
	
	private void setXp(Integer level) {
		for (UHCPlayer p : game.getAllConnectedPlayers()) {
			p.getPlayer().setLevel(level);
			p.getPlayer().setExp(0);
		}
	}
	
	@Override
	public void stop() {
		setXp(0);
		cancel();
		
		game.broadcast(Lang.BC_GAME_START_CANCEL.get());
		game.playSound(Sound.NOTE_BASS_GUITAR, 1f);
		
		game.setRunner(null);
		game.setState(GameState.LOBBY);
	}
	
	private void start() {
		setXp(0);
		cancel();
		
		game.setRunner(null);
		game.setState(GameState.LOADING);
		
		new Loading_task(game).load();
	}
	
	@Override
	public Integer getTime() {
		return Timer;
	}
}

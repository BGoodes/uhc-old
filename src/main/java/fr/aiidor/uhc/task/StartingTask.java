package fr.aiidor.uhc.task;

import org.bukkit.Sound;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.Scenario;

public class StartingTask extends UHCTask {

	private Game game;
	private Integer timer;
	
	public StartingTask(Game game) {
		timer = 31;
		
		this.game = game;
	}
	
	@Override
	public void launch() {
		
		if (game.getMainWorld().getMainWorld() == null) {
			game.log(Lang.ST_ERROR_WORLD_GENERATION.get());
			return;
		}
		
		if (game.isRunning()) game.getRunner().stop();
		
		game.setRunner(this);
		game.setState(GameState.STARTING);
		
		for (Scenario s : game.getSettings().getActivatedScenarios()) {
			s.checkConditions(true);
		}
		
		this.runTaskTimer(UHC.getInstance(), 0, 20);
	}
	
	private String[] color = {"§a", "§e", "§6", "§c", "§4"};
	
	@Override
	public void run() {
		
		timer --;
		
		if (timer <= 0) {
			start();
			return;
		}
		
		setXp(timer);
		
		if (timer == 10 || timer == 20 || timer == 30) {
			game.playSound(Sound.CLICK, 0.6f);
			game.broadcast(Lang.BC_GAME_STARTING.get());
		}
		
		if (timer <= 5) {
			game.playSound(Sound.NOTE_PLING, 0.6f);
			game.title(color[timer - 1] + timer, 5, 20, 0);
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
		
		new WaitingTask(game).launch();
		game.setState(GameState.WAITING);
	}
	
	private void start() {
		setXp(0);
		cancel();
		
		new LoadingTask(game).launch();
	}
	
	@Override
	public Integer getTime() {
		return timer;
	}
}

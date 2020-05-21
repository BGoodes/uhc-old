package fr.aiidor.uhc.task;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class Game_Task extends UHC_Task {

	private Game game;
	private Integer Timer;
	
	private Integer episode;
	private Integer next_ep;
	
	public Game_Task(Game game) {
		Timer = -1;
		this.game = game;
		this.episode = 1;
		
		this.next_ep = game.getSettings().ep_time * 60;
		
		this.runTaskTimer(UHC.getInstance(), 0, 20);
	}
	
	@Override
	public void run() {
		
		Timer++;
		
		if (game.getState() != GameState.GAME) {
			stop();
			return;
		}
		
		//INVINCIBILITY
		if (Timer <= game.getSettings().invincibility_time) {
			
			Integer time = game.getSettings().invincibility_time - Timer;
			String replace = time + "s";
			
			if (time == 0) replace = "Â§cOFF";
			
			else if (time >= 60) {
				
				replace = (int) time/60	+ "m" + (int) time%60 + "s";
			}
			
			game.actionText(Lang.INVINCIBILITY_BAR.get().replace(LangTag.TIME.toString(), replace));
			if (Timer == game.getSettings().invincibility_time) game.broadcast(Lang.BC_INVINCIBILITY_END.get());
		}
		
		//PVP
		if (Timer <= game.getSettings().pvp_time * 60) {
			
			if (Timer == game.getSettings().pvp_time * 60) {
				game.playSound(Sound.ENDERDRAGON_GROWL, 0.5f);
				game.broadcast(Lang.BC_PVP_START.get());
				pvpStart();
			}
			else {
				Integer time = game.getSettings().pvp_time * 60 - Timer;
				
				if (time == 60 * 30 || time == 60 * 15 || time == 60 * 5 || time == 60) {
					Integer minute = time/60;
					game.broadcast(Lang.BC_PVP_WAIT.get().replace(LangTag.TIME.toString(), minute.toString()));
				}
			}
		} else {
			
			if (ScenariosManager.ASSASSINS.isActivated(game)) {
				ScenariosManager.ASSASSINS.setDistance();
			}
		}
		

		playerRunnable();
		
		if (Timer == next_ep) {
			//NEW EPISODE
			game.broadcast(Lang.BC_EPISODE_END.get());
			
			episode++;
			this.next_ep = Timer + game.getSettings().ep_time * 60;
		}
		
		
		game.isEnd();
	}
	

	@Override
	public void stop() {
		cancel();
		game.setRunner(null);
	}

	@Override
	public Integer getTime() {
		return Timer;
	}
	
	public Integer getEpisode() {
		return episode;
	}
	
	public void pvpStart() {
		if (ScenariosManager.ASSASSINS.isActivated(game)) {
			ScenariosManager.ASSASSINS.setTargets(game);
		}
	}
	
	
	public void playerRunnable() {
		for (UHCPlayer player : game.getIngamePlayers()) {
			if (player.isConnected()) {
				Player p = player.getPlayer();
				
				if (ScenariosManager.CAT_EYES.isActivated(game)) {
					p.addPotionEffect(ScenariosManager.CAT_EYES.nightVision);
				}
			}
		}
	}
}

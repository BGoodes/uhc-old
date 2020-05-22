package fr.aiidor.uhc.task;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.tools.Titles;

public class GameTask extends UHCTask {

	private Game game;
	
	private Integer timer;
	private GameSettings settings;
	
	private Integer episode;
	private Integer next_ep;
	
	public GameTask(Game game) {
		timer = -1;
		this.game = game;
		this.episode = 1;
		
		this.settings = game.getSettings();
		this.next_ep = settings.ep1_time * 60;
	}
	
	@Override
	public void launch() {
		game.setRunner(this);
		game.setState(GameState.RUNNING);
		
		this.runTaskTimer(UHC.getInstance(), 0, 20);
	}
	
	@Override
	public void run() {
		
		timer++;
		
		if (game.getState() != GameState.RUNNING) {
			stop();
			return;
		}
		
		//INVINCIBILITY
		if (settings.invincibility_time > 0 && timer <= settings.invincibility_time * 60) {
				
			Integer time = settings.invincibility_time * 60 - timer;
			String replace = time + "s";
				
			if (time == 0) replace = "§cOFF";
				
			else if (time >= 60) {
					
				replace = (int) time/60	+ "m" + (int) time%60 + "s";
			}
				
			for (UHCPlayer p : game.getPlayingPlayers()) {
				new Titles(p.getPlayer()).sendActionText(Lang.INVINCIBILITY_BAR.get().replace(LangTag.TIME.toString(), replace));
			}
				
			if (timer == settings.invincibility_time * 60) game.broadcast(Lang.BC_INVINCIBILITY_END.get());
		}

		//-----------------------------------
		
		//PVP
		if (timer <= settings.pvp_time * 60) {
			
			if (timer == settings.pvp_time * 60) {
				game.playSound(Sound.ENDERDRAGON_GROWL, 0.6f);
				game.broadcast(Lang.BC_PVP_START.get());
				pvpStart();
			}
			else {
				Integer time = settings.pvp_time * 60 - timer;
				
				if (time == 60 * 30 || time == 60 * 15 || time == 60 * 5 || time == 60) {
					Integer minute = time/60;
					game.broadcast(Lang.BC_PVP_WAIT.get().replace(LangTag.TIME.toString(), minute.toString()));
				}
			}
			
			//PVP ON
		} else {
			
			if (ScenariosManager.ASSASSINS.isActivated() && !ScenariosManager.ASSASSINS.compass) {
				ScenariosManager.ASSASSINS.setDistance();
			}
		}
		
		//-----------------------------------

		//BORDER
		if (timer <= settings.wb_time * 60) {
			
			if (timer == settings.wb_time * 60) {
				
				game.playSound(Sound.ENDERDRAGON_GROWL, 0.6f);
				game.broadcast(Lang.BC_WB_START.get().replace(LangTag.VALUE.toString(), settings.wb_size_min.toString()));
				game.getMainWorld().getMainWorld().getWorldBorder().setSize(settings.wb_size_min * 2, (long) ((settings.wb_size_max - settings.wb_size_min) / settings.wb_speed) * 2);
			}
			else {
				Integer time = settings.wb_time * 60 - timer;
				
				if (time == 60 * 30 || time == 60 * 15 || time == 60 * 5 || time == 60) {
					Integer minute = time/60;
					game.broadcast(Lang.BC_WB_WAIT.get().replace(LangTag.TIME.toString(), minute.toString()));
				}
			}
			
			//PVP ON
		} else {
			
			//NETHER & END OFF ?
		}
		
		//-----------------------------------
		
		playerRunnable();
		
		if (timer >= next_ep) {
			//NEW EPISODE
			game.broadcast(Lang.BC_EPISODE_END.get());
			
			episode++;
			this.next_ep = timer + settings.ep_time * 60;
		}
		
		
		if (game.getSettings().can_win) UHC.getInstance().getGameManager().end();
	}
	

	@Override
	public void stop() {
		cancel();
		game.setRunner(null);
	}

	@Override
	public Integer getTime() {
		return timer;
	}
	
	public Integer getEpisode() {
		return episode;
	}
	
	public void pvpStart() {
		if (ScenariosManager.ASSASSINS.isActivated()) {
			ScenariosManager.ASSASSINS.start(game);
		}
		
		if (ScenariosManager.FINAL_HEAL.isActivated()) {
			ScenariosManager.FINAL_HEAL.heal(game);
		}
		
		if (ScenariosManager.ASSAULT_AND_BATTERY.isActivated()) {
			ScenariosManager.ASSAULT_AND_BATTERY.load(game);
		}
	}
	
	public void playerRunnable() {
		for (UHCPlayer player : game.getIngamePlayers()) {
			if (player.isConnected()) {
				Player p = player.getPlayer();
				
				if (ScenariosManager.CAT_EYES.isActivated()) {
					p.addPotionEffect(ScenariosManager.CAT_EYES.nightVision);
				}
			}
		}
	}
}

package fr.aiidor.uhc.task;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.tools.Teleportation;
import fr.aiidor.uhc.tools.Titles;

public class GameTask extends UHCTask {

	private Game game;
	
	private Integer timer;
	private GameSettings settings;
	
	private Integer episode;
	private Integer next_ep;
	
	private TimeTask time_task;
	
	public GameTask(Game game) {
		timer = -1;
		this.game = game;
		this.episode = 1;
		
		this.settings = game.getSettings();
		this.next_ep = settings.ep1_time * 60;
		
		this.time_task = null;
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
				
				for (World w : game.getWorlds())  {
					w.getWorldBorder().setSize(settings.wb_size_min * 2, (long) ((settings.wb_size_max - settings.wb_size_min) / settings.wb_speed) * 2);
				}
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
		
		if (game.getSettings().uhc_cycle && episode > 1) {
			Integer m = game.getSettings().double_uhc_cycle ? 2 : 1;
			
			if (timer%60 == 0) {
				
				Integer min = timer/60;
				
				if (min%(10/m) == 0) {
					if (min%(20/m) == 0) {
						
						game.getWorlds().forEach(w->w.setTime(23500));
						game.getUHCMode().day(episode);
					} else {
						
						game.getWorlds().forEach(w->w.setTime(13500));
						game.getUHCMode().night(episode);
					}
				}
			}
		}
		
		//-----------------------------------
		playerRunnable();
		
		if (timer >= next_ep) {
			//NEW EPISODE
			game.broadcast(Lang.BC_EPISODE_END.get());
			
			
			if (episode == 1) {
				game.getUHCMode().begin();
				
				if (game.getSettings().uhc_cycle) {
					
					time_task = new TimeTask(game);
					time_task.runTaskTimer(UHC.getInstance(), 0, 1);
				}
			}
			
			episode++;
			game.getUHCMode().episode(episode);
			
			this.next_ep = timer + settings.ep_time * 60;
		}
		
		game.getUHCMode().run();
		UHC.getInstance().getGameManager().end();
	}
	

	@Override
	public void stop() {
		cancel();
		if (time_task != null) time_task.cancel();
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
		
		if (ScenariosManager.NETHERIBUS.isActivated()) {
			game.broadcast(Lang.NETHERIBUS_ANNOUNCE_START.get());
		}
		
		if (ScenariosManager.SKYHIGH.isActivated()) {
			game.broadcast(Lang.SKYHIGH_ANNOUNCE_START.get());
		}
		
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
		for (UHCPlayer p : game.getPlayingPlayers()) {
			
			Player player = p.getPlayer();
			World world = player.getWorld();
			
			if (ScenariosManager.CAT_EYES.isActivated()) {
				p.addPotionEffect(ScenariosManager.CAT_EYES.nightVision);
			}
				
			if (ScenariosManager.INFINITE_ENCHANTER.isActivated()) {
				player.setLevel(1000);
			}
			
			if (ScenariosManager.SPEEDY_MINER.isActivated()) {
				ScenariosManager.SPEEDY_MINER.setEffects(p);
			}
			
			if (ScenariosManager.PROGRESSIVE_SPEED.isActivated()) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, p.getKills(), false, false));
			}
			
			if (game.isPvpTime() && timer%30 == 0) {
				
				if (ScenariosManager.NETHERIBUS.isActivated() && player.getWorld().getEnvironment() != Environment.NETHER) {
					player.damage(2);
					player.sendMessage(Lang.NETHERIBUS_DAMAGE.get());
				}
				
				if (ScenariosManager.SKYHIGH.isActivated() && player.getLocation().getBlockY() < 200) {
					player.damage(2);
					player.sendMessage(Lang.SKYHIGH_DAMAGE.get());
				}
			}
			
			if (timer < 60 && ScenariosManager.CHUNK_APOCALYPSE.isActivated() && player.getLocation().getY() < 0) {
				Location loc = Teleportation.getRandomLocation(player.getWorld(), (int) world.getWorldBorder().getSize()/2 - 20);
				player.teleport(loc);
			}
		}
	}
}

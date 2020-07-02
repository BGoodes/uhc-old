package fr.aiidor.uhc.task;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.game.Game;

public class TimeTask extends BukkitRunnable {
	
	private Game game;
	
	private Boolean buffer;
	
	public TimeTask(Game game) {
		this.game = game;
		buffer = true;
		
		game.getWorlds().forEach(w->w.setTime(23500));
	}
	
	@Override
	public void run() {
		
		for (World w : game.getWorlds()) {	
			w.setGameRuleValue("doDaylightCycle", "false");
			Long time = w.getTime();
			
			int i = 1;
			
			if (time >= 5100 && time <= 9000) { //8800
				i++;
			}
			
			if (time >= 16500 && time <= 18500) {
				if (!buffer) i--;
			}
			
			if (i != 0) {
				if (!game.getSettings().double_uhc_cycle) w.setTime(time + i);
				else w.setTime(time + i * 2);
			}
		}
		
		buffer = !buffer;
	}
	
	public static Boolean isDay(World w) {
		long time = w.getTime();
		return time >= 23500 || time < 13000;
	}
	
	public static Boolean isNight(World w) {
		long time = w.getTime();
		return time >= 13000 && time < 23500;
	}
}

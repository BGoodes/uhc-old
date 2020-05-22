package fr.aiidor.uhc.task;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class UHCTask extends BukkitRunnable {
	
	public abstract void launch();
	
	@Override
	public abstract void run();
	
	public abstract void stop();
	public abstract Integer getTime();
	
	
	public String getTimeString(String format) {
		
		int Timer = getTime();
		
		int sec = Timer%60;
		int min = (Timer/60)%60;
		int h = Timer/3600;
		
		if (!format.contains("%m")) {
			sec = Timer;
		}
		
		if (!format.contains("%h")) {
			min = Timer/60;
		}
		
		for (int i = 1; i != 4; i++) {
			String percent = "%";
			
			for (int a = i; a != 3; a++) {
				percent = percent + "%";
			}
			
			format = replace(format, sec, i, percent + "s");
			format = replace(format, min, i, percent + "m");
			format = replace(format, h, i, percent + "h");
		}
		
		return format;
	}
	
	private String replace(String format, Integer time, Integer index, String key) {
		
		if (format.contains(key)) {
			
			String string = time.toString();
			
			Integer unit = 1;
			for (int i = index; i < 3; i++) {
				if (time < Math.pow(10, unit)) {
					string = "0" + string;
				}
				unit++;
			}
			
			format = format.replace(key, string);
				
		}
		
		return format;
	}
}

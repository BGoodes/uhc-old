package fr.aiidor.uhc.gamemodes;

import org.bukkit.Sound;

import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.GameSettings;

public class Classic extends UHCMode {
	
	@Override
	public final UHCType getUHCType() {
		return UHCType.CLASSIC;
	}
	
	@Override
	public void checkConditions() {
		
	}
	
	@Override
	public void init() {
		GameSettings s = game.getSettings();
		
		s.uhc_cycle = false;
		s.double_uhc_cycle = false;
		
		s.setDisplayLife(true);
		s.death_lightning = true;
		s.death_sound = Sound.WITHER_SPAWN;
	}
	
	@Override
	public void loading() {
		
	}
	
	@Override
	public void begin() {
		
	}
	
	@Override
	public void firstEpisode() {
		
	}

	@Override
	public void episode(Integer ep) {
		
	}
	
	@Override
	public void run(Integer timer) {
		
	}

	@Override
	public void stop() {
		
	}
}

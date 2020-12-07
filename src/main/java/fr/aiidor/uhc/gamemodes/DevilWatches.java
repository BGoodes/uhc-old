package fr.aiidor.uhc.gamemodes;

import org.bukkit.Sound;

import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.GameSettings;

public class DevilWatches extends UHCMode {

	@Override
	public UHCType getUHCType() {
		return UHCType.DEVIL_WATCHES;
	}

	@Override
	public void checkConditions() {
		
	}

	@Override
	public void init() {
		GameSettings s = game.getSettings();
		
		s.setTeamSize(1);
		
		s.uhc_cycle = true;
		s.double_uhc_cycle = true;
		s.setDisplayLife(false);
		s.death_lightning = false;
		
		s.death_sound = Sound.AMBIENCE_THUNDER;
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

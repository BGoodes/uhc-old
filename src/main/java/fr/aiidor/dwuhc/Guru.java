package fr.aiidor.dwuhc;

import org.bukkit.entity.Player;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.gamemodes.DevilWatches;
import fr.aiidor.uhc.task.TimeTask;

public class Guru extends DWRoleEpisode {
	
	public Guru(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.GURU;
	}
	
	public void broadcast(String message) {
		
		DevilWatches dw = DevilWatches.getDevilWatches();
		
		Player player = p.getUHCPlayer().getPlayer();
		
		if (!hasPower()) {
			player.sendMessage(Lang.DW_ERROR_NO_POWER.get());
			return;
		}
		
		if (!canUsePower()) {
			player.sendMessage(Lang.DW_ERROR_NO_POWER_EPISODE.get());
			return;
		}
		
		if (!p.isAlive()) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_ALIVE.get());
			return;
		}
		
		if (!TimeTask.isNight(player.getWorld())) {
			player.sendMessage(Lang.DW_ANNOUNCE_NIGHT.get());
			return;
		}
		
		for (DWplayer p : dw.getAliveSectarians()) {
			if (p.getUHCPlayer().isConnected()) {
				
				Player s = p.getUHCPlayer().getPlayer();
				s.sendMessage(Lang.DW_GURU_BC.get().replace(LangTag.VALUE.toString(), message));
			}
		}
		
		setUse(false);
	}
	
	@Override
	public Boolean hasSectariansList() {
		return true;
	}

	@Override
	protected Integer getTimeUse() {
		return 0;
	}

	@Override
	protected String getUseMessage() {
		return Lang.DW_GURU_USE.get();
	}
}

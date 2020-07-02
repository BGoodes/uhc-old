package fr.aiidor.tg;

import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.team.UHCTeam;

public class TGMole {

	private UHCPlayer player;
	public UHCTeam baseTeam;
	
	public TGMole(UHCPlayer player) {
		this.player = player;
		this.baseTeam = player.getTeam();
	}
	
	public UHCPlayer getUHCPlayer() {
		return player;
	}
	
	public void giveKit() {
		
	}
}

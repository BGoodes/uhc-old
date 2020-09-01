package fr.aiidor.tg;

import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.team.UHCTeam;

public class TGMole {

	private UHCPlayer player;
	public UHCTeam mole_team;
	
	private Boolean super_mole;
	
	public TGMole(UHCPlayer player, UHCTeam mole_team) {
		this.player = player;
		this.mole_team = mole_team;
		
		super_mole = false;
	}
	
	public UHCPlayer getUHCPlayer() {
		return player;
	}
	
	public Boolean isSuperMole() {
		return super_mole;
	}
	
	public void setSuperMole(Boolean state) {
		this.super_mole = state;
	}
	
	public void giveKit() {
		
	}
}

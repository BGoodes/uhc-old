package fr.aiidor.uhc.enums;

import java.util.List;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;

public enum Rank {
	
	PLAYER, SPECTATOR, ORGA, HOST, STAFF;
	
	private String name;
	
	private Rank() {
		this.name = (String) UHCFile.LANG.getJSONObject("rank").get(name());
	}
	
	public String getRankName() {
		return name;
	}
	
	public Boolean hasPermission(Permission perm) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return false;		
		Game game = UHC.getInstance().getGameManager().getGame();
		
		List<Permission> perms = game.getPermissions().get(this);
		
		if (perms.contains(Permission.NONE)) return false;
		if (perms.contains(Permission.ALL)) return true;
		
		return perms.contains(perm);
	}
}

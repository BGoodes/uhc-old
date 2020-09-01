package fr.aiidor.uhc.enums;

import java.util.List;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;

public enum Rank {
	
	PLAYER, SPECTATOR, ORGA, HOST, STAFF;
	
	private final String name;
	private final String prefix;
	
	private Rank() {
		this.name = (String) UHCFile.LANG.getJSONObject("rank/" + name()).get("name");
		this.prefix = (String) UHCFile.LANG.getJSONObject("rank/" + name()).get("prefix");
	}
	
	public String getRankName() {
		return name;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public Boolean hasPermission(Permission perm) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return false;		
		Game game = UHC.getInstance().getGameManager().getGame();
		
		List<Permission> perms = game.getSettings().permissions.get(this);
		
		if (perms.contains(Permission.NONE)) return false;
		if (perms.contains(Permission.ALL)) return true;
		
		return perms.contains(perm);
	}
}

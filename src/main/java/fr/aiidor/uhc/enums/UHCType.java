package fr.aiidor.uhc.enums;

import fr.aiidor.uhc.gamemodes.Classic;
import fr.aiidor.uhc.gamemodes.DevilWatches;
import fr.aiidor.uhc.gamemodes.TaupeGun;
import fr.aiidor.uhc.gamemodes.UHCMode;

public enum UHCType {
	
	CLASSIC, DEVIL_WATCHES, TAUPE_GUN;
	
	private final String name;
	private final String tabName;
	
	private UHCType() {
		this.name = (String) UHCFile.LANG.getJSONObject("uhc-type/" + name()).get("name");
		this.tabName = (String) UHCFile.LANG.getJSONObject("uhc-type/" + name()).get("tab-name");
	}
	
	public String getName() {
		return name;
	}
	
	public String getTabName() {
		return tabName;
	}
	
	public UHCMode getNew() {
		if (this == CLASSIC) return new Classic();
		if (this == DEVIL_WATCHES) return new DevilWatches();
		if (this == TAUPE_GUN) return new TaupeGun();
		return null;
	}
}

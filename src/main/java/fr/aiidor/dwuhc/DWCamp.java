package fr.aiidor.dwuhc;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.UHCFile;

public enum DWCamp {
		
	VILLAGERS("§a"), SECTARIANS("§d"), DEMONS("§c"), SOLOS("§e"), NEUTRALS("§b");
	
	private final String name;
	private final String member;
	private final String goal;
	private final String victory_message;
	private final String prefix;
	
	private DWCamp(String prefix) {
		this.name = (String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/camps/" + name()).get("name");
		this.member = (String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/camps/" + name()).get("member");
		this.goal = (String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/camps/" + name()).get("goal");
		this.victory_message = Lang.format((String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/camps/" + name()).get("victory-message"));
		this.prefix = prefix;
	}
	
	public String getName() {
		return name;
	}
	
	public String getMemberName() {
		return member;
	}
	
	public String getGoal() {
		return goal;
	}
	
	public String getVictoryMessage() {
		return victory_message;
	}
	
	public String getPrefix() {
		return prefix;
	}
}

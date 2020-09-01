package fr.aiidor.lguhc;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.UHCFile;

public enum LGCamp {
		
	VILLAGERS("§a"), WEREWOLVES("§c"), SOLOS("§e"), NEUTRALS("§b");
	
	private final String name;
	private final String goal;
	private final String victory_message;
	private final String prefix;
	
	private LGCamp(String prefix) {
		this.name = (String) UHCFile.LANG.getJSONObject("gamemodes/werewolf/camps/" + name()).get("name");
		this.goal = (String) UHCFile.LANG.getJSONObject("gamemodes/werewolf/camps/" + name()).get("goal");
		this.victory_message = Lang.format((String) UHCFile.LANG.getJSONObject("gamemodes/werewolf/camps/" + name()).get("victory-message"));
		this.prefix = prefix;
	}
	
	public String getName() {
		return name;
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

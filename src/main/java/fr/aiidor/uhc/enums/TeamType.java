package fr.aiidor.uhc.enums;

public enum TeamType {
	
	CHOOSE, RANDOM, CREATE;
	
	private final String name;
	
	private TeamType() {
		this.name = (String) UHCFile.LANG.getJSONObject("team/team-type").get(name());
	}

	public String getName() {
		return name;
	}
}

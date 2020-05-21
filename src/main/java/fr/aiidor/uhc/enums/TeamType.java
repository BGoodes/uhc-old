package fr.aiidor.uhc.enums;

public enum TeamType {
	
	CHOOSE, RANDOM, CREATE;
	
	private String name;
	
	private TeamType() {
		this.name = (String) UHCFile.LANG.getJSONObject("team-type").get(name());
	}

	public String getName() {
		return name;
	}
}

package fr.aiidor.uhc.enums;

public enum UHCType {
	
	CLASSIC;
	
	private String name;
	
	private UHCType() {
		this.name = (String) UHCFile.LANG.getJSONObject("uhc-type").get(name());
	}
	
	public String getName() {
		return name;
	}
}

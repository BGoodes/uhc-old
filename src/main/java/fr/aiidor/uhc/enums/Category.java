package fr.aiidor.uhc.enums;

public enum Category {
	
	ALL, RUN, PVP, SURVIVAL, GENERATION, ENCHANTMENT, FUN, ANIME, OTHER;
	
	private final String name;
	
	private Category() {
		name = (String) UHCFile.LANG.getJSONObject("inventory/categories").get(name());
	}
	
	public String getDisplayName() {
		return name;
	}
	
	public static Category find(String displayName) {
		
		for (Category cat : Category.values()) {
			if (displayName.equals(cat.getDisplayName())) {
				return cat;
			}
		}
			
		return null;
	}
}

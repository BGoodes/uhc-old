package fr.aiidor.uhc.enums;

public enum TeamColor {
	
	RED("§c", 14, 1), 
	GOLD("§6", 1, 14), 
	YELLOW("§e", 4, 11), 
	GREEN("§a", 5, 10),
	AQUA("§b", 3, 12),
	LIGHT_PURPLE("§d", 9, 13), 
	BLUE("§9", 11, 4),
	DARK_PURPLE("§5", 10, 5), 
	DARK_AQUA("§3", 9, 6),
	DARK_GREEN("§2", 13, 2),
	GRAY("§7", 8, 7);
	//11
	
	private String name;
	private String chatcolor;

	
	private int woolcolor;
	private int bannercolor;
	
	private TeamColor(String chatcolor, Integer woolcolor, Integer bannercolor) {
		this.chatcolor = chatcolor;
		this.woolcolor = woolcolor;
		this.bannercolor = bannercolor;
		
		this.name = (String) UHCFile.LANG.getJSONObject("team-color").get(name());
	}

	public String getColorName() {
		return name;
	}
	
	public String getChatcolor() {
		return chatcolor;
	}

	public byte getWoolColor() {
		return (byte) woolcolor;
	}

	public byte getBannerColor() {
		return (byte) bannercolor;
	}
	
	public byte getGlassColor() {
		return (byte) bannercolor;
	}
	
	//21
	public static String[] prefix = {"❶", "①", "❷", "②", "❸", "③", "❹", "④", "❺", "⑤", "❻", "⑥", "❼", "⑦", "❽", "⑧", "❾", "⑨", "❤", "✪", "❂"};
}

package fr.aiidor.uhc.enums;

import org.bukkit.Color;

public enum TeamColor {
	
	RED("§c", 14, 1, Color.RED), 
	GOLD("§6", 1, 14, Color.ORANGE), 
	YELLOW("§e", 4, 11, Color.YELLOW), 
	GREEN("§a", 5, 10, Color.LIME),
	AQUA("§b", 3, 12,Color.AQUA),
	LIGHT_PURPLE("§d", 2, 13, Color.FUCHSIA), 
	BLUE("§9", 11, 4, Color.BLUE),
	DARK_PURPLE("§5", 10, 5, Color.PURPLE), 
	DARK_AQUA("§3", 9, 6, Color.NAVY),
	DARK_GREEN("§2", 13, 2, Color.GREEN),
	GRAY("§7", 8, 7, Color.GRAY);
	//11
	
	private final String name;
	private final String chatcolor;

	private final int woolcolor;
	private final int bannercolor;
	
	private final Color color;
	
	private TeamColor(String chatcolor, Integer woolcolor, Integer bannercolor, Color color) {
		this.chatcolor = chatcolor;
		this.woolcolor = woolcolor;
		this.bannercolor = bannercolor;
		this.color = color;
		
		this.name = (String) UHCFile.LANG.getJSONObject("team/team-color").get(name());
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
		return (byte) woolcolor;
	}
	
	public Color getColor() {
		return color;
	}
	
	//22
	public static String[] prefix = {"", "① ", "❶ ", "② ", "❷ ", "③ ", "❸ ", "④ ", "❹ ", "⑤ ", "❺ ", "⑥ ", "❻ ", "⑦ ", "❼ ", "⑧ ", "❽ ", "⑨ ", "❾ ", "❤ ", "✪ ", "❂ "};
	//"❶", "①", "❷", "②", "❸", "③", "❹", "④", "❺", "⑤", "❻", "⑥", "❼", "⑦", "❽", "⑧", "❾", "⑨", "❤", "✪", "❂"
}

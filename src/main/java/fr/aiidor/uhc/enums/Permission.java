package fr.aiidor.uhc.enums;

public enum Permission {
	
	ALL, NONE, CONFIG, RESTART, INVSEE, REVIVE, KICK,
	LOG, ALERT, CHAT, 
	
	CLEAR("bukkit.command.clear"), DIFFICULTY("bukkit.command.difficulty"), EFFECT("bukkit.command.effect "), ENCHANT("bukkit.command.enchant"), 
	GAMEMODE("bukkit.command.gamemode"), GAMERULE("bukkit.command.gamerule"), GIVE("bukkit.command.give"), KILL("bukkit.command.kill"), SEED("bukkit.command.seed"), 
	TIME_ADD("bukkit.command.time.add"), TIME_SET("bukkit.command.time.set"), TP("bukkit.command.teleport"), WEATHER("bukkit.command.weather"), XP("bukkit.command.xp");
	
	private final String spigot_perm;
	
	private Permission() {
		this.spigot_perm = null;
	}
	
	private Permission(String perm) {
		this.spigot_perm = perm;
	}
	
	public String getSpigotPerm() {
		return spigot_perm;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}

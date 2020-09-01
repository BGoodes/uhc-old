package fr.aiidor.uhc.enums;

public enum LangTag {
	
	PREFIX,
	ERROR,
	INFO_PREFIX,
	DW_PREFIX,
	TG_PREFIX,
	LG_PREFIX,
	
	AC_CANCEL,
	AC_VALIDATE,
	
	EPISODE,
	
	TIME,
	
	TEAM_NAME,
	TEAM_PLAYER_COUNT,
	TEAM_TYPE,
	TEAM_NUMBER,
	
	INVINCIBILITY_TIME,
	PVP_TIME,
	EPISODE1_TIME,
	EPISODE_TIME,
	WB_TIME,
	
	REASON,
	METHOD,
	
	VALUE,
	VALUE_1,
	VALUE_2,
	VALUE_3,
	VALUE_4,
	
	PLAYER_NAME,
	KILLER_NAME,
	
	WORLD_NAME,
	
	GAME_NAME,
	GAME_TYPE,
	JOIN_STATE,
	
	SERVER_SLOTS,
	SPECTATOR_SLOTS,
	PLAYER_SLOTS,
	PLAYER_COUNT,
	TEAM_COUNT,
	TEAM_SIZE,
	MAX_PLAYERS,
	
	WB_INITIAL_SIZE,
	WB_FINAL_SIZE,
	WB_SPEED,
	
	PERM,
	COMMAND,
	
	FILE_PATH,
	FILE_NAME,
	
	TIMER_S,
	TIMER_M,
	
	SCENARIO_NAME,
	
	ROLE_NAME,
	ROLE_CAMP_PREFIX,
	CAMP_NAME,
	GOAL,
	POWER,
	
	DW_SECTARIAN,
	
	CONSOLE_COLOR_RESET("cc.reset", "\u001B[0m"),
	CONSOLE_COLOR_BLACK("cc.black", "\u001B[30m"),
	CONSOLE_COLOR_RED("cc.red", "\u001B[31m"),
	CONSOLE_COLOR_GREEN("cc.green", "\u001B[32m"),
	CONSOLE_COLOR_YELLOW("cc.yellow", "\u001B[33m"),
	CONSOLE_COLOR_BLUE("cc.blue", "\u001B[34m"),
	CONSOLE_COLOR_PURPLE("cc.purple", "\u001B[35m"),
	CONSOLE_COLOR_CYAN("cc.cyan", "\u001B[36m"),
	CONSOLE_COLOR_WHITE("cc.white", "\u001B[37m"),;
	
	private String name;
	private String value;
	
	private LangTag() {
		this.value = null;
		this.name = name().toLowerCase().replace("_", "-");
	}
	

	private LangTag(String name) {
		this.value = null;
		this.name = name;
	}
	
	private LangTag(String name, String value) {
		this.value = value;
		this.name = name;
	}
	
	public Boolean hasValue() {
		return value != null;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "{" + name + "}";
	}
}

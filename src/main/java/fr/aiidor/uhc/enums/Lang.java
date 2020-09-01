package fr.aiidor.uhc.enums;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.task.GameTask;

public enum Lang {
	
	PREFIX,
	ERROR,
	INFO_PREFIX,
	DW_PREFIX,
	TG_PREFIX,
	LG_PREFIX,
	
	//ENCHANTS
	ENCHANTMENTS("enchants"),
	
	ENCH_ARROW_DAMAGE("ARROW_DAMAGE", "enchants"),
	ENCH_ARROW_FIRE("ARROW_FIRE", "enchants"),	
	ENCH_ARROW_INFINITE("ARROW_INFINITE", "enchants"),
	ENCH_ARROW_KNOCKBACK("ARROW_KNOCKBACK", "enchants"),	
	ENCH_DAMAGE_ALL("DAMAGE_ALL", "enchants"),
	ENCH_DAMAGE_ARTHROPODS("DAMAGE_ARTHROPODS", "enchants"),
	ENCH_DAMAGE_UNDEAD("DAMAGE_UNDEAD", "enchants"),
	ENCH_DEPTH_STRIDER("DEPTH_STRIDER", "enchants"),
	ENCH_DIG_SPEED("DIG_SPEED", "enchants"),
	ENCH_DURABILITY("DURABILITY", "enchants"),
	ENCH_FIRE_ASPECT("FIRE_ASPECT", "enchants"),
	ENCH_KNOCKBACK("KNOCKBACK", "enchants"),
	ENCH_LOOT_BONUS_BLOCKS("LOOT_BONUS_BLOCKS", "enchants"),
	ENCH_LOOT_BONUS_MOBS("LOOT_BONUS_MOBS", "enchants"),
	ENCH_LUCK("LUCK", "enchants"),
	ENCH_LURE("LURE", "enchants"),
	ENCH_OXYGEN("OXYGEN", "enchants"),
	ENCH_PROTECTION_ENVIRONMENTAL("PROTECTION_ENVIRONMENTAL", "enchants"),	
	ENCH_PROTECTION_EXPLOSIONS("PROTECTION_EXPLOSIONS", "enchants"),	
	ENCH_PROTECTION_FALL("PROTECTION_FALL", "enchants"),	
	ENCH_PROTECTION_FIRE("PROTECTION_FIRE", "enchants"),
	ENCH_PROTECTION_PROJECTILE("PROTECTION_PROJECTILE", "enchants"),
	ENCH_SILK_TOUCH("SILK_TOUCH", "enchants"),
	ENCH_THORNS("THORNS", "enchants"),
	ENCH_WATER_WORKER("WATER_WORKER", "enchants"),
	
	//POTIONS
	POTIONS("potions"),
	
	SPEED("SPEED", "potions"),
	FAST_DIGGING("FAST_DIGGING", "potions"),
	DAMAGE_RESISTANCE("DAMAGE_RESISTANCE", "potions"),
	
	//INVENTORY
	INV_CONFIGURATION("inventory"),
	INV_TEAM_CHOOSE("inventory"),
	INV_TEAM_RANDOM("inventory"),
	
	INV_EXIT("inventory"),
	INV_BACK("inventory"),
	INV_NEXT_PAGE("inventory"),
	INV_LAST_PAGE("inventory"),
	INV_ON("inventory"),
	INV_OFF("inventory"),
	INV_RANDOM("inventory"),
	INV_STATE("inventory"),
	
	INV_LEFT_CLICK("inventory"),
	INV_RIGHT_CLICK("inventory"),
	INV_LEFT_SHIFT_CLICK("inventory"),
	INV_RIGHT_SHIFT_CLICK("inventory"),
	INV_RIGHT_CLICK_OPTION("inventory"),
	
	INV_CATEGORIES("inventory"),
	INV_CATEGORY("inventory"),
	
	INV_TIME_OPTION("inventory"),
	MINUTE("inventory"),
	SECOND("inventory"),
	MINUTES("inventory"),
	SECONDS("inventory"),
	
	INV_PROBABILITY_OPTION("inventory"),
	LEVEL("inventory"),
	SEARCH("inventory"),
	SETTINGS("inventory"),
	
	INV_LOCKED("inventory"),
	
	INV_START_GAME("inventory/config-menu"),
	INV_CANCEL_GAME("inventory/config-menu"),
	INV_SCENARIOS("inventory/config-menu"),
	INV_TEAMS("inventory/config-menu"),
	INV_PLAYERS("inventory/config-menu"),
	INV_STUFF("inventory/config-menu"),
	INV_TIME("inventory/config-menu"),
	INV_WORLD_BORDER("inventory/config-menu"),
	INV_WORLDS("inventory/config-menu"),
	
	INV_LIFE("inventory/config-menu"),
	INV_LOOTS("inventory/config-menu"),
	INV_RULES("inventory/config-menu"),
	
	INV_CONFIG("inventory/config-menu"),
	
	INV_CONFIG_LOAD("inventory/config-configs"),
	INV_CONFIG_SAVE("inventory/config-configs"),
	
	INV_TEAM_SIZE("inventory/config-teams"),
	INV_TEAM_NUMBER("inventory/config-teams"),
	INV_TEAM_TYPE("inventory/config-teams"),
	INV_TEAM_NUMBER_CONFIG("inventory/config-teams"),
	INV_TEAM_SIZE_CONFIG("inventory/config-teams"),
	
	INV_JOIN_STATE("inventory/config-players"),
	
	INV_START_ITEMS("inventory/config-stuff"),
	INV_DEATH_ITEMS("inventory/config-stuff"),
	INV_STUFF_ENCHANTS("inventory/config-stuff"),
	INV_STUFF_POTIONS("inventory/config-stuff"),
	
	INV_INVINCIBILITY_TIME("inventory/config-time"),
	INV_INVINCIBILITY_TIME_CONFIG("inventory/config-time"),
	INV_PVP_TIME("inventory/config-time"),
	INV_PVP_TIME_CONFIG("inventory/config-time"),
	INV_EP1_TIME("inventory/config-time"),
	INV_EP1_TIME_CONFIG("inventory/config-time"),
	INV_EP_TIME("inventory/config-time"),
	INV_EP_TIME_CONFIG("inventory/config-time"),
	INV_WB_TIME("inventory/config-time"),
	INV_WB_TIME_CONFIG("inventory/config-time"),
	
	INV_WB_SIZE("inventory/config-wb"),
	INV_WB_SIZE_CONFIG("inventory/config-wb"),
	INV_WB_FINAL_SIZE("inventory/config-wb"),
	INV_WB_FINAL_SIZE_CONFIG("inventory/config-wb"),
	INV_WB_SPEED("inventory/config-wb"),
	INV_BLOCK_PER_SECOND("inventory/config-wb"),
	
	INV_W_MAIN_WORLD("inventory/config-worlds"),
	INV_W_LOBBY_WORLD("inventory/config-worlds"),
	INV_W_OVERWORLD("inventory/config-worlds"),
	INV_W_NETHER("inventory/config-worlds"),
	INV_W_END("inventory/config-worlds"),
	INV_W_CREATION("inventory/config-worlds"),
	INV_W_SETTINGS("inventory/config-worlds"),
	INV_W_TP("inventory/config-worlds"),
	INV_W_GENERATE_STRUCTURE("inventory/config-worlds"),
	INV_W_TYPE("inventory/config-worlds"),
	INV_W_SEED("inventory/config-worlds"),
	INV_W_DELETE("inventory/config-worlds"),
	
	INV_UHC_GAPPLES("inventory/config-life"),
	INV_GAPPLES_CONFIG("inventory/config-life"),
	INV_LIFE_DISPLAY("inventory/config-life"),
	INV_LIFE_DISPLAY_CONFIG("inventory/config-life"),
	INV_LIFE_DISPLAY_HEAD("inventory/config-life"),
	INV_LIFE_DISPLAY_BOW("inventory/config-life"),
		
	INV_LIFE_GAPPLE("inventory/config-life"),
	INV_LIFE_HAPPLE("inventory/config-life"),
	INV_LIFE_NAPPLE("inventory/config-life"),
	
	INV_LIFE_ABSO("inventory/config-life"),
	INV_LIFE_EFFECTS("inventory/config-life"),
	
	INV_UHC_DROP("inventory/config-loots"),
	INV_TREES("inventory/config-loots"),
	INV_TREES_DROP_APPLE("inventory/config-loots"),
	INV_TREES_RATE("inventory/config-loots"),
	INV_TREES_ALL_TREES("inventory/config-loots"),
	INV_TREES_SHEARS("inventory/config-loots"),
	INV_TREES_GOLDEN_APPLE("inventory/config-loots"),
	INV_TREES_DROP_SAPLING("inventory/config-loots"),
	
	INV_SCENARIO_LIST("inventory/config-scenarios"),
	INV_SCENARIO_HIDE("inventory/config-scenarios"),
	INV_SCENARIO_RANDOM("inventory/config-scenarios"),
	
	PLAYER_HEAD_NAME("items"),
	GOLDEN_HEAD_NAME("items"),
	
	TEAM_JOIN("team"),
	TEAM_LEAVE("team"),
	TEAM_FULL("team"),
	TEAM_PLAYERS("team"),
	TEAM_PLAYER_CANNOT_PLAY("team"),
	TEAM_SPEC_NAME("team"),
	
	SB_LINE("scoreboard"),
	SB_EPISODE("scoreboard"),
	SB_PLAYERS("scoreboard"),
	SB_TEAMS("scoreboard"),
	SB_KILLS("scoreboard"),
	SB_WAITING("scoreboard"),
	SB_STARTING("scoreboard"),
	SB_LOADING("scoreboard"),
	SB_TIMER("scoreboard"),
	SB_BORDER("scoreboard"),
	SB_HOST("scoreboard"),
	SB_KILL_SCORE("scoreboard"),

	TL_CONNEXION("tablist"),
	
	INVINCIBILITY_BAR("title"),
	BC_PLAYER_TP("title"),
	BC_WORLD_GENERATION("title"),
	
	PM_ERROR_CHAT("permission"),
	
	ST_BECOME_HOST("staff"),
	ST_ERROR_GAME_START("staff/error"),
	ST_ERROR_GAME_CANCEL("staff/error"),
	ST_ERROR_NO_GAME("staff/error"),
	ST_ERROR_PERM("staff/error"),
	ST_ERROR_SCENARIO_START("staff/error"),
	ST_ERROR_SCENARIO_PVP("staff/error"),
	ST_ERROR_SCENARIO_CONDITION("staff/error"),
	ST_ERROR_SCENARIO_COMPATIBILITY("staff/error"),
	ST_ERROR_OPTION_START("staff/error"),
	ST_ERROR_OPTION_PVP("staff/error"),
	ST_ERROR_WORLD_GENERATION("staff/error"),
	ST_ERROR_WORLD_CREATION_SIZE("staff/error"),
	ST_ERROR_UNLOAD_FAIL("staff/error"),
	ST_ERROR_MAIN_WORLD("staff/error"),
	ST_ERROR_LOBBY_WORLD("staff/error"),
	ST_ERROR_WORLD_EXIST("staff/error"),
	ST_ERROR_SERVER_RESTART("staff/error"),
	
	MSG_CHAT("message"),
	MSG_CHAT_TEAM("message"),
	MSG_BOW_DAMAGE("message"),
	
	MSG_ERROR_SCENARIO_LIST_OFF("message/error"),
	MSG_ERROR_PLACE_BLOCK("message/error"),
	MSG_ERROR_BREAK_BLOCK("message/error"),
	
	MSG_PRIVATE_SEND("message"),
	MSG_PRIVATE_RECEIVE("message"),
	
	AC_CANCEL("message/action-chat"),
	AC_VALIDATE("message/action-chat"),
	AC_CANCEL_MESSAGE("message/action-chat"),
	AC_CHAT("message/action-chat"),
	
	BC_PLAYER_JOIN("announce"),
	BC_SPECTATOR_JOIN("announce"),
	BC_PLAYER_LEAVE("announce"),
	BC_SPECTATOR_LEAVE("announce"),
	
	BC_INVINCIBILITY_END("announce"),
	
	BC_BROADCAST("announce"),
	BC_REVIVE("announce"),
	
	SCENARIO("scenarios"),
	CONDITION("scenarios"),
	
	BC_PVP_WAIT("announce"),
	BC_PVP_START("announce"),
	BC_EPISODE_END("announce"),
	
	BC_WB_WAIT("announce"),
	BC_WB_START("announce"),
	
	BC_DEATH("announce/death"),
	BC_DEATH_KILL("announce/death"),
	BC_DEATH_HIDE("announce/death"),
	
	//STARTING
	BC_GAME_STARTING("announce/starting"),
	BC_GAME_START_CANCEL("announce/starting"),
	BC_GAME_START("announce/starting"),
	
	//LOADING
	BC_RANDOM_TEAMS("announce/loading"),
	BC_TEAMS_LOADING("announce/loading"),
	BC_PLAYERS_LOADING("announce/loading"),
	BC_TELEPORTATION_LOADING("announce/loading"),
	BC_TELEPORTATION_FIND("announce/loading"),
	BC_CHUNK_LOADING("announce/loading"),
	BC_TELEPORTATION("announce/loading"),
	
	//END
	BC_VICTORY_TITLE_LINE_1("0", "announce/end/bc-victory-title"),
	BC_VICTORY_TITLE_LINE_2("1", "announce/end/bc-victory-title"),
	BC_LOSE_TITLE_LINE_1("0", "announce/end/bc-lose-title"),
	BC_LOSE_TITLE_LINE_2("1", "announce/end/bc-lose-title"),
	BC_DRAW_LINE_1("0", "announce/end/bc-draw-title"),
	BC_DRAW_LINE_2("1", "announce/end/bc-lose-title"),
	BC_DRAW("announce/end"),
	BC_SOLO_VICTORY("announce/end"),
	BC_TEAM_VICTORY("announce/end"),
	BC_SERVER_RESTART("announce/end"),
	
	//COMMANDS
	CMD_ERROR_PERM("commands"),
	CMD_HELP("commands"),
	CMD_ERROR_INVALID_ARGUMENT("commands"),
	CMD_ERROR_INVALID_COMMAND("commands"),
	CMD_ERROR_PLAYER_OFFLINE("commands"),
	CMD_ERROR_PLAYER_OFFGAME("commands"),
	CMD_ERROR_PLAYER_DEAD("commands"),
	CMD_ERROR_PLAYER_ALIVE("commands"),
	CMD_ERROR_PLAYER_NOT_DEAD("commands"),
	CMD_ERROR_GAMEMODE("commands"),
	CMD_ERROR_TEAM("commands"),
	CMD_ERROR_OFF("commands"),
	CMD_ERROR_SELECTOR("commands"),
	
	CMD_ERROR_NO_ROLE("commands"),
	CMD_ERROR_ROLE("commands"),
	
	CMD_ERROR_SELF_MSG("commands"),

	CMD_ERROR_NAME_SIZE("commands"),
	
	CMD_EXECUTE("commands"),
	
	//ENUMS
	OPEN("OPEN", "join-state"),
	CLOSE("CLOSE", "join-state"),
	WHITELIST("WHITELIST", "join-state"),
	
	//SCENARIOS
	PVP_ONLY("scenarios"),
	ERROR_CONDITION_TO2("scenarios"),
	
	FURNACE_SPEED("scenarios/fastsmelting"),
	
	FLOWER_DROP("scenarios/flower-power"),
	
	ROD("scenarios/rodless"),
	SNOWBALL("scenarios/rodless"),
	EGG("scenarios/rodless"),
	
	NO_FIRE_DAMAGE("scenarios/fireless"),
	NO_FIRE_TICK("scenarios/fireless"),
	LAVA_BUCKET("scenarios/fireless"),
	FLINT_AND_STEEL("scenarios/fireless"),
	PLAYER_ONLY("scenarios/fireless"),
	
	STINGY_MOBS("scenarios/stingy-world"),
	STINGY_BLOCKS("scenarios/stingy-world"),
	STINGY_TREES("scenarios/stingy-world"),
	STINGY_LAKES("scenarios/stingy-world"),
	STINGY_BOMBS("scenarios/stingy-world"),
	
	TARGET_MSG("scenarios/assassins"),
	NO_TARGET_MSG("scenarios/assassins"),
	TARGET_DEATH_MSG("scenarios/assassins"),
	NO_STUFF_DROP("scenarios/assassins"),
	SB_TARGET_NAME("scenarios/assassins"),
	SB_TARGET_DISTANCE("scenarios/assassins"),
	TRACKER("scenarios/assassins"),
	
	TNT_BOOST("scenarios/tnt-fly"),
	TNT_REDUCTION("scenarios/tnt-fly"),
	TNT_CHAIN("scenarios/tnt-fly"),
	
	BC_FINAL_HEAL("scenarios/final-heal"),
	
	BC_ANNOUNCE_SWORD("scenarios/assault-and-battery"),
	BC_ANNOUNCE_BOW("scenarios/assault-and-battery"),
	BC_ANNOUNCE_SOLO("scenarios/assault-and-battery"),
	BC_ANNOUNCE_USE_ALL("scenarios/assault-and-battery"),
	
	BC_ONE_WINNER("scenarios/only-one-winner"),
	
	POWERS("scenarios/superheroes"),
	POWER_ANNOUNCE("scenarios/superheroes"),
	
	CUPID_HEAL("scenarios/cupid"),
	INVENTION("scenarios/inventors"),
	
	TRACKER_NO_TARGET("scenarios/tracker"),
	TRACKER_TARGET("scenarios/tracker"),
	
	START_LEVEL("scenarios/master-level"),
	
	WATER_BUCKET("scenarios/no-bucket"),
	
	ENCHANT_TABLE_GENERATION_ERROR("scenarios/enchanting-center"),
	
	SKYHIGH_ANNOUNCE("scenarios/skyhigh"),
	SKYHIGH_DAMAGE("scenarios/skyhigh"),
	
	//MOTD
	MOTD_WAITING_LINE_1("1", "motd/WAITING"),
	MOTD_WAITING_LINE_2("2", "motd/WAITING"),
	
	MOTD_STARTING_LINE_1("1", "motd/STARTING"),
	MOTD_STARTING_LINE_2("2", "motd/STARTING"),
	
	MOTD_LOADING_LINE_1("1", "motd/LOADING"),
	MOTD_LOADING_LINE_2("2", "motd/LOADING"),
	
	MOTD_RUNNING_LINE_1("1", "motd/RUNNING"),
	MOTD_RUNNING_LINE_2("2", "motd/RUNNING"),
	
	MOTD_ENDING_LINE_1("1", "motd/ENDING"),
	MOTD_ENDING_LINE_2("2", "motd/ENDING"),
	
	//KICK AND BAN
	CAUSE_GAME_CLOSE("kick-reason"),
	CAUSE_GAME_WHITELIST("kick-reason"),
	CAUSE_GAME_SLOT("kick-reason"),
	CAUSE_WORLD_DELETING("kick-reason"),
	CAUSE_WORLD_GENERATING("kick-reason"),
	CAUSE_SERVER_CLOSE("kick-reason"),
	
	TG_MOLE("gamemodes/taupe-gun"),
	TG_MOLE_PREFIX("gamemodes/taupe-gun"),
	TG_MOLE_SELECTION("gamemodes/taupe-gun"),
	TG_MOLE_ANNOUNCE("gamemodes/taupe-gun"),
	TG_SUPER_MOLE_ANNOUNCE("gamemodes/taupe-gun"),
	TG_MOLE_REVEAL("gamemodes/taupe-gun"),
	TG_SUPER_MOLE_REVEAL("gamemodes/taupe-gun"),
	
	TG_CMD_ERROR_MOLE("gamemodes/taupe-gun"),
	TG_CMD_ERROR_REVEAL("gamemodes/taupe-gun"),
	TG_CMD_ERROR_KIT("gamemodes/taupe-gun"),
	
	DW_ANNOUNCE_ROLE("gamemodes/devil-watches/announce"),
	DW_SECTARIAN("gamemodes/devil-watches/announce"),
	DW_SEAL("gamemodes/devil-watches/announce"),
	DW_DEATH_REASON("gamemodes/devil-watches/announce"),
	
	DW_VICTORY_DRAW("gamemodes/devil-watches/announce"),
	DW_VICTORY_WIN("gamemodes/devil-watches/announce"),
	DW_VICTORY_SOLO("gamemodes/devil-watches/announce"),
	
	DW_ERROR_NO_POWER("gamemodes/devil-watches/message"),
	DW_ERROR_NO_POWER_EPISODE("gamemodes/devil-watches/message"),
	DW_ERROR_NO_POWER_EPISODE_TIME("gamemodes/devil-watches/message"),
	DW_ERROR_NO_POWER_USE("gamemodes/devil-watches/message"),
	DW_ERROR_POWER_TIME_LIMIT_EPISODE("gamemodes/devil-watches/message"),
	DW_SECTARIANS_LIST("gamemodes/devil-watches/message"),
	DW_DEMONS_LIST("gamemodes/devil-watches/message"),
	DW_NEW_SECTARIAN("gamemodes/devil-watches/message"),
	
	DW_HUNTER_INSPECT_NULL("gamemodes/devil-watches/roles/HUNTER"),
	DW_HUNTER_INSPECT_LOCATE("gamemodes/devil-watches/roles/HUNTER"),
	
	DW_GURU_USE("gamemodes/devil-watches/roles/GURU"),
	DW_GURU_BC("gamemodes/devil-watches/roles/GURU"),
	DW_ANNOUNCE_NIGHT("gamemodes/devil-watches/roles/GURU"),
	
	DW_GUARDIAN_CAN_USE("gamemodes/devil-watches/roles/GUARDIAN"),
	DW_GUARDIAN_ERROR_TARGET("gamemodes/devil-watches/roles/GUARDIAN"),
	DW_GUARDIAN_PROTECTION_MSG("gamemodes/devil-watches/roles/GUARDIAN"),
	DW_GUARDIAN_PROTECTED_MSG("gamemodes/devil-watches/roles/GUARDIAN"),
	DW_GUARDIAN_PROTECTED_DEATH_MSG("gamemodes/devil-watches/roles/GUARDIAN"),
	DW_GUARDIAN_PROTECTED_DEATH("gamemodes/devil-watches/roles/GUARDIAN"),
	
	DW_PROPHET_PROPHECY("gamemodes/devil-watches/roles/PROPHET"),
	
	DW_PROWLER_JOIN("gamemodes/devil-watches/roles/PROWLER"),
	
	DW_CAT_MEOW("gamemodes/devil-watches/roles/CAT_LADY"),
	DW_CAT_WHISPER("gamemodes/devil-watches/roles/CAT_LADY"),
	DW_CAT_WHISPER_DEATH("gamemodes/devil-watches/roles/CAT_LADY"),
	DW_CAT_WHISPER_DEATH_AROUND("gamemodes/devil-watches/roles/CAT_LADY"),
	DW_CAT_WHISPER_DEATH_KILLER("gamemodes/devil-watches/roles/CAT_LADY"),
	DW_CAT_WHISPER_KILL("gamemodes/devil-watches/roles/CAT_LADY"),
	DW_CAT_NUMBER("gamemodes/devil-watches/roles/CAT_LADY"),
	
	DW_GREEDY_PREVENT("gamemodes/devil-watches/roles/GREEDY_DEMON"),
	
	DW_ITEM_HOLY_WATER("gamemodes/devil-watches/inventory"),
	INV_DW_COMPO("gamemodes/devil-watches/inventory"),
	
	//CONSOLE
	ERROR_WORLD_NOT_FOUND("console"),
	ERROR_WORLD_EXIST("console"),
	WORLD_GENERATION("console"),
	WORLD_DELETING("console"),
	WORLD_DELETING_DONE("console"),
	ERROR_WORLD_DELETING_FAIL("console"),
	ERROR_COMMAND_PLAYER("console"),
	
	CAGE_GENERATE("console"),
	
	LANG_FILE_LOAD("console/files"),
	FILE_CREATED("console/files");
	
	private static final Map<Lang, String> VALUES = new HashMap<Lang, String>();
	
	private String[] path = null;
	private String key;
	
	private Lang() {
		path = null;
		this.key = name().toLowerCase().replace("_", "-");
	}
	
	private Lang(String path) {
		this.path = path.split("/");
		this.key = name().toLowerCase().replace("_", "-");
	}
	
	
	private Lang(String key, String path) {
		this.path = path.split("/");
		this.key = key;
	}
	
	static {
		for (Lang lang : Lang.values()) {
			VALUES.put(lang, lang.getFromFile());
		}
		
		UHC.getInstance().getLogger().info(LANG_FILE_LOAD.get());
	}
	
	public static String getPrefix() {
		return PREFIX.get() + ChatColor.RESET + " ";
	}
	
	private String getFromFile() {
		
		JSONObject config = UHCFile.LANG.getJSONConfig();
		
		if (path != null) {
			for (String object : path) {
				config = (JSONObject) config.get(object);
			}
		}
		
		String value = (String) config.get(key);
		
		if (value == null) {
			value = "";
		}
		
		return ChatColor.translateAlternateColorCodes('§', value);
	}
	
	public static String format(String value) {
		
		value = value.replace(LangTag.PREFIX.toString(), VALUES.get(PREFIX));
		value = value.replace(LangTag.ERROR.toString(), VALUES.get(ERROR));
		value = value.replace(LangTag.INFO_PREFIX.toString(), VALUES.get(INFO_PREFIX));
		
		value = value.replace(LangTag.DW_PREFIX.toString(), VALUES.get(DW_PREFIX));
		value = value.replace(LangTag.TG_PREFIX.toString(), VALUES.get(TG_PREFIX));
		value = value.replace(LangTag.LG_PREFIX.toString(), VALUES.get(LG_PREFIX));
		
		value = value.replace(LangTag.AC_CANCEL.toString(), VALUES.get(AC_CANCEL));
		value = value.replace(LangTag.AC_VALIDATE.toString(), VALUES.get(AC_VALIDATE));
		
		//LANG VALUES
		for (LangTag tag : LangTag.values()) {
			if (value.contains(tag.toString())) {
				if (tag.hasValue()) {
					value = value.replace(tag.toString(), tag.getValue());
				}
			}
		}
		
		//HASGAME
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			GameSettings settings = game.getSettings();
			
			value = value.replace(LangTag.PLAYER_SLOTS.toString(), settings.getSlots().toString());
			value = value.replace(LangTag.SPECTATOR_SLOTS.toString(), settings.getSpectatorSlots().toString());
			value = value.replace(LangTag.SERVER_SLOTS.toString(), Bukkit.getMaxPlayers() + "");
			value = value.replace(LangTag.PLAYER_COUNT.toString(), game.getPlayerCount().toString());
			value = value.replace(LangTag.GAME_NAME.toString(), game.getName());
			value = value.replace(LangTag.GAME_TYPE.toString(), game.getType().getName());
			
			value = value.replace(LangTag.JOIN_STATE.toString(), VALUES.get(Lang.valueOf(settings.getJoinState().name())));
			
			value = value.replace(LangTag.TEAM_NUMBER.toString(), settings.getTeamNumber().toString());
			value = value.replace(LangTag.TEAM_SIZE.toString(), settings.getTeamSize().toString());
			value = value.replace(LangTag.TEAM_TYPE.toString(), settings.team_type.getName());
			
			value = value.replace(LangTag.WB_INITIAL_SIZE.toString(), settings.wb_size_max.toString());
			value = value.replace(LangTag.WB_FINAL_SIZE.toString(), settings.wb_size_min.toString());
			value = value.replace(LangTag.WB_SPEED.toString(), settings.wb_speed.toString());
			
			value = value.replace(LangTag.INVINCIBILITY_TIME.toString(), settings.invincibility_time.toString());
			value = value.replace(LangTag.PVP_TIME.toString(), settings.pvp_time.toString());
			value = value.replace(LangTag.EPISODE1_TIME.toString(), settings.ep1_time.toString());
			value = value.replace(LangTag.EPISODE_TIME.toString(), settings.ep_time.toString());
			value = value.replace(LangTag.WB_TIME.toString(), settings.wb_time.toString());
			
			if (game.hasTeam()) {
				//TEAM COUNT A CHANGER
				value = value.replace(LangTag.TEAM_COUNT.toString(), game.getAliveTeams().size() + "");
			}
			
			if (game.getState() == GameState.RUNNING) value = value.replace(LangTag.MAX_PLAYERS.toString(), game.getInGamePlayerCount().toString());
			else value = value.replace(LangTag.MAX_PLAYERS.toString(), settings.getSlots().toString());
			
			if (game.isRunning()) {
				value = value.replace(LangTag.TIMER_S.toString(), game.getTask().getTimeString("%s"));
				value = value.replace(LangTag.TIMER_M.toString(), game.getTask().getTimeString("%m : %%s"));
				
				if (game.getRunner() instanceof GameTask) {
					
					GameTask task = (GameTask) game.getRunner();
					value = value.replace(LangTag.EPISODE.toString(), task.getEpisode().toString());
				}
			}
		}
		
		return value;
	}
	
	public String get() {
		return format(VALUES.get(this));
	}
	
	public static String removeColor(String string) {
		
		StringBuilder b = new StringBuilder();
		
		Boolean color = false;
		for (Character c : string.toCharArray()) {
			
			if (color) color = false;
			else 
				if (c == '§') color = true;
				else b.append(c);
		}
		
		return b.toString();
	}
	
	public static String getFromFile(String path, String key) {
		
		JSONObject config = UHCFile.LANG.getJSONConfig();
		
		for (String object : path.split("/")) {
			config = (JSONObject) config.get(object);
		}
		
		String value = (String) config.get(key);
		
		if (value == null) {
			value = "";
		}
		
		return ChatColor.translateAlternateColorCodes('§', value);
	}
}

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
	
	//ENCHANTS
	LEVEL("enchants"),
	ENCHANTMENTS("enchants"),
	
	DURABILITY("DURABILITY", "enchants"),
	DIG_SPEED("DIG-SPEED" , "enchants"),
	LOOT_BONUS_BLOCKS("LOOT-BONUS-BLOCKS", "enchants"),
	SILK_TOUCH("SILK-TOUCH", "enchants"),

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
	INV_STATE("inventory"),
	
	INV_LEFT_CLICK("inventory"),
	INV_RIGHT_CLICK("inventory"),
	INV_LEFT_SHIFT_CLICK("inventory"),
	INV_RIGHT_SHIFT_CLICK("inventory"),
	INV_RIGHT_CLICK_OPTION("inventory"),
	
	INV_CATEGORIES,
	INV_CATEGORY,
	
	MINUTE("inventory"),
	SECOND("inventory"),
	
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
	INV_CRAFTS("inventory/config-menu"),
	INV_CHAT("inventory/config-menu"),
	INV_RULES("inventory/config-menu"),
	
	INV_TEAM_SIZE("inventory/config-teams"),
	INV_TEAM_NUMBER("inventory/config-teams"),
	INV_TEAM_TYPE("inventory/config-teams"),
	INV_TEAM_NUMBER_CONFIG("inventory/config-teams"),
	INV_TEAM_SIZE_CONFIG("inventory/config-teams"),
	
	INV_JOIN_STATE("inventory/config-players"),
	
	INV_START_ITEMS("inventory/config-stuff"),
	INV_DEATH_ITEMS("inventory/config-stuff"),
	INV_STUFF_LIMITS("inventory/config-stuff"),
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
	
	INV_UHC_GAPPLES("inventory/config-life"),
	INV_GAPPLES_CONFIG("inventory/config-life"),
	
	INV_UHC_DROP("inventory/config-loots"),
	INV_TREES("inventory/config-loots"),
	INV_TREES_DROP_APPLE("inventory/config-loots"),
	INV_TREES_RATE("inventory/config-loots"),
	INV_TREES_ALL_TREES("inventory/config-loots"),
	INV_TREES_SHEARS("inventory/config-loots"),
	INV_TREES_GOLDEN_APPLE("inventory/config-loots"),
	INV_TREES_DROP_SAPLING("inventory/config-loots"),
	
	INV_SCENARIO_LIST("inventory"),
	
	TEAM_JOIN("team"),
	TEAM_LEAVE("team"),
	TEAM_FULL("team"),
	TEAM_PLAYERS("team"),
	TEAM_PLAYER_CANNOT_PLAY("team"),
	
	SB_EPISODE("scoreboard"),
	SB_PLAYERS("scoreboard"),
	SB_TEAMS("scoreboard"),
	SB_WAITING("scoreboard"),
	SB_STARTING("scoreboard"),
	SB_LOADING("scoreboard"),
	SB_TIMER("scoreboard"),
	SB_BORDER("scoreboard"),
	SB_HOST("scoreboard"),

	INVINCIBILITY_BAR("title"),
	BC_PLAYER_TP("title"),
	
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
	
	MSG_BOW_DAMAGE("message"),
	
	BC_PLAYER_JOIN("announce"),
	BC_SPECTATOR_JOIN("announce"),
	BC_PLAYER_LEAVE("announce"),
	BC_SPECTATOR_LEAVE("announce"),
	
	BC_INVINCIBILITY_END("announce"),
	
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
	
	
	//COMMANDS
	CMD_ERROR_PERM("commands"),
	CMD_HELP("commands"),
	CMD_INVALID_ARGUMENT("commands"),
	CMD_INVALID_COMMAND("commands"),
	CMD_PLAYER_OFFLINE("commands"),

	//ENUMS
	OPEN("OPEN", "join-state"),
	CLOSE("CLOSE", "join-state"),
	WHITELIST("WHITELIST", "join-state"),
	
	//SCENARIOS
	PVP_ONLY("scenarios"),
	ERROR_CONDITION_TO2("scenarios"),
	
	FURNACE_SPEED("scenarios/fastsmelting"),
	
	FLOWER_DROP("scenarios/flower-power"),
	
	ENCHANT_LEVEL("scenarios/hastey-boys"),
	
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
	
	CUPID_HEAL("scenarios/cupid"),
	
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
	
	//CONSOLE
	ERROR_WORLD_NOT_FOUND("console"),
	ERROR_WORLD_EXIST("console"),
	WORLD_GENERATION("console"),
	WORLD_DELETING("console"),
	WORLD_DELETING_DONE("console"),
	ERROR_WORLD_DELETING_FAIL("console"),
	ERROR_COMMAND_PLAYER("console"),
	
	CAGE_GENERATE("console"),
	
	LANG_FILE_LOAD("console/files");
	
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
	
	public String get() {
		
		String value = VALUES.get(this);
		value = value.replace(LangTag.PREFIX.toString(), VALUES.get(PREFIX));
		value = value.replace(LangTag.ERROR.toString(), VALUES.get(ERROR));
		
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

package fr.aiidor.uhc.enums;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.task.Game_Task;

public enum Lang {

	PREFIX,

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

	INV_LEFT_CLICK("inventory"),
	INV_RIGHT_CLICK("inventory"),

	INV_CATEGORIES,
	INV_CATEGORY,

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

	ST_BECOME_HOST("staff"),
	ST_ERROR_GAME_START("staff/error"),
	ST_ERROR_GAME_CANCEL("staff/error"),
	ST_ERROR_NO_GAME("staff/error"),
	ST_ERROR_PERM("staff/error"),
	ST_ERROR_SCENARIO_START("staff/error"),

	BC_PLAYER_JOIN("announce"),
	BC_SPECTATOR_JOIN("announce"),
	BC_PLAYER_LEAVE("announce"),
	BC_SPECTATOR_LEAVE("announce"),

	BC_INVINCIBILITY_END("announce"),

	BC_PVP_WAIT("announce"),
	BC_PVP_START("announce"),
	BC_EPISODE_END("announce"),

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
	FURNACE_SPEED("scenarios/fastsmelting"),

	ENCHANT_LEVEL("scenarios/hastey-boys"),

	ROD("scenarios/rodless"),
	SNOWBALL("scenarios/rodless"),
	EGG("scenarios/rodless"),
	PVP_ONLY("scenarios/rodless"),

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
	SB_TARGET_NAME("scenarios/assassins"),
	SB_TARGET_DISTANCE("scenarios/assassins"),

	TNT_BOOST("scenarios/tnt-fly"),
	TNT_REDUCTION("scenarios/tnt-fly"),
	TNT_CHAIN("scenarios/tnt-fly"),

	//MOTD
	MOTD_LOBBY_LINE_1("1", "motd/LOBBY"),
	MOTD_LOBBY_LINE_2("2", "motd/LOBBY"),

	MOTD_STARTING_LINE_1("1", "motd/STARTING"),
	MOTD_STARTING_LINE_2("2", "motd/STARTING"),

	MOTD_LOADING_LINE_1("1", "motd/LOADING"),
	MOTD_LOADING_LINE_2("2", "motd/LOADING"),

	MOTD_GAME_LINE_1("1", "motd/GAME"),
	MOTD_GAME_LINE_2("2", "motd/GAME"),

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

			if (game.hasTeam()) {
				//TEAM COUNT A CHANGER
				value = value.replace(LangTag.TEAM_COUNT.toString(), game.getAliveTeams().size() + "");
			}

			if (game.getState() == GameState.GAME) value = value.replace(LangTag.MAX_PLAYERS.toString(), game.getInGamePlayerCount().toString());
			else value = value.replace(LangTag.MAX_PLAYERS.toString(), settings.getSlots().toString());

			if (game.isRunning()) {
				value = value.replace(LangTag.TIMER_S.toString(), game.getTask().getTimeString("%s"));
				value = value.replace(LangTag.TIMER_M.toString(), game.getTask().getTimeString("%m : %%s"));

				if (game.getRunner() instanceof Game_Task) {

					Game_Task task = (Game_Task) game.getRunner();
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

package fr.aiidor.uhc.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.comparators.KillComparator;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.JoinState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.enums.TeamColor;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.gamemodes.UHCMode;
import fr.aiidor.uhc.task.StartingTask;
import fr.aiidor.uhc.task.UHCTask;
import fr.aiidor.uhc.task.WaitingTask;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.utils.Titles;
import fr.aiidor.uhc.utils.UHCItem;
import fr.aiidor.uhc.world.UHCWorld;
import fr.aiidor.uhc.world.WorldManager;
import fr.aiidor.uhc.world.WorldPanel;

public class Game {
	
	private String name;
	private UHCMode uhc_mode;
	
	private Scoreboard sb;
	
	private Set<UHCPlayer> players;
	private List<UHCTeam> teams;
	
	private List<UHCWorld> worlds;
	
	private UHCWorld main_world = null;
	private WorldPanel world_panel = null;
	
	private GameState state;
	private GameSettings settings;
	
	private UHCTask task;
	
	public Game(String name, UHCMode uhc_mode, GameSettings settings, Player host, Set<UHCPlayer> players, UHCWorld world, List<UHCWorld> worlds) {
		
		this.setName(name);
		this.setUHCMode(uhc_mode);
		this.setState(GameState.WAITING);
		
		uhc_mode.setGame(this);
		this.uhc_mode = uhc_mode;
		
		if (worlds != null) this.worlds = worlds;
		else this.worlds = new ArrayList<UHCWorld>();
		
		this.main_world = world;
		if (!this.worlds.contains(world)) this.worlds.add(main_world);
		
		//WORLD
		for (World w : getWorlds()) {
			new WorldManager(w.getName()).load();
		}
		
		//PLAYER
		if (players != null) this.players = players;
		else this.players = new HashSet<>();
		
		if (host != null) players.add(new UHCPlayer(host, PlayerState.ALIVE, Rank.HOST, this));
		
		if (settings == null) this.settings = new GameSettings(this);
		else {
			this.settings = settings;
			this.settings.setGame(this);
		}
		
		this.sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Objective name_h = sb.registerNewObjective("name_h", "dummy");
		name_h.setDisplayName("§c❤");
		
		Objective tab_h = sb.registerNewObjective("tab_h", "health");
		tab_h.setDisplayName("§c❤");
		
		if (this.settings.getDisplayHeadLife()) name_h.setDisplaySlot(DisplaySlot.BELOW_NAME);
		if (this.settings.getDisplayTabLife()) tab_h.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		
		if (sb.getTeam(Lang.TEAM_SPEC_NAME.get()) == null) sb.registerNewTeam(Lang.TEAM_SPEC_NAME.get());
		
		Team t = sb.getTeam(Lang.TEAM_SPEC_NAME.get());
		t.setPrefix(Rank.SPECTATOR.getPrefix());
		t.setCanSeeFriendlyInvisibles(true);
		
		//TASK
		new WaitingTask(this).launch();
		
		this.settings.setTeamSize(this.settings.getTeamSize());
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public UHCType getType() {
		return uhc_mode.getUHCType();
	}
	
	public UHCMode getUHCMode() {
		return uhc_mode;
	}
	
	public void setUHCMode(UHCMode uhc_mode) {
		this.uhc_mode = uhc_mode;
		uhc_mode.loading();
	}
	
	public Scoreboard getScoreboard() {
		return sb;
	}
	
	public GameState getState() {
		return state;
	}
	
	public Boolean isStart() {
		return state == GameState.RUNNING || state == GameState.ENDING ;
	}

	public Boolean isPvpTime() {
		return getState() == GameState.RUNNING && isRunning() && getRunner().getTime() > settings.pvp_time * 60;
	}
	
	public Boolean canPvp() {
		return isPvpTime();
	}
	
	public Boolean isWaiting() {
		return state == GameState.WAITING;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
	public GameSettings getSettings() {
		return settings;
	}
	
	public UHCTask getTask() {
		return task;
	}
	
	public Boolean isRunning() {
		return task != null;
	}
	
	public void setRunner(UHCTask task) {
		this.task = task;
	}
	
	public UHCTask getRunner() {
		return task;
	}
	
	public Boolean start() {
		
		if (getState() == GameState.WAITING) {
			new StartingTask(this).launch();
			return true;
		}
		
		return false;
		
	}
	
	public List<World> getWorlds() {
		List<World> wl = new ArrayList<World>();
		for (UHCWorld w : worlds) {
			wl.addAll(w.getAll());
		}
		return wl;
	}
	
	public List<UHCWorld> getUHCWorlds() {
		return worlds;
	}
	
	public Boolean canRegenWorlds() {
		for (UHCWorld w : worlds) {
			if (w.canRegen()) return true;
		}
		return false;
	}
	
	public Boolean hasWorldPanel() {
		return world_panel != null;
	}
	
	public WorldPanel getWorldPanel() {
		return world_panel;
	}
	
	public void setWorldPanel(WorldPanel panel) {
		this.world_panel = panel;
	}
	
	public void addUHCWorld(UHCWorld w) {
		worlds.add(w);
	}
	
	public void removeUHCWorld(UHCWorld w) {
		worlds.remove(w);
	}
	
	public UHCWorld getUHCWorld(String worldname) {
		for (UHCWorld w : worlds) {
			if (w.getOverWorldName().equals(worldname)) return w;
		}
		
		return null;
	}
	
	public UHCWorld getUHCWorld(World world) {
		for (UHCWorld w : worlds) {
			if (w.getAll().contains(world)) return w;
		}
		
		return null;
	}
	
	public Boolean isUHCWorld(World world) {
		return getUHCWorld(world) != null;
	}
	
	public Boolean isUHCWorld(String worldname) {
		return getUHCWorld(worldname) != null;
	}
	
	public void setMainWorld(UHCWorld w) {
		this.main_world = w;
	}
	
	public UHCWorld getMainWorld() {
		return main_world;
	}

	//PLAYERS
	public Integer getPlayerCount() {
		return getPlayingPlayers().size();
	}
	
	public Integer getInGamePlayerCount() {
		return getIngamePlayers().size();
	}
	
	public Set<Player> getInWorldsPlayers() {
		Set<Player> players = new HashSet<Player>();
		
		for (World world : getWorlds()) {
			players.addAll(world.getPlayers());
		}
		
		if (!getWorlds().contains(UHC.getInstance().getSettings().getLobbyWorld())) {
			players.addAll(UHC.getInstance().getSettings().getLobbyWorld().getPlayers());
		}
		
		return players;
	}
	
	public UHCPlayer getRandomPlayer() {
		
		if (getAlivePlayers().isEmpty()) return null;
		return (UHCPlayer) getAlivePlayers().toArray()[new Random().nextInt(getAlivePlayers().size())];
	}
	
	public UHCPlayer getRandomPlayingPlayer() {
		
		if (getPlayingPlayers().isEmpty()) return null;
		return (UHCPlayer) getPlayingPlayers().toArray()[new Random().nextInt(getAlivePlayers().size())];
	}
	
	public Set<UHCPlayer> getAllPlayers() {
		
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		players.addAll(this.players);
		return players;
	}
	
	public Set<UHCPlayer> getAllConnectedPlayers() {
		
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.isConnected()) players.add(p);
		}
		
		return players;
	}
	
	public Set<UHCPlayer> getAlivePlayers() {
		
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.isAlive()) players.add(p);
		}
		
		return players;
	}
	
	public Set<UHCPlayer> getConnectedPlayers() {
		
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.isConnected()) players.add(p);
		}
		
		return players;
	}
	
	public Set<UHCPlayer> getPlayingPlayers() {
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.isPlaying()) players.add(p);
		}
		
		return players;
	}
	
	public Set<UHCPlayer> getIngamePlayers() {
		
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.getState() != PlayerState.SPECTATOR) players.add(p);
		}
		
		return players;
	}
	
	public List<UHCPlayer> getPlayersSort() {
		List<UHCPlayer> list = new ArrayList<UHCPlayer>();
		
		for (UHCPlayer p : getIngamePlayers()) {
			list.add(p);
		}
		
		list.sort(new KillComparator());
		
		return list;
	}
	
	public Boolean isHere(UUID uuid) {
		return getUHCPlayer(uuid) != null;
	}
	
	public void addUHCPlayer(UHCPlayer player) {
		players.add(player);
	}
	
	public void removeUHCPlayer(UHCPlayer player) {
		
		
		if (player.hasTeam()) player.leaveTeam(true);
		
		if (isStart()) {
			if (player.getRank() == Rank.HOST || player.getRank() == Rank.ORGA) {
				player.setState(PlayerState.DEAD);
				return;
			}
		}
		
		players.remove(player);
	}
	
	public UHCPlayer getUHCPlayer(UUID uuid) {
		
		for (UHCPlayer p : players) {
			if (p.getUUID().equals(uuid)) {
				return p;
			}
		}
		
		return null;
	}
	
	public Boolean isWhitelisted(Player player) {
		return getSettings().getWhitelist().contains(player.getName().toLowerCase());
	}
	
	public Boolean join(Player player) {
		
		if (isHere(player.getUniqueId())) return true;
		if (getSettings().getJoinState().equals(JoinState.OPEN)) return true;
		
		if (getSettings().getJoinState().equals(JoinState.CLOSE)) {
			if (player.hasPermission("uhc.host") && !hasHost()) return true;
		}
		
		if (getSettings().getJoinState().equals(JoinState.WHITELIST)) {
			
			if (player.hasPermission("uhc.bypass.whitelist")) return true;
			if (isWhitelisted(player))	return true;
		}
		
		player.kickPlayer(Lang.valueOf("CAUSE_GAME_" + getSettings().getJoinState().toString()).get());
		return false;
	}
	
	//TEAMS
	public Boolean hasTeam() {
		return teams != null;
	}
	
	public List<UHCTeam> getTeams() {
		List<UHCTeam> teams = new ArrayList<UHCTeam>();
		teams.addAll(this.teams);
		return teams;
	}
	
	public UHCTeam getPlayerTeam(UHCPlayer player) {
		
		if (hasTeam()) {
			for (UHCTeam team : getTeams()) {
				if (team.isInTeam(player)) {
					return team;
				}
			}
		}
		
		return null;
	}
	
	public Boolean playerHasTeam(UHCPlayer player) {
		return getPlayerTeam(player) != null;
	}
	
	public Boolean teamExist(String name) {
		return getTeam(name) != null;
	}
	
	public void removeTeam(UHCTeam team) {
		teams.remove(team);
	}
	
	public UHCTeam getTeam(String name) {
		
		if (hasTeam()) {
			for (UHCTeam t : getTeams()) {
				if (t.getName().equals(name)) {
					return t;
				}
			}
		}

		return null;
	}
	
	public void addTeam(UHCTeam team) {
		teams.add(team);
	}
	
	public void createTeam() {
		
		if (!hasTeam()) {
			teams = new ArrayList<UHCTeam>();
			
			if (getSettings().team_type == TeamType.CHOOSE && !isStart()) {
				for (UHCPlayer p : getPlayingPlayers()) {
					p.getPlayer().getInventory().setItem(8, UHCItem.getTeamSelecter());
				}
			}
		}
		
		for (String s : TeamColor.prefix) {
			for (TeamColor c : TeamColor.values()) {

				String prefix = c.getChatcolor() + s;
					
				if (!teamExist(prefix + c.getColorName())) {
					teams.add(new UHCTeam(prefix, c, getSettings().getTeamSize(), this));
					return;
				}
			}
		}
	}
	
	public void destroyTeams() {
		
		if (!isStart()) {
			for (UHCPlayer p : getPlayingPlayers()) {
				p.getPlayer().getInventory().removeItem(UHCItem.getTeamSelecter());
			}
		}
		
		if (hasTeam()) {
			for (UHCTeam team : teams) {
				team.destroy();
			}
		}
		
		this.teams = null;
	}

	public List<UHCTeam> getNotFullTeams() {
		List<UHCTeam> teams = new ArrayList<UHCTeam>();
		
		if (hasTeam()) {
			for (UHCTeam t : getTeams()) {
				if (!t.isFull()) teams.add(t);
			}
		}
		
		return teams;
	}
	
	public List<UHCTeam> getPlayingTeams() {
		List<UHCTeam> teams = new ArrayList<UHCTeam>();
		
		if (hasTeam()) {
			for (UHCTeam t : getTeams()) {
				for (UHCPlayer p : t.getPlayers()) {
					if (p.isConnected() && p.isAlive()) {
						teams.add(t);
						break;
					}
				}
			}
		}
		
		return teams;
	}
	
	public List<UHCTeam> getAliveTeams() {
		List<UHCTeam> teams = new ArrayList<UHCTeam>();
		
		if (hasTeam()) {
			for (UHCTeam t : getTeams()) {
				for (UHCPlayer p : t.getPlayers()) {
					if (p.isAlive()) {
						teams.add(t);
						break;
					}
				}
			}
		}
		
		return teams;
	}
	
	public UHCPlayer getHost() {
		
		for (UHCPlayer p : players) {
			if (p.getRank() == Rank.HOST)
				return p;
		}
		
		return null;
	}
	
	public Boolean hasHost() {
		return getHost() != null;
	}
	
	public Boolean isHost(UHCPlayer player) {
		return player.getRank() == Rank.HOST;
	}
	
	public Boolean isOrga(UHCPlayer player) {
		return player.getRank() == Rank.ORGA;
	}
	
	//EFFECTS ----------------------------------------------
	public void broadcast(String message) {
		broadcast(message, true);
	}
	
	public void broadcast(String message, Boolean log) {
		
		for (Player p : getInWorldsPlayers()) {
			p.sendMessage(message);
		}
		
		if (UHC.getInstance().getSettings().log_game_bc && log) {
			Bukkit.getConsoleSender().sendMessage(message);
		}
	}
	
	public void log(String message) {
		
		for (UHCPlayer p : getConnectedPlayers()) {
			if (p.hasPermission(Permission.LOG)) {
				p.getPlayer().sendMessage(message);
			}
		}
		
		if (UHC.getInstance().getSettings().log_game_bc) {
			Bukkit.getConsoleSender().sendMessage(message);
		}
	}
	
	//TITLE
	public void title(String title) {
		title(title, "", 60);
	}
	
	public void title(String title, Integer tick) {
		title(title, "", tick);
	}
	
	
	public void title(String title, String subtitle) {
		title(title, subtitle, 60);
	}
	
	public void title(String title, String subtitle, Integer tick) {
		title(title, subtitle, 20, tick, 20);
	}
	
	public void title(String title, Integer appear, Integer tick, Integer disappear) {
		title(title, "", appear, tick, disappear);
	}
	
	public void title(String title, String subtitle, Integer appear, Integer tick, Integer disappear) {
		
		for (Player p : getInWorldsPlayers()) {
			new Titles(p).sendTitle(title, subtitle, appear, tick, disappear);
		}
	}
	
	public void actionText(String message) {
		
		for (Player p : getInWorldsPlayers()) {
			new Titles(p).sendActionText(message);
		}
	}

	
	//SOUND
	public void playSound(Sound sound) {
		playSound(sound, 1, 1);
	}
	
	public void playSound(Sound sound, float volume) {
		playSound(sound, volume, 1);
	}
	
	public void playSound(Sound sound, float volume, float pitch) {
		
		for (Player p : getInWorldsPlayers()) {
			p.playSound(p.getLocation(), sound, volume, pitch);
		}
	}
}

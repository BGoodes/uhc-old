package fr.aiidor.uhc.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.JoinState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.enums.TeamColor;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.task.Starting_Task;
import fr.aiidor.uhc.task.UHC_Task;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.tools.Titles;
import fr.aiidor.uhc.tools.UHCItem;
import fr.aiidor.uhc.world.WorldManager;

public class Game {
	
	private String name;
	private Scoreboard sb;
	
	private Set<UHCPlayer> players;
	private List<UHCTeam> teams;
	
	private List<World> worlds = new ArrayList<World>();
	
	private World overworld = null;
	private World nether = null;
	private World end = null;
	
	private GameState state;
	private HashMap<Rank, List<Permission>> permissions;
	
	private UHC_Task task;
	
	public Game(String name, Player host, World world) {
		
		this.setName(name);
		this.setState(GameState.LOBBY);
		
		this.overworld = world;
		this.worlds.add(overworld);
		
		Logger logger = UHC.getInstance().getLogger();
		
		//WORLD
		String nethername = world.getName() + "_nether";
		String endname = world.getName() + "_the_end";
		
		if (Bukkit.getWorld(nethername) != null) {
			this.nether = Bukkit.getWorld(nethername);
			this.worlds.add(nether);
		} else {
			logger.warning(Lang.ERROR_WORLD_NOT_FOUND.get().replace(LangTag.WORLD_NAME.toString(), nethername));
		}
		
		if (Bukkit.getWorld(endname) != null) {
			this.end = Bukkit.getWorld(endname);
			this.worlds.add(end);
		} else {
			logger.warning(Lang.ERROR_WORLD_NOT_FOUND.get().replace(LangTag.WORLD_NAME.toString(), endname));
		}
		
		for (World w : getWorlds()) {
			new WorldManager(w.getName()).load();
		}
		
		//PLAYER
		this.players = new HashSet<>();
		if (host != null) players.add(new UHCPlayer(host, Rank.HOST, this));
		
		UHC.getInstance().setGameSettings(new GameSettings(this));
		
		this.sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		//SETTING
		this.task = null;
		
		this.permissions = new HashMap<Rank, List<Permission>>();
		
		this.permissions.put(Rank.HOST, Arrays.asList(Permission.ALL));
		this.permissions.put(Rank.ORGA, Arrays.asList(Permission.ALL));
		this.permissions.put(Rank.STAFF, Arrays.asList(Permission.ALL));
		this.permissions.put(Rank.PLAYER, Arrays.asList(Permission.CHAT));
		this.permissions.put(Rank.SPECTATOR, Arrays.asList(Permission.NONE));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Scoreboard getScoreboard() {
		return sb;
	}
	
	public GameState getState() {
		return state;
	}
	
	public Boolean isStart() {
		return state == GameState.GAME || state == GameState.ENDING ;
	}

	public void setState(GameState state) {
		this.state = state;
	}
	
	public GameSettings getSettings() {
		return UHC.getInstance().getGameSettings();
	}
	
	public UHC_Task getTask() {
		return task;
	}
	
	public Boolean isRunning() {
		return task != null;
	}
	
	public void setRunner(UHC_Task task) {
		this.task = task;
	}
	
	public UHC_Task getRunner() {
		return task;
	}
	
	public Boolean start() {
		
		if (getState() == GameState.LOBBY) {
			
			if (isRunning()) getRunner().stop();
			
			setRunner(new Starting_Task(this));
			return true;
		}
		
		return false;
		
	}
	
	public List<World> getWorlds() {
		return worlds;
	}
	
	public World getOverWorld() {
		return overworld;
	}
	
	public World getNether() {
		return nether;
	}
	
	public World getEnd() {
		return end;
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
			if (p.getState() == PlayerState.ALIVE) players.add(p);
		}
		
		return players;
	}
	
	public Set<UHCPlayer> getPlayingPlayers() {
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.isConnected() && p.getState() == PlayerState.ALIVE) players.add(p);
		}
		
		return players;
	}
	
	public Set<UHCPlayer> getIngamePlayers() {
		
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.getState() == PlayerState.ALIVE || p.getState() == PlayerState.DEAD) players.add(p);
		}
		
		return players;
	}
	
	public Boolean isHere(UUID uuid) {
		return getUHCPlayer(uuid) != null;
	}
	
	public void addUHCPlayer(UHCPlayer player) {
		players.add(player);
	}
	
	public void removeUHCPlayer(UHCPlayer player) {
		
		if (player.hasTeam()) player.leaveTeam();
		
		if (player.getRank() == Rank.HOST || player.getRank() == Rank.ORGA) {
			player.setState(PlayerState.DEAD);
			return;
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
			
			if (player.hasPermission("whitelist.bypass")) return true;
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

				String prefix = c.getChatcolor() + s + " ";
				
				if (!teamExist(prefix + c.getColorName())) {
					teams.add(new UHCTeam(prefix, c, this));
					return;
				}
			}
		}
	}
	
	public void destroyTeams() {
		
		//RESET LES NOMS
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
	
	public List<UHCTeam> getAliveTeams() {
		List<UHCTeam> teams = new ArrayList<UHCTeam>();
		
		if (hasTeam()) {
			for (UHCTeam t : getTeams()) {
				for (UHCPlayer p : t.getPlayers()) {
					if (p.isConnected() && p.getState() == PlayerState.ALIVE) {
						teams.add(t);
						break;
					}
				}
			}
		}
		
		return teams;
	}
	
	//PERMISSIONS
	public HashMap<Rank, List<Permission>> getPermissions() {
		return permissions;
	}
	
	public Boolean rankHasPermission(Rank rank, Permission perm) {
		return rank.hasPermission(perm);
	}
	
	public Boolean playerHasPermission(UHCPlayer player, Permission perm) {
		return rankHasPermission(player.getRank(), perm);
	}
	
	public Boolean playerHasPermission(Player player, Permission perm) {
		return playerHasPermission(player.getUniqueId(), perm);
	}
	
	public Boolean playerHasPermission(UUID uuid, Permission perm) {
		
		if (!isHere(uuid)) return false;
		return rankHasPermission(getUHCPlayer(uuid).getRank(), perm);
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
		
		for (Player p : getInWorldsPlayers()) {
			p.sendMessage(message);
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
			p.getPlayer().playSound(p.getPlayer().getLocation(), sound, volume, pitch);
		}
	}
	
	//END
	public Boolean isEnd() {
		
		if (hasTeam()) {
			if (getAliveTeams().size() == 1) {
				
			}
		}
		return false;
	}
}
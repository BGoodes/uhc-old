package fr.aiidor.uhc.team;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.TeamColor;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.utils.ItemBuilder;

public class UHCTeam {
	
	private String name;
	
	private String prefix;
	private String suffix;
	
	private Game game;
	private TeamColor color;
	
	private Team team;
	private int size;
	
	private Set<UHCPlayer> players;
	
	public UHCTeam(String prefix, TeamColor color, Integer size, Game game) {
		this(prefix, color, null, size, game);
	}
	
	public UHCTeam(String prefix, TeamColor color, String name, Integer size, Game game) {
		players = new HashSet<UHCPlayer>();
		
		if (name == null) setName(prefix + color.getColorName());
		else setName(name);
		
		this.game = game;
		this.setColor(color);
		this.setSize(size);
		this.prefix = prefix;
		
		Scoreboard sb = game.getScoreboard();
		if (sb.getTeam(getName()) == null) sb.registerNewTeam(getName());
		team = sb.getTeam(getName());
		
		this.setSuffix("");
		
		team.setDisplayName(getName());
		team.setPrefix(prefix);
		
		team.setAllowFriendlyFire(game.getSettings().friendly_fire);
		team.setCanSeeFriendlyInvisibles(true);
		team.setNameTagVisibility(NameTagVisibility.ALWAYS);
	}

	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Team getTeam() {
		return team;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		team.setPrefix(prefix);
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
		team.setSuffix(suffix);
	}
	
	public TeamColor getColor() {
		return color;
	}

	public void setColor(TeamColor color) {
		this.color = color;
	}

	public void addPlayer(UHCPlayer player) {
		players.add(player);
	}
	
	public void removePlayer(UHCPlayer player) {
		players.remove(player);
	}
	
	public Boolean isFull() {
		if (!UHC.getInstance().getGameManager().hasGame()) return true;
		return getPlayerCount() >= size;
	}
	
	public Set<UHCPlayer> getPlayers() {
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		players.addAll(this.players);
		return players;
	}
	
	public Set<UHCPlayer> getPlayingPlayers() {
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.isConnected() && p.isAlive()) players.add(p);
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
	
	public Set<UHCPlayer> getAlivePlayers() {
		Set<UHCPlayer> players = new HashSet<UHCPlayer>();
		
		for (UHCPlayer p : this.players) {
			if (p.isAlive()) players.add(p);
		}
		
		return players;
	}
	
	public Integer getPlayerCount() {
		return players.size();
	}
	
	public Boolean isInTeam(UHCPlayer player) {
		return getPlayers().contains(player);
	}
	
	public void broadcast(String message) {
		for (UHCPlayer p : getConnectedPlayers()) {
			p.getPlayer().sendMessage(message);
		}
		
		if (UHC.getInstance().getSettings().log_game_bc) {
			Bukkit.getConsoleSender().sendMessage(getName() + " > " + message);
		}
	}
	
	public void destroy() {
		
		for (UHCPlayer player : getPlayers()) {
			team.removeEntry(player.getName());
		}
		
		team.unregister();
	}
	
	public Integer getSize() {
		return size;
	}
	
	public void setSize(Integer size) {
		
		if (getPlayerCount() > size && size >= 1) {
			while (getPlayerCount() > size && size >= 1) {
				((UHCPlayer) getPlayers().toArray()[new Random().nextInt(getPlayers().size())]).leaveTeam(true);
			}
		}
	
		this.size = size;
	}
	
	public void join(UHCPlayer player, Boolean announce) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return;
		
		if (!isFull()) {
			if (player.isConnected()) {
				
				player.leaveTeam(announce);
				Player p = player.getPlayer();
				
				players.add(player);
				team.addEntry(player.getName());
				
				if (announce) {
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.6f, 1f);
					p.sendMessage(Lang.TEAM_JOIN.get()
							.replace(LangTag.TEAM_NAME.toString(), getName())
							.replace(LangTag.TEAM_PLAYER_COUNT.toString(), getPlayerCount().toString()));
				}

				ItemStack hand = p.getItemInHand();
				if (hand != null && hand.getType() == Material.BANNER && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() 
					&& hand.getItemMeta().getDisplayName().equalsIgnoreCase(Lang.INV_TEAM_CHOOSE.get())) {
					
					p.setItemInHand(new ItemBuilder(Material.BANNER, Lang.INV_TEAM_CHOOSE.get(), color.getBannerColor()).getItem());
				}
			}
			
		} else {
			
			if (player.isConnected()) {
				player.getPlayer().sendMessage(Lang.TEAM_FULL.get()
						.replace(LangTag.TEAM_NAME.toString(), getName())
						.replace(LangTag.TEAM_PLAYER_COUNT.toString(), getPlayerCount().toString()));
			}
		}
	}
	
	public void leave(UHCPlayer player, Boolean announce) {
		
		players.remove(player);
		team.removeEntry(player.getName());
		
		if (announce) player.getPlayer().sendMessage(Lang.TEAM_LEAVE.get().replace(LangTag.TEAM_NAME.toString(), getName()));
		
		if (player.isConnected()) {
			Player p = player.getPlayer();
			
			ItemStack hand = p.getItemInHand();
			if (hand != null && hand.getType() == Material.BANNER && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() 
				&& hand.getItemMeta().getDisplayName().equalsIgnoreCase(Lang.INV_TEAM_CHOOSE.get())) {
				
				p.setItemInHand(new ItemBuilder(Material.BANNER, Lang.INV_TEAM_CHOOSE.get(), (byte) 15).getItem());
			}
		}
	}
}

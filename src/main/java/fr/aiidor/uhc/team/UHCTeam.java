package fr.aiidor.uhc.team;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.TeamColor;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.tools.ItemBuilder;

public class UHCTeam {
	
	private String prefix;
	private Game game;
	private TeamColor color;
	
	private Team team;
	private int size;
	
	private Set<UHCPlayer> players;
	
	public UHCTeam(String prefix, TeamColor color, Integer size, Game game) {
		
		players = new HashSet<UHCPlayer>();
		
		this.setPrefix(prefix);
		this.setColor(color);
		this.setSize(size);
		
		this.game = game;
		
		Scoreboard sb = game.getScoreboard();
		if (sb.getTeam(getName()) == null) sb.registerNewTeam(getName());
		
		team = sb.getTeam(getName());
		
		team.setDisplayName(getName());
		team.setPrefix(getPrefix());
		team.setSuffix("");
		
		team.setAllowFriendlyFire(game.getSettings().friendly_fire);
		team.setCanSeeFriendlyInvisibles(true);
		team.setNameTagVisibility(NameTagVisibility.ALWAYS);
	}

	public String getPrefix() {
		return prefix;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Team getTeam() {
		return team;
	}

	public String getName() {
		return prefix + color.getColorName();
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
			if (p.isConnected() && p.getState() == PlayerState.ALIVE) players.add(p);
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
				((UHCPlayer) getPlayers().toArray()[new Random().nextInt(getPlayers().size())]).leaveTeam();
			}
		}
	
		this.size = size;
	}
	
	public void join(UHCPlayer player) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return;
		
		if (!isFull()) {
			if (player.isConnected()) {
				
				player.leaveTeam();
				Player p = player.getPlayer();
				
				players.add(player);
				team.addEntry(player.getName());
				
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.6f, 1f);
				p.sendMessage(Lang.TEAM_JOIN.get()
						.replace(LangTag.TEAM_NAME.toString(), getName())
						.replace(LangTag.TEAM_PLAYER_COUNT.toString(), getPlayerCount().toString()));
				
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
	
	public void leave(UHCPlayer player) {
		
		players.remove(player);
		team.removeEntry(player.getName());
		
		player.getPlayer().sendMessage(Lang.TEAM_LEAVE.get().replace(LangTag.TEAM_NAME.toString(), getName()));
		
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

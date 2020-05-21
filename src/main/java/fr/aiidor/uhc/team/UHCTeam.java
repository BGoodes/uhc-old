package fr.aiidor.uhc.team;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.TeamColor;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class UHCTeam {
	
	private String prefix;
	private Game game;
	private TeamColor color;
	
	private Team team;
	
	private Set<UHCPlayer> players;
	
	public UHCTeam(String prefix, TeamColor color, Game game) {
		this.setPrefix(prefix);
		this.setColor(color);
		
		players = new HashSet<UHCPlayer>();
		this.game = game;
		
		Scoreboard sb = game.getScoreboard();
		if (sb.getTeam(getName()) == null) sb.registerNewTeam(getName());
		
		team = sb.getTeam(getName());
		
		team.setDisplayName(getName());
		team.setPrefix(getPrefix());
		
		team.setAllowFriendlyFire(game.getSettings().friendly_fire);
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
		Game game = UHC.getInstance().getGameManager().getGame();
		
		return getPlayerCount() >= game.getSettings().getTeamSize();
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
	
	public void join(UHCPlayer player) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return;
		
		if (!isFull()) {
			if (player.isConnected()) {
				
				player.leaveTeam();
				Player p = player.getPlayer();
				
				players.add(player);
				team.addEntry(player.getName());
				player.setDisplayName(getPrefix() + player.getName()); 
				
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 0.6f, 1f);
				p.sendMessage(Lang.TEAM_JOIN.get()
						.replace(LangTag.TEAM_NAME.toString(), getName())
						.replace(LangTag.TEAM_PLAYER_COUNT.toString(), getPlayerCount().toString()));
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
		player.setDisplayName(player.getName()); 
		
		player.getPlayer().sendMessage(Lang.TEAM_LEAVE.get().replace(LangTag.TEAM_NAME.toString(), getName()));
	}
}

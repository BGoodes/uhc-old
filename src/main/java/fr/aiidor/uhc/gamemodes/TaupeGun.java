package fr.aiidor.uhc.gamemodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Sound;

import fr.aiidor.tg.TGSettings;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.TeamColor;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.team.UHCTeam;

public class TaupeGun extends UHCMode {
	
	private TGSettings settings;
	//private List<TGMole> moles;
	
	public TaupeGun() {
		settings = new TGSettings();
		//moles = null;
	}
	
	@Override
	public final UHCType getUHCType() {
		return UHCType.TAUPE_GUN;
	}
	
	@Override
	public void checkConditions() {
		
	}
	
	@Override
	public void loading() {
		
		for (int i = settings.mole_team_number; i != 0; i--) {
			
			String name = "§c" + Lang.TG_MOLE.get() + " " + i;
			if (game.hasTeam()) game.addTeam(new UHCTeam(name + " ", TeamColor.RED, name, settings.mole_team_size, game));
		}
	}
	
	@Override
	public void begin() {
		game.getSettings().show_death_reason = false;
		if (!game.hasTeam()) return;
			
		if (!settings.apocalypse) {
			List<UHCTeam> teams = game.getTeams();
			
			while (!teams.isEmpty() && !getNotFullMoleTeams().isEmpty()) {
				
				UHCTeam t = teams.get(new Random().nextInt(teams.size()));
				Set<UHCPlayer> players = t.getAlivePlayers();
				
				if (!isMoleTeam(t)) {
					for (UHCTeam mt : getNotFullMoleTeams()) {
						if (players.isEmpty()) break;
						else {
							
							UHCPlayer m = (UHCPlayer) players.toArray()[new Random().nextInt(players.size())];
							setMole(m, mt);
							players.remove(m);
						}
					}
				}
				
				teams.remove(t);
			}
			
			
		} else {
			//Integer moleNumber = settings.mole_number;
			
		}
		
		game.broadcast(Lang.TG_MOLE_SELECTION.get());
		game.playSound(Sound.ORB_PICKUP);
	}

	@Override
	public void episode(Integer ep) {
		
	}
	
	@Override
	public void run() {
		
	}

	@Override
	public void stop() {
		
	}
	
	public Boolean isMoleTeam(UHCTeam team) {
		return team.getName().startsWith("§c" + Lang.TG_MOLE.get());
	}
	
	public List<UHCTeam> getNotFullMoleTeams() {
		List<UHCTeam> teams = new ArrayList<UHCTeam>();
		
		for (UHCTeam t : getMoleTeams()) {
			if (isMoleTeam(t) && t.getPlayerCount() < settings.mole_team_number) teams.add(t);
		}
		
		return teams;
	}
	
	public List<UHCTeam> getMoleTeams() {
		List<UHCTeam> teams = new ArrayList<UHCTeam>();
		
		for (UHCTeam t : game.getTeams()) {
			if (isMoleTeam(t)) teams.add(t);
		}
		
		return teams;
	}
	
	public Boolean isMole(UUID uuid) {
		if (game.isHere(uuid)) {
			UHCPlayer p = game.getUHCPlayer(uuid);
			
			if (!p.hasTeam()) return true;
			if (game.getScoreboard().getEntryTeam(p.getName()) == null) return true;
			if (!game.getScoreboard().getEntryTeam(p.getName()).equals(p.getTeam().getTeam())) return true;
			
		}
		return false;
	}
	
	public void setMole(UHCPlayer p, UHCTeam mt) {
		UHCTeam t = p.getTeam();
		
		t.removePlayer(p);
		mt.addPlayer(p);
		
		if (p.isConnected()) {
			p.getPlayer().sendMessage(Lang.TG_MOLE_ANNOUNCE.get());
		}
	}
	
	/*public Boolean isMole(UUID uuid) {
		return getMole(uuid) != null;
	}
	
	public TGMole getMole(UUID uuid) {
		if (molesAreAnnounced()) {
			for (TGMole m : moles) {
				if (m.getUHCPlayer().getUUID().equals(uuid)) return m;
			}
		}
		return null;
	}
	
	public Boolean molesAreAnnounced() {
		return moles != null;
	}*/
	
	public TGSettings getSettings() {
		return settings;
	}
}

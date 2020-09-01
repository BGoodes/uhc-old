package fr.aiidor.uhc.gamemodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.aiidor.tg.TGMole;
import fr.aiidor.tg.TGSettings;
import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.TeamColor;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.tools.Titles;

public class TaupeGun extends UHCMode {
	
	private TGSettings settings;
	private List<TGMole> moles;
	
	public TaupeGun() {
		settings = new TGSettings();
		moles = null;
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
		
		GameSettings s = game.getSettings();
		s.death_lightning = false;
		
		for (int i = settings.mole_team_number; i != 0; i--) {
			
			String name = "§c" + Lang.TG_MOLE.get() +  "§f" + i;
			String prefix = Lang.TG_MOLE_PREFIX.get().replace(LangTag.VALUE.toString(), ""+i);
			
			if (game.hasTeam()) game.addTeam(new UHCTeam(prefix, TeamColor.RED, name, settings.mole_team_size, game));
		}
	}
	
	@Override
	public void begin() {
		game.getSettings().show_death_reason = false;
		
		if (!game.hasTeam()) return;
		moles = new ArrayList<TGMole>();
		
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
							
							while (isMole(m.getUUID()) && !players.isEmpty()) {
								players.remove(m);
								m = (UHCPlayer) players.toArray()[new Random().nextInt(players.size())];
							}

							if (!isMole(m.getUUID())) {
								setMole(m, mt);
								players.remove(m);
							}
						}
					}
				}
				
				teams.remove(t);
			}
			
			
		} else {
			/*Integer moleNumber = settings.mole_number;
			while (moleNumber > 0) {
				
			}*/
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
	
	public void setMole(UHCPlayer p, UHCTeam mt) {
		moles.add(new TGMole(p, mt));
		
		if (p.isConnected()) {
			p.getPlayer().sendMessage(Lang.TG_MOLE_ANNOUNCE.get());
		}
	}
	
	public void removeMole(TGMole m) {
		this.moles.remove(m);
	}
	
	public Boolean isMole(UUID uuid) {
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
	}
	
	public TGSettings getSettings() {
		return settings;
	}
	
	public void end() {
		
		if (!game.hasTeam()) {
			super.end();
			return;
		}
		
		if (!molesAreAnnounced()) return;
		
		if (game.getAlivePlayers().size() == 1) {
			UHCPlayer winner = (UHCPlayer) game.getAlivePlayers().toArray()[0];
			if (!winner.hasTeam()) {
					
				Bukkit.broadcastMessage(Lang.BC_SOLO_VICTORY.get().replace(LangTag.PLAYER_NAME.toString(), winner.getDisplayName()));
					
				if (winner.isConnected()) {
						
					new Titles(winner.getPlayer()).sendTitle(Lang.BC_VICTORY_TITLE_LINE_1.get(), Lang.BC_VICTORY_TITLE_LINE_2.get(), 60);
					FireWork(winner, Color.PURPLE);
				}
			
				for (UHCPlayer p : game.getIngamePlayers()) {
					if (p.isConnected() && p.isDead()) {
						new Titles(p.getPlayer()).sendTitle(Lang.BC_LOSE_TITLE_LINE_1.get(), Lang.BC_LOSE_TITLE_LINE_2.get(), 60);
					}
				}
					
				UHC.getInstance().getGameManager().stopGame();
				return;
			}
		}
			
		if (game.getAliveTeams().size() == 1) {
				
			if (ScenariosManager.ONLY_ONE_WINNER.isActivated()) {
				game.destroyTeams();
				game.playSound(Sound.GHAST_MOAN, 0.6f);
				game.broadcast(Lang.BC_ONE_WINNER.get());
				return;
			}
				
			for (UHCTeam team : game.getAliveTeams()) {
				
				for (UHCPlayer p : team.getAlivePlayers()) {
					if (isMole(p.getUUID())) return;
				}
				
				Bukkit.broadcastMessage(Lang.BC_TEAM_VICTORY.get().replace(LangTag.TEAM_NAME.toString(), team.getName()));
					
				for (UHCPlayer p : game.getIngamePlayers()) {
					if (p.isConnected()) {
						if (p.isDead()) new Titles(p.getPlayer()).sendTitle(Lang.BC_LOSE_TITLE_LINE_1.get(), Lang.BC_LOSE_TITLE_LINE_2.get(), 60);
						else {
							new Titles(p.getPlayer()).sendTitle(Lang.BC_VICTORY_TITLE_LINE_1.get(), Lang.BC_VICTORY_TITLE_LINE_2.get(), 60);
							FireWork(p, team.getColor().getColor());
						}
					}
				}
					
				UHC.getInstance().getGameManager().stopGame();
				break;
			}
		}
	}
	
	private void FireWork(UHCPlayer player, Color color) {
		
		Player p = player.getPlayer();
		
		Firework f = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		f.detonate();
		
		FireworkMeta fM = f.getFireworkMeta();
		
		FireworkEffect effect = FireworkEffect.builder()
				.flicker(true)
				.withColor(color)
				.withFade(Color.ORANGE)
				.with(Type.values()[new Random().nextInt(Type.values().length)])
				.trail(true)
				.build();
		
		fM.addEffect(effect);
		fM.setPower(1);
		f.setFireworkMeta(fM);
		
	}
}

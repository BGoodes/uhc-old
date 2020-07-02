package fr.aiidor.uhc.gamemodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.dwuhc.DWCamp;
import fr.aiidor.dwuhc.DWEventManager;
import fr.aiidor.dwuhc.DWEventManager.CorpseTask;
import fr.aiidor.dwuhc.DWRole;
import fr.aiidor.dwuhc.DWRoleEpisode;
import fr.aiidor.dwuhc.DWRoleType;
import fr.aiidor.dwuhc.DWSettings;
import fr.aiidor.dwuhc.DWplayer;
import fr.aiidor.dwuhc.Prowler;
import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.game.UHCPlayer;

public class DevilWatches extends UHCMode {
	
	private List<DWplayer> players = null;
	private HashMap<DWRoleType, Integer> game_roles = null;
	
	private DWSettings settings;
	
	private DWEventManager eventManager;
	
	public DevilWatches() {
		settings = new DWSettings();
		eventManager = new DWEventManager(this);
	}
	
	@Override
	public final UHCType getUHCType() {
		return UHCType.DEVIL_WATCHES;
	}

	@Override
	public void checkConditions() {
		
	}
	
	@Override
	public void loading() {
		GameSettings s = game.getSettings();
		s.uhc_cycle = true;
		s.double_uhc_cycle = true;
		s.display_life = false;
		
	}
	
	@Override
	public void begin() {
		game_roles = new HashMap<DWRoleType, Integer>();
		Set<UHCPlayer> uhcPlayers = game.getAlivePlayers();
 		
		Map<DWRoleType, Integer> compo = new HashMap<DWRoleType, Integer>();
		List<DWplayer> roles = new ArrayList<DWplayer>();
		
		for (Entry<DWRoleType, Integer> r : settings.compo.entrySet()) {
			if (r.getValue() > 0) compo.put(r.getKey(), r.getValue());
		}
		
		Integer sectarians = settings.sectarians;
		
		while (!uhcPlayers.isEmpty()) {
			
			DWRoleType type = DWRoleType.VILLAGER;
			
			if (!compo.isEmpty()) type = (DWRoleType) compo.keySet().toArray()[new Random().nextInt(compo.size())];
			UHCPlayer p = (UHCPlayer) uhcPlayers.toArray()[new Random().nextInt(uhcPlayers.size())];
			
			DWplayer dwp = new DWplayer(p);
			
			if (type != DWRoleType.VILLAGER) {
				compo.put(type, compo.get(type) - 1);
				if (compo.get(type) <= 0) compo.remove(type);
			}
			
			if (type.canSectarian() && sectarians > 0) {
				
				//SCEAU
				dwp.setRole(type, DWCamp.SECTARIANS);
				sectarians--;
				
			} else dwp.setRole(type, type.getBaseCamp());
			
			if (game_roles.containsKey(type)) game_roles.put(type, game_roles.get(type) + 1);
			else game_roles.put(type, 1);
			
			uhcPlayers.remove(p); 
			roles.add(dwp);
		}
		
		Bukkit.getScheduler().runTaskLater(UHC.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				players = new ArrayList<DWplayer>();
				
				for (DWplayer p : roles) {
					p.announceRole();
					players.add(p);
					
					episode(2);
				}
			}
		}, 3 * 20);
	}

	public HashMap<DWRoleType, Integer> getGameRoles() {
		return game_roles;
	}
	
	@Override
	public void episode(Integer ep) {
		if (playersHasRole()) {
			for (DWplayer p : getAlivePlayers()) {
				if (p.hasRole()) {
					if (p.getRole() instanceof DWRoleEpisode) {
						((DWRoleEpisode) p.getRole()).givePower(ep);
					}
					
					if (p.getRole() instanceof Prowler) {
						((Prowler) p.getRole()).time = 0;
					}
				}
			}
		}
	}
	
	@Override
	public void run() {
		
		Integer time = game.getRunner().getTime();
		
		if (playersHasRole()) {
			for (DWplayer dwp : getPlayingPlayers()) {
				UHCPlayer p = dwp.getUHCPlayer();
				if (dwp.hasRole()) {
					
					DWRole r = dwp.getRole();
					if (dwp.hasPotionEffects()) {
						for (PotionEffect pe : dwp.getPotionEffects()) {
							dwp.getUHCPlayer().addPotionEffect(pe);
						}
					}
					
					Integer min = ((time - time%60)/60);
					Boolean isDay = ((min - min%10)/10)%2 == 0;
					
					if (isDay) { //DAY
						
						if (dwp.hasDayPotionEffects()) {
							for (PotionEffect pe : dwp.getDayPotionEffects()) {
								p.addPotionEffect(pe);
							}
						}
						
					} else { //NIGHT
						
						if (dwp.hasNightPotionEffects()) {
							for (PotionEffect pe : dwp.getNightPotionEffects()) {
								p.addPotionEffect(pe);
							}
						}
					}
					
					if (r.getRoleType() == DWRoleType.PROWLER && dwp.getRole().hasPower()) {
						Prowler role = (Prowler) r;
						
						Integer power_time =  game.getSettings().ep_time * 60/2 - role.time;
						
						if (!p.hasArmor() && power_time >= 0) {
							
							role.time++;
							p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, power_time * 20, 0, false, false));
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, power_time * 20, 0, false, false));
							
						} else {
							p.removePotionEffect(PotionEffectType.INVISIBILITY);
							p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						}
					}
					
					if (r.getRoleType() == DWRoleType.HERMIT && dwp.getRole().hasPower()) {
						int number = getPlayersArround(dwp.getUHCPlayer().getPlayer());
						
						if (number >= 4) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2*20, 0, false, false));
						}
						
						if (number <= 2) {
							if (isDay) p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2*20, 0, false, false));
							else p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2*20, 0, false, false));
						}
						
						if (number <= 1) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2*20, 0, false, false));
						}
					}
				}
			}
		}
	}
	
	private Integer getPlayersArround(Player player) {
		int i = 0;
		for (Entity e : player.getNearbyEntities(30, 30, 30)) {
			if (e instanceof Player) {
				Player p = (Player) e;
				
				if (hasRole(p.getUniqueId()) && getDWPlayer(p.getUniqueId()).isAlive()) i++;
			}
		}
		return i;
	}
	
	@Override
	public void end() {
		
		if (playersHasRole()) {
			
			Integer villagers = getPlayingPlayers(DWCamp.VILLAGERS).size();
			Integer sectarians = getPlayingPlayers(DWCamp.SECTARIANS).size();
			Integer demons = getPlayingPlayers(DWCamp.DEMONS).size();
			Integer solos = getPlayingPlayers(DWCamp.SOLOS).size();
			Integer neutrals = getPlayingPlayers(DWCamp.NEUTRALS).size();
			
			if ((neutrals > 0 && sectarians == 0 && demons == 0 && solos == 0) || getPlayingPlayers().size() == 0) {
				//DRAW
				game.broadcast(DWCamp.NEUTRALS.getVictoryMessage());
				stopGame();
			}
			
			if ((villagers == 0 && sectarians == 0 && demons == 0 && solos == 0) || getPlayingPlayers().size() == 0) {
				//DRAW
				game.broadcast(Lang.DW_VICTORY_DRAW.get());
				stopGame();
			}
			
			if (villagers > 0 && sectarians == 0 && demons == 0 && solos == 0) {
				//VILLAGERS WIN
				game.broadcast(DWCamp.VILLAGERS.getVictoryMessage());
				stopGame();
			}
			
			if (sectarians > 0 && villagers == 0 && demons == 0 && solos == 0) {
				//SECTARIANS WIN
				game.broadcast(DWCamp.SECTARIANS.getVictoryMessage());
				stopGame();
			}
			
			if (demons > 0 && sectarians == 0 && villagers == 0 && solos == 0) {
				//DEMONS WIN
				game.broadcast(DWCamp.DEMONS.getVictoryMessage());
				stopGame();
			}
			
			if (solos == 1 && sectarians == 0 && demons == 0 && villagers == 0) {
				//SOLO WIN
				String name = getPlayingPlayers(DWCamp.SOLOS).get(0).getUHCPlayer().getDisplayName();
				game.broadcast(DWCamp.SOLOS.getVictoryMessage().replace(LangTag.PLAYER_NAME.toString(), name));
				stopGame();
			}
		}
	}
	
	private void stopGame() {
		
		for (DWplayer p : players) {
			if (p.hasRole()) {
				UHCPlayer up = p.getUHCPlayer();
				
				String sectarian = p.getCamp() == DWCamp.SECTARIANS ? Lang.removeColor(Lang.DW_SECTARIAN.get()) : "";
				
				String player_name = up.getDisplayName() + " - ";
				String role = p.getRole().getName() + sectarian;
				
				if (!p.isAlive()) {
					player_name = "§m" + player_name;
					role = "§m" + role;
				}
				
				String message = "§d" + player_name + "§d" + role;
				
				game.broadcast(message, false);
			}
		}
		
		UHC.getInstance().getGameManager().stopGame();
	}
	
	@Override
	public void stop() {
		
		for (DWplayer p : players) {
			if (p.hasRole()) p.getRole().stop();
		}
		
		players = null;
		
		for (CorpseTask t : eventManager.corpse_tasks) {
			t.stop();
		}
		
		eventManager.corpse_tasks = new ArrayList<DWEventManager.CorpseTask>();
	}
	
	public Boolean isDWPlayer(UUID uuid) {
		return playersHasRole() && getDWPlayer(uuid) != null;
	}
	
	public Boolean hasRole(UUID uuid) {
		return playersHasRole() && isDWPlayer(uuid) && getDWPlayer(uuid).hasRole();
	}
	
	public DWplayer getDWPlayer(UUID uuid) {
		
		if (playersHasRole()) {
			for (DWplayer p : players) {
				if (p.getUHCPlayer().getUUID().equals(uuid)) {
					return p;
				}
			}
		}
		
		return null;
	}
	
	public List<DWplayer> getAliveSectarians() {
		List<DWplayer> players = new ArrayList<DWplayer>();
		
		for (DWplayer p : this.players) {
			if (p.isAlive() && p.hasRole() && p.getRole().isSectaireMember()) players.add(p);
		}
		
		return players;
	}
	
	public List<DWplayer> getAliveDemons() {
		List<DWplayer> players = new ArrayList<DWplayer>();
		
		for (DWplayer p : this.players) {
			if (p.isAlive() && p.hasRole() && p.getRole().isDemonMember()) players.add(p);
		}
		
		return players;
	}
	
	public List<DWplayer> getAlivePlayers() {
		List<DWplayer> players = new ArrayList<DWplayer>();
		
		for (DWplayer p : this.players) {
			if (p.isAlive()) players.add(p);
		}
		
		return players;
	}
	
	public List<DWplayer> getAlivePlayers(DWCamp camp) {
		List<DWplayer> players = new ArrayList<DWplayer>();
		
		for (DWplayer p : this.players) {
			if (p.isAlive() && p.getRole().getCamp() == camp) players.add(p);
		}
		
		return players;
	}
	
	public List<DWplayer> getPlayingPlayers(DWCamp camp) {
		List<DWplayer> players = new ArrayList<DWplayer>();
		
		for (DWplayer p : this.players) {
			if (p.isAlive() && p.getUHCPlayer().isConnected() && p.getRole().getCamp() == camp) players.add(p);
		}
		
		return players;
	}
	
	public List<DWplayer> getPlayingPlayers(DWRoleType role) {
		List<DWplayer> players = new ArrayList<DWplayer>();
		
		for (DWplayer p : this.players) {
			if (p.isAlive() && p.getUHCPlayer().isConnected() && p.getRole().getRoleType() == role) players.add(p);
		}
		
		return players;
	}
	
	public List<DWplayer> getPlayingPlayers() {
		List<DWplayer> players = new ArrayList<DWplayer>();
		
		for (DWplayer p : this.players) {
			if (p.getUHCPlayer().isConnected() && p.isAlive()) players.add(p);
		}
		
		return players;
	}
	
	public Boolean playersHasRole() {
		return players != null;
	}
	
	public DWSettings getSettings() {
		return settings;
	}
	
	public DWEventManager getEventManager() {
		return eventManager;
	}
	
	public Game getGame() {
		return game;
	}
	
	public static Boolean isDevilWatches() {
		GameManager gm = UHC.getInstance().getGameManager();
		if (gm.hasGame() && gm.getGame().getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) return true;
		return false;
	}
	
	public static DevilWatches getDevilWatches() {
		return (DevilWatches) UHC.getInstance().getGameManager().getGame().getUHCMode();
	}
}

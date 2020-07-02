package fr.aiidor.uhc.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ItemScenario;
import fr.aiidor.uhc.scenarios.ItemScenario.GiveTime;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.tools.Teleportation;

public class LoadingTask extends UHCTask {

	private Game game;
	private HashMap<UHCPlayer, Location> spawns;
	
	public LoadingTask(Game game) {
		this.game = game;
		this.spawns = new HashMap<UHCPlayer, Location>();
	}
	
	private String[] color = {"§a", "§e", "§6", "§c", "§4", "§5", "§5", "§5", "§5", "§5"};
	private Integer timer;
	
	@Override
	public void launch() {
		
		
		timer = 20;
		
		game.setState(GameState.LOADING);
		game.getMainWorld().getMainWorld().setTime(23500 - 20 * timer);
		
		for (World w : game.getWorlds()) {
			WorldBorder wb = w.getWorldBorder();
			
			wb.setSize(game.getSettings().wb_size_max * 2);
			wb.setDamageAmount(game.getSettings().wb_damage);
			wb.setDamageBuffer(0);	
		}
		
		for (UHCPlayer p : game.getAllPlayers()) {
			if (p.isConnected()) {
				
				if (p.getState() == PlayerState.SPECTATOR) {
					spec(p);
					continue;
				}
				
				p.setState(PlayerState.ALIVE);
				p.reset();
				
			} else {
				game.removeUHCPlayer(p);
			}
		}
		
		//TEAM
		if (game.hasTeam()) {
			
			//RANDOM TEAMS
			if (game.getSettings().team_type == TeamType.RANDOM) {
				game.broadcast(Lang.BC_RANDOM_TEAMS.get());
				
				List<UHCPlayer> players = new ArrayList<UHCPlayer>();
				players.addAll(game.getAlivePlayers());
				
				for (UHCTeam t : game.getTeams()) {
					while (!t.isFull() && !players.isEmpty()) {
						
						UHCPlayer p = players.get(new Random().nextInt(players.size()));
						t.join(p);
						players.remove(p);
					}
				}
			}
			
			if (game.getSettings().team_type == TeamType.CHOOSE) {
				
				for (UHCPlayer p : game.getAlivePlayers()) {
					
					if (p.isConnected()) {
						if (!p.hasTeam()) {
							if (!game.getNotFullTeams().isEmpty()) {
								game.getNotFullTeams().get(new Random().nextInt(game.getNotFullTeams().size())).join(p);
							}
						}
					}
				}
			}
			
			for (UHCTeam t : game.getTeams()) {
				if (t.getPlayerCount() == 0) {
					game.removeTeam(t);
				}
			}
			
			for (UHCPlayer p : game.getAlivePlayers()) {
				if (!p.hasTeam()) {
					if (p.isConnected()) {
						
						p.getPlayer().sendMessage(Lang.TEAM_PLAYER_CANNOT_PLAY.get());
						spec(p);
						
					} else {
						game.removeUHCPlayer(p);
					}
				}
			}
			
			game.broadcast("§8-------------------------------");
			game.broadcast(Lang.BC_TEAMS_LOADING.get());
			
		} else {
			game.broadcast("§8-------------------------------");
			game.broadcast(Lang.BC_PLAYERS_LOADING.get());
			
		}
		
		game.broadcast(Lang.BC_TELEPORTATION_LOADING.get());
		game.broadcast("§8-------------------------------");
		
		
		//SPAWNS
		if (!game.hasTeam()) {
			for (UHCPlayer player : game.getAlivePlayers()) {
				if (player.isConnected()) {
					
					spawns.put(player, Teleportation.getRandomLocation(game.getMainWorld().getMainWorld(), game.getSettings().wb_size_max - 20));
					
				} else {
					game.removeUHCPlayer(player);
					//SET SPEC FOR OP
				}
			}
		} else {
			for (UHCTeam t : game.getTeams()) {
				if (t.getPlayerCount() > 0) {
					Location loc = Teleportation.getRandomLocation(game.getMainWorld().getMainWorld(), game.getSettings().wb_size_max - 20);
					
					for (UHCPlayer player : t.getPlayers()) {
						if (player.isConnected()) {
							spawns.put(player, loc);
							
						} else {
							game.removeUHCPlayer(player);
							//SET SPEC FOR OP
						}
					}
					
				} else {
					game.removeTeam(t);
				}
			}
		}
		
		game.broadcast(Lang.BC_TELEPORTATION_FIND.get());
		game.broadcast(Lang.BC_CHUNK_LOADING.get());
		
		for (Location loc : spawns.values()) {
			generateChunks(loc, 8);
		}
		
		game.broadcast(Lang.BC_TELEPORTATION.get());
		
		//TP
		for (UHCPlayer player : game.getAlivePlayers()) {
			if (player.isConnected()) {
				Player p = player.getPlayer();
				
				//EFFECT
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, true, true));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, true, true));
			}
		}
		
		for (Entry<UHCPlayer, Location> map : spawns.entrySet()) {
			if (map.getKey().isConnected()) {
				
				
				game.actionText(Lang.BC_PLAYER_TP.get().replace(LangTag.PLAYER_NAME.toString(), map.getKey().getName()));
				
				map.getKey().getPlayer().teleport(map.getValue().clone().add(0, 15, 0));
				game.playSound(Sound.CHICKEN_EGG_POP);
				
			} else {
				game.removeUHCPlayer(map.getKey());
			}
		}
		
		game.getUHCMode().loading();
		if (UHC.getInstance().getSettings().hasCage()) UHC.getInstance().getSettings().cage.destroy();
		
		game.setRunner(this);
		runTaskTimer(UHC.getInstance(), 0, 20);
	}
	
	@Override
	public void run() {
		
		timer--;
		
		if (timer <= 0) {
				
			this.cancel();
				
			for (UHCPlayer player : game.getIngamePlayers()) {
				if (player.isConnected()) {
						
					Player p = player.getPlayer();
					
					p.setLevel(0);
					p.setExp(0);
					
					p.getActivePotionEffects().forEach(pot -> p.removePotionEffect(pot.getType()));
					p.setMaxHealth(game.getSettings().start_life);	
					
					game.getSettings().setStartItems(p);
						
					for (ItemScenario s : UHC.getInstance().getScenarioManager().getItemScenarios(GiveTime.LOADING)) {
						if (s.isActivated()) s.GiveItems(player);
					}
						
					if (ScenariosManager.CAT_EYES.isActivated()) {
						p.addPotionEffect(ScenariosManager.CAT_EYES.nightVision);
					}
						
					if (ScenariosManager.BELIEVE_FLY.isActivated()) {
						p.setAllowFlight(true);
					}
					
					if (ScenariosManager.MASTER_LEVEL.isActivated()) {
						p.setLevel(ScenariosManager.MASTER_LEVEL.level);
					}
					
					if (game.getSettings().start_abso > 0) {
						((CraftPlayer) p).getHandle().setAbsorptionHearts(game.getSettings().start_abso * 2);
					}
						
				} else {
					game.removeUHCPlayer(player);
				}
			}
			
			if (ScenariosManager.SUPER_HEROES.isActivated()) {
				ScenariosManager.SUPER_HEROES.start(game);
			}
			
			//BROADCAST
			game.title("§5§lUHC");
			game.playSound(Sound.ENDERDRAGON_GROWL, 0.5f);
			game.broadcast("§8-------------------------------");
			game.broadcast(Lang.BC_GAME_START.get());
			game.broadcast("§8-------------------------------");
				
			for (World w : game.getWorlds()) {
				w.setTime(23500);
			}
				
			//START
			new GameTask(game).launch();
			return;
		}
			
		if (timer <= 10) {
			game.playSound(Sound.SUCCESSFUL_HIT, 0.6f);
			game.title(color[timer - 1] + timer, 5, 20, 0);
		}
	}
	
	
	private void generateChunks(Location loc, Integer size) {
		
		if (!loc.getChunk().isLoaded()) loc.getChunk().load();
		
		for (int x = -size; x <= size; x++) {
			for (int y = -size; y <= size; y++) {
				
				Chunk c = loc.clone().add(x * 16, 0, y * 16).getChunk();
				if (!c.isLoaded()) c.load(true);
			}
		}
	}
	
	private void spec(UHCPlayer player) {
		player.setState(PlayerState.SPECTATOR);
		
		if (player.hasTeam()) player.leaveTeam();
		
		if (player.isConnected()) {
			Player p = player.getPlayer();
			
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(new Location(game.getMainWorld().getMainWorld(), 0, 150, 0));
		}
	}

	@Override
	public void stop() {
		cancel();
		game.setState(GameState.WAITING);
	}

	@Override
	public Integer getTime() {
		return timer;
	}

}

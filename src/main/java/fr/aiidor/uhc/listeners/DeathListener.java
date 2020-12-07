package fr.aiidor.uhc.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.UHCPlayerDeathEvent;
import fr.aiidor.uhc.scenarios.ItemScenario;
import fr.aiidor.uhc.scenarios.ItemScenario.GiveTime;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.utils.Teleportation;

public class DeathListener implements Listener {
	
	private static HashMap<DamageCause, List<String>> reasons;
	
	public DeathListener() {
		reasons = new HashMap<EntityDamageEvent.DamageCause, List<String>>();
		
		for (DamageCause cause : DamageCause.values()) {
			
			JSONObject obj = UHCFile.LANG.getJSONObject("announce/death/reasons/" + cause.name());
			
			if (obj != null) {
				Integer i = 0;
				
				List<String> messages = new ArrayList<String>();
				
				while (obj.get(i.toString()) != null) {
					messages.add((String) obj.get(i.toString()));
					reasons.put(cause, messages);
					i++;
				}
			}
		}
		
		JSONObject obj = UHCFile.LANG.getJSONObject("announce/death/reasons/null");
		
		Integer i = 0;
		
		List<String> messages = new ArrayList<String>();
		
		while (obj.get(i.toString()) != null) {
			messages.add((String) obj.get(i.toString()));
			reasons.put(null, messages);
			i++;
		}
	}
	
	@EventHandler
	public void entityDeathEvent(EntityDeathEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		LivingEntity ent = e.getEntity();
		
		Player killer = ent.getKiller();
		
		//CUTCLEAN
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (e.getEntityType() == EntityType.ZOMBIE && ScenariosManager.BETA_ZOMBIE.isActivated()) {
			int d = new Random().nextInt(2);
			if (d > 0) e.getDrops().add(new ItemStack(Material.FEATHER, d));
		}
		
		ItemStack hand = killer != null ? killer.getItemInHand() : null;
		Integer luck = hand != null ? hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) : 0;
		
		if (game.getSettings().uhc_pearls && e.getEntityType() == EntityType.ENDERMAN) {
			e.getDrops().clear();
			
			if (game.getSettings().pearls_drop != 0 && new Random().nextInt(100) <= (game.getSettings().pearls_drop)) {
				Integer amount = luck != 0 ? new Random().nextInt(luck) + 1 : 1;
				e.getDrops().add(new ItemStack(Material.ENDER_PEARL, amount));
			}
		}
		
		if (ScenariosManager.CUTCLEAN.isActivated()) {
			ScenariosManager.CUTCLEAN.heat(e);
		}
		
		if (ScenariosManager.BOMBERS.isActivated()) {
			ScenariosManager.BOMBERS.addDrops(ent.getKiller(), e.getDrops());
		}
		
		if (ScenariosManager.OVIPAROUS.isActivated()) {
			ScenariosManager.OVIPAROUS.addDrops(ent, e.getDrops());
		}
		
		if (ScenariosManager.AIIDOR_HARDCORE.isActivated()) {
			ScenariosManager.AIIDOR_HARDCORE.entityDeathEvent(e);
		}
		
		if (!(ent instanceof Player)) {
			if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyMobs) {
				e.getDrops().clear();
			}
		}
	}
	
	
	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		
		Player player = e.getEntity();
		Player killer = player.getKiller();
		
		if (!game.isStart()) return;
		if (!game.isHere(player.getUniqueId())) return;
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		UHCPlayer k = null;
		if (killer != null && game.isHere(killer.getUniqueId())) k = game.getUHCPlayer(killer.getUniqueId());
		
		UHCPlayerDeathEvent event = new UHCPlayerDeathEvent(p, k, player.getLocation(), new ArrayList<ItemStack>(e.getDrops()), e.getDroppedExp(), getDeathMessage(e, game), game);
		
		e.setKeepInventory(true);
		e.setKeepLevel(true);
		e.setDeathMessage(null);
		e.getDrops().clear();
		e.setNewExp(0);
		e.setNewLevel(0);
		e.setNewTotalExp(0);
		
		player.setHealth(player.getMaxHealth());
		p.setState(PlayerState.DYING);

		Bukkit.getServer().getPluginManager().callEvent(event);
	}
	
	@EventHandler
	public void UHCPlayerDeathEvent(UHCPlayerDeathEvent e) {
		
		UHCPlayer p = e.getUHCPlayer();
		UHCPlayer k = e.getKiller();
		Game game = e.getGame();
		
		e.getDrops().addAll(game.getSettings().getDeathItems());
		
		if (!e.isCancelled()) {
			
			p.setState(PlayerState.DEAD);
			p.saveDeath();
			
			if (game.getSettings().show_death_message) {
				game.broadcast(e.getDeathMessage());
				
				game.playSound(e.getDeathSound(), game.getSettings().death_sound_volume);
			}

			
			//SCENARIOS ----------------------------------------------
			if (e.hasKiller()) {
				k.addKill();
				
				if (k.isConnected()) {
					
					if (ScenariosManager.NO_CLEAN_UP.isActivated()) {
						ScenariosManager.NO_CLEAN_UP.healPlayer(k);
					}
					
					if (ScenariosManager.ASSASSINS.isActivated()) {
						if (!ScenariosManager.ASSASSINS.canDrop(p, k)) {
							e.getDrops().clear();
						}
					}
				}
				
				if (ScenariosManager.WEBCAGE.isActivated()) {
					ScenariosManager.WEBCAGE.generate(e.getLocation());
				}
				
				if (ScenariosManager.BEST_PVE.isActivated()) {
					ScenariosManager.BEST_PVE.add(k);
				}
			}
			
			if (ScenariosManager.ENCHANTED_DEATH.isActivated()) {
				e.getDrops().add(new ItemStack(Material.ENCHANTMENT_TABLE));
			}
			
			if (ScenariosManager.BOOKCEPTION.isActivated()) {
				e.getDrops().add(ScenariosManager.BOOKCEPTION.getRandomBook(p.getName()));
			}
			
			if (ScenariosManager.ASSASSINS.isActivated()) {
				ScenariosManager.ASSASSINS.removeTarget(p);
				ScenariosManager.ASSASSINS.checkTargets(p);
			}
			
			if (ScenariosManager.GOOD_GAME.isActivated()) {
				ScenariosManager.GOOD_GAME.death = true;
			}
			
			if (ScenariosManager.ASSAULT_AND_BATTERY.isActivated()) {
				ScenariosManager.ASSAULT_AND_BATTERY.removeTeamRestrictions(p);
			}
			
			if (ScenariosManager.SUPER_HEROES.isActivated()) {
				ScenariosManager.SUPER_HEROES.removePower(p);
			}
			
			for (ItemScenario s : UHC.getInstance().getScenarioManager().getItemScenarios(GiveTime.DEATH)) {
				if (s.isActivated()) e.getDrops().addAll(s.getItems());
			}
			

			Boolean up = false;
			
			if (ScenariosManager.TIME_BOMB.isActivated() && !e.getDrops().isEmpty()) {
				if (ScenariosManager.TIME_BOMB.createChest(e.getDrops(), e.getLocation())) {
					e.getDrops().clear();
					up = true;
				}
			} 
			
			if (ScenariosManager.FENCE_HEAD.isActivated()) {
				ScenariosManager.FENCE_HEAD.generate(e.getLocation(), p.getName(), up);
			}
			
            if (ScenariosManager.REINCARNATION.isActivated()) {
            	ScenariosManager.REINCARNATION.spawnRandomEntity(e.getLocation());
            }
            
            if (ScenariosManager.ENDER_REPLACEMENT.isActivated()) {
            	for (UHCPlayer pl : game.getPlayingPlayers()) {
            		pl.getPlayer().teleport(e.getLocation());
            	}
            }
			
			if (game.getSettings().death_lightning) {
				e.getLocation().getWorld().strikeLightningEffect(e.getLocation());
			}
			
			//----------------------------------------------
			if (!e.getDrops().isEmpty()) {
				for (ItemStack item : e.getDrops()) {
					if (item != null && item.getType() != Material.AIR) e.getLocation().getWorld().dropItemNaturally(e.getLocation(), item);
				}
			}

			if (e.getDroppedExp() > 0) {
				ExperienceOrb orb = (ExperienceOrb) e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.EXPERIENCE_ORB);
				orb.setExperience(e.getDroppedExp());
			}
			
			p.reset();
			
		} else {
			Player player = p.getPlayer();
			
			p.setState(PlayerState.ALIVE);
			player.setHealth(player.getMaxHealth());
			
	    	if (e.getCancelTeleport()) new Teleportation(p).revive(300).teleport();
	    	else player.teleport(e.getLocation());
		}
	}
	
	public static String getDeathMessage(PlayerDeathEvent e, Game game) {
		
		Player player = e.getEntity();
		String reason = e.getDeathMessage().substring(player.getName().length() + 1);
		
		if (getReason(player.getLastDamageCause()) != null) reason = getReason(player.getLastDamageCause());
		
		if (!game.getSettings().show_death_reason) return Lang.BC_DEATH_HIDE.get().replace(LangTag.PLAYER_NAME.toString(), getPlayerName(player, game));
		
		//KILLER
		if (player.getKiller() != null) {
			return Lang.BC_DEATH_KILL.get()
					.replace(LangTag.PLAYER_NAME.toString(), getPlayerName(player, game))
					.replace(LangTag.KILLER_NAME.toString(), getPlayerName(player.getKiller(), game));
		}
			
		//REASON
		return Lang.BC_DEATH.get()
				.replace(LangTag.PLAYER_NAME.toString(), getPlayerName(player, game))
				.replace(LangTag.REASON.toString(), reason);
	}
	
	private static String getPlayerName(Player player, Game game) {
		
		if (game.isHere(player.getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			return p.getDisplayName();
		}
		
		return player.getName();
	}
	
	private static String getReason(EntityDamageEvent e) {
		
		if (e == null) {
			List<String> messages = reasons.get(null);
			
			if (messages.isEmpty()) return null;
			return messages.get(new Random().nextInt(messages.size()));
		}
		
		if (reasons.containsKey(e.getCause())) {
			List<String> messages = reasons.get(e.getCause());
			
			if (messages.isEmpty()) return null;
			return messages.get(new Random().nextInt(messages.size()));
		}
		
		return null;
	}
	
	@EventHandler
	public void entityRespawnEvent(PlayerRespawnEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (game.isHere(player.getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			if (p.isSpec() && game.isStart()) {
				
				p.reset();
				
				//!HASTEAM && canSPec
				
				if (player.getKiller() != null) {
					Player killer = player.getKiller();
					e.setRespawnLocation(killer.getLocation());
					
				} else {
					e.setRespawnLocation(e.getPlayer().getLocation());
				}
			}
		}
	}
}

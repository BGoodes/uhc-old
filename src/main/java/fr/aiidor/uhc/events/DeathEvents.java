package fr.aiidor.uhc.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.tools.Cage;

public class DeathEvents implements Listener {
	
	private HashMap<DamageCause, List<String>> reasons;
	
	public DeathEvents() {
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
		
		//CUTCLEAN
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (e.getEntityType() == EntityType.ZOMBIE && ScenariosManager.BETA_ZOMBIE.isActivated()) {
			int d = new Random().nextInt(2);
			if (d > 0) e.getDrops().add(new ItemStack(Material.FEATHER, d));
		}
		
		if (ScenariosManager.CUTCLEAN.isActivated()) {
			ScenariosManager.CUTCLEAN.heat(e);
		}
		
		if (!(e.getEntity() instanceof Player)) {
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
		String reason = e.getDeathMessage().replace(player.getName() + " ", "");
		
		if (!game.isHere(player.getUniqueId())) return;
		if (!game.isStart()) return;
		
		e.getDrops().addAll(game.getSettings().getDeathItems());
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (ScenariosManager.BLEEDING_SWEETS.isActivated()) {
			e.getDrops().addAll(ScenariosManager.BLEEDING_SWEETS.items);
		}
		
		if (ScenariosManager.ENCHANTED_DEATH.isActivated()) {
			e.getDrops().add(new ItemStack(Material.ENCHANTMENT_TABLE));
		}
		
		if (ScenariosManager.BOOKCEPTION.isActivated()) {
			e.getDrops().add(ScenariosManager.BOOKCEPTION.getRandomBook(p.getName()));
		}

		p.setState(PlayerState.DEAD);
		
		if (player.getKiller() != null) {
				
			Player killer = player.getKiller();
			reason = reason.replace(killer.getName() + " ", "");
				
			if (game.isHere(killer.getUniqueId())) {
				UHCPlayer k = game.getUHCPlayer(killer.getUniqueId());
				k.addKill();
				
				if (ScenariosManager.NO_CLEAN_UP.isActivated()) {
					ScenariosManager.NO_CLEAN_UP.healPlayer(k);
				}
				
				//SCENARIOS
				if (ScenariosManager.ASSASSINS.isActivated()) {
					
					if (!ScenariosManager.ASSASSINS.canDrop(p, k)) {
						e.getDrops().clear();
					}
					
					ScenariosManager.ASSASSINS.removeTarget(p);
					ScenariosManager.ASSASSINS.checkTargets(p);
				}
				
				if (ScenariosManager.WEBCAGE.isActivated()) {
					new Cage(player.getLocation().subtract(0, 1, 0), 3, 6, 2,  Material.WEB, Material.WEB, Material.WEB, false).create();
				}
			}
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
		
		//MESSAGE
		p.reset();
		e.setDeathMessage(getDeathMessage(e, game));
	}
	
	private String getDeathMessage(PlayerDeathEvent e, Game game) {
		
		Player player = e.getEntity();
		String reason = e.getDeathMessage().substring(player.getName().length() + 1);
		
		if (getReason(player.getLastDamageCause()) != null) reason = getReason(player.getLastDamageCause());
		
		if (!game.getSettings().show_death_message) return "";
		
		game.playSound(game.getSettings().death_sound, game.getSettings().death_sound_volume);
		
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
	
	private String getPlayerName(Player player, Game game) {
		
		if (game.isHere(player.getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			if (p.hasTeam()) return p.getTeam().getPrefix() + p.getName();
			else return p.getName();
		}
		
		return player.getName();
	}
	
	private String getReason(EntityDamageEvent e) {
		
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
	public void entityDeathEvent(PlayerRespawnEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (game.isHere(player.getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			if (p.isSpec() && game.isStart()) {
				
				p.reset();
				
				if (player.getKiller() != null) {
					Player killer = player.getKiller();
					e.setRespawnLocation(killer.getLocation());
					
					//!HASTEAM && canSPec
					
				} else {
					
					//TP AT LAST LOCATION
					e.setRespawnLocation(new Location(game.getMainWorld().getMainWorld(), 0, 150, 0));
				}
				
			}
		}
	}
}

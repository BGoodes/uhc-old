package fr.aiidor.uhc.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;

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
	}
	
	@EventHandler
	public void entityDeathEvent(EntityDeathEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		//CUTCLEAN
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (ScenariosManager.CUTCLEAN.isActivated(game)) {
			ScenariosManager.CUTCLEAN.heat(e);
		}
		
		if (!(e.getEntity() instanceof Player)) {
			if (ScenariosManager.STINGY_WORLD.isActivated(game) && ScenariosManager.STINGY_WORLD.stingyMobs) {
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
		
		if (ScenariosManager.BLEEDING_SWEETS.isActivated(game)) {
			e.getDrops().addAll(ScenariosManager.BLEEDING_SWEETS.items);
		}
		
		if (ScenariosManager.ENCHANTED_DEATH.isActivated(game)) {
			e.getDrops().add(new ItemStack(Material.ENCHANTMENT_TABLE));
		}
		
		if (game.isHere(player.getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			p.setState(PlayerState.DEAD);
			
			if (player.getKiller() != null) {
				
				Player killer = player.getKiller();
				reason = reason.replace(killer.getName() + " ", "");
				
				if (game.isHere(killer.getUniqueId())) {
					UHCPlayer k = game.getUHCPlayer(killer.getUniqueId());
					k.addKill();
					
					if (ScenariosManager.NO_CLEAN_UP.isActivated(game)) {
						ScenariosManager.NO_CLEAN_UP.healPlayer(k);
					}
				}
			}
		}
		
		//SCENARIOS
		if (ScenariosManager.ASSASSINS.isActivated(game)) {
			if (!ScenariosManager.ASSASSINS.canDrop(player)) {
				e.getDrops().clear();
			}
		}
		
		//MESSAGE
		e.setDeathMessage(getDeathMessage(e, game));
	}
	
	private String getDeathMessage(PlayerDeathEvent e, Game game) {
		
		Player player = e.getEntity();
		String reason = e.getDeathMessage().substring(player.getName().length() + 1);
		
		if (getReason(player.getLastDamageCause()) != null) reason = getReason(player.getLastDamageCause());
		
		if (!game.getSettings().showDeathMessage) return "";
		
		game.playSound(game.getSettings().DeathSound, game.getSettings().DeathSound_Volume);
		
		if (!game.getSettings().showDeathReason) Lang.BC_DEATH_HIDE.get().replace(LangTag.PLAYER_NAME.toString(), getPlayerName(player, game));
		
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
		DamageCause cause = e.getCause();
		
		if (reasons.containsKey(cause)) {
			List<String> messages = reasons.get(cause);
			
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
			
			if (p.getState() == PlayerState.DEAD && game.getState() == GameState.GAME) {
				
				player.setGameMode(GameMode.SPECTATOR);
				
				if (player.getKiller() != null) {
					Player killer = player.getKiller();
					e.setRespawnLocation(killer.getLocation());
					
					//!HASTEAM && canSPec
					player.setSpectatorTarget(killer);
					
				} else {
					
					//TP AT LAST LOCATION
					e.setRespawnLocation(new Location(game.getOverWorld(), 0, 150, 0));
				}
				
			}
		}
	}
}

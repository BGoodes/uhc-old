package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.utils.ItemBuilder;

public class Assassins extends Scenario {
	
	private HashMap<UHCPlayer, UHCPlayer> targets;
	private HashMap<UHCPlayer, Integer> distance;
	
	public Boolean compass;
	private BukkitRunnable task;
	private GuiBuilder gui;
	
	public Assassins(ScenariosManager manager) {
		super(manager);
		
		targets = new HashMap<UHCPlayer, UHCPlayer>();
		distance = new HashMap<UHCPlayer, Integer>();
		compass = false;
		
		gui = new GuiBuilder() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				e.setCancelled(true);
				
				if (event.getGame().isPvpTime()) {
					event.getPlayer().sendMessage(Lang.ST_ERROR_OPTION_PVP.get());
					event.getPlayer().closeInventory();
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 0) {
					compass = !compass;
					update();
					return;
				}
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
			}
			
			@Override
			public String getTitle() {
				return getName();
			}
			
			@Override
			public String[][] getMatrix() {

				String[][] items = {
						{"P", " ", " ", " ", " ", " ", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				
				dictionnary.put("P", getConfigItem(Material.COMPASS, Lang.TRACKER.get(), compass));
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		if (e.getGame().isPvpTime()) {
			e.setCancelled(true);
			
			if (e.getPlayer() != null) {
				e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_PVP.get());
				e.getPlayer().closeInventory();
			}
		}
		
		super.changeStateEvent(e);
	}
	
	@Override
	public String getID() {
		return "assassins";
	}

	@Override
	public Material getIcon() {
		return Material.IRON_SWORD;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}
	
	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.TRACKER)) return false;
		if (scenario.equals(ScenariosManager.KILL_THE_WITCH)) return false;
		return true;
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	public void checkTargets(UHCPlayer player) {
		
		Game game = UHC.getInstance().getGameManager().getGame();
		
		for (UHCPlayer p : game.getAlivePlayers()) {
			if (hasTarget(p) && getTarget(p).equals(player)) {
				if (p.isConnected()) p.getPlayer().sendMessage(Lang.TARGET_DEATH_MSG.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
				setTarget(p);
			}
		}
	}
	
	public Boolean hasTarget(UHCPlayer player) {
		return targets.containsKey(player);
	}
	
	public Boolean hasTarget(UHCTeam team, UHCPlayer target) {
		
		for (UHCPlayer p : team.getAlivePlayers()) {
			if (hasTarget(p) && getTarget(p).equals(target)) return true;
		} 
		
		return false;
	}
	
	public UHCPlayer getTarget(UHCPlayer player) {
		return targets.get(player);
	}	
	
	public Integer getDistance(UHCPlayer player) {
		return distance.get(player);
	}
	
	public void setDistance() {
		for (Entry<UHCPlayer, UHCPlayer> map : targets.entrySet()) {
			distance.put(map.getKey(), (int) map.getKey().distance(map.getValue()));
		}
	}
	
	public void start(Game game) {
		for (UHCPlayer p : game.getAlivePlayers()) {
			if (setTarget(p) && compass) {
				p.giveItem(new ItemBuilder(Material.COMPASS, Lang.TRACKER.get()).getItem());
			}
		}
		
		task = new BukkitRunnable() {
			
			@Override
			public void run() {
				for (UHCPlayer p : targets.keySet()) {
					UHCPlayer t = targets.get(p);
					if (p.isConnected() && t.isConnected()) {
						p.getPlayer().setCompassTarget(t.getPlayer().getLocation());
					}
				}
			}
		};
		
		task.runTaskTimer(UHC.getInstance(), 0, 5);
	}
	
	public Boolean setTarget(UHCPlayer player) {
		Game game = UHC.getInstance().getGameManager().getGame();
		if (game.getAlivePlayers().size() > 1 && (!game.hasTeam() || game.getAliveTeams().size() > 1)) {
			
			UHCPlayer t = player;
			while (t.equals(player) || (player.hasTeam() && player.getTeam().isInTeam(t))) {
				
				if (game.getAlivePlayers().size() <= 1 || (game.hasTeam() && game.getAliveTeams().size() <= 1)) {
					setTarget(player, null);
					break;
				}
				
				t = game.getRandomPlayer();
			}
			
			setTarget(player, t);
			return true;
			
		}
		else {
			setTarget(player, null);
			return false;
		}
	}
	
	public void removeTarget(UHCPlayer player) {
		if (targets.containsKey(player)) targets.remove(player);
	}
	
	public void setTarget(UHCPlayer player, UHCPlayer target) {
		
		if (target != null) {
			
			targets.put(player, target);
			if (player.isConnected()) player.getPlayer().sendMessage(Lang.TARGET_MSG.get().replace(LangTag.PLAYER_NAME.toString(), target.getName()));
			
			
		} else {
			if (hasTarget(player)) removeTarget(player);
			if (player.isConnected()) player.getPlayer().sendMessage(Lang.NO_TARGET_MSG.get());
		}
	}
	
	public Boolean canDrop(UHCPlayer player, UHCPlayer killer) {
		
		if (hasTarget(player) && hasTarget(killer)) {
				
			if (getTarget(killer).equals(player) || getTarget(player).equals(killer)) {
				return true;
			}
				
			if ((player.hasTeam() && hasTarget(player.getTeam(), killer)) || (killer.hasTeam() && hasTarget(killer.getTeam(), player))) {
				return true;
			}
				
			Bukkit.getScheduler().runTaskLater(UHC.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (killer.isConnected()) killer.getPlayer().sendMessage(Lang.NO_STUFF_DROP.get().replace(LangTag.PLAYER_NAME.toString(), player.getName()));
				}
			}, 1);
				
			return false;
		}
		
		return true;
	}
	
	@Override
	public void reload() {
		
		if (task != null) task.cancel();
		task = null;
		targets = new HashMap<UHCPlayer, UHCPlayer>();
		distance = new HashMap<UHCPlayer, Integer>();
	}
}

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
import fr.aiidor.uhc.tools.ItemBuilder;

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
			e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_PVP.get());
			e.getPlayer().closeInventory();
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
	
	public Boolean hasTarget(UHCPlayer p) {
		return targets.containsKey(p);
	}
	
	public Boolean hasTarget(UHCTeam t, UHCPlayer target) {
		for (UHCPlayer p : t.getAlivePlayers()) {
			if (hasTarget(p) && getTarget(p).equals(target)) return true;
		} 
		
		return false;
	}
	
	public UHCPlayer getTarget(UHCPlayer p) {
		return targets.get(p);
	}	
	
	public Integer getDistance(UHCPlayer p) {
		return distance.get(p);
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
	
	public void removeTarget(UHCPlayer p) {
		if (targets.containsKey(p)) targets.remove(p);
	}
	
	public void setTarget(UHCPlayer p, UHCPlayer t) {
		
		if (t != null) {
			
			targets.put(p, t);
			if (p.isConnected()) p.getPlayer().sendMessage(Lang.TARGET_MSG.get().replace(LangTag.PLAYER_NAME.toString(), t.getName()));
			
			
		} else {
			if (hasTarget(p)) removeTarget(p);
			if (p.isConnected()) p.getPlayer().sendMessage(Lang.NO_TARGET_MSG.get());
		}
	}
	
	public Boolean canDrop(UHCPlayer p, UHCPlayer k) {
		
		if (hasTarget(p) && hasTarget(k)) {
				
			if (getTarget(k).equals(p) || getTarget(p).equals(k)) {
				return true;
			}
				
			if ((p.hasTeam() && hasTarget(p.getTeam(), k)) || (k.hasTeam() && hasTarget(k.getTeam(), p))) {
				return true;
			}
				
			Bukkit.getScheduler().runTaskLater(UHC.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (k.isConnected()) k.getPlayer().sendMessage(Lang.NO_STUFF_DROP.get().replace(LangTag.PLAYER_NAME.toString(), p.getName()));
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

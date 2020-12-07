package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class KillTheWitch extends Scenario {
	
	private UHCPlayer witch;
	private Gui gui;
	
	public Integer time = 10;
	
	public KillTheWitch(ScenariosManager manager) {
		super(manager);
		
		witch = null;
		gui = new GuiBuilder() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				e.setCancelled(true);
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 2) {
					
					time -= 5;
					if (time < 1) time = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				
				if (e.getSlot() == 3) {
					
					time--;
					if (time < 1) time = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					time++;
					if (time > 60) time = 60;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 6) {
					
					time += 5;
					if (time > 60) time = 60;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
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
						{" ", " ", "--", "-", "F", "+", "++", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				String name = Lang.SCENARIO_PERIOD.get().replace(LangTag.VALUE.toString(), time.toString());
				
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.WATCH, name).getItem()); 
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "§c-1").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "§a+1").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("--",  new ItemBuilder(Material.STONE_BUTTON, "§c-5").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("++",  new ItemBuilder(Material.STONE_BUTTON, "§a+5").setLore(Arrays.asList(name)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add(Lang.SCENARIO_PERIOD.get().replace(LangTag.VALUE.toString(), time.toString()));
		
		return lore;
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public String getID() {
		return "kill-the-witch";
	}

	@Override
	public Material getIcon() {
		return Material.COMPASS;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.TRACKER)) return false;
		if (scenario.equals(ScenariosManager.ASSASSINS)) return false;
		return true;
	}
	
	public void findWitch(Game game) {
		
		witch = game.getRandomPlayer();
		
		if (witch != null) {
			game.broadcast(Lang.WITCH_ANNOUNCE.get().replace(LangTag.VALUE.toString(), witch.getDisplayName()));
			witch.giveItem(new ItemStack(Material.GOLDEN_APPLE));
		}
	}
	
	public void track(UHCPlayer p) {
		
		if (p.isConnected()) {
			Player player = p.getPlayer();
			
			if (witch != null) {
				if (witch.isConnected()) player.setCompassTarget(witch.getPlayer().getLocation());
			}
		}
	}
	
	@Override
	public void reload() {
		witch = null;
	}
}

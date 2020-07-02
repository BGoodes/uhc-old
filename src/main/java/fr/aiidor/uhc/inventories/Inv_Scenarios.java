package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.comparators.NameComparator;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.scenarios.Scenario;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Scenarios extends Gui {

	private final HashMap<String, ItemStack> dictionary;
	
	private final Integer[] scenarios_slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
	
	private final String[][] matrix = {
			{"G", "G", "L", "L", "L", "L", "L", "G", "G"},
			{"G", " ", " ", " ", " ", " ", " ", " ", "G"}, //10, 11, 12, 13, 14, 15, 16
			{"L", " ", " ", " ", " ", " ", " ", " ", "L"}, //19, 20, 21, 22, 23, 24, 25
			{"L", " ", " ", " ", " ", " ", " ", " ", "L"}, //28, 29, 30, 31, 32, 33, 34
			{"G", " ", " ", " ", " ", " ", " ", " ", "G"}, //37, 38, 39, 40, 41, 42, 43
			{"G", "G", "L", "L", "L", "L", "L", "G", "X"},        // size : 28
	};
	
	//CLAY : GREEN = 13 / RED = 14
	
	public Inv_Scenarios() {
		
		dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getBackIcon());
		
		dictionary.put("L", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 5, " ").getItem());
		dictionary.put("G", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 13, " ").getItem());
	}
	
	@Override
	public Boolean titleIsDynamic() {
		return true;
	}
	
	@Override
	public String getTitle() {
		return Lang.INV_SCENARIOS.get();
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		ItemStack clicked = event.getItemClicked();
		
		if (e.getSlot() == 53) {
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			playClickSound(event.getPlayer());
			return;
		}
		
		Inventory inv = event.getInventory();
		
		if (clicked.getType() == Material.STAINED_CLAY && clicked.getItemMeta().hasDisplayName()) {
			
			String displayName = clicked.getItemMeta().getDisplayName();
			
			for (Scenario scenario : UHC.getInstance().getScenarioManager().getScenarios()) {
				
				if (scenario.getName().equals(displayName.subSequence(0, displayName.length() - 4))) {
					
					if (e.getClick() == ClickType.RIGHT) {
						//SETTINGS
						if (scenario.hasSettings()) {
							playClickSound(event.getPlayer());
							event.getPlayer().openInventory(scenario.getSettings().getInventory());
						}
						return;
					}
					
					ChangeScenarioStateEvent scenario_event = new ChangeScenarioStateEvent(scenario, !event.getGame().getSettings().IsActivated(scenario), event.getPlayer(), event.getGame());
					scenario.changeStateEvent(scenario_event);
					
					if (scenario_event.isCancelled()) return;
					
					event.getGame().getSettings().setActivated(scenario, !event.getGame().getSettings().IsActivated(scenario));
					playClickSound(event.getPlayer());
					update();
					return;
				}
			}
		}
		
		if (e.getSlot() == 6) {
			if (clicked.getType() == Material.NAME_TAG) {
				
				if (e.getClick() == ClickType.LEFT) inv.setItem(6, getCategoryIcon(getNextCategory(getCategory(inv))));
				else if (e.getClick() == ClickType.RIGHT) inv.setItem(6, getCategoryIcon(getLastCategory(getCategory(inv))));
				
				playClickSound(event.getPlayer());
				event.getPlayer().openInventory(getInventory(1, inv));
			}
		}

		if (clicked.getType() == Material.SIGN) {
			if (e.getSlot() == 44) {
				playClickSound(event.getPlayer());
				event.getPlayer().openInventory(getInventory(getPage(inv) + 1, inv));
			}
			
			if (e.getSlot() == 35) {
				playClickSound(event.getPlayer());
				event.getPlayer().openInventory(getInventory(getPage(inv) - 1, inv));
			}
		}

	}
	
	@Override
	public void update() {
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getOpenInventory() != null) {
					if (isSameInventory(p.getOpenInventory().getTopInventory())) {
						
						Inventory inv = p.getOpenInventory().getTopInventory();
						
						p.openInventory(getInventory(getPage(inv), inv));
					}
				}
			}
	}
	
	public Category getCategory(Inventory inv) {
		if (inv.getItem(6) != null && inv.getItem(6).getType() == Material.NAME_TAG) {
			String value = inv.getItem(6).getItemMeta().getDisplayName().substring(Lang.INV_CATEGORY.get().length());
			return Category.find(value);
		}
		
		return Category.ALL;
	}
	
	public Category getNextCategory(Category cat) {
		List<Category> categories = new ArrayList<Category>();
		
		for (Category category : Category.values()) {
			categories.add(category);
		}
		
		Integer index = categories.indexOf(cat) + 1;
		
		if (index >= categories.size()) {
			index = 0;
		}
		
		return categories.get(index);
	}
	
	public Category getLastCategory(Category cat) {
		List<Category> categories = new ArrayList<Category>();
		
		for (Category category : Category.values()) {
			categories.add(category);
		}
		
		Integer index = categories.indexOf(cat) - 1;
		
		if (index < 0) {
			index = categories.size() - 1;
		}
		
		return categories.get(index);
	}
	
	public Integer getPage(Inventory inv) {
		return Integer.valueOf(getPageValues(inv)[0]);
	}
	
	public Integer getMaxPage(Inventory inv) {
		return Integer.valueOf(getPageValues(inv)[1]);
	}
	
	private String[] getPageValues(Inventory inv) {
		return inv.getTitle().substring(Lang.INV_SCENARIOS.get().length() + 4).replace(")", "").split("/");
	}
	
	@Override
	public Inventory getInventory() {
		return getInventory(1, null);
	}
	
	public Inventory getInventory(Integer page, Inventory last) {
		
		if (page < 1) page = 1;
		
		Category cat = Category.ALL;
		
		if (last != null) {
			cat = getCategory(last);
		}
		
		if (!UHC.getInstance().getGameManager().hasGame()) return Bukkit.createInventory(null, 54, getTitle());
		
		ScenariosManager sm = UHC.getInstance().getScenarioManager();
		List<Scenario> scenarios = sm.getScenarios(new NameComparator(true), cat);
		
		Integer max_pages = (scenarios.size() / 28) + 1;
		
		Inventory inv = Bukkit.createInventory(null, 54, getTitle() + "§e (" + page + "/" + max_pages + ")");
		
		for (int c = 0; c != 6; c++) {
			for (int l = 0; l != 9; l++) {
				String s = matrix[c][l];
				
				if (dictionary.containsKey(s)) {
					inv.setItem(c * 9 + l, dictionary.get(s));
				}
			}
		}
		
		//CATEGORIES
		inv.setItem(6, getCategoryIcon(cat));
		
		//SCENARIOS
		Integer index = (page - 1) * 28;
		
		for (int i = 0; i < scenarios_slots.length; i++) {
			int slot = scenarios_slots[i];
			
			if (scenarios.size() > index + i) {
				
				Scenario s = scenarios.get(index + i);
				inv.setItem(slot, s.getScenarioIcon(false, true, true));
				
			} else {
				break;
			}

		}

		
		//SIGNS
		if (page < max_pages) {
			inv.setItem(44, new ItemBuilder(Material.SIGN, Lang.INV_NEXT_PAGE.get()).getItem());
		}
		
		if (page > 1) {
			inv.setItem(35, new ItemBuilder(Material.SIGN, Lang.INV_LAST_PAGE.get()).getItem());
		}
		
		return inv;
	}
	
	private ItemStack getCategoryIcon(Category cat) {
		return new ItemBuilder(Material.NAME_TAG, Lang.INV_CATEGORY.get() + cat.getDisplayName()).getItem();
	}
}

package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.scenarios.Scenario;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Scenarios_List extends Gui {

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
	
	public Inv_Scenarios_List() {
		
		dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getExitIcon());
		
		dictionary.put("L", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 5, " ").getItem());
		dictionary.put("G", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 13, " ").getItem());
	}
	
	@Override
	public Boolean titleIsDynamic() {
		return true;
	}
	
	@Override
	public String getTitle() {
		return Lang.INV_SCENARIO_LIST.get();
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		ItemStack clicked = event.getItemClicked();
		Inventory inv = event.getInventory();
		
		if (e.getSlot() == 53) {
			event.getPlayer().closeInventory();
			playClickSound(event.getPlayer());
			return;
		}
		
		if (clicked.getType() == Material.SIGN) {
			if (e.getSlot() == 44) {
				playClickSound(event.getPlayer());
				event.getPlayer().openInventory(getInventory(getPage(inv) + 1));
			}
			
			if (e.getSlot() == 35) {
				playClickSound(event.getPlayer());
				event.getPlayer().openInventory(getInventory(getPage(inv) - 1));
			}
		}

	}
	
	public Integer getPage(Inventory inv) {
		return Integer.valueOf(getPageValues(inv)[0]);
	}
	
	public Integer getMaxPage(Inventory inv) {
		return Integer.valueOf(getPageValues(inv)[1]);
	}
	
	private String[] getPageValues(Inventory inv) {
		return inv.getTitle().substring(getTitle().length() + 4).replace(")", "").split("/");
	}
	
	@Override
	public Inventory getInventory() {
		return getInventory(1);
	}
	
	public Inventory getInventory(Integer page) {
		
		if (page < 1) page = 1;
		
		if (!UHC.getInstance().getGameManager().hasGame()) return Bukkit.createInventory(null, 54, getTitle());
		
		Game game = UHC.getInstance().getGameManager().getGame();
		GameSettings gs = game.getSettings();
	
		Integer max_pages = (gs.getActivatedScenarios().size() / 28) + 1;
		
		Inventory inv = Bukkit.createInventory(null, 54, getTitle() + "Â§e (" + page + "/" + max_pages + ")");
		
		for (int c = 0; c != 6; c++) {
			for (int l = 0; l != 9; l++) {
				String s = matrix[c][l];
				
				if (dictionary.containsKey(s)) {
					inv.setItem(c * 9 + l, dictionary.get(s));
				}
			}
		}
		
		
		//TEAMS
		Integer index = (page - 1) * 28;
		
		for (int i = 0; i < scenarios_slots.length; i++) {
			int slot = scenarios_slots[i];
			
			if (gs.getActivatedScenarios().size() > index + i) {
				
				Scenario s = gs.getActivatedScenarios().get(index + i);
				inv.setItem(slot, s.getScenarioIcon(true, false, false));

				
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
}

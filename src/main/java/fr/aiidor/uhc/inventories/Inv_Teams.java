package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Teams extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_TEAMS.get();
	}

	@Override
	public Boolean titleIsDynamic() {
		return false;
	}
	
	@Override
	public InventoryHolder getHolder() {
		return null;
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"S", "E", " ", " ", " ", " ", " ", "T", "X"},
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getBackIcon());
		
		dictionary.put("S", new ItemBuilder(Material.SIGN, Lang.INV_TEAM_SIZE.get()).getItem());
		dictionary.put("E", new ItemBuilder(Material.ANVIL, Lang.INV_TEAM_NUMBER.get()).getItem());
		dictionary.put("T", new ItemBuilder(Material.BOOK_AND_QUILL, Lang.INV_TEAM_TYPE.get()).getItem());
		
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			return;
		}
		
		if (e.getSlot() == 0) {
			event.getPlayer().openInventory(GuiManager.INV_TEAM_SIZE.getInventory());
			playClickSound(event.getPlayer());
			return;
		}
		
		if (e.getSlot() == 1) {
			event.getPlayer().openInventory(GuiManager.INV_TEAM_NUMBER.getInventory());
			playClickSound(event.getPlayer());
			return;
		}
		
		if (e.getSlot() == 7) {
			playClickSound(event.getPlayer());
			event.getGame().getSettings().team_type = getNextTeamType(event.getGame().getSettings().team_type);
			update();
			return;
		}
	}
	
	private TeamType getNextTeamType(TeamType type) {
		List<TeamType> values = new ArrayList<TeamType>();
		
		for (TeamType value : TeamType.values()) {
			values.add(value);
		}
		
		Integer index = values.indexOf(type) + 1;
		
		if (index >= values.size()) {
			index = 0;
		}
		
		return values.get(index);
	}
}

package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Stuff extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_STUFF.get();
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
				{"C", "E", " ", " ", " ", " ", "P", "L", "X"},
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getBackIcon());
		
		dictionary.put("C", new ItemBuilder(Material.CHEST, Lang.INV_START_ITEMS.get()).getItem());
		dictionary.put("E", new ItemBuilder(Material.ENDER_CHEST, Lang.INV_DEATH_ITEMS.get()).getItem());
		dictionary.put("P", new ItemBuilder(Material.BREWING_STAND_ITEM, Lang.INV_STUFF_POTIONS.get()).getItem());
		dictionary.put("L", new ItemBuilder(Material.BOOK_AND_QUILL, Lang.INV_STUFF_LIMITS.get()).getItem());
		
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
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_START_STUFF.getInventory());
			return;
		}
		
		if (e.getSlot() == 1) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_DEATH_STUFF.getInventory());
			return;
		}
	}
}

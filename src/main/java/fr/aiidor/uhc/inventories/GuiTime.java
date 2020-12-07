package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiTime extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_TIME.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"I", "P", "W", "E", "A", " ", " ", " ", "X"},
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getBackIcon());
		
		dictionary.put("I", new ItemBuilder(Material.GOLD_CHESTPLATE, Lang.INV_INVINCIBILITY_TIME.get()).getItem());
		dictionary.put("P", new ItemBuilder(Material.GOLD_SWORD, Lang.INV_PVP_TIME.get()).getItem());
		dictionary.put("W", new ItemBuilder(Material.BEACON, Lang.INV_WB_TIME.get()).getItem());
		dictionary.put("E", new ItemBuilder(Material.BOOK, Lang.INV_EP_TIME.get()).getItem());
		dictionary.put("A", new ItemBuilder(Material.BOOK_AND_QUILL, Lang.INV_EP1_TIME.get()).getItem());
		
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 0) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_INVINCIBILITY_TIME.getInventory());
			return;
		}
		
		if (e.getSlot() == 1) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_PVP_TIME.getInventory());
			return;
		}
		
		if (e.getSlot() == 2) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WB_TIME.getInventory());
			return;
		}
		
		if (e.getSlot() == 3) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_EP_TIME.getInventory());
			return;
		}
		
		if (e.getSlot() == 4) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_EP1_TIME.getInventory());
			return;
		}

		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			return;
		}
	}
}

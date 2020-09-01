package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Life_Apples extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_GAPPLES_CONFIG.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"G", "N", "H", " ", " ", " ", " ", " ", "X"}
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			
			dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
			dictionary.put("X", getBackIcon());
			
			dictionary.put("G", new ItemBuilder(Material.GOLDEN_APPLE, Lang.INV_LIFE_GAPPLE.get()).getItem());
			dictionary.put("N", new ItemBuilder(Material.GOLDEN_APPLE, Lang.INV_LIFE_NAPPLE.get(), (byte) 1).getItem());
			dictionary.put("H", new ItemBuilder(Material.SKULL_ITEM, Lang.INV_LIFE_HAPPLE.get()).getItem());
		}
		
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 0) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_GAPPLES.getInventory());
			return;
		}
		
		if (e.getSlot() == 1) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_NAPPLES.getInventory());
			return;
		}
		
		if (e.getSlot() == 2) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_HAPPLES.getInventory());
			return;
		}
		
		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_LIFE.getInventory());
			return;
		}
	}
}

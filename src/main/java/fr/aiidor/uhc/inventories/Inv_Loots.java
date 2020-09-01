package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Loots extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_LOOTS.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"T", " ", " ", " ", " ", " ", " ", " ", " "},
				{"G", "G", "G", "G", "G", "G", "G", "G", "G"},
				{"T1", " ", " ", " ", " ", " ", " ", " ", "X"}
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			Game game = UHC.getInstance().getGameManager().getGame();
			
			dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
			dictionary.put("X", getBackIcon());
			
			dictionary.put("G", getGlass((byte) 5));
			
			dictionary.put("T", new ItemBuilder(Material.SAPLING, Lang.INV_TREES.get()).getItem());
			dictionary.put("T1", getConfigItem(null, Lang.INV_UHC_DROP.get(), game.getSettings().uhc_trees));
			
		}
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		ItemStack clicked = event.getItemClicked();
		Game game = event.getGame();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 0) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_TREES.getInventory());
			return;
		}
		
		if (e.getSlot() == 18 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().uhc_trees = !game.getSettings().uhc_trees;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 26) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			return;
		}
	}
}

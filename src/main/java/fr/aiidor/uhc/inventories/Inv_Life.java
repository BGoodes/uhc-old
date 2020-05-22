package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Life extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_LIFE.get();
	}

	@Override
	public Boolean titleIsDynamic() {
		return false;
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"A", " ", " ", " ", " ", " ", " ", " ", " "},
				{"G", "G", "G", "G", "G", "G", "G", "G", "G"},
				{"A1", " ", " ", " ", " ", " ", " ", " ", "X"}
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
			
			dictionary.put("G", getGlass((byte) 6));
			
			dictionary.put("A", new ItemBuilder(Material.GOLDEN_APPLE, Lang.INV_GAPPLES_CONFIG.get()).getItem());
			dictionary.put("A1", getConfigItem(null, Lang.INV_UHC_GAPPLES.get(), game.getSettings().uhc_apple));
			
		}
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		Game game = event.getGame();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 0) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_GAPPLES.getInventory());
			return;
		}
		
		if (e.getSlot() == 18) {
			game.getSettings().uhc_apple = !game.getSettings().uhc_apple;
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
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
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Death_Stuff extends Gui {

	private final HashMap<String, ItemStack> dictionary;
	private final String[][] matrix = {
			{" ", " ", " ", " ", " ", " ", " ", " ", " "}, 
			{" ", " ", " ", " ", " ", " ", " ", " ", " "}, 
			{" ", " ", " ", " ", " ", " ", " ", " ", " "}, 
			{"G", "G", "G", "G", "G", "G", "G", "T", "X"},      
	};
	
	//CLAY : GREEN = 13 / RED = 14
	
	public Inv_Death_Stuff() {
		
		dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put("X", getBackIcon());
		dictionary.put("T", new ItemBuilder(Material.REDSTONE_TORCH_ON, "§2Sauvegarder ?").getItem());
		dictionary.put("G", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 7, " ").getItem());
	}
	
	@Override
	public Boolean titleIsDynamic() {
		return false;
	}
	
	@Override
	public String getTitle() {
		return Lang.INV_DEATH_ITEMS.get();
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		ItemStack clicked = event.getItemClicked();
		
		if (e.getSlot() >= 26) {
			e.setCancelled(true);
		}
		
		//BACK
		if (e.getSlot() == 35 && clicked.getType() == Material.BARRIER) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_STUFF.getInventory());
			return;
		}
		
		//SAVE
		if (e.getSlot() == 34 && clicked.getType() == Material.REDSTONE_TORCH_ON) {
			playClickSound(event.getPlayer());
			event.getGame().getSettings().saveDeathItems(event.getInventory());
			update();
			return;
		}
	}
	
	@Override
	public Inventory getInventory() {

		if (!UHC.getInstance().getGameManager().hasGame()) return Bukkit.createInventory(null, 36, getTitle());
		Game game = UHC.getInstance().getGameManager().getGame();
		
		Inventory inv = Bukkit.createInventory(null, 36, getTitle());
		
		for (int i = 0; i < game.getSettings().deathItems.size(); i++) {
			inv.setItem(i, game.getSettings().deathItems.get(i));
		}
		
		for (int c = 0; c != 4; c++) {
			for (int l = 0; l != 9; l++) {
				String s = matrix[c][l];
				
				if (dictionary.containsKey(s)) {
					inv.setItem(c * 9 + l, dictionary.get(s));
				}
			}
		}
		
		return inv;
	}

}

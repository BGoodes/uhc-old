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
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Start_Stuff extends Gui {

	private final HashMap<String, ItemStack> dictionary;
	private final String[][] matrix = {
			{" ", " ", " ", " ", " ", " ", " ", " ", " "}, 
			{" ", " ", " ", " ", " ", " ", " ", " ", " "}, 
			{" ", " ", " ", " ", " ", " ", " ", " ", " "}, 
			{" ", " ", " ", " ", " ", " ", " ", " ", " "}, 
			{" ", " ", " ", " ", "G", "G", "G", "T", "X"},      
	};
	
	//CLAY : GREEN = 13 / RED = 14
	
	public Inv_Start_Stuff() {
		
		dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put("X", getBackIcon());
		dictionary.put("T", new ItemBuilder(Material.REDSTONE_TORCH_ON, "§2Sauvegarder ?").getItem());
		dictionary.put("G", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 7, " ").getItem());
	}
	
	@Override
	public String getTitle() {
		return Lang.INV_START_ITEMS.get();
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		ItemStack clicked = event.getItemClicked();
		
		if (e.getSlot() >= 40) {
			e.setCancelled(true);
		}
		
		//BACK
		if (e.getSlot() == 44 && clicked.getType() == Material.BARRIER) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_STUFF.getInventory());
			return;
		}
		
		//SAVE
		if (e.getSlot() == 43 && clicked.getType() == Material.REDSTONE_TORCH_ON) {
			playClickSound(event.getPlayer());
			event.getGame().getSettings().saveStartItems(event.getInventory());
			update();
			return;
		}
	}
	
	@Override
	public Inventory getInventory() {

		if (!UHC.getInstance().getGameManager().hasGame()) return Bukkit.createInventory(null, 54, getTitle());
		Game game = UHC.getInstance().getGameManager().getGame();
		
		Inventory inv = Bukkit.createInventory(null, 45, getTitle());
		
		for (int i = 0; i < game.getSettings().startItems.size(); i++) {
			inv.setItem(i, game.getSettings().startItems.get(i));
		}
		
		for (int c = 0; c != 5; c++) {
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

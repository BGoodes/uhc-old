package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class GuiBuilder extends Gui {
	
	public InventoryHolder getHolder() {
		return null;
	}
	
	public abstract String[][] getMatrix();
	public abstract HashMap<String, ItemStack> getDictionary();
	
	public Integer getLines() {
		return getMatrix().length;
	}
	
	public Integer getColumns() {
		return 9;
	}
	
	public Integer getSize() {
		return getLines() * getColumns();
	}
	
	@Override
	public Inventory getInventory() {
		
		Inventory inv = Bukkit.createInventory(getHolder(), getSize(), getTitle());
		
		for (int c = 0; c != getLines(); c++) {
			for (int l = 0; l != 9; l++) {
				String s = getMatrix()[c][l];
				
				if (getDictionary().containsKey(s)) {
					inv.setItem(c * 9 + l, getDictionary().get(s));
				}
			}
		}
		
		return inv;
	}
}

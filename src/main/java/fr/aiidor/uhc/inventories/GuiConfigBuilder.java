package fr.aiidor.uhc.inventories;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.tools.ItemBuilder;

public abstract class GuiConfigBuilder extends Gui {
	
	private HashMap<String, ItemStack> dictionnary;
	private String[][] matrix = {
			{"C", "B", "B", "B", "B", "B", "B", "B", "C"},
			{"B", " ", " ", " ", "O", " ", " ", " ", "B"},
			{"C", "B", "B", "B", "B", "B", "B", "B", "X"},
	};
	
	
	public GuiConfigBuilder() {
		dictionnary = new HashMap<String, ItemStack>();
		
		dictionnary.put("X", getBackIcon());
		
		setCenter(new ItemBuilder(Material.ANVIL, "null").getItem());
		setBorder(new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 15, " ").getItem());
		setCorners(new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 15, " ").getItem());
	}
	
	public void setBorder(ItemStack item) {
		dictionnary.put("B", item);
	}
	
	public void setCorners(ItemStack item) {
		dictionnary.put("C", item);
	}
	
	public void setCenter(ItemStack item) {
		dictionnary.put("O", item);
	}
	
	
	public ItemStack getBanner(Integer amount) {
		ItemBuilder builder;
		
		if (amount == 0) {
			builder = new ItemBuilder(Material.BANNER, 1, (byte) 15);
			builder.setDisplayName("§7+0");
			
		} else if (amount > 0) {
			builder = new ItemBuilder(Material.BANNER, 1, (byte) 2);
			builder.setDisplayName("§a+" + amount);
			
		} else {
			builder = new ItemBuilder(Material.BANNER, 1, (byte) 1);
			builder.setDisplayName("§c" + amount);
		}
		
		builder.setLore(getLore());
		return builder.getItem();
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		if (e.getSlot() == 26) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(getBackInventory().getInventory());
			return;
		}
		
		if (event.getItemClicked().getType() == Material.BANNER && event.getItemClicked().getItemMeta().hasDisplayName()) {
			if (e.getSlot() >= 10 && e.getSlot() <= 16 && e.getSlot() != 13) {
				addValue(Integer.valueOf(event.getItemClicked().getItemMeta().getDisplayName().substring(2)));
				playClickSound(event.getPlayer());
				update();
				return;
			}
		}
		
	}
	
	@Override
	public Boolean titleIsDynamic() {
		return true;
	}
	
	public abstract String getTitle();
	public abstract Gui getBackInventory();
	public abstract Integer[] getBannersValues();
	public abstract void addValue(Integer value);
	public abstract List<String> getLore();
	
	@Override
	public Inventory getInventory() {
		
		Inventory inv = Bukkit.createInventory(null, 27, getTitle());
		
		for (int i = 0; i != getBannersValues().length; i++) {
			inv.setItem(10 + i, getBanner(getBannersValues()[i]));
		}
		
		for (int c = 0; c != 3; c++) {
			for (int l = 0; l != 9; l++) {
				String s = matrix[c][l];
				
				if (dictionnary.containsKey(s)) {
					inv.setItem(c * 9 + l, dictionnary.get(s));
				}
			}
		}
		

		
		return inv;
	}
}

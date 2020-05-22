package fr.aiidor.uhc.inventories;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.tools.ItemBuilder;

public abstract class GuiConfigBuilder extends Gui {
	
	private HashMap<String, ItemStack> dictionnary;
	private String[][] matrix = {
			{"C", "B", "B", "B", "B", "B", "B", "B", "C"},
			{"B", " ", " ", " ", "O", " ", " ", " ", "B"},
			{"C", "B", "B", "B", "B", "B", "B", "B", "X"},
	};
	
	public abstract boolean startProtection();
	
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
			builder.setDisplayName("§7+0" + getPrefix());
			
		} else if (amount > 0) {
			builder = new ItemBuilder(Material.BANNER, 1, (byte) 2);
			builder.setDisplayName("§a+" + amount + getPrefix());
			
		} else {
			builder = new ItemBuilder(Material.BANNER, 1, (byte) 1);
			builder.setDisplayName("§c" + amount + getPrefix());
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
				
				if (startProtection()  && !event.getGame().isWaiting()) {
					event.getPlayer().sendMessage(Lang.ST_ERROR_OPTION_START.get());
					event.getPlayer().closeInventory();
					return;
				}
				
				addValue(Integer.valueOf(event.getItemClicked().getItemMeta().getDisplayName().substring(2).split(" ")[0]));
				playClickSound(event.getPlayer());
				update();
				return;
			}
		}
		
	}
	
	@Override
	public Boolean titleIsDynamic() {
		return false;
	}
	
	public abstract String getTitle();
	public abstract Gui getBackInventory();
	public abstract Integer[] getBannersValues();
	public abstract void addValue(Integer value);
	public abstract List<String> getLore();
	
	public String getPrefix() {
		return "";
	}
	
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

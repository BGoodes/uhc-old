package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiEnchants extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_STUFF_ENCHANTS.get();
	}
	
	@Override
	public InventoryHolder getHolder() {
		return null;
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{" ", " ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " ", "X"}
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getBackIcon());
		
		return dictionary;
	}
	
	@Override
	public Inventory getInventory() {
		
		Inventory inv = super.getInventory();
		GameSettings s = UHC.getInstance().getGameManager().getGame().getSettings();
		
		ArrayList<Enchantment> enchants = new ArrayList<Enchantment>();
		for (Enchantment ench : Enchantment.values()) {
			enchants.add(ench);
		}
		
		enchants.sort(new NameComparator());
		
		Integer i = 0;
		for (Enchantment ench : enchants) {
			
			Integer l = s.enchants_limit.get(ench);
			ItemBuilder item = new ItemBuilder(Material.ENCHANTED_BOOK, l, "§d" + Lang.valueOf("ENCH_" + ench.getName()).get() + " : §6" + l);
			
			List<String> lore = new ArrayList<String>();
			
			if (l > 0) item.addStoredEnchant(ench, l);
			else lore.add(Lang.INV_OFF.get());
			
			lore.add("");
			lore.add(Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "1 " + Lang.LEVEL.get().toLowerCase() + "."));
			lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "1 " + Lang.LEVEL.get().toLowerCase()) + ".");

			item.setLore(lore);
			
			inv.setItem(i, item.getItem());
			i++;
		}
		
		return inv;
	}
	
	private class NameComparator implements Comparator<Enchantment> {

		@Override
		public int compare(Enchantment arg0, Enchantment arg1) {
			return Lang.removeColor(Lang.valueOf("ENCH_" + arg0.getName()).get()).compareTo(Lang.removeColor(Lang.valueOf("ENCH_" + arg1.getName()).get()));
		}

	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		ItemStack item = event.getItemClicked();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 26) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_STUFF.getInventory());
			return;
		}
		
		if (item.getType() == Material.ENCHANTED_BOOK) {
			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				
				String displayName = item.getItemMeta().getDisplayName();
				
				if (displayName.startsWith("§d")) {
					
					GameSettings s = event.getGame().getSettings();
					 
					for (Enchantment ench : Enchantment.values()) {
						if (displayName.startsWith("§d" + Lang.valueOf("ENCH_" + ench.getName()).get())) {
							
							if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
								Integer i = s.enchants_limit.get(ench) + 1;
								
								if (i > ench.getMaxLevel()) i = ench.getMaxLevel();
								s.enchants_limit.put(ench, i);
								
								playClickSound(event.getPlayer());
								update();
								return;
							}
							
							if (e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {
								Integer i = s.enchants_limit.get(ench) - 1;
								
								if (i < 0) i = 0;
								s.enchants_limit.put(ench, i);
								
								playClickSound(event.getPlayer());
								update();
								return;
							}
						}
					}
				}
			}
		}
	}
}
